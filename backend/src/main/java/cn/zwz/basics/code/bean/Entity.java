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
public class Entity {

    @ApiModelProperty(value = "Controller layer package")  // 控制器层包
    private String controllerPackage;

    @ApiModelProperty(value = "Service layer implementation package")  // 服务层实现包
    private String serviceImplPackage;

    @ApiModelProperty(value = "Service layer interface package")  // 服务层接口包
    private String servicePackage;

    @ApiModelProperty(value = "Data link layer package")  // 数据链路层包
    private String daoPackage;

    @ApiModelProperty(value = "Entity VO package")  // 实体VO包
    private String entityPackage;

    @ApiModelProperty(value = "Class name")  // 类名
    private String className;

    @ApiModelProperty(value = "Class name in lowercase")  // 类名小写
    private String classNameLowerCase;

    @ApiModelProperty(value = "Notes")  // 备注
    private String description;

    @ApiModelProperty(value = "Author")  // 作者
    private String author;

    @ApiModelProperty(value = "Table name")  // 表名
    private String tableName;

    @ApiModelProperty(value = "Primary key data type")  // 主键数据类型
    private String primaryKeyType;

    @ApiModelProperty(value = "Is it tree-structured")  // 是否树状
    private Boolean isTree;

    @ApiModelProperty(value = "Is it a process")  // 是否流程
    private Boolean activiti;
}

