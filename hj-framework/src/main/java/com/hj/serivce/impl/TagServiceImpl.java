package com.hj.serivce.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hj.domain.entity.Tag;
import com.hj.mapper.TagMapper;
import com.hj.serivce.TagService;
import org.springframework.stereotype.Service;

/**
 * 标签(Tag)表服务实现类
 *
 * @author makejava
 * @since 2023-04-22 19:24:42
 */
@Service("tagService")
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

}

