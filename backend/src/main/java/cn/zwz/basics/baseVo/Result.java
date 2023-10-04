package cn.zwz.basics.baseVo;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@ApiOperation(value = "Response Data VO Class")  // 返回数据VO类
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result<T> implements Serializable{

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "Primary returned data",notes = "Generic type")  // 返回主数据 泛型
    private T result;

    @ApiModelProperty(value = "Whether request was successful",notes = "true means request was successful, false means request failed")
    // 是否请求成功 true为请求成功,false为请求失败
    private boolean success;

    @ApiModelProperty(value = "Response status code",notes = "Default 200 is successful")  // 返回状态代码 默认200为成功
    private Integer code;

    @ApiModelProperty(value = "Timestamp",notes = "Current system's timestamp")  // 时间戳 当前系统的时间戳
    private long timestamp = System.currentTimeMillis();

    @ApiModelProperty(value = "Prompt message",notes = "Additional prompt message")  // 提示信息  额外的提示信息
    private String message;
}
