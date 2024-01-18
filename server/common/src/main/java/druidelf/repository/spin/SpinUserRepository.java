package druidelf.repository.spin;

import druidelf.bean.spin.SpinUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpinUserRepository extends JpaRepository<SpinUser,String> {

    SpinUser findSpinUserByUsername( String username );

    SpinUser findSpinUserByOpenId( String openId );
}
