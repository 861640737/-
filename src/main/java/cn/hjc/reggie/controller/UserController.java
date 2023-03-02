package cn.hjc.reggie.controller;

import cn.hjc.reggie.common.R;
import cn.hjc.reggie.domain.User;
import cn.hjc.reggie.service.UserService;
import cn.hjc.reggie.util.CheckCodeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 用户操作类
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;


    /**
     * 用户获取验证码
     * @param user
     * @return
     */
    @PostMapping("/sendMsg")
    public R<String> getCheckCode(@RequestBody User user, HttpServletRequest request) {
        //1. 获取验证码
        String checkCode = CheckCodeUtil.getCheckCode();
        log.info("您的验证码 ---> " + checkCode);

        //2. 将手机号验证码存入session
        request.getSession().setAttribute(user.getPhone(), checkCode);

        return null;
    }


    /**
     * 用户登录
     * @param map
     * @param request
     * @return
     */
    @PostMapping("/login")
    public R<User> login(@RequestBody Map map, HttpServletRequest request) {
        String phone = (String) map.get("phone");
        String code = (String) map.get("code");
        if (phone == null || code == null) {
            return R.error("登入失败~~");
        }

        //获取session中的验证码
        String code1 = (String) request.getSession().getAttribute(phone);

        //将用户传入的验证码于session中的作比较
        if (code.equals(code1)) {
            //登入成功
            User user = userService.loginCheck(phone);
            if (user != null) {
                request.getSession().setAttribute("user", phone);
                request.getSession().setAttribute("userId", user.getId());
                return R.success(user);
            }
        }

        return R.error("登入失败~~");
    }


}
