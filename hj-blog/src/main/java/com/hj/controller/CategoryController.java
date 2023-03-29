package com.hj.controller;


import com.hj.domain.ResponseResult;
import com.hj.domain.entity.Category;
import com.hj.serivce.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
private CategoryService categoryService;
    /**
     * 查询分类列表
     */
    @GetMapping("/getCategoryList")
    public ResponseResult<Category> getCategory(){
        return categoryService.getCategoryList();

    }


}
