package cn.zwz.data.vo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;


@Api(tags = "Menu VO category")
@Data
public class MenuVo {

    @ApiModelProperty(value = "menu title")
    private String title;

    @ApiModelProperty(value = "Menu ID")
    private String id;

    @ApiModelProperty(value = "Menu name")
    private String name;

    @ApiModelProperty(value = "Parent menu ID")
    private String parentId;

    @ApiModelProperty(value = "menu hierarchy")
    private Integer level;

    @ApiModelProperty(value = "Whether to force display")
    private Boolean showAlways;

    @ApiModelProperty(value = "Menu Type", notes = "1: Specific Operation, -1: Top-level Menu, 0: Page")
    private Integer type;

    @ApiModelProperty(value = "Component")
    private String component;

    @ApiModelProperty(value = "Page Path")
    private String path;

    @ApiModelProperty(value = "PC Icon")
    private String icon;

    @ApiModelProperty(value = "Button Type")
    private String buttonType;

    @ApiModelProperty(value = "Web Link")
    private String url;

    @ApiModelProperty(value = "List of Sub-Permission Types")
    private List<String> permTypes;

    @ApiModelProperty(value = "List of Child Menus")
    private List<MenuVo> children;
