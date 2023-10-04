package cn.zwz.data.vo;

import io.swagger.annotations.Api;
import lombok.Data;

import java.math.BigDecimal;


@Api(tags = "Chart VO class")
@Data
public class AntvVo {
    private String title;
    private String type;
    private BigDecimal value;
}

