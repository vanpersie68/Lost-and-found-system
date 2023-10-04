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

@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "a_look_things")
@TableName("a_look_things")
//@ApiModel(value = "寻物")
@ApiModel(value = "look_things")
public class LookThings extends ZwzBaseEntity {

    private static final long serialVersionUID = 1L;

    //@ApiModelProperty(value = "物品名称")
    @ApiModelProperty(value = "Item name")
    private String title;

    //@ApiModelProperty(value = "丢失时间")
    @ApiModelProperty(value = "Pick up time")
    private String time;

    //@ApiModelProperty(value = "丢失地点")
    @ApiModelProperty(value = "Pick up Location")
    private String address;

    //@ApiModelProperty(value = "所属类型")
    @ApiModelProperty(value = "Item Type")
    private String type;

    //@ApiModelProperty(value = "相关描述")
    @ApiModelProperty(value = "Description")
    private String content;

    //@ApiModelProperty(value = "物品图片")
    @ApiModelProperty(value = "Item picture")
    private String image;

    //@ApiModelProperty(value = "发布人")
    @ApiModelProperty(value = "publisher")
    private String userId;
}