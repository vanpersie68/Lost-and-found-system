package cn.zwz.basics.exception;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;


@ApiOperation(value = "Custom Exception")  // 自定义异常
@Data
public class ZwzException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private static final String DEFAULT_MSG = "System encountered an error";  // 系统出现错误

    @ApiModelProperty(value = "Exception Message Content")  // 异常消息内容
    private String msg;

    public ZwzException(){
        super(DEFAULT_MSG);
        this.msg = DEFAULT_MSG;
    }

    public ZwzException(String msg){
        super(msg);
        this.msg = msg;
    }
}
