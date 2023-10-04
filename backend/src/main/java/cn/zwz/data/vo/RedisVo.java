package cn.zwz.data.vo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;


@Api(tags = "Cache VO Class")
@Data
@AllArgsConstructor
public class RedisVo {

    @ApiModelProperty(value = "Redis Key")
    private String key;

    @ApiModelProperty(value = "Redis Value")
    private String value;

    @ApiModelProperty(value = "Time to Live (TTL) in Seconds")
    private Long expireTime;
}
