package druidelf.repository.security;

import druidelf.bean.security.SysSecurityRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SysSecurityRoleRepository extends JpaRepository<SysSecurityRole,String> {
}
