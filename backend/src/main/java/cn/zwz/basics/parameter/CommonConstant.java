package cn.zwz.basics.parameter;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;


@ApiOperation(value = "System Base Constants") //系统基础常量
public interface CommonConstant {

    @ApiModelProperty(value = "Normal")
    Integer USER_STATUS_NORMAL = 0;

    @ApiModelProperty(value = "Disabled")
    Integer USER_STATUS_LOCK = -1;

    @ApiModelProperty(value = "Top Level Menu")
    Integer PERMISSION_NAV = -1;

    @ApiModelProperty(value = "Regular Menu")
    Integer PERMISSION_PAGE = 0;

    @ApiModelProperty(value = "Button Menu")
    Integer PERMISSION_OPERATION = 1;

    @ApiModelProperty(value = "Top Level Menu")
    Integer LEVEL_ZERO = 0;

    @ApiModelProperty(value = "1st Level Menu")
    Integer LEVEL_ONE = 1;

    @ApiModelProperty(value = "2nd Level Menu")
    Integer LEVEL_TWO = 2;

    @ApiModelProperty(value = "3rd Level Menu")
    Integer LEVEL_THREE = 3;

    @ApiModelProperty(value = "Main Department ID")
    String PARENT_ID = "0";

    @ApiModelProperty(value = "Avatar URL") //头像URL
    String USER_DEFAULT_AVATAR = "https://asoa-1305425069.cos.ap-shanghai.myqcloud.com/1669635627773202432.png";
}

