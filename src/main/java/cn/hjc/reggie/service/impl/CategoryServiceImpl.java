package cn.hjc.reggie.service.impl;

import cn.hjc.reggie.common.CustomException;
import cn.hjc.reggie.domain.Category;
import cn.hjc.reggie.domain.Dish;
import cn.hjc.reggie.domain.Page;
import cn.hjc.reggie.domain.Setmeal;
import cn.hjc.reggie.mapper.CategoryMapper;
import cn.hjc.reggie.mapper.DishMapper;
import cn.hjc.reggie.mapper.SetmealMapper;
import cn.hjc.reggie.service.CategoryService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private SetmealMapper setmealMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public boolean save(Category category) {
        int insert = categoryMapper.insert(category);
        return insert > 0;
    }

    @Override
    public Page<Category> page(int current, int pageSize) {
        //获取总记录数
        Integer total = categoryMapper.selectCount(new QueryWrapper<>());
        //如数据库中没有记录,则直接返回
        if (total < 1) {
            return new Page<Category>(total, current, pageSize, null);
        }

        //若页码小于1,默认改为1
        if (current < 1) current = 1;
        //每页数小于1,默认改为3
        if (pageSize < 1) pageSize = 3;
        //页码数大于最大页码数,则改为最大页码数
        int currentMax = total % pageSize == 0? total / pageSize: total / pageSize + 1;
        if (current > currentMax) current = currentMax;

        //查询
        List<Category> categories = categoryMapper.selectAllByPage((current - 1) * pageSize, pageSize);

        return new Page<Category>(total, current, pageSize, categories);
    }

    @Override
    public void delete(Long id) {
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<Dish>();
        dishLambdaQueryWrapper.eq(Dish::getCategoryId, id);
        Integer count1 = dishMapper.selectCount(dishLambdaQueryWrapper);
        if (count1 > 0) {
            throw new CustomException("当前分类下关联了菜品,不能删除");
        }

        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<Setmeal>();
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId, id);
        Integer count2 = setmealMapper.selectCount(setmealLambdaQueryWrapper);
        if (count2 > 0) {
            throw new CustomException("当前分类下关联了套餐,不能删除");
        }

        categoryMapper.deleteById(id);
    }

    @Override
    public boolean update(Category category) {

        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<Category>();
        wrapper.eq(Category::getId, category.getId());
        int update = categoryMapper.update(category, wrapper);

        return update > 0;
    }

    @Override
    public List<Category> findByType(Integer type) {
        if (type == null) {
            return null;
        }

        return categoryMapper.selectByType(type);
    }

    @Override
    public List<Category> getAllList() {

        List<Category> list = (List<Category>) redisTemplate.opsForValue().get("category-list");
        if (list != null && list.size() > 0) {
            return list;
        }

        list = categoryMapper.selectAll();
        redisTemplate.opsForValue().set("category-list", list);
        return list;
    }
}
