package wlw.zc.demo.utils;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import static com.alibaba.fastjson.JSON.parseObject;
import static com.alibaba.fastjson.JSON.toJSONString;

/**
 * Created by mawei on 2019/6/14 10:52
 */
public class JSONUtils {

    public static <T> Object getObjectFromMap(Map<String, Object> map, Class<T> clazz) {
        return parseObject(toJSONString(map),clazz);
    }

    public static Map<String,Object> getMapFromObject(Object obj){
        Map<String,Object> parseObject = parseObject(toJSONString(obj));
        ArrayList<String> arrayList = new ArrayList<>();//用来存放要删除的key
        //为什么这么做因为增强for循环使用的迭代器, 当对要遍历的集合改变size时会抛异常java.util.ConcurrentModificationException
        //所以这里可以先存起来key
        Set<String> keySet = parseObject.keySet();
        for (String string : keySet) {
            if(ObjectUtils.isNull(parseObject.get(string)) || StringUtils.isBlank((String)parseObject.get(string))) {
                arrayList.add(string);
            }
        }
        //最后统一删除
        for (String string : arrayList) {
            parseObject.remove(string);
        }
        return parseObject;
    }

    public static Map<String,Object> getMapFromObjectOld(Object obj){
        return parseObject(toJSONString(obj));
    }
}
