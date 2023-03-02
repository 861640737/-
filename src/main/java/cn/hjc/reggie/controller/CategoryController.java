package cn.hjc.reggie.controller;

import cn.hjc.reggie.common.BaseContext;
import cn.hjc.reggie.common.R;
import cn.hjc.reggie.domain.Category;
import cn.hjc.reggie.domain.Page;
import cn.hjc.reggie.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 分类
 */
@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 新增分类
     * @param category
     * @return
     */
    @PostMapping
    public R<String> save(HttpServletRequest request, @RequestBody Category category) {
        log.info("category:{}",category);
        long empId = (long) request.getSession().getAttribute("employee");
        BaseContext.setCurrentId(empId);
        boolean save = categoryService.save(category);
        return save? R.success("新增分类成功"): R.error("新增分类失败");
    }

    /**
     * 分页查询
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    public R<Page<Category>> page(int page, int pageSize) {

        Page<Category> page1 = categoryService.page(page, pageSize);

        return R.success(page1);
    }

    /**
     * 根据id删除分类
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> delete(Long ids) {
        categoryService.delete(ids);
        return R.success("分类信息删除成功");
    }

    /**
     * 修改分类信息
     * @param category
     * @return
     */
    @PutMapping
    public R<String> updateCategory(HttpServletRequest request, @RequestBody Category category) {
        BaseContext.setCurrentId((Long) request.getSession().getAttribute("employee"));

        boolean flag = categoryService.update(category);
        return flag? R.success("修改信息成功"): R.error("修改信息失败");
    }

    /**
     * 根据分类查询数据
     * @param type
     * @return
     */
    @GetMapping("/list")
    public R<List<Category>> listByType(Integer type) {
        if (type == null){
            return R.success(categoryService.getAllList());
        } else {
            return R.success(categoryService.findByType(type));
        }
    }

//    /**
//     * 查询所有分类数据
//     * @return
//     */
//    @GetMapping("/list")
//    public R<List> list() {
//        List<Category> list = categoryService.getAllList();
//        return R.success(list);
//    }

}
