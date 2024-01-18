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
@Comment("角色表")
@Accessors(chain = true)
public class SysSecurityRole implements Serializable {

private static final long serialVersionUID = 1018080623228620093L;

    @Id
    @Comment("主键ID")
    @GeneratedValue(generator ="snowFlake" )
    @GenericGenerator(name="snowFlake", strategy="druidelf.sqlcomment.SnowIdGenerator")
    private String id;

    @Comment("角色名称")
    @Column( nullable = false, length = 20)
    private String name;

    @Comment("角色id")
    @Column( nullable = false, length = 32)
    private String roleId;


}
