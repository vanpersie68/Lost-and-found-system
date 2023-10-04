package cn.zwz.basics.code.bean;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@ApiOperation(value = "Code Generator Base Class")  // 代码生成器基础类
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Item {

    @ApiModelProperty(value = "Sub-item title")  // 子项标题
    private String title;

    @ApiModelProperty(value = "Sub-item category")  // 子项类别
    private String type;

    @ApiModelProperty(value = "Sub-item in uppercase")  // 子项大写
    private String upperName;

    @ApiModelProperty(value = "Sub-item in lowercase")  // 子项小写
    private String lowerName;

    @ApiModelProperty(value = "Sub-item row name")  // 子项行名
    private String lineName;
}

