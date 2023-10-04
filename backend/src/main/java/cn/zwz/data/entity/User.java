package cn.zwz.data.entity;

import cn.zwz.basics.baseClass.ZwzBaseEntity;
import cn.zwz.basics.parameter.CommonConstant;
import cn.zwz.data.vo.PermissionDTO;
import cn.zwz.data.vo.RoleDTO;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.List;


@Data
@Accessors(chain = true)
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "a_user")
@TableName("a_user")
@ApiModel(value = "user")
public class User extends ZwzBaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "name")
    @NotNull(message = "Name cannot be empty")
    @Size(max = 20, message = "Name length cannot exceed 20 characters")
    private String nickname;

    @ApiModelProperty(value = "account")
    @Column(unique = true, nullable = false)
    @Pattern(regexp = "^[a-zA-Z0-9_\\u4e00-\\u9fa5]{4,16}$", message = "The account length is illegal")
    private String username;

    @ApiModelProperty(value = "password")
    @NotNull(message = "password can not be blank")
    private String password;

    @ApiModelProperty(value = "password strength")
    @Column(length = 2)
    private String passStrength;

    @ApiModelProperty(value = "Phone number")
    @Pattern(regexp = "^[1][3,4,5,6,7,8,9][0-9]{9}$", message = "Malformed phone number")
    private String mobile;

    @ApiModelProperty(value = "Department ID")
    private String departmentId;

    @ApiModelProperty(value = "department")
    private String departmentTitle;

    @ApiModelProperty(value = "E-mail")
    @Pattern(regexp = "^(([^<>()\\[\\]\\\\.,;:\\s@\"]+(\\.[^<>()\\[\\]\\\\.,;:\\s@\"]+)*)|(\".+\"))@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$", message = "邮箱格式错误")
    private String email;

    @ApiModelProperty(value = "gender")
    private String sex;

    @ApiModelProperty(value = "Districts and counties")
    private String address;

    @ApiModelProperty(value = "user type")
    private Integer type;

    @ApiModelProperty(value = "personal portal")
    private String myDoor;

    @ApiModelProperty(value = "Enabled status")
    private Integer status = CommonConstant.USER_STATUS_NORMAL;

    @ApiModelProperty(value = "avatar")
    private String avatar = CommonConstant.USER_DEFAULT_AVATAR;

    @Transient
    @TableField(exist=false)
    @ApiModelProperty(value = "Whether to default role")
    private Integer defaultRole;

    @Transient
    @TableField(exist=false)
    @ApiModelProperty(value = "List of menus owned by the user")
    private List<PermissionDTO> permissions;

    @Transient
    @TableField(exist=false)
    @ApiModelProperty(value = "List of roles owned by the user")
    private List<RoleDTO> roles;
}
