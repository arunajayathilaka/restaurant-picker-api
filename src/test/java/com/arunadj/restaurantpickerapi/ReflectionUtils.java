package com.arunadj.restaurantpickerapi;

import java.lang.reflect.Field;

public class ReflectionUtils {
    public static void setFieldValue(Object instance, String fieldName, Object value)
            throws NoSuchFieldException, IllegalAccessException {
        Field field = instance.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(instance, value);
    }
}
