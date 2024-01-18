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
@Comment("订单表")
@Accessors(chain = true)
public class SpinOrder {

    private static final long serialVersionUID = -4093579445965429516L;

    @Id
    @Comment("主键ID")
    @GeneratedValue(generator ="snowFlake" )
    @GenericGenerator(name="snowFlake", strategy="druidelf.sqlcomment.SnowIdGenerator")
    private String id;

    @Comment("制衣厂商名称")
    @Column( nullable = false, length = 50)
    private String spinFactory;

    @Comment("订单编号")
    @Column( nullable = false, length = 32)
    private String orderNumber;

    @Comment("工件数量")
    @Column( nullable = false, length = 10)
    private Integer workpieceCount;

    @Comment("工件包数量")
    @Column( nullable = false, length = 6)
    private Integer SpinWorkpiecePackageCount;

    @Comment("创建时间")
    @Column(nullable = false)
    private LocalDateTime createTime;


}
