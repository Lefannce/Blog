package com.hj.serivce;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hj.domain.ResponseResult;
import com.hj.domain.VO.UpdateArticleVo;
import com.hj.domain.dto.AddArticleDto;
import com.hj.domain.dto.ArticleDto;
import com.hj.domain.entity.Article;
import org.springframework.stereotype.Service;


public interface ArticleService extends IService<Article> {

    ResponseResult hotArticleList();

    ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId);

    ResponseResult getArticleDetail(Long id);

    ResponseResult updateViewCount(Long id);

    ResponseResult add(AddArticleDto article);

    ResponseResult<Article> pageList(Integer pageNum, Integer pageSize, ArticleDto articleDto);

    ResponseResult<UpdateArticleVo> getArticleById(Long id);

    ResponseResult articleUpdate(AddArticleDto articleDto);

    ResponseResult delArticle(Long id);
}
