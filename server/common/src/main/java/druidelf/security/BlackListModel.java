package druidelf.security;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Data
@EqualsAndHashCode(callSuper = true)
@Log4j2
@Component
public class BlackListModel extends ConcurrentHashMap<String, BlackListModel.BlackIp> implements ApplicationRunner {

    /**
     * 将锁定的IP信息存放在黑名单中，时间一到就将IP移除黑名单
     */
    private final DelayQueue<BlackIp> delayQueue = new DelayQueue<>();

    /**
     * 开启额外线程，将黑名单队列中过期的信息弹出（为了使一段时间后被锁定IP的用户能继续访问服务器）
     */
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    /**
     * 不停拿延迟队列数据的开关
     */
    private volatile boolean allowRun = true;

    /**
     * 客户端IP在黑名单中存在有效时长
     */
    @Value("${ip-visit-setting.not-allow-time}")
    private long notAllowTime;

    /**
     * 客户端IP在黑名单中存在有效时长单位
     */
    @Value("${ip-visit-setting.time-unit}")
    private String timeUnit;

    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    /**
     * spring启动后开启异步线程来单独去将延迟队列过期的黑名单信息移除
     */
    @Override
    public void run(ApplicationArguments args) {

        log.info("==============================启动黑名单引擎================================");
        executorService.execute(() -> {
            while ( allowRun ) {
                try {
                    //  阻塞等待地去拿延迟队列里面的数据并将其弹出延迟队列
                    BlackIp take = delayQueue.take();
                    //  将相应对的Map存储的数据也删除，避免废弃数据累加，导致内存溢出
                    super.remove(take.getIp());
                } catch (InterruptedException e) {
                    log.error(e);
                }
            }
        });
    }

    /**
     * 黑名单对象
     */
    @Data
    public static class BlackIp implements Delayed {

        private final String ip;
        private final long scheduleTime;

        public BlackIp(String ip, long delayed, TimeUnit unit) {
            this.ip = ip;
            this.scheduleTime = System.currentTimeMillis() + unit.toMillis(delayed);
        }

        // 获取还有多少剩余时间
        @Override
        public long getDelay(TimeUnit unit) {

            return unit.convert(scheduleTime-System.currentTimeMillis(),TimeUnit.MILLISECONDS);
        }

        // 两个时间差在毫秒级别作比较
        @Override
        public int compareTo(Delayed o) {
            return (int)(this.scheduleTime-((BlackIp)o).scheduleTime);
        }
    }

    /**
     * IP是否存在黑名单中
     *      说明：这里查map和delayQueue里的数据由于并发场景，
     *           可能会出现map取得到数据而delayQueue取不到数据，这时IP即使存在黑名单中也会让其通过访问服务器。
     *           所以为了防止这种情况，这里加了读锁，同理，在 addBlackList(String ip) 方法里加了写锁。
     * @param ip    客户端访问的IP地址
     * @return      true 存在 false 不存在
     */
    public boolean isExistBlackList( String ip ){

        Lock lock = readWriteLock.readLock();
        boolean contains;
        lock.lock();
        try {
            BlackIp blackIp = super.get(ip);
            contains = delayQueue.contains(blackIp);
        }finally {
            lock.unlock();
        }

        return contains;
    }

    /**
     * 将客户端传来的IP地址加入到服务器黑名单中
     * @param ip    客户端传来的IP地址
     */
    public void addBlackList ( String ip ) {

        Lock lock = readWriteLock.writeLock();
        lock.lock();
        try {
            BlackIp blackIp = new BlackIp(ip, notAllowTime, TimeUnit.valueOf(timeUnit));
            super.put(ip,blackIp);
            delayQueue.put(blackIp);
        }finally {
            lock.unlock();
        }

    }

    /**
     * 关闭黑名单引擎
     */
    public void shutDownBlackList(){
        allowRun = false;
        executorService.shutdown();
    }
}
