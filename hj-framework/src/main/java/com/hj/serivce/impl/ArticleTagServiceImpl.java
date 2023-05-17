package com.hj.serivce.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hj.domain.entity.ArticleTag;
import com.hj.mapper.ArticleTagMapper;
import com.hj.serivce.ArticleTagService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 文章标签关联表(ArticleTag)表服务实现类
 *
 * @author makejava
 * @since 2023-05-09 15:17:38
 */
@Service("articleTagService")
public class ArticleTagServiceImpl extends ServiceImpl<ArticleTagMapper, ArticleTag> implements ArticleTagService {

    @Override
    public List<Long> getTagsById(Long id) {

        LambdaQueryWrapper<ArticleTag> q = new LambdaQueryWrapper<>();
        q.eq(ArticleTag::getArticleId,id);
        List<ArticleTag> list = list(q);

        //把实体类中的article转换为list集合
        List<Long> Tags = list.stream()
                .map(ArticleTag::getArticleId)
                .collect(Collectors.toList());
        return Tags;
    }
}

