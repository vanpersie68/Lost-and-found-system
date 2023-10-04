package cn.zwz.basics.code;

import cn.zwz.basics.code.vue.MyBatisPlusUtils;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@ApiOperation(value = "Backend Code Generator Executor Class")  // 后端代码生成器执行类
public class MyBatisPlusCodeUtils {

    @ApiOperation(value = "One-click code generation for CRUD operations")  // 一键产生增删改查代码
    public static void main(String[] args) throws Exception {
        new MyBatisPlusUtils(CLASS_NAME,DESCRIPTION,CLASS_PATH,REMOVE_FLAG).work();
    }

    @ApiModelProperty(value = "Class Name")  // 类名
    private static final String CLASS_NAME = "DynamicTableData";

    @ApiModelProperty(value = "Class Description")  // 类备注
    private static final String DESCRIPTION = "Dynamic Table Values";  // 动态表格值

    @ApiModelProperty(value = "Generation Path")  // 生成路径
    private static final String CLASS_PATH = "cn.zwz.test";

    @ApiModelProperty(value = "Whether to Delete Code")  // 是否删除代码
    private static final Boolean REMOVE_FLAG = false;
}
