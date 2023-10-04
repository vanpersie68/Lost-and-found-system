package cn.zwz.lost.serviceimpl;

import cn.zwz.lost.mapper.LookThingsMapper;
import cn.zwz.lost.entity.LookThings;
import cn.zwz.lost.service.ILookThingsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 寻物 服务层接口实现
 */
@Slf4j
@Service
@Transactional
public class ILookThingsServiceImpl extends ServiceImpl<LookThingsMapper, LookThings> implements ILookThingsService {

    @Autowired
    private LookThingsMapper lookThingsMapper;
}