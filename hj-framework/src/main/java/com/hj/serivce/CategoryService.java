package com.hj.serivce;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hj.domain.ResponseResult;
import com.hj.domain.VO.PageVo;
import com.hj.domain.entity.Category;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;


/**
 * 分类表(Category)表服务接口
 * @author makejava
 * @since 2023-03-29 12:39:56
 */
public interface CategoryService extends IService<Category> {


    ResponseResult getCategoryList();

    PageVo selectCategoryList(Category category, Integer pageNum, Integer pageSize);
}

