package cn.zwz.data.vo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;


@Api(tags = "Temporary Menu VO Class")
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class PermissionDTO {

    @ApiModelProperty(value = "Page Path")
    private String path;

    @ApiModelProperty(value = "Menu Title")
    private String title;
}

