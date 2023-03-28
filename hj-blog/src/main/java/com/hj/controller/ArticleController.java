package com.hj.controller;

import com.hj.domain.ResponseResult;
import com.hj.domain.entity.Article;
import com.hj.serivce.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @GetMapping("/hotArticleList")
    public ResponseResult<Article> hotArticleList(){
        ResponseResult result = articleService.hotArticleList();
    return result;
    }
}

