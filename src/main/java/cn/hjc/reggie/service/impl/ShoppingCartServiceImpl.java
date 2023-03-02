package cn.hjc.reggie.service.impl;

import cn.hjc.reggie.domain.ShoppingCart;
import cn.hjc.reggie.mapper.ShoppingCartMapper;
import cn.hjc.reggie.service.ShoppingCartService;
import cn.hjc.reggie.util.KeyGeneration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;


    @Override
    @Transactional
    public boolean add(ShoppingCart shoppingCart) {
        Integer integer;
        if (shoppingCart.getDishId() != null) {
            // 商品为菜品, 查询数据
            ShoppingCart dish = shoppingCartMapper.getDish(shoppingCart.getDishId(), shoppingCart.getUserId());

            if (dish != null) {
                // 已存在该菜品, 加一
                integer = shoppingCartMapper.upNumber(dish.getNumber() + 1, dish.getId());
            } else {
                // 不存在该菜品, 创建
                shoppingCart.setId(KeyGeneration.getId());
                shoppingCart.setCreateTime(LocalDateTime.now());
                integer = shoppingCartMapper.saveDish(shoppingCart);
            }

        } else {
            // 商品为套餐, 查询数据
            ShoppingCart setmeal = shoppingCartMapper.getSetmeal(shoppingCart.getSetmealId(), shoppingCart.getUserId());

            if (setmeal != null) {
                // 存在该套餐，加一
                integer = shoppingCartMapper.upNumber(setmeal.getNumber() + 1, setmeal.getId());
            } else {
                // 不存在该套餐，创建
                shoppingCart.setId(KeyGeneration.getId());
                shoppingCart.setCreateTime(LocalDateTime.now());
                integer = shoppingCartMapper.saveSetmeal(shoppingCart);
            }
        }

        return integer > 0;
    }

    @Override
    @Transactional
    public boolean sub(ShoppingCart shoppingCart) {
        Integer integer;
        if (shoppingCart.getDishId() != null) {
            // 菜品
            ShoppingCart dish = shoppingCartMapper.getDish(shoppingCart.getDishId(), shoppingCart.getUserId());
            if (dish.getNumber() == 1) {
                integer = shoppingCartMapper.delOne(dish.getId());
            } else {
                integer = shoppingCartMapper.upNumber(dish.getNumber() - 1, dish.getId());
            }

        } else {
            // 套餐
            ShoppingCart setmeal = shoppingCartMapper.getSetmeal(shoppingCart.getSetmealId(), shoppingCart.getUserId());
            if (setmeal.getNumber() == 1) {
                integer = shoppingCartMapper.delOne(setmeal.getId());
            } else {
                integer = shoppingCartMapper.upNumber(setmeal.getNumber() - 1, setmeal.getId());
            }
        }

        return integer > 0;
    }

    @Override
    public List<ShoppingCart> getAllDate(Long userId) {
        return shoppingCartMapper.getAll(userId);
    }

    @Override
    public boolean delAll(Long userId) {
        return shoppingCartMapper.delByUserId(userId) > 0;
    }
}
