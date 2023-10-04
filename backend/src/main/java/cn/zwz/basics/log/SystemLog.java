package cn.zwz.basics.log;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;

import java.lang.annotation.*;


@ApiOperation(value = "Log Entity Class")  // 日志实体类
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SystemLog {

        @ApiModelProperty(value = "Log Name")  // 日志名称
        public String about() default "System Log";  // 系统日志

        @ApiModelProperty(value = "Log Type")  // 日志类型
        public LogType type() default LogType.DEFAULT_OPERATION;

        @ApiModelProperty(value = "Operation Code")  // 操作代码
        String doType() default "";
}
