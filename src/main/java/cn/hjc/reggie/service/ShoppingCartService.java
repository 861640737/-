package cn.hjc.reggie.service;


import cn.hjc.reggie.domain.ShoppingCart;

import java.util.List;

public interface ShoppingCartService {

    /**
     * 从购物车增加一个商品
     */
    boolean add(ShoppingCart shoppingCart);

    /**
     * 从购物车减少一个商品
     */
    boolean sub(ShoppingCart shoppingCart);

    /**
     * 获取购物车所有数据
     */
    List<ShoppingCart> getAllDate(Long userId);

    /**
     * 删除所有数据
     */
    boolean delAll(Long userId);
}
