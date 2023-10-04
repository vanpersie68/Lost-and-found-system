package cn.zwz.data.entity;

import cn.zwz.basics.baseClass.ZwzBaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.math.BigDecimal;


@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "a_dict")
@TableName("a_dict")
@ApiModel(value = "Data Dictionary")
public class Dict extends ZwzBaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "Data dictionary title")
    private String title;

    @ApiModelProperty(value = "Data dictionary sort value")
    @Column(precision = 10, scale = 2)
    private BigDecimal sortOrder;

    @ApiModelProperty(value = "Data dictionary remarks")
    private String description;

    @ApiModelProperty(value = "Data dictionary type")
    private String type;
}