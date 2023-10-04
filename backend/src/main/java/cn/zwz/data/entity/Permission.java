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
@Table(name = "a_permission")
@TableName("a_permission")
@ApiModel(value = "Menu permissions")
public class Permission extends ZwzBaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "Menu name")
    private String name;

    @ApiModelProperty(value = "menu title")
    private String title;

    @ApiModelProperty(value = "menu hierarchy")
    private Integer level;

    @ApiModelProperty(value = "Enabled status")
    private Integer status = 0;

    @ApiModelProperty(value = "Menu button type")
    private Integer type;

    @ApiModelProperty(value = "Front-end component name")
    private String component;

    @ApiModelProperty(value = "Page path")
    private String path;

    @ApiModelProperty(value = "PC icon")
    private String icon;

    @ApiModelProperty(value = "Parent node ID")
    private String parentId;

    @ApiModelProperty(value = "button type")
    private String buttonType;

    @ApiModelProperty(value = "Remark")
    private String description;

    @ApiModelProperty(value = "menu sort value")
    @Column(precision = 10, scale = 2)
    private BigDecimal sortOrder;

    @Transient
    @TableField(exist=false)
    @ApiModelProperty(value = "Node expansion status")
    private Boolean expand = true;

    @Transient
    @TableField(exist=false)
    @ApiModelProperty(value = "Node selected state")
    private Boolean selected = false;

    @Transient
    @TableField(exist=false)
    @ApiModelProperty(value = "Node check status")
    private Boolean checked = false;

    @Transient
    @TableField(exist=false)
    @ApiModelProperty(value = "Submenu list")
    private List<Permission> children;

    @Transient
    @TableField(exist=false)
    @ApiModelProperty(value = "Menu sub-permission list")
    private List<String> permTypes;
}