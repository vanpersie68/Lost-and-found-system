package cn.zwz.test.controller;

import cn.zwz.basics.log.LogType;
import cn.zwz.basics.log.SystemLog;
import cn.zwz.basics.utils.PageUtil;
import cn.zwz.basics.utils.ResultUtil;
import cn.zwz.basics.baseVo.PageVo;
import cn.zwz.basics.baseVo.Result;
import cn.zwz.data.utils.ZwzNullUtils;
import cn.zwz.data.vo.AntvVo;
import cn.zwz.test.entity.Teacher;
import cn.zwz.test.service.ITeacherService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@RestController
//@Api(tags = "教师管理接口")
@Api(tags = "Teacher management interface")
@RequestMapping("/zwz/teacher")
@Transactional
public class TeacherController {

    @Autowired
    private ITeacherService iTeacherService;

    @SystemLog(about = "Search for one teacher", type = LogType.MORE_MOUDLE,doType = "TEACHER-01")
    @RequestMapping(value = "/getOne", method = RequestMethod.GET)
    //@ApiOperation(value = "查询单条教师")
    @ApiOperation(value = "Search for one teacher")
    public Result<Teacher> get(@RequestParam String id){
        return new ResultUtil<Teacher>().setData(iTeacherService.getById(id));
    }

    @SystemLog(about = "Search the num of teacher", type = LogType.MORE_MOUDLE,doType = "TEACHER-02")
    @RequestMapping(value = "/count", method = RequestMethod.GET)
    //@ApiOperation(value = "查询全部教师个数")
    @ApiOperation(value = "Search the num of teacher")
    public Result<Long> getCount(){
        return new ResultUtil<Long>().setData(iTeacherService.count());
    }

    @SystemLog(about = "Search all teacher", type = LogType.MORE_MOUDLE,doType = "TEACHER-03")
    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    //@ApiOperation(value = "查询全部教师")
    @ApiOperation(value = "Search all teacher")
    public Result<List<Teacher>> getAll(){
        return new ResultUtil<List<Teacher>>().setData(iTeacherService.list());
    }

    @SystemLog(about = "Search teacher", type = LogType.MORE_MOUDLE,doType = "TEACHER-04")
    @RequestMapping(value = "/getByPage", method = RequestMethod.GET)
    //@ApiOperation(value = "查询教师")
    @ApiOperation(value = "Search teacher")
    public Result<IPage<Teacher>> getByPage(@ModelAttribute Teacher teacher ,@ModelAttribute PageVo page){
        QueryWrapper<Teacher> qw = new QueryWrapper<>();
        if(!ZwzNullUtils.isNull(teacher.getName())) {
            qw.like("name",teacher.getName());
        }
        if(!ZwzNullUtils.isNull(teacher.getEducation())) {
            qw.eq("education",teacher.getEducation());
        }
        if(!ZwzNullUtils.isNull(teacher.getGraduated())) {
            qw.like("graduated",teacher.getGraduated());
        }
        IPage<Teacher> data = iTeacherService.page(PageUtil.initMpPage(page),qw);
        return new ResultUtil<IPage<Teacher>>().setData(data);
    }

    @SystemLog(about = "Update teacher", type = LogType.MORE_MOUDLE,doType = "TEACHER-05")
    @RequestMapping(value = "/insertOrUpdate", method = RequestMethod.POST)
    //@ApiOperation(value = "增改教师")
    @ApiOperation(value = "Update teacher")
    public Result<Teacher> saveOrUpdate(Teacher teacher){
        if(iTeacherService.saveOrUpdate(teacher)){
            return new ResultUtil<Teacher>().setData(teacher);
        }
        return ResultUtil.error();
    }

    @SystemLog(about = "Insert teacher", type = LogType.MORE_MOUDLE,doType = "TEACHER-06")
    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    //@ApiOperation(value = "新增教师")
    @ApiOperation(value = "Insert teacher")
    public Result<Teacher> insert(Teacher teacher){
        iTeacherService.saveOrUpdate(teacher);
        return new ResultUtil<Teacher>().setData(teacher);
    }

    @SystemLog(about = "Edit teacher", type = LogType.MORE_MOUDLE,doType = "TEACHER-07")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    //@ApiOperation(value = "编辑教师")
    @ApiOperation(value = "Edit teacher")
    public Result<Teacher> update(Teacher teacher){
        iTeacherService.saveOrUpdate(teacher);
        return new ResultUtil<Teacher>().setData(teacher);
    }

    //@SystemLog(about = "Delete teacher", type = LogType.MORE_MOUDLE,doType = "TEACHER-08")
    @RequestMapping(value = "/delByIds", method = RequestMethod.POST)
    @ApiOperation(value = "Delete teacher")
    public Result<Object> delByIds(@RequestParam String[] ids){
        for(String id : ids){
            iTeacherService.removeById(id);
        }
        return ResultUtil.success();
    }

    @SystemLog(about = "Search graph data", type = LogType.CHART,doType = "CHART-01")
    @RequestMapping(value = "/getAntvVoList", method = RequestMethod.GET)
    //@ApiOperation(value = "查询图表数据")
    @ApiOperation(value = "Search graph data")
    public Result<List<AntvVo>> getAntvVoList(){
        List<AntvVo> ansList = new ArrayList<>();
        List<Teacher> teacherList = iTeacherService.list();
        for (Teacher o : teacherList) {
            boolean flag = false;
            for (AntvVo vo : ansList) {
                if(Objects.equals(vo.getTitle(),o.getName())) {
                    flag = true;
                    vo.setValue(vo.getValue().add(o.getWages()));
                    break;
                }
            }
            if(!flag) {
                AntvVo vo = new AntvVo();
                vo.setTitle(o.getName());
                vo.setType("工资金额");
                vo.setValue(o.getWages());
                ansList.add(vo);
            }
        }
        return new ResultUtil<List<AntvVo>>().setData(ansList);
    }
}
