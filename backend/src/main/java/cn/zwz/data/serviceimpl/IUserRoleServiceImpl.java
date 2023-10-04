package cn.zwz.data.serviceimpl;

import cn.zwz.data.dao.mapper.UserRoleMapper;
import cn.zwz.data.entity.UserRole;
import cn.zwz.data.service.IUserRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;


@Service
public class IUserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements IUserRoleService {

}
