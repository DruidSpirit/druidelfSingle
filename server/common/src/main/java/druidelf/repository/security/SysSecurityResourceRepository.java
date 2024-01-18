package druidelf.repository.security;

import druidelf.bean.security.SysSecurityResource;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SysSecurityResourceRepository extends JpaRepository<SysSecurityResource,String> {

    List<SysSecurityResource> findAllByPermissionIdIn( List<String> PermissionIds );
}
