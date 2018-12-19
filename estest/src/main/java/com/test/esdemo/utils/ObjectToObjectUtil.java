package com.test.esdemo.utils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class ObjectToObjectUtil {

    public static Map<String,Object> objectToMap(Object object) throws IllegalAccessException {
        if(object==null){
            return null;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        Field[] fields=object.getClass().getDeclaredFields();
        for(Field field:fields){
            field.setAccessible(true);
            map.put(field.getName(),field.get(object));
        }
        return map;
    }


}
