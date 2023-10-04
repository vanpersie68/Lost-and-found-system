package cn.zwz.data.utils;

import cn.zwz.data.vo.OssSettingVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;


@Api(tags = "File configuration interface class")
public interface FileManage {

    @ApiOperation(value = "Delete Files")
    void deleteFile(String key);

    @ApiOperation(value = "Rename file")
    String renameFile(String fromKey, String toKey);

    @ApiOperation(value = "Get configuration")
    OssSettingVo getOssSetting();

    @ApiOperation(value = "Copy files")
    String copyFile(String fromKey, String toKey);

    @ApiOperation(value = "File stream upload")
    String inputStreamUpload(InputStream inputStream, String key, MultipartFile file);
}
