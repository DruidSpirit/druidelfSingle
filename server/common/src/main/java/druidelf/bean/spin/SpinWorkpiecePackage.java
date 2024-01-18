package druidelf.bean.spin;

import druidelf.sqlcomment.Comment;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;

@Data
@Entity
@Comment("工件包表")
@Accessors(chain = true)
public class SpinWorkpiecePackage {

    private static final long serialVersionUID = -4093579445965429516L;

    @Id
    @Comment("主键ID")
    @GeneratedValue(generator ="snowFlake" )
    @GenericGenerator(name="snowFlake", strategy="druidelf.sqlcomment.SnowIdGenerator")
    private String id;

    @Comment("工件编号")
    @Column( nullable = false, length = 32)
    private String WorkpieceNumber;

    @Comment("扎号")
    @Column( nullable = false, length = 8)
    private Integer bundleCode;

    @Comment("工件数量")
    @Column( nullable = false, length = 6)
    private Integer workpieceCount;

    @Comment("关联订单ID")
    @Column( nullable = false, length = 32)
    private String orderID;

    @Comment("工艺类型")
    @Column( nullable = false, length = 2)
    private Integer technologyType;

    @Comment("款式类型")
    @Column( nullable = false, length = 2)
    private Integer styleType;

    @Comment("型号大小")
    @Column( nullable = false, length = 2)
    private Integer size;

    @Comment("颜色")
    @Column( nullable = false, length = 2)
    private Integer colour;

    @Comment("单价")
    @Column( nullable = false, length = 10, scale = 2)
    private BigDecimal price;

    @Comment("归属人用户ID")
    @Column(length = 32)
    private String beLongerUserId;
}
