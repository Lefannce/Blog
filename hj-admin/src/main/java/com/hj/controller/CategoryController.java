package com.hj.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hj.domain.ResponseResult;
import com.hj.domain.VO.CategoryVo;
import com.hj.domain.VO.ExcelCategoryVo;
import com.hj.domain.VO.PageVo;
import com.hj.domain.entity.Category;
import com.hj.enums.AppHttpCodeEnum;
import com.hj.serivce.CategoryService;
import com.hj.utils.BeanCopyUtils;
import com.hj.utils.WebUtils;
import com.sun.xml.internal.bind.v2.TODO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

import static com.hj.constants.SystemConstants.NORMAL;

@RestController
@RequestMapping("/content/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * 查询所有分类接口
     *
     * @return
     */
    @GetMapping("/listAllCategory")
    public ResponseResult listAllCategory() {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Category::getStatus, NORMAL);
        List<Category> list = categoryService.list(queryWrapper);
        List<CategoryVo> categoryVos = BeanCopyUtils.copyBeanList(list, CategoryVo.class);
        return ResponseResult.okResult(categoryVos);


    }

    @GetMapping("/export")
    public void export(HttpServletResponse response) {
        try {
            //设置下载文件的请求头
            WebUtils.setDownLoadHeader("分类.xlsx", response);
            //获取需要导出的数据
            List<Category> categoryVos = categoryService.list();

            List<ExcelCategoryVo> excelCategoryVos = BeanCopyUtils.copyBeanList(categoryVos, ExcelCategoryVo.class);
            //把数据写入到Excel中
            EasyExcel.write(response.getOutputStream(), ExcelCategoryVo.class)
                    .autoCloseStream(Boolean.FALSE)
                    .sheet("分类导出")
                    .doWrite(excelCategoryVos); //需要传入单列集合,数据

        } catch (Exception e) {
            //如果出现异常也要响应json
            ResponseResult result = ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
            WebUtils.renderString(response, JSON.toJSONString(result));
        }
    }

    /**
     * 分类列表查询
     *
     * @param category
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("/list")
    public ResponseResult CategoryList(Category category, Integer pageNum, Integer pageSize) {
        PageVo vo = categoryService.selectCategoryList(category, pageNum, pageSize);
        return ResponseResult.okResult(vo);
    }

    /**
     * 新增分类
     * @param category
     * @return
     */
    @PostMapping
    public ResponseResult addCategory(@RequestBody Category category){
    categoryService.save(category);
    return ResponseResult.okResult();
    }

    /**
     * 修改分类回显
     * @param id
     * @return
     */
    @GetMapping("{id}")
    public ResponseResult selCategoryById(@PathVariable Long id){
        Category byId = categoryService.getById(id);
        return ResponseResult.okResult(byId);

    }

    @PutMapping
    public ResponseResult edit(@RequestBody Category category){
        categoryService.updateById(category);
        return ResponseResult.okResult();
    }

    @DeleteMapping("{id}")
    public ResponseResult del(@PathVariable Long id){
        categoryService.removeById(id);
        return ResponseResult.okResult();
    }
}
