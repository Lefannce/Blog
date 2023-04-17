package com.hj.serivce;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hj.domain.ResponseResult;
import com.hj.domain.entity.Comment;


/**
 * 评论表(Comment)表服务接口
 *
 * @author makejava
 * @since 2023-04-10 09:23:47
 */
public interface CommentService extends IService<Comment> {

    ResponseResult commentList(String commentType, Long articleId, Integer pageNum, Integer pageSize);

    ResponseResult addComment(Comment comment);
}

