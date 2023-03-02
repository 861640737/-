package cn.hjc.reggie.service.impl;

import cn.hjc.reggie.domain.User;
import cn.hjc.reggie.mapper.UserMapper;
import cn.hjc.reggie.service.UserService;
import cn.hjc.reggie.util.KeyGeneration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper;

    @Override
    public User loginCheck(String phone) {
        // 从数据库查询用户
        User user = userMapper.getUserByPhone(phone);

        // 判断数据库是否有此用户
        if (user != null) {
            // 数据库有此用户信息,返回
            return user;
        }

        // 数据库无此用户信息, 创建用户并返回
        user = new User();
        user.setId(KeyGeneration.getId());
        user.setPhone(phone);
        user.setStatus(0);
        Integer save = userMapper.save(user);
        if (save > 0) {
            return user;
        }

        return null;
    }


}
