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
@Comment("服务器安全保护路径资源表")
@Accessors(chain = true)
public class SysSecurityResource implements Serializable {

private static final long serialVersionUID = -3249228344275178326L;

    @Id
    @Comment("主键ID")
    @GeneratedValue(generator ="snowFlake" )
    @GenericGenerator(name="snowFlake", strategy="druidelf.sqlcomment.SnowIdGenerator")
    private String id;

    @Comment("资源名称")
    @Column( nullable = false, length = 20)
    private String name;

    @Comment("资源路径")
    @Column( nullable = false)
    private String url;

    @Comment("权限id")
    @Column( nullable = false, length = 32)
    private String permissionId;


}
