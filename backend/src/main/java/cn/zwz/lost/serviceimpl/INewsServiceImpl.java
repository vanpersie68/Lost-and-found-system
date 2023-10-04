package cn.zwz.lost.serviceimpl;

import cn.zwz.lost.mapper.NewsMapper;
import cn.zwz.lost.entity.News;
import cn.zwz.lost.service.INewsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 公告 服务层接口实现
 */
@Slf4j
@Service
@Transactional
public class INewsServiceImpl extends ServiceImpl<NewsMapper, News> implements INewsService {

    @Autowired
    private NewsMapper newsMapper;
}