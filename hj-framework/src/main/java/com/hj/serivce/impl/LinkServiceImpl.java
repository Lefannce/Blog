package com.hj.serivce.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hj.domain.ResponseResult;
import com.hj.domain.VO.LinkVo;
import com.hj.domain.entity.Link;
import com.hj.mapper.LinkMapper;
import com.hj.serivce.LinkService;
import com.hj.utils.BeanCopyUtils;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.hj.constants.SystemConstants.LINK_STATUS_NORMAL;

/**
 * 友链(Link)表服务实现类
 *
 * @author makejava
 * @since 2023-03-30 10:25:15
 */
@Service("linkService")
public class LinkServiceImpl extends ServiceImpl<LinkMapper, Link> implements LinkService {


    /**
     * 	在友链页面要查询出所有的审核通过的友链。
     * @return
     */
    @Override
    public ResponseResult getAllLink() {
        LambdaQueryWrapper<Link> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Link::getStatus,LINK_STATUS_NORMAL);
        List<Link> links = list(queryWrapper);

        //封装Vo
        List<LinkVo> linkVos = BeanCopyUtils.copyBeanList(links, LinkVo.class);
        return ResponseResult.okResult(linkVos);
    }
}

