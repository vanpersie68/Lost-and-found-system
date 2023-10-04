package cn.zwz.data.vo;

import io.swagger.annotations.Api;
import lombok.Data;


@Api(tags = "User Menu Permission VO Class")
@Data
public class UserByPermissionVo {
    private String userId;
    private String userName;
    private String roleStr;
    private String code;
    private String mobile;
}

