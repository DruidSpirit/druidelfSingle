package druidelf.security;

import druidelf.bean.security.SysSecurityResource;
import druidelf.bean.security.SysSecurityRolePermission;
import druidelf.bean.spin.SpinUser;
import druidelf.repository.security.SysSecurityResourceRepository;
import druidelf.repository.security.SysSecurityRolePermissionRepository;
import druidelf.repository.spin.SpinUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * springSecurity授权配置
 */
@Component("rbacPermission")
@RequiredArgsConstructor
public class RbacPermission {

    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    private final SpinUserRepository userRepository;

    private final SysSecurityRolePermissionRepository sysSecurityRolePermissionRepository;

    private final SysSecurityResourceRepository sysSecurityResourceRepository;

    public boolean hasPermission(HttpServletRequest request, Authentication authentication) throws AccessDeniedException {

        Object principal = authentication.getPrincipal();
        boolean hasPermission = false;
        if(principal instanceof UserDetails) {
            String username = ((UserDetails) principal).getUsername();
            // 获取用户角色id
            SpinUser user = userRepository.findSpinUserByUsername(username);
            //  获取用户权限列表
            List<SysSecurityRolePermission> allByRoleId = sysSecurityRolePermissionRepository.findAllByRoleId(user.getRoleId());
              if ( CollectionUtils.isEmpty( allByRoleId ) ) throw new AuthorizationServiceException("该用户无权限");

            //  获取该用户权限下可访问的url资源列表
            List<String> collect = allByRoleId.stream().map(SysSecurityRolePermission::getPermissionId).collect(Collectors.toList());
            List<SysSecurityResource> resourceList = sysSecurityResourceRepository.findAllByPermissionIdIn(collect);

             // 校验该用户是否具有访问的权限
            Set<String> urls = resourceList.stream().map(SysSecurityResource::getUrl).collect(Collectors.toSet());
            for (String url : urls) {
                if(antPathMatcher.match(url, request.getRequestURI())) {
                    hasPermission = true;
                    break;
                }
            }
        }
        if ( !hasPermission ) throw new AuthorizationServiceException("该用户无权限");

        return true;

    }
}
