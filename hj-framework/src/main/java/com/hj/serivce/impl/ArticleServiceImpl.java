package com.hj.serivce.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hj.constants.SystemConstants;
import com.hj.domain.ResponseResult;
import com.hj.domain.VO.*;
import com.hj.domain.dto.AddArticleDto;
import com.hj.domain.dto.ArticleDto;
import com.hj.domain.entity.Article;
import com.hj.domain.entity.ArticleTag;
import com.hj.mapper.ArticleMapper;
import com.hj.serivce.ArticleService;
import com.hj.serivce.ArticleTagService;
import com.hj.serivce.CategoryService;
import com.hj.utils.BeanCopyUtils;
import com.hj.utils.RedisCache;
import io.jsonwebtoken.lang.Strings;
import io.netty.util.internal.ObjectUtil;
import io.swagger.models.auth.In;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

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
@Autowired
private RedisCache redisCache;

@Autowired
private ArticleTagService articleTagService;
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

    /**
     * 查询文章正文
     * 要求在文章列表点击阅读全文时能够跳转到文章详情页面，可以让用户阅读文章正文。
     *
     * 	①要在文章详情中展示其分类名
     *
     * @param id  文章id
     * @return
     */
    @Override
    public ResponseResult getArticleDetail(Long id) {
        //1.根据id查询article对象
        Article article = getById(id);
        //2.把name查询出来,并把数据写入对象
        article.setCategoryName( categoryService.getById(article.getCategoryId()).getName());
        //3.封装vo
        ArticleDetailVo articleDetailVo = BeanCopyUtils.copyBean(article, ArticleDetailVo.class);

        //从redis获取viewCount,写入vo
        Integer cacheMapValue = redisCache.getCacheMapValue("article:viewCount", id.toString());
        articleDetailVo.setViewCount(cacheMapValue.longValue());
        return ResponseResult.okResult(articleDetailVo);
    }

    /**
     * 更新redis中的数据
     * @param id
     * @return
     */
    @Override
    public ResponseResult updateViewCount(Long id) {

         //更新redis中对应 id的浏览量
        redisCache.incrementCacheMapValue("article:viewCount",id.toString(),1);
        return ResponseResult.okResult();
    }

    /**
     * 添加博文
     * @param articleDto
     * @return
     */
    @Override
    @Transactional  //事务
    public ResponseResult add(AddArticleDto articleDto) {
        //添加博客
        //把dto转换为article
        Article article = BeanCopyUtils.copyBean(articleDto, Article.class);
        save(article);

        //把传进来的tag转换为tag实体类
        List<ArticleTag> articleTags = articleDto.getTags().stream()
                .map(tagId -> new ArticleTag(article.getId(), tagId))
                .collect(Collectors.toList());

        //添加 博客和标签的关联
        articleTagService.saveBatch(articleTags);
        return ResponseResult.okResult();
    }

    /**
     * 文章列表分页查询
     * @param pageNum
     * @param pageSize
     * @param articleDto
     * @return
     */
    @Override
    public ResponseResult<Article> pageList(Integer pageNum, Integer pageSize, ArticleDto articleDto) {
        //根据标题和摘要进行模糊查询
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasText(articleDto.getSummary()),Article::getSummary,articleDto.getSummary());
        queryWrapper.like(StringUtils.hasText(articleDto.getTitle()),Article::getTitle,articleDto.getTitle());
        //分页查询
        Page<Article> articlePage = new Page<>();
        articlePage.setCurrent(pageNum);
        articlePage.setSize(pageSize);
        page(articlePage,queryWrapper);
        //封装数据返回
        PageVo pageVo = new PageVo(articlePage.getRecords(), articlePage.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    /**
     * 修改文章,查询文章,回显
     * @param id
     * @return
     */
    @Override
    public ResponseResult<UpdateArticleVo> getArticleById(Long id) {
        Article byId = getById(id);

        //查询文章关联的标签id
        LambdaQueryWrapper<ArticleTag> articleTagLambdaQueryWrapper = new LambdaQueryWrapper<>();
        articleTagLambdaQueryWrapper.eq(ArticleTag::getArticleId,id);
        List<ArticleTag> list = articleTagService.list(articleTagLambdaQueryWrapper);
        List<Long> collect = list.stream()
                .map(articleTag -> articleTag.getTagId()).collect(Collectors.toList());
        UpdateArticleVo updateArticleVo = BeanCopyUtils.copyBean(byId, UpdateArticleVo.class);
        updateArticleVo.setTags(collect);
        return ResponseResult.okResult(updateArticleVo);
    }

    @Override
    public ResponseResult articleUpdate(AddArticleDto articleDto) {
        //更新博客
        Article article = BeanCopyUtils.copyBean(articleDto, Article.class);
        updateById(article);
        //删除原有的tag
        LambdaQueryWrapper<ArticleTag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ArticleTag::getArticleId,article.getId());
        articleTagService.remove(queryWrapper);
        //把传进来的tag转换为tag实体类
        List<ArticleTag> articleTags = articleDto.getTags().stream()
                .map(tagId -> new ArticleTag(article.getId(), tagId))
                .collect(Collectors.toList());

        //添加 博客和标签的关联
        articleTagService.saveBatch(articleTags);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult delArticle(Long id) {
        //删除文章
        removeById(id);
        //删除文章关联tag表
        LambdaQueryWrapper<ArticleTag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ArticleTag::getArticleId,id);
        articleTagService.remove(queryWrapper);
        return ResponseResult.okResult();
    }


}
