package cn.hjc.reggie.controller;

import cn.hjc.reggie.common.R;
import cn.hjc.reggie.domain.ShoppingCart;
import cn.hjc.reggie.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 购物车
 */
@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    /**
     * 增加商品
     */
    @PostMapping("/add")
    public R<String> addShoppingCart(@RequestBody ShoppingCart shoppingCart, HttpServletRequest request) {
        shoppingCart.setUserId((Long) request.getSession().getAttribute("userId"));
        boolean flag = shoppingCartService.add(shoppingCart);
        if (flag) {
            return R.success("添加成功");
        }
        return R.error("添加失败");
    }

    /**
     * 减少商品
     */
    @PostMapping("/sub")
    public R<String> subShoppingCart(@RequestBody ShoppingCart shoppingCart, HttpServletRequest request) {
        shoppingCart.setUserId((Long) request.getSession().getAttribute("userId"));
        boolean flag = shoppingCartService.sub(shoppingCart);
        if (flag) {
            return R.success("操作成功");
        }
        return R.error("操作失败");
    }

    /**
     * 获取购物车数据
     */
    @GetMapping("/list")
    public R<List> getDate(HttpServletRequest request) {
        List<ShoppingCart> shoppingCarts = shoppingCartService.getAllDate((Long) request.getSession().getAttribute("userId"));
        return R.success(shoppingCarts);
    }

    /**
     * 清空该用户的购物车
     */
    @DeleteMapping("/clean")
    public R<String> delAllDate(HttpServletRequest request) {
        boolean flag = shoppingCartService.delAll((Long) request.getSession().getAttribute("userId"));
        if (flag) {
            return R.success("成功");
        }
        return R.error("操作失败");
    }

}
