package cn.zwz.basics.baseClass;

import cn.zwz.basics.utils.PageUtil;
import cn.zwz.basics.utils.ResultUtil;
import cn.zwz.basics.baseVo.PageVo;
import cn.zwz.basics.baseVo.Result;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;


@ApiOperation(value = "Template Controller Layer")  // 模板控制器层
public abstract class ZwzBaseController<E, ID extends Serializable> {

    @Autowired
    public abstract ZwzBaseService<E,ID> getZwzService();

    @RequestMapping(value = "/getOne", name = "Query single data", method = RequestMethod.GET)  // 查询单个数据
    @ResponseBody
    @ApiOperation(value = "Query single data")  // 查询单个数据
    public Result<E> getOne(@RequestParam ID id){
        return new ResultUtil<E>().setData(getZwzService().get(id));
    }

    @RequestMapping(value = "/getAll", name = "Query all data",  method = RequestMethod.GET)  // 查询全部数据
    @ResponseBody
    @ApiOperation(value = "Query all data")  // 查询全部数据
    public Result<List<E>> getAll(){
        return new ResultUtil<List<E>>().setData(getZwzService().getAll());
    }

    @RequestMapping(value = "/getByPage", name = "Query data",  method = RequestMethod.GET)  // 查询数据
    @ResponseBody
    @ApiOperation(value = "Query data")  // 查询数据
    public Result<Page<E>> getByPage(PageVo page){
        return new ResultUtil<Page<E>>().setData(getZwzService().findAll(PageUtil.initPage(page)));
    }

    @RequestMapping(value = "/save", name = "Add data",  method = RequestMethod.POST)  // 新增数据
    @ResponseBody
    @ApiOperation(value = "Add data")  // 新增数据
    public Result<E> save(E entity){
        return new ResultUtil<E>().setData(getZwzService().save(entity));
    }

    @RequestMapping(value = "/update", name = "Edit data",  method = RequestMethod.PUT)  // 编辑数据
    @ResponseBody
    @ApiOperation(value = "Edit data")  // 编辑数据
    public Result<E> update(E entity){
        return new ResultUtil<E>().setData(getZwzService().update(entity));
    }

    @RequestMapping(value = "/count", name = "Query data count",  method = RequestMethod.POST)  // 查询数据条数
    @ResponseBody
    @ApiOperation(value = "Query data count")  // 查询数据条数
    public Result<Long> count(){
        return new ResultUtil<Long>().setData(getZwzService().count());
    }
    @RequestMapping(value = "/delOne", name = "Delete data",  method = RequestMethod.POST)  // 删除数据
    @ResponseBody
    @ApiOperation(value = "Delete data")  // 删除数据
    public Result<Object> delByIds(@RequestParam ID id){
        getZwzService().delete(id);
        return new ResultUtil<Object>().setSuccessMsg("OK");
    }

    @RequestMapping(value = "/delByIds", name = "Delete data",  method = RequestMethod.POST)  // 删除数据
    @ResponseBody
    @ApiOperation(value = "Delete data")  // 删除数据
    public Result<Object> delByIds(@RequestParam ID[] ids){
        for(ID id:ids){
            getZwzService().delete(id);
        }
        return new ResultUtil<Object>().setSuccessMsg("OK");
    }
}
