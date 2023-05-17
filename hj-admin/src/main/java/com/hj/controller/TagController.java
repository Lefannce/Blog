package com.hj.controller;

import com.hj.domain.ResponseResult;
import com.hj.domain.VO.PageVo;
import com.hj.domain.VO.TagVo;
import com.hj.domain.dto.TagListDto;
import com.hj.domain.entity.Tag;
import com.hj.serivce.TagService;
import com.hj.utils.BeanCopyUtils;
import jdk.nashorn.internal.runtime.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("content/tag")
public class TagController {
    @Autowired
    private TagService tagService;

    /**
     * 查询标签列表
     * @param pageNum
     * @param pageSize
     * @param tagListDto
     * @return
     */
    @GetMapping("list")
    public ResponseResult<PageVo> list(Integer pageNum, Integer pageSize, TagListDto tagListDto){
        return tagService.pageTagList(pageNum,pageSize,tagListDto);
    }

    /**
     * 添加标签
     * @param tag
     * @return
     */
    @PostMapping()
    public ResponseResult addtag(@RequestBody Tag tag){
        tagService.addtag(tag);
        return ResponseResult.okResult();

    }

    /**
     * 批量删除标签
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseResult deleteTag(@PathVariable List<Long> id){
      tagService.removeByIds(id);
      return ResponseResult.okResult();

    }

    /**
     * 修改标签页-根据id获取标签信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseResult getTag(@PathVariable Long id){
        Tag tag = tagService.getById(id);
        TagVo tagVo = BeanCopyUtils.copyBean(tag, TagVo.class);
        return ResponseResult.okResult(tagVo);
    }

    @PutMapping()
    public ResponseResult updateTag(@RequestBody Tag tag){
        tagService.updateById(tag);
        return ResponseResult.okResult();

    }

    @GetMapping("/listAllTag")
    public ResponseResult listAllTag(){
        List<Tag> list = tagService.list();
        List<TagVo> tagVos = BeanCopyUtils.copyBeanList(list, TagVo.class);
        return ResponseResult.okResult(tagVos);

    }



}

