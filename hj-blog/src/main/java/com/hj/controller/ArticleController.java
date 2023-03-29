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
/**
 * 热门文章查询
 */
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @GetMapping("/hotArticleList")
    public ResponseResult<Article> hotArticleList(){
        ResponseResult result = articleService.hotArticleList();
    return result;
    }

    /**
     * 首页列表分页查询和上方分类查询
     * @param pageNum 开始页
     * @param pageSize  一页多少条数据
     * @param categoryId   分类id
     * @return
     */
    @GetMapping("/articleList")
    public ResponseResult articleList(Integer pageNum,Integer pageSize,Long categoryId){
    return articleService.articleList(pageNum,pageSize,categoryId);

    }
}

