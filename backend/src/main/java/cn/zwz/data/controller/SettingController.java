package cn.zwz.data.controller;

import cn.zwz.basics.baseVo.Result;
import cn.zwz.basics.log.LogType;
import cn.zwz.basics.log.SystemLog;
import cn.zwz.basics.utils.ResultUtil;
import cn.zwz.data.entity.Setting;
import cn.zwz.data.service.ISettingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;


@RestController
@Api(tags = "Global settings interface")
@RequestMapping("/zwz/setting")
public class SettingController {

    @Autowired
    private ISettingService iSettingService;

    @SystemLog(about = "View a single configuration", type = LogType.DATA_CENTER,doType = "SETTING-01")
    @RequestMapping(value = "/getOne", method = RequestMethod.GET)
    @ApiOperation(value = "View a single configuration")
    public Result<Setting> getOne(@RequestParam String id) {
        return new ResultUtil<Setting>().setData(iSettingService.getById(id));
    }

    @SystemLog(about = "Modify a single configuration", type = LogType.DATA_CENTER,doType = "SETTING-02")
    @RequestMapping(value = "/setOne", method = RequestMethod.GET)
    @ApiOperation(value = "Modify a single configuration")
    public Result<Object> setOne(@RequestParam String id,@RequestParam String value) {
        Setting setting = iSettingService.getById(id);
        if(setting == null) {
            return ResultUtil.error("does not exist");
        }
        if(!Objects.equals(value,setting.getValue())) {
            setting.setValue(value);
            iSettingService.saveOrUpdate(setting);
        }
        return ResultUtil.success();
    }
}
