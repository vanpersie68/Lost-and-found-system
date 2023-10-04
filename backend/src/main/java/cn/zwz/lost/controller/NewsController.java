package cn.zwz.lost.controller;

import cn.zwz.basics.utils.PageUtil;
import cn.zwz.basics.utils.ResultUtil;
import cn.zwz.basics.baseVo.PageVo;
import cn.zwz.basics.baseVo.Result;
import cn.zwz.data.utils.ZwzNullUtils;
import cn.zwz.lost.entity.News;
import cn.zwz.lost.service.INewsService;
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
//@Api(tags = "公告管理接口")
@Api(tags = "News management interface")
@RequestMapping("/zwz/news")
@Transactional
public class NewsController {

    @Autowired
    private INewsService iNewsService;

    @RequestMapping(value = "/getOne", method = RequestMethod.GET)
    //@ApiOperation(value = "查询单条公告")
    @ApiOperation(value = "Search for a single News")
    public Result<News> get(@RequestParam String id){
        return new ResultUtil<News>().setData(iNewsService.getById(id));
    }

    @RequestMapping(value = "/count", method = RequestMethod.GET)
    //@ApiOperation(value = "查询全部公告个数")
    @ApiOperation(value = "Search the num of all News")
    public Result<Long> getCount(){
        return new ResultUtil<Long>().setData(iNewsService.count());
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    //@ApiOperation(value = "查询全部公告")
    @ApiOperation(value = "Search all News")
    public Result<List<News>> getAll(){
        return new ResultUtil<List<News>>().setData(iNewsService.list());
    }

    @RequestMapping(value = "/getByPage", method = RequestMethod.GET)
    //@ApiOperation(value = "查询公告")
    @ApiOperation(value = "Search News")
    public Result<IPage<News>> getByPage(@ModelAttribute News news ,@ModelAttribute PageVo page){
        QueryWrapper<News> qw = new QueryWrapper<>();
        if(!ZwzNullUtils.isNull(news.getTitle())) {
            qw.like("title",news.getTitle());
        }
        if(!ZwzNullUtils.isNull(news.getContent())) {
            qw.like("content",news.getContent());
        }
        IPage<News> data = iNewsService.page(PageUtil.initMpPage(page),qw);
        return new ResultUtil<IPage<News>>().setData(data);
    }

    @RequestMapping(value = "/insertOrUpdate", method = RequestMethod.POST)
    //@ApiOperation(value = "增改公告")
    @ApiOperation(value = "Update News")
    public Result<News> saveOrUpdate(News news){
        if(iNewsService.saveOrUpdate(news)){
            return new ResultUtil<News>().setData(news);
        }
        return ResultUtil.error();
    }

    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    //@ApiOperation(value = "新增公告")
    @ApiOperation(value = "Insert News")
    public Result<News> insert(News news){
        iNewsService.saveOrUpdate(news);
        return new ResultUtil<News>().setData(news);
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    //@ApiOperation(value = "编辑公告")
    @ApiOperation(value = "Edit News")
    public Result<News> update(News news){
        iNewsService.saveOrUpdate(news);
        return new ResultUtil<News>().setData(news);
    }

    @RequestMapping(value = "/delByIds", method = RequestMethod.POST)
    //@ApiOperation(value = "删除公告")
    @ApiOperation(value = "Delete News")
    public Result<Object> delByIds(@RequestParam String[] ids){
        for(String id : ids){
            iNewsService.removeById(id);
        }
        return ResultUtil.success();
    }
}
