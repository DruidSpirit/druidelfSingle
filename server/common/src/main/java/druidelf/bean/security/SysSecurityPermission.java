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
@Comment("权限表")
@Accessors(chain = true)
public class SysSecurityPermission implements Serializable {

private static final long serialVersionUID = -7780324738004791834L;

    @Id
    @Comment("主键ID")
    @GeneratedValue(generator ="snowFlake" )
    @GenericGenerator(name="snowFlake", strategy="druidelf.sqlcomment.SnowIdGenerator")
    private String id;

    @Comment("权限名称")
    @Column( nullable = false, length = 20)
    private String name;


}
