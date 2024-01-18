package druidelf.bean.security;

import druidelf.sqlcomment.Comment;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

@Data
@Entity
@Comment("角色权限表")
@Accessors(chain = true)
public class SysSecurityRolePermission implements Serializable {

private static final long serialVersionUID = 2212948817542923335L;

    @Id
    @Comment("主键ID")
    @GeneratedValue(generator ="snowFlake" )
    @GenericGenerator(name="snowFlake", strategy="druidelf.sqlcomment.SnowIdGenerator")
    private String id;

    @Comment("角色id")
    @Column( nullable = false, length = 32)
    private String roleId;

    @Comment("权限id")
    @Column( nullable = false, length = 32)
    private String permissionId;

}
