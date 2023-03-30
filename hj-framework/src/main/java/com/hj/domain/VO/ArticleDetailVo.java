package com.hj.domain.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleDetailVo {
    private Long id;

    //所属分类id
    private Long categoryId;

    private String categoryName;

    //文章内容
    private String content;
    //创建时间
    private Date createTime;

    //是否允许评论
    private String isComment;

    //标题
    private String title;

    //访问量
    private Long viewCount;
}
