package cn.zwz.data.serviceimpl;

import cn.zwz.data.dao.mapper.DictMapper;
import cn.zwz.data.entity.Dict;
import cn.zwz.data.service.IDictService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;


@Service
public class IDictServiceImpl extends ServiceImpl<DictMapper, Dict> implements IDictService {

}
