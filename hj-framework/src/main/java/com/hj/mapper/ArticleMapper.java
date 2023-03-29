package com.hj.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hj.domain.entity.Article;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface ArticleMapper extends BaseMapper<Article> {

}
