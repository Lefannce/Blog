package com.hj.serivce.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hj.domain.ResponseResult;
import com.hj.domain.VO.PageVo;
import com.hj.domain.dto.TagListDto;
import com.hj.domain.entity.Tag;
import com.hj.enums.AppHttpCodeEnum;
import com.hj.exception.SystemException;
import com.hj.mapper.TagMapper;
import com.hj.serivce.TagService;
import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.util.StringUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 标签(Tag)表服务实现类
 *
 * @author makejava
 * @since 2023-04-22 19:24:42
 */
@Service("tagService")
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

    @Override
    public ResponseResult<PageVo> pageTagList(Integer pageNum, Integer pageSize, TagListDto tagListDto) {
        //分页查询
        LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasText(tagListDto.getName()),Tag::getName,tagListDto.getName());
        queryWrapper.like(StringUtils.hasText(tagListDto.getRemark()),Tag::getRemark,tagListDto.getRemark());
        Page<Tag> tagPage = new Page<>();
        tagPage.setCurrent(pageNum);
        tagPage.setSize(pageSize);
        page(tagPage,queryWrapper);
        //封装数据返回
        PageVo pageVo = new PageVo(tagPage.getRecords(),tagPage.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public void addtag(Tag tag) {
        //标签名和备注不能为空
      if(!StringUtils.hasText(tag.getName())){
            throw new SystemException(AppHttpCodeEnum.CONTENT_NOT_NULL);
        }else if (!StringUtils.hasText(tag.getRemark())){
            throw new SystemException(AppHttpCodeEnum.CONTENT_NOT_NULL);
      }
        save(tag);

    }

    @Override
    public void deleteTag(Long id) {
          /*Tag tag = new Tag();
        tag.setId(id);
        tag.setDelFlag(1);
        updateById(tag);*/
        removeById(id);


    }
}

