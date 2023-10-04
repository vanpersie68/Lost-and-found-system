package cn.zwz.basics.baseVo;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@ApiOperation(value = "Pagination VO Class")  // 分页VO类
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "Sort Name", notes = "Field name for sorting")  // 排序名称 排序的字段名
    private String sort;

    @ApiModelProperty(value = "Page Number", notes = "That is, display which page")  // 页码编号 即展示第几页
    private int pageNumber;

    @ApiModelProperty(value = "Sort Type", notes = "Ascending (asc), Descending (desc)")  // 排序类型 升序asc,降序desc
    private String order;

    @ApiModelProperty(value = "Number per page", notes = "Suggested setting is 15")  // 每页个数  建议设置为15
    private int pageSize;
}

