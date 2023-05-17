package com.hj.serivce;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hj.domain.entity.ArticleTag;

import java.util.List;


/**
 * 文章标签关联表(ArticleTag)表服务接口
 *
 * @author makejava
 * @since 2023-05-09 15:17:38
 */
public interface ArticleTagService extends IService<ArticleTag> {

    List<Long> getTagsById(Long id);
}

