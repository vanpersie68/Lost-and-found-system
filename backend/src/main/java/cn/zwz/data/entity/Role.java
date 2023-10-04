package cn.zwz.data.entity;

import cn.zwz.basics.baseClass.ZwzBaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import java.util.List;


@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "a_role")
@TableName("a_role")
@ApiModel(value = "Role")
public class Role extends ZwzBaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "Role Name")
    private String name;

    @ApiModelProperty(value = "Data permission")
    private int dataType;

    @ApiModelProperty(value = "Is it the default")
    private Boolean defaultRole;

    @ApiModelProperty(value = "Role notes")
    private String description;

    @Transient
    @TableField(exist=false)
    @ApiModelProperty(value = "Role owns menu list")
    private List<RolePermission> permissions;
}
