package cn.zwz.data.vo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Api(tags = "File Storage Configuration VO Class")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OssSettingVo {

    @ApiModelProperty(value = "File View")
    private String fileView;

    @ApiModelProperty(value = "HTTP")
    private String fileHttp;

    @ApiModelProperty(value = "Path")
    private String filePath;
}
