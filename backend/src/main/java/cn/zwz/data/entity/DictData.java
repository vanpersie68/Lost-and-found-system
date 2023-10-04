package cn.zwz.data.entity;

import cn.zwz.basics.baseClass.ZwzBaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import java.math.BigDecimal;


@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "a_dict_data")
@TableName("a_dict_data")
@ApiModel(value = "Data dictionary value")
public class DictData extends ZwzBaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "Data dictionary ID")
    private String dictId;

    @Transient
    @TableField(exist=false)
    @ApiModelProperty(value = "Data dictionary name")
    private String dictName;

    @ApiModelProperty(value = "Data dictionary key")
    private String title;

    @ApiModelProperty(value = "Data dictionary value sort value")
    @Column(precision = 10, scale = 2)
    private BigDecimal sortOrder;

    @ApiModelProperty(value = "Data dictionary value")
    private String value;

    @ApiModelProperty(value = "Data dictionary value remarks")
    private String description;

    @ApiModelProperty(value = "Whether to enable")
    private Integer status = 0;
}