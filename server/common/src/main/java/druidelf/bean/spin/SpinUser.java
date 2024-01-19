package druidelf.bean.spin;

import druidelf.sqlcomment.Comment;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Data
@Entity
@Comment("用户表")
@Accessors(chain = true)
public class SpinUser {

    private static final long serialVersionUID = -4093589445965429516L;

    @Id
    @Comment("主键ID")
    @GeneratedValue(generator ="snowFlake" )
    @GenericGenerator(name="snowFlake", strategy="druidelf.sqlcomment.SnowIdGenerator")
    private String id;

    @Comment("用户名称")
    @Column( nullable = false, length = 20)
    private String username;

    @Comment("真实名称")
    @Column( nullable = false, length = 20)
    private String realName;

    @Comment("用户密码")
    @Column( nullable = false, length = 100)
    private String password;

    @Comment("用户邮箱")
    @Column(length = 50)
    private String email;

    @Comment("手机号码")
    @Column(length = 20)
    private String phone;

    @Comment("角色id")
    @Column(length = 32)
    private String roleId;

    @Comment("用户状态")
    @Column(nullable = false,length = 1)
    private Boolean status;

    @Comment("微信用户唯一标识")
    @Column(length = 60,unique = true)
    private String openId;

    @Comment("注册时间")
    @Column(nullable = false)
    private LocalDateTime addTime;


}
