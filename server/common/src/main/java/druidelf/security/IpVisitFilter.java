package druidelf.security;


import druidelf.enums.ResponseDataEnums;
import druidelf.model.ResponseData;
import druidelf.util.UtilForHttpServletData;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import javax.annotation.PreDestroy;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * IP访问过滤器
 * 该过滤器的主要作用是将那些恶意访问的用户过滤掉，防止其恶意消耗服务器资源
 */
@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
@Log4j2
@Component
public class IpVisitFilter implements Filter, ApplicationRunner {

    /**
     * 清除ip访问次数记录临时数据的时间
     */
    @Value("${ip-visit-setting.visit-time}")
    private Long visitTime;

    /**
     * 暴力访问最大限度
     */
    @Value("${ip-visit-setting.visit-count}")
    private int visitCount;

    /**
     * ip白名单
     */
    @Value("#{'${ip-visit-setting.white-ip-list}'.split(',')}")
    private List<String> whiteIpList;

    /**
     * 黑名单引擎
     */
    @Autowired
    private BlackListModel blackList;

    /**
     * 临时IP存放容器
     */
    private final Map<String, AtomicInteger> ipTemContent = new ConcurrentHashMap<>();


    //  建立定时线程池
    private final ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

    /**
     * 在容器启动完成后执行线程任务，定期清理ipTemContent的数据以及做些初始化相关的事情
     */
    @Override
    public void run(ApplicationArguments args){

        log.info("==============================开启IP访问过滤线程池================================");
        //  每隔一段时间定时清除IP临时容器
        scheduledExecutorService.scheduleAtFixedRate(() -> {
            if ( ipTemContent.size() > 0 ) {
                ipTemContent.clear();
            }
        }, 0, visitTime, TimeUnit.SECONDS);

    }

    /**
     * 当该实例被spring容器销毁时，优雅关闭线程池，防止内存泄漏
     */
    @PreDestroy
    public void closeThreadPool (){
        scheduledExecutorService.shutdown();
        blackList.shutDownBlackList();
        log.info("==============================成功关闭IP访问过滤相关线程池================================");
    }

    /**
     * 请求过滤拦截
     *      拦截规则：
     *      1.位于白名单的IP地址不受此规则限制
     *      2.swagger接口不受此规则限制
     *      3.同一个IP,每visit-time秒访问visit-count次，被认为是恶意攻击，将其加入黑名单
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        //  组装拒绝访问前端返回信息
        ResponseData<Object> failure = ResponseData.FAILURE(ResponseDataEnums.RESPONSE_FAIL_FREQUENTLY_VISIT);
        failure.setMessage(failure.getMessage()+"，锁定时长："+blackList.getNotAllowTime() + " " + blackList.getTimeUnit());

        HttpServletRequest request = (HttpServletRequest) servletRequest;

        // 从request对象中获取客户端ip
        String clientIp = UtilForHttpServletData.getRealIp(request);
        if(!whiteIpList.contains(clientIp)&&blackList.isExistBlackList(clientIp)) {
            log.info("=====>IP:" + clientIp + " 访问过于频繁，已被加入黑名单，拒绝其访问！");
            UtilForHttpServletData.responseDataForHttp( servletResponse, failure);
            return ;
        }

        // 记录IP访问频率
        AtomicInteger atomicInteger = ipTemContent.computeIfPresent(clientIp, (s, integer) -> {
            integer.set(integer.incrementAndGet());
            return integer;
        });
        if ( atomicInteger == null ) {
            ipTemContent.put(clientIp,new AtomicInteger(1));
        }

        //  如果超过设定的频率则将其加入黑名单中
        int count = ipTemContent.get(clientIp) == null ? 0 : ipTemContent.get(clientIp).get();
        if ( !whiteIpList.contains(clientIp) && count > visitCount ) {
            blackList.addBlackList(clientIp);
            log.info("=====>IP:" + clientIp + " 访问过于频繁，已被加入黑名单，拒绝其访问！");
            UtilForHttpServletData.responseDataForHttp( servletResponse, failure );
            return;
        }

        //  放行
        filterChain.doFilter(servletRequest,servletResponse);
    }

}
