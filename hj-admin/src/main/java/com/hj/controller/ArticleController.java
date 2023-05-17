package com.hj.controller;

import com.hj.domain.ResponseResult;
import com.hj.domain.VO.UpdateArticleVo;
import com.hj.domain.dto.AddArticleDto;
import com.hj.domain.dto.ArticleDto;
import com.hj.domain.entity.Article;
import com.hj.serivce.ArticleService;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/content/article")
public class ArticleController {


    @Autowired
    private ArticleService articleService;
    @PostMapping
    public ResponseResult add(@RequestBody AddArticleDto articleDto) {

       return articleService.add(articleDto);

    }

    @GetMapping("/list")
    public ResponseResult<Article> ArticleList(Integer pageNum, Integer pageSize, ArticleDto articleDto){
    return articleService.pageList(pageNum,pageSize,articleDto);

    }

    /**
     * 根据id查询文章的信息,包括关联标签tag的id
     * @param id
     * @return
     */
    @GetMapping("{id}")
    public ResponseResult<UpdateArticleVo> getArticleById(@PathVariable Long id){
       return articleService.getArticleById(id);

    }

    /**
     * 更新文章消息
     * @param articleDto
     * @return
     */
     @PutMapping
    public ResponseResult update(@RequestBody AddArticleDto articleDto) {

       return articleService.articleUpdate(articleDto);

    }

    @DeleteMapping("{id}")
    public ResponseResult delete(@PathVariable Long id){
        return articleService.delArticle(id);
    }



}
