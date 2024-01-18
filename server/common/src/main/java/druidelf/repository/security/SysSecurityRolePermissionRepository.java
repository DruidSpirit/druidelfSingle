package druidelf.repository.security;

import druidelf.bean.security.SysSecurityRolePermission;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SysSecurityRolePermissionRepository extends JpaRepository<SysSecurityRolePermission,String> {

    List<SysSecurityRolePermission> findAllByRoleId(String roleId);
}
