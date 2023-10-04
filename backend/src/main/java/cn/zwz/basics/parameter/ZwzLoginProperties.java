package cn.zwz.basics.parameter;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import org.springframework.context.annotation.Configuration;


@ApiOperation(value = "Login Constants Class") //登录常量类
@Data
@Configuration
public class ZwzLoginProperties {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "Is it single sign-on?") //是否单点登录
    private Boolean ssoFlag = true;

    @ApiModelProperty(value = "Is the permission saved?") //是否保存权限
    private Boolean saveRoleFlag = true;

    @ApiModelProperty(value = "User Token expiration days", notes = "Duration to save the Token, can't auto-login without the Token")
    //用户Token过期天数  保存Token的时间,Token没了也不能自动登录
    private Integer userTokenInvalidDays = 30;

    @ApiModelProperty(value = "User saved login days", notes = "Duration for auto-login if selected")
    //用户保存登录天数 选择了自动登录,记录保存的时间
    private Integer userSaveLoginTokenDays = 7;

    @ApiModelProperty(value = "Maximum login failure times")
    //最大登录失败次数
    private Integer maxLoginFailTimes = 10;

    @ApiModelProperty(value = "Minutes to lock after exceeding max login failures")
    //登录失败超限后锁定分钟
    private Integer loginFailMaxThenLockTimes = 10;

    @ApiModelProperty(value = "Global rate limiting")
    //全局限流
    private Boolean allLimiting = false;

    @ApiModelProperty(value = "Number of global rate limits")
    //全局限流个数
    private Integer allLimitingSize = 100;

    @ApiModelProperty(value = "Duration of global rate limiting")
    //全局限流单位时长
    private Long allLimitingTime = 1000L;

    @ApiModelProperty(value = "Single IP rate limiting")
    //单IP限流
    private Boolean oneLimiting = false;

    @ApiModelProperty(value = "Number of rate limits per IP")
    //单IP限流个数
    private Integer oneLimitingSize = 100;

    @ApiModelProperty(value = "Duration of rate limiting per IP")
    //单IP限流单位时长
    private Long oneLimitingTime = 1000L;

    public static final String HTTP_HEADER = "accessToken";

    public static final String SAVE_LOGIN_PRE = "saveLogin";

    public static final String HTTP_TOKEN_PRE = "ZWZ_TOKEN_PRE:";

    public static final String USER_TOKEN_PRE = "ZWZ_USER_TOKEN:";
}
