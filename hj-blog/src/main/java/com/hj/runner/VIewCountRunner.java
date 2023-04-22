package com.hj.runner;

import com.hj.domain.entity.Article;
import com.hj.mapper.ArticleMapper;
import com.hj.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component

/**
 * 每次启动程序的初始化操作
 */
public class VIewCountRunner implements CommandLineRunner {
    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private RedisCache redisCache;
    @Override
    public void run(String... args) throws Exception {
        //查询博客信息,id viewCount

        List<Article> articles = articleMapper.selectList(null);
        //通过toMap把key设置为id,值为,viewCount然后转换为int类型,收集为Map
        Map<String, Integer> viewCountMap = articles.stream()
                .collect(Collectors.toMap(article -> article.getId().toString(), article -> {
                    return article.getViewCount().intValue();//
                }));

        //写入redis
        redisCache.setCacheMap("article:viewCount",viewCountMap);
    }
}
