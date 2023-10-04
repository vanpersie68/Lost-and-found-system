package cn.zwz.lost.entity;

import cn.zwz.basics.baseClass.ZwzBaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.math.BigDecimal;

@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "a_news")
@TableName("a_news")
@ApiModel(value = "公告")
public class News extends ZwzBaseEntity {

    private static final long serialVersionUID = 1L;

    //@ApiModelProperty(value = "公告标题")
    @ApiModelProperty(value = "News title")
    private String title;

    //@ApiModelProperty(value = "公告内容")
    @ApiModelProperty(value = "News content")
    private String content;

    //@ApiModelProperty(value = "发布状态")
    @ApiModelProperty(value = "publish status")
    private String status;

    //@ApiModelProperty(value = "排序值")
    @ApiModelProperty(value = "sort Order")
    private BigDecimal sortOrder;
}