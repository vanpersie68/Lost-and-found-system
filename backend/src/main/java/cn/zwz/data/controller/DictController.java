package cn.zwz.data.controller;

import cn.zwz.basics.log.LogType;
import cn.zwz.basics.log.SystemLog;
import cn.zwz.basics.utils.ResultUtil;
import cn.zwz.basics.baseVo.Result;
import cn.zwz.data.entity.Dict;
import cn.zwz.data.entity.DictData;
import cn.zwz.data.service.IDictDataService;
import cn.zwz.data.service.IDictService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;


@RestController
@Api(tags = "Dictionary management interface") //字典管理接口
@RequestMapping("/zwz/dict")
@Transactional
public class DictController {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private IDictService iDictService;

    @Autowired
    private IDictDataService iDictDataService;


    @SystemLog(about = "Query all data dictionaries", type = LogType.DATA_CENTER,doType = "DICT-01")
    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    @ApiOperation(value = "Query all data dictionaries")
    public Result<List<Dict>> getAll(){
        return new ResultUtil<List<Dict>>().setData(iDictService.list());
    }

    @SystemLog(about = "Simulate Search Data Dictionary", type = LogType.DATA_CENTER,doType = "DICT-02")
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    @ApiOperation(value = "Simulate Search Data Dictionary")
    public Result<List<Dict>> search(@RequestParam String key){
        QueryWrapper<Dict> qw = new QueryWrapper<>();
        qw.like("title",key);
        return new ResultUtil<List<Dict>>().setData(iDictService.list(qw));
    }

    @SystemLog(about = "Add data dictionary", type = LogType.DATA_CENTER,doType = "DICT-03")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation(value = "Add data dictionary")
    public Result<Object> add(Dict dict){
        QueryWrapper<Dict> qw = new QueryWrapper<>();
        qw.eq("type",dict.getType());
        long dictCount = iDictService.count(qw);
        if(dictCount < 1L) {
            iDictService.saveOrUpdate(dict);
            return ResultUtil.success();
        }
        return ResultUtil.error("字典已存在,不能同名"); //字典已存在,不能同名
    }

    @SystemLog(about = "Edit data dictionary", type = LogType.DATA_CENTER,doType = "DICT-04")
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation(value = "Edit data dictionary")
    public Result<Object> edit(Dict dict){
        Dict oldDict = iDictService.getById(dict.getId());
        if(oldDict == null) {
            return ResultUtil.error("Dictionary has been deleted"); //字典已被删除
         }
        if(!Objects.equals(dict.getType(),oldDict.getType())){
            QueryWrapper<Dict> qw = new QueryWrapper<>();
            qw.eq("type",dict.getType());
            long dictCount = iDictService.count(qw);
            if(dictCount > 0L) {
                return ResultUtil.error("The dictionary already exists and cannot have the same name"); //字典已存在,不能同名
            }
        }
        iDictService.saveOrUpdate(dict);
        return ResultUtil.success();
    }

    @SystemLog(about = "Delete data dictionary", type = LogType.DATA_CENTER,doType = "DICT-05")
    @RequestMapping(value = "/delByIds", method = RequestMethod.POST)
    @ApiOperation(value = "Delete data dictionary")
    public Result<Object> delByIds(@RequestParam String[] ids){
        for (String distId : ids){
            Dict selectDict = iDictService.getById(distId);
            if(selectDict == null) {
                continue;
            }
            iDictService.removeById(distId);
            QueryWrapper<DictData> qw = new QueryWrapper<>();
            qw.eq("dict_id",distId);
            iDictDataService.remove(qw);
            redisTemplate.delete("dictData::" + selectDict.getType());
        }
        return ResultUtil.success();
    }
}