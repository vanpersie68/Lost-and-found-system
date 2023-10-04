package cn.zwz.lost.controller;

import cn.zwz.basics.utils.PageUtil;
import cn.zwz.basics.utils.ResultUtil;
import cn.zwz.basics.baseVo.PageVo;
import cn.zwz.basics.baseVo.Result;
import cn.zwz.basics.utils.SecurityUtil;
import cn.zwz.data.entity.User;
import cn.zwz.data.service.IUserService;
import cn.zwz.data.utils.ZwzNullUtils;
import cn.zwz.lost.entity.Draw;
import cn.zwz.lost.service.IDrawService;
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
//@Api(tags = "招领管理接口")
@Api(tags = "Draw management interface")
@RequestMapping("/zwz/draw")
@Transactional
public class DrawController {

    @Autowired
    private IDrawService iDrawService;

    @Autowired
    private SecurityUtil securityUtil;

    @RequestMapping(value = "/getOne", method = RequestMethod.GET)
    //@ApiOperation(value = "查询单条招领")
    @ApiOperation(value = "Search for a single find")
    public Result<Draw> get(@RequestParam String id){
        return new ResultUtil<Draw>().setData(iDrawService.getById(id));
    }

    @RequestMapping(value = "/count", method = RequestMethod.GET)
    //@ApiOperation(value = "查询全部招领个数")
    @ApiOperation(value = "Search the number of all find")
    public Result<Long> getCount(){
        return new ResultUtil<Long>().setData(iDrawService.count());
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    //@ApiOperation(value = "查询全部招领")
    @ApiOperation(value = "Search for all find")
    public Result<List<Draw>> getAll(){
        return new ResultUtil<List<Draw>>().setData(iDrawService.list());
    }

    @RequestMapping(value = "/getByPage", method = RequestMethod.GET)
    //@ApiOperation(value = "查询招领")
    @ApiOperation(value = "Search find")
    public Result<IPage<Draw>> getByPage(@ModelAttribute Draw draw ,@ModelAttribute PageVo page){
        QueryWrapper<Draw> qw = new QueryWrapper<>();
        if(!ZwzNullUtils.isNull(draw.getTitle())) {
            qw.like("title",draw.getTitle());
        }
        if(!ZwzNullUtils.isNull(draw.getContent())) {
            qw.like("content",draw.getContent());
        }
        IPage<Draw> data = iDrawService.page(PageUtil.initMpPage(page),qw);
        return new ResultUtil<IPage<Draw>>().setData(data);
    }

    @RequestMapping(value = "/getByPage2", method = RequestMethod.GET)
    //@ApiOperation(value = "查询招领")
    @ApiOperation(value = "Search find")
    public Result<IPage<Draw>> getByPage2(@ModelAttribute Draw draw ,@ModelAttribute PageVo page){
        QueryWrapper<Draw> qw = new QueryWrapper<>();
        User currUser = securityUtil.getCurrUser();
        qw.eq("user_id",currUser.getId());
        if(!ZwzNullUtils.isNull(draw.getTitle())) {
            qw.like("title",draw.getTitle());
        }
        if(!ZwzNullUtils.isNull(draw.getContent())) {
            qw.like("content",draw.getContent());
        }
        IPage<Draw> data = iDrawService.page(PageUtil.initMpPage(page),qw);
        return new ResultUtil<IPage<Draw>>().setData(data);
    }

    @RequestMapping(value = "/insertOrUpdate", method = RequestMethod.POST)
    //@ApiOperation(value = "增改招领")
    @ApiOperation(value = "Update find")
    public Result<Draw> saveOrUpdate(Draw draw){
        if(iDrawService.saveOrUpdate(draw)){
            return new ResultUtil<Draw>().setData(draw);
        }
        return ResultUtil.error();
    }

    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    //@ApiOperation(value = "新增招领")
    @ApiOperation(value = "Insert find")
    public Result<Draw> insert(Draw draw){
        User currUser = securityUtil.getCurrUser();
        draw.setUserId(currUser.getId());
        iDrawService.saveOrUpdate(draw);
        return new ResultUtil<Draw>().setData(draw);
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    //@ApiOperation(value = "编辑招领")
    @ApiOperation(value = "Edit find")
    public Result<Draw> update(Draw draw){
        iDrawService.saveOrUpdate(draw);
        return new ResultUtil<Draw>().setData(draw);
    }

    @RequestMapping(value = "/delByIds", method = RequestMethod.POST)
    //@ApiOperation(value = "删除招领")
    @ApiOperation(value = "Delete find")
    public Result<Object> delByIds(@RequestParam String[] ids){
        for(String id : ids){
            iDrawService.removeById(id);
        }
        return ResultUtil.success();
    }
}
