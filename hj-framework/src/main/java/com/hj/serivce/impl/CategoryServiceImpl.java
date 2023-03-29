package com.hj.serivce.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hj.constants.SystemConstants;
import com.hj.domain.ResponseResult;
import com.hj.domain.VO.CategoryVo;
import com.hj.domain.entity.Article;
import com.hj.domain.entity.Category;
import com.hj.mapper.CategoryMapper;
import com.hj.serivce.ArticleService;
import com.hj.serivce.CategoryService;
import com.hj.utils.BeanCopyUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.hj.constants.SystemConstants.ARTICLE_STATUS_NORMAL;
import static com.hj.constants.SystemConstants.STATUS_NORMAL;

/**
 * 分类表(Category)表服务实现类
 * ①要求只展示有发布正式文章的分类
 * ②必须是正常状态的分类
 *
 * @author makejava
 * @since 2023-03-29 12:39:58
 */
@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    private ArticleService articleService;
    @Autowired
    private CategoryService categoryService;

    @Override
    public ResponseResult getCategoryList() {
        //先查询文章表,状态为已发布的(status为0)
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Article::getStatus,ARTICLE_STATUS_NORMAL);
        List<Article> articleList = articleService.list(queryWrapper);

        //获取文章分类id并去重(set集合)
        Set<Long> categoryId = articleList.stream()
                .map(article -> article.getCategoryId())//把输入article返回CategoryId
                .collect(Collectors.toSet());
        //然后根据查询的文章去查询分类表,查出有文章的分类
        List<Category> categories = categoryService.listByIds(categoryId);

        //状态为正常的
        List<Category> list = listByIds(categoryId);
        List<Category> collect = list.stream()
                .filter(category -> STATUS_NORMAL.equals(category.getStatus()))
                .collect(Collectors.toList());
        //VO
        List<CategoryVo> categoryVos = BeanCopyUtils.copyBeanList(collect, CategoryVo.class);

        return ResponseResult.okResult(categoryVos);
    }
}

