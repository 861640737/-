package cn.hjc.reggie.util;

import java.util.UUID;

public class KeyGeneration {


    /**
     * 获取一个10位数的随机id
     * @return
     */
    public static Long getId() {
        int shashCode = UUID.randomUUID().toString().hashCode();
        if (shashCode <0){
            shashCode=-shashCode;
        }
        // 0 代表前面补充0
        // 10 代表长度为10
        // d 代表参数为正数型
        String sformat = String.format("%010d", shashCode).substring(0,10);
        long sid = Long.parseLong(sformat);
        return sid;
    }

}
