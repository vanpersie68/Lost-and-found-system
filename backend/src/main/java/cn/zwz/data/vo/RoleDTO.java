package cn.zwz.data.vo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;


@Api(tags = "Role VO Class")
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class RoleDTO {

    @ApiModelProperty(value = "Role Name")
    private String name;

    @ApiModelProperty(value = "Role ID")
    private String id;

    @ApiModelProperty(value = "Role Description")
    private String description;
}
