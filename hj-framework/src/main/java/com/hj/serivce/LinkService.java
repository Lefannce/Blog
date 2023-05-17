package com.hj.serivce;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hj.domain.ResponseResult;
import com.hj.domain.VO.PageVo;
import com.hj.domain.entity.Link;


/**
 * 友链(Link)表服务接口
 *
 * @author makejava
 * @since 2023-03-30 10:25:15
 */
public interface LinkService extends IService<Link> {

    ResponseResult getAllLink();

    PageVo selectLinkPage(Link link, Integer pageNum, Integer pageSize);
}

