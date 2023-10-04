package cn.zwz.data.utils;

import cn.zwz.data.entity.Permission;
import cn.zwz.data.vo.MenuVo;
import cn.hutool.core.bean.BeanUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


@Api(tags = "Menu conversion VO class")
public class VoUtil {

    @ApiOperation(value = "Menu conversion VO class conversion")
    public static MenuVo permissionToMenuVo(Permission permission){
        MenuVo vo = new MenuVo();
        BeanUtil.copyProperties(permission, vo);
        return vo;
    }
}
