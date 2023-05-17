package com.hj.domain.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateArticleVo {
    private Long id;

    private String title;

    private String summary;

    private String thumbnail;

    private Long viewCount;


    private Date createTime;


    private Date updateTime;

    private String isComment;

    private String updateBy;

    private String createBy;

    private String isTop;

    private String status;

    private Long categoryId;

    private String content;

    private Integer delFlag;

    private List<Long> tags;

}