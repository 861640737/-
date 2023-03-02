package cn.hjc.reggie.util;

import java.util.Random;

/**
 * 验证码创建工具
 */
public class CheckCodeUtil {

    static Random randObj = new Random();

    /**
     * 获取一个四位数验证码
     * @return
     */
    public static String getCheckCode() {
        return Integer.toString(1000 + randObj.nextInt(9000));
    }

}
