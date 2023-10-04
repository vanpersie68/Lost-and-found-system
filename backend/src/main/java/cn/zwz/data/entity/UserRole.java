package cn.zwz.data.entity;

import cn.zwz.basics.baseClass.ZwzBaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;


@Data
@Accessors(chain = true)
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "a_user_role")
@TableName("a_user_role")
@ApiModel(value = "user role")
public class UserRole extends ZwzBaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "Role ID")
    private String roleId;

    @ApiModelProperty(value = "User ID")
    private String userId;

    @Transient
    @TableField(exist=false)
    @ApiModelProperty(value = "username")
    private String userName;

    @Transient
    @TableField(exist=false)
    @ApiModelProperty(value = "role name")
    private String roleName;
}
