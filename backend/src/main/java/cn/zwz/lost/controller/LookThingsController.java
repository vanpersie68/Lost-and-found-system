package cn.zwz.lost.controller;

import cn.zwz.basics.utils.PageUtil;
import cn.zwz.basics.utils.ResultUtil;
import cn.zwz.basics.baseVo.PageVo;
import cn.zwz.basics.baseVo.Result;
import cn.zwz.basics.utils.SecurityUtil;
import cn.zwz.data.entity.User;
import cn.zwz.data.service.IUserService;
import cn.zwz.data.utils.ZwzNullUtils;
import cn.zwz.lost.entity.LookThings;
import cn.zwz.lost.service.ILookThingsService;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RestController
//@Api(tags = "寻物管理接口")
@Api(tags = "LookThings management interface")
@RequestMapping("/zwz/lookThings")
@Transactional
public class LookThingsController {

    @Autowired
    private ILookThingsService iLookThingsService;

    @Autowired
    private SecurityUtil securityUtil;

    @RequestMapping(value = "/getOne", method = RequestMethod.GET)
    //@ApiOperation(value = "查询单条寻物")
    @ApiOperation(value = "Search for a single item")
    public Result<LookThings> get(@RequestParam String id){
        return new ResultUtil<LookThings>().setData(iLookThingsService.getById(id));
    }

    @RequestMapping(value = "/count", method = RequestMethod.GET)
    //@ApiOperation(value = "查询全部寻物个数")
    @ApiOperation(value = "Search the count of all item")
    public Result<Long> getCount(){
        return new ResultUtil<Long>().setData(iLookThingsService.count());
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    //@ApiOperation(value = "查询全部寻物")
    @ApiOperation(value = "Search all item")
    public Result<List<LookThings>> getAll(){
        return new ResultUtil<List<LookThings>>().setData(iLookThingsService.list());
    }

    @RequestMapping(value = "/getByPage", method = RequestMethod.GET)
    //@ApiOperation(value = "查询寻物")
    @ApiOperation(value = "Search item")
    public Result<IPage<LookThings>> getByPage(@ModelAttribute LookThings lookThings ,@ModelAttribute PageVo page){
        QueryWrapper<LookThings> qw = new QueryWrapper<>();
        if(!ZwzNullUtils.isNull(lookThings.getTitle())) {
            qw.like("title",lookThings.getTitle());
        }
        if(!ZwzNullUtils.isNull(lookThings.getContent())) {
            qw.like("content",lookThings.getContent());
        }
        IPage<LookThings> data = iLookThingsService.page(PageUtil.initMpPage(page),qw);
        return new ResultUtil<IPage<LookThings>>().setData(data);
    }

    @RequestMapping(value = "/getByPage2", method = RequestMethod.GET)
    //@ApiOperation(value = "查询寻物")
    @ApiOperation(value = "Search item")
    public Result<IPage<LookThings>> getByPage2(@ModelAttribute LookThings lookThings ,@ModelAttribute PageVo page){
        QueryWrapper<LookThings> qw = new QueryWrapper<>();
        User currUser = securityUtil.getCurrUser();
        qw.eq("user_id",currUser.getId());
        if(!ZwzNullUtils.isNull(lookThings.getTitle())) {
            qw.like("title",lookThings.getTitle());
        }
        if(!ZwzNullUtils.isNull(lookThings.getContent())) {
            qw.like("content",lookThings.getContent());
        }
        IPage<LookThings> data = iLookThingsService.page(PageUtil.initMpPage(page),qw);
        return new ResultUtil<IPage<LookThings>>().setData(data);
    }

    @RequestMapping(value = "/insertOrUpdate", method = RequestMethod.POST)
    //@ApiOperation(value = "增改寻物")
    @ApiOperation(value = "update item")
    public Result<LookThings> saveOrUpdate(LookThings lookThings){
        if(iLookThingsService.saveOrUpdate(lookThings)){
            return new ResultUtil<LookThings>().setData(lookThings);
        }
        return ResultUtil.error();
    }

    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    //@ApiOperation(value = "新增寻物")
    @ApiOperation(value = "insert item")
    public Result<LookThings> insert(LookThings lookThings){
        User currUser = securityUtil.getCurrUser();
        lookThings.setUserId(currUser.getId());
        iLookThingsService.saveOrUpdate(lookThings);
        return new ResultUtil<LookThings>().setData(lookThings);
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    //@ApiOperation(value = "编辑寻物")
    @ApiOperation(value = "edit item")
    public Result<LookThings> update(LookThings lookThings){
        iLookThingsService.saveOrUpdate(lookThings);
        return new ResultUtil<LookThings>().setData(lookThings);
    }

    @RequestMapping(value = "/delByIds", method = RequestMethod.POST)
    //@ApiOperation(value = "删除寻物")
    @ApiOperation(value = "delete item")
    public Result<Object> delByIds(@RequestParam String[] ids){
        for(String id : ids){
            iLookThingsService.removeById(id);
        }
        return ResultUtil.success();
    }
}
