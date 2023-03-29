package com.hj.serivce.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hj.constants.SystemConstants;
import com.hj.domain.ResponseResult;
import com.hj.domain.VO.ArticleListVo;
import com.hj.domain.VO.HotArticleListVo;
import com.hj.domain.VO.PageVo;
import com.hj.domain.entity.Article;
import com.hj.mapper.ArticleMapper;
import com.hj.serivce.ArticleService;
import com.hj.serivce.CategoryService;
import com.hj.utils.BeanCopyUtils;
import io.netty.util.internal.ObjectUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.stream.Collectors;

import static com.hj.constants.SystemConstants.ARTICLE_STATUS_NORMAL;
import static com.hj.constants.SystemConstants.STATUS_NORMAL;

@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService{

@Autowired
private CategoryService categoryService;
    @Override
    public ResponseResult hotArticleList() {
        //查询热门文章 封装成ResponseResult返回
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        //必须是正式文章
        queryWrapper.eq(Article::getStatus,ARTICLE_STATUS_NORMAL);
        //按照浏览量进行排序
        queryWrapper.orderByDesc(Article::getViewCount);
        //最多只查询10条(分页查询)
        Page<Article> page = new Page<>(1,10);
        //查询
        page(page,queryWrapper);
        //获取page查询出来的数据
        List<Article> list = page.getRecords();

        //遍历list中的数据拷贝到vo中,然后把vo对象加入hotArticleListVos中
//        ArrayList<HotArticleListVo> hotArticleListVos = new ArrayList<>();
//        for (Article article : list) {
//            HotArticleListVo vo = new HotArticleListVo();
//            BeanUtils.copyProperties(article,vo);
//            hotArticleListVos.add(vo);
//        }

        //使用工具类
        List<HotArticleListVo> hotArticleListVos = BeanCopyUtils.copyBeanList(list, HotArticleListVo.class);
        return ResponseResult.okResult(hotArticleListVos);
    }


    /**
     * 	在首页和分类页面都需要查询文章列表。
     * 	首页：查询所有的文章
     * 	分类页面：查询对应分类下的文章
     * 	要求：①只能查询正式发布的文章 ②置顶的文章要显示在最前面
     * @param pageNum
     * @param pageSize
     * @param categoryId
     * @return
     */
    @Override
    public ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        //如果有categoryId 就要查询是传入
        queryWrapper.eq(Objects.nonNull(categoryId)&&categoryId>0,Article::getCategoryId,categoryId);
        //状态是正式发布的
        queryWrapper.eq(Article::getStatus,ARTICLE_STATUS_NORMAL);
        //对isTop进行排序
        queryWrapper.orderByDesc(Article::getIsTop);
        //分页查询
        Page<Article> page = new Page<>(pageNum,pageSize);
        page(page,queryWrapper);
        //categoryName没有,需要重新查询
         List<Article> articles = page.getRecords();

         articles.stream()
                 .map(article ->
                 article.setCategoryName(                   //4.设置为name(需要把set方法返回值改为返回本身)
                         categoryService.getById(         //2.根据id查询category对象
                                 article.getCategoryId()//1.获取categoryId
                         ).getName()))                   //3.获取对象的name
                 .collect(Collectors.toList());


        //封装VO
        List<ArticleListVo> articleListVos = BeanCopyUtils.copyBeanList(articles, ArticleListVo.class);
        PageVo pageVo = new PageVo(articleListVos,page.getTotal());
        return ResponseResult.okResult(pageVo);
    }


}
