package druidelf.security.handle;

import druidelf.bean.spin.SpinUser;
import druidelf.repository.spin.SpinUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Log4j2
@Component
@RequiredArgsConstructor
public class MyUserDetailsService implements UserDetailsService {

    private final SpinUserRepository spinUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws AuthenticationException {

        SpinUser spinUserByUsername = spinUserRepository.findSpinUserByUsername(username);
        if ( spinUserByUsername == null ) throw new BadCredentialsException("用户名或密码错误");
        if ( !spinUserByUsername.getStatus() ) throw new DisabledException("账户被禁用，请联系管理员");
        String password = spinUserByUsername.getPassword();
        // 封装用户信息，并返回。参数分别是：用户名，密码，用户权限
        return new User(username, password,
                AuthorityUtils.commaSeparatedStringToAuthorityList(null));
    }
}
