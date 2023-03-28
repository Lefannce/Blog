package com.hj.serivce;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hj.domain.ResponseResult;
import com.hj.domain.entity.Article;
import org.springframework.stereotype.Service;


public interface ArticleService extends IService<Article> {
    ResponseResult hotArticleList();
}
