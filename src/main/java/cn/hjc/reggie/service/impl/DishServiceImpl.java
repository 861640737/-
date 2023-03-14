package cn.hjc.reggie.service.impl;

import cn.hjc.reggie.common.BaseContext;
import cn.hjc.reggie.domain.Category;
import cn.hjc.reggie.domain.Dish;
import cn.hjc.reggie.domain.DishFlavor;
import cn.hjc.reggie.domain.Page;
import cn.hjc.reggie.dto.DishDto;
import cn.hjc.reggie.mapper.CategoryMapper;
import cn.hjc.reggie.mapper.DishFlavorMapper;
import cn.hjc.reggie.mapper.DishMapper;
import cn.hjc.reggie.service.DishService;
import cn.hjc.reggie.util.KeyGeneration;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DishServiceImpl implements DishService {

    //菜品
    @Autowired
    private DishMapper dishMapper;

    //菜品口味
    @Autowired
    private DishFlavorMapper dishFlavorMapper;

    //分类
    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    @Transactional
    public boolean save(DishDto dishDto) {
        long id = KeyGeneration.getId();

        //设置创建、修改的用户和时间,id
        dishDto.setId(id);
        LocalDateTime localDateTime = LocalDateTime.now();
        if (dishDto.getCreateTime() == null) {
            dishDto.setCreateTime(localDateTime);
            dishDto.setCreateUser(BaseContext.getCurrentId());
        }
        dishDto.setUpdateTime(localDateTime);
        dishDto.setUpdateUser(BaseContext.getCurrentId());

        boolean dishInsert = dishMapper.insert(dishDto) > -1;
        boolean dishFlavorInsert = true;
        List<DishFlavor> dishFlavors = dishDto.getFlavors();
        if (dishFlavors != null && dishFlavors.size() > 0) {
            //设置口味创建、修改的用户和时间
            for (int i = 0; i < dishFlavors.size(); i ++) {
                if (dishFlavors.get(i).getCreateUser() == null) {
                    dishFlavors.get(i).setCreateUser(BaseContext.getCurrentId());
                    dishFlavors.get(i).setCreateTime(localDateTime);
                    dishFlavors.get(i).setDishId(id);

                    dishFlavors.get(i).setId(KeyGeneration.getId());
                }
                dishFlavors.get(i).setUpdateUser(BaseContext.getCurrentId());
                dishFlavors.get(i).setUpdateTime(localDateTime);
            }
            dishFlavorInsert = dishFlavorMapper.insert(dishDto.getFlavors()) > -1;
        }

        return dishInsert && dishFlavorInsert;
    }


    @Override
    public Page<DishDto> getDishPage(Integer current, Integer pageSize, String name) {
        // 1.处理模糊匹配字段
        if (name != null && !"".equals(name)) {
            StringBuilder sb = new StringBuilder();
            sb.append("%").append(name).append("%");
            name = sb.toString();
        }

        // 2.获取匹配的菜品总数
        Integer total = dishMapper.getTotal(name);

        // 若没有匹配数据,则直接返回
        if (total < 1) {
            return new Page<DishDto>(total, current, pageSize, null);
        }

        // 3.若当前页码小于1,则改为1
        //   若每页显示条数小于1,则改为10
        //   若当前页码大于最大页码,则改为支持的最大页码
        if (current == null || current < 1) current = 1;
        if (pageSize == null || pageSize < 1) pageSize = 10;
        int currentMax = total % pageSize == 0? total / pageSize: total / pageSize + 1;
        if (current > currentMax) current = currentMax;

        // 4.查询菜品
        List<Dish> dishes = dishMapper.selectAllByPage((current - 1) * pageSize, pageSize, name);
        Page<DishDto> page = new Page<>(total, current, pageSize, null);

        // 5.查询所有口味信息,分类名,并进行封装
        List<DishDto> list = dishes.stream().map((item) -> {
            DishDto dishDto = new DishDto();

            //拷贝菜品信息
            BeanUtils.copyProperties(item, dishDto);

            //查询菜品对应的分类名
            Long categoryId = item.getCategoryId();
            Category category = categoryMapper.selectById(categoryId);
            if (category != null) {
                dishDto.setCategoryName(category.getName());
            }

            //查询菜品对应的所有口味信息
//            List<DishFlavor> flavorByDishId = dishFlavorMapper.getFlavorByDishId(dishDto.getId());
//            dishDto.setFlavors(flavorByDishId);

            return dishDto;
        }).collect(Collectors.toList());

        page.setRecords(list);

        return page;
    }

    @Override
    @Transactional
    public boolean updateStatus(String status, String id) {
        // 1.若状态不为0或1,则返回失败
        if ("0".equals(status) && "1".equals(status)) return false;

        // 获取需要存储的新状态
        int newStatus = Integer.parseInt(status);

        // 获取每个id
        String[] split = id.split(",");

        if (split.length < 0) return false;

        try {
            for (int i = 0; i < split.length; i++){
                dishMapper.upStatus(newStatus, Long.parseLong(split[i]));
            }
        } catch (NumberFormatException e) {
            return false;
        }


//        if (dishMapper.upStatus(newStatus, newId) < 1) {
//            return false;
//        }

        return true;
    }

    @Override
    public DishDto getDishAndFlavorById(Long id) {
        Dish dish = dishMapper.getById(id);
        if (dish == null) {
            return null;
        }
        List<DishFlavor> dishFlavor = dishFlavorMapper.getFlavorByDishId(id);
        Category category = categoryMapper.selectById(dish.getCategoryId());
        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish, dishDto);
        dishDto.setFlavors(dishFlavor);
        dishDto.setCategoryName(category.getName());

        return dishDto;
    }

    @Override
    @Transactional
    public boolean update(DishDto dishDto) {
        Long id = BaseContext.getCurrentId();
        LocalDateTime now = LocalDateTime.now();

        // 1.更新菜品口味信息
        dishFlavorMapper.delByDishId(dishDto.getId());
        // 补充信息
        List<DishFlavor> flavors = dishDto.getFlavors();
        if (flavors != null) {
            for (int i = 0; i < flavors.size(); i++) {
                DishFlavor flavor = flavors.get(i);

                // 第一次创建
                if (flavor.getCreateUser() == null) {
                    flavor.setId(KeyGeneration.getId());
                    flavor.setDishId(dishDto.getId());
                    flavor.setCreateUser(id);
                    flavor.setUpdateUser(id);
                    flavor.setCreateTime(now);
                    flavor.setUpdateTime(now);
                } else {
                    // 已创建过，修改部分信息
                    flavor.setUpdateTime(now);
                    flavor.setUpdateUser(id);
                }
            }
        }
        dishFlavorMapper.insert(dishDto.getFlavors());

        // 添加修改信息
        dishDto.setUpdateUser(id);
        dishDto.setUpdateTime(now);

        dishMapper.update(dishDto);

        return true;
    }

    @Override
    @Transactional
    public boolean del(Long[] ids) {
        try {
            for (int i = 0; i < ids.length; i++) {
                dishMapper.delById(ids[i]);
                dishFlavorMapper.delByDishId(ids[i]);
            }
        } catch (NumberFormatException e) {
            return false;
        }

        return true;
    }

    @Override
    public List<DishDto> getDishByCategoryId(Long categoryId) {
        String key = "category-id-" + categoryId;

        List<DishDto> dishDtoList = null;

        try {
            dishDtoList = (List<DishDto>) redisTemplate.opsForValue().get(key);
        } catch (Exception e) {
        }

        if (dishDtoList != null && dishDtoList.size() > 0) {
            return dishDtoList;
        }

        List<Dish> dish = dishMapper.getDishByCategoryId(categoryId);

        dishDtoList = dish.stream().map((item) -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);
            // 设置每个菜品的口味规格
            dishDto.setFlavors(dishFlavorMapper.getFlavorByDishId(item.getId()));
            return dishDto;
        }).collect(Collectors.toList());

        try {
            redisTemplate.opsForValue().set(key, dishDtoList);
        } catch (Exception e) {
        }

        return dishDtoList;
    }
}
