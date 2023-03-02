package cn.hjc.reggie.service;

import cn.hjc.reggie.domain.User;

public interface UserService {

    /**
     * 获取登入用户的信息, 或创建第一次登入的用户
     * @param phone
     * @return
     */
    public User loginCheck(String phone);

}
