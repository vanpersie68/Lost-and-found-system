package cn.zwz.basics.code.bean;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

import java.math.BigDecimal;


@ApiOperation(value = "Code Generator Base Class")  // 代码生成器基础类
@Data
public class Field {

    @ApiModelProperty(value = "Field name")  // 字段名称
    private String name;

    @ApiModelProperty(value = "Field identifier")  // 字段标识
    private String field;

    @ApiModelProperty(value = "Field level")  // 字段层级
    private String level;

    @ApiModelProperty(value = "Is it displayed in the table")  // 是否在表格显示
    private Boolean tableShow;

    @ApiModelProperty(value = "Sort value")  // 排序值
    private BigDecimal sortOrder;

    @ApiModelProperty(value = "Is it searchable")  // 是否可搜索
    private Boolean searchable;

    @ApiModelProperty(value = "Is it addable/editable")  // 是否可添加编辑
    private Boolean editable;

    @ApiModelProperty(value = "Field type")  // 字段类型
    private String type;

    @ApiModelProperty(value = "Search level")  // 搜索层级
    private String searchLevel;

    @ApiModelProperty(value = "Search bar type")  // 搜索栏类型
    private String searchType;

    @ApiModelProperty(value = "Default sort rule")  // 默认排序规则
    private String defaultSortType;

    @ApiModelProperty(value = "Field validation")  // 字段
    private Boolean validate;

    @ApiModelProperty(value = "Is it the default sort")  // 是否为默认排序
    private Boolean defaultSort;

    @ApiModelProperty(value = "Is it sortable")  // 是否可排序
    private Boolean sortable;
}
