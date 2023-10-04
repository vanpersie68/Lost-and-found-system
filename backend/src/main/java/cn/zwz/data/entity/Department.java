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
import java.util.List;


@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "a_department")
@TableName("a_department")
@ApiModel(value = "department")
public class Department extends ZwzBaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "Department title")
    private String title;

    @ApiModelProperty(value = "state")
    private Integer status = 0;

    @ApiModelProperty(value = "sort value")
    @Column(precision = 10, scale = 2)
    private BigDecimal sortOrder;

    @ApiModelProperty(value = "Parent node ID")
    private Boolean isParent = false;

    @ApiModelProperty(value = "Parent department ID")
    private String parentId;

    @Transient
    @TableField(exist=false)
    @ApiModelProperty(value = "leader")
    private List<String> mainHeader;

    @Transient
    @TableField(exist=false)
    @ApiModelProperty(value = "deputy leader")
    private List<String> viceHeader;

    @Transient
    @TableField(exist=false)
    @ApiModelProperty(value = "Parent department name")
    private String parentTitle;
}