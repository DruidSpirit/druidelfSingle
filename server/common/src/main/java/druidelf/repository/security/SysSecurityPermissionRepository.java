package druidelf.repository.security;

import druidelf.bean.security.SysSecurityPermission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SysSecurityPermissionRepository extends JpaRepository<SysSecurityPermission,String> {
}
