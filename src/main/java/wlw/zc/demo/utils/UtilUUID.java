package wlw.zc.demo.utils;

import java.util.UUID;

/**
 * 生成唯一的UUID
 *
 * @author zhouwentao
 * @date 2018/10/06
 */
public class UtilUUID {

    /**
     * 生成小写的32位的uuid，并去掉"-"
     *
     * @return uuid
     */
    public static String getUUID32() {
        return UUID.randomUUID().toString().replace("-", "").toLowerCase();
    }
}
