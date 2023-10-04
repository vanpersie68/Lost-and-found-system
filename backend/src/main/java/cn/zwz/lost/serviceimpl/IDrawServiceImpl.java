package cn.zwz.lost.serviceimpl;

import cn.zwz.lost.mapper.DrawMapper;
import cn.zwz.lost.entity.Draw;
import cn.zwz.lost.service.IDrawService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 招领 服务层接口实现
 */
@Slf4j
@Service
@Transactional
public class IDrawServiceImpl extends ServiceImpl<DrawMapper, Draw> implements IDrawService {

    @Autowired
    private DrawMapper drawMapper;
}