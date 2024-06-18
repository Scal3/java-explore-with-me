package ru.practicum.util;

import org.springframework.beans.BeanUtils;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.List;
import java.util.function.BiConsumer;

public class CopyNonNullProperties {

    public static void copyNonNullProperties(Object src, Object target, String ...excludeDefaultConvert) {
        PropertyDescriptor[] targetPds = BeanUtils.getPropertyDescriptors(target.getClass());

        for (PropertyDescriptor targetPd : targetPds) {
            Method targetSetter = targetPd.getWriteMethod();

            if (targetSetter != null) {
                try {
                    PropertyDescriptor srcPd = BeanUtils.getPropertyDescriptor(src.getClass(), targetPd.getName());

                    if (srcPd != null) {
                        if (List.of(excludeDefaultConvert).contains(srcPd.getName())) {
                            continue;
                        }

                        Method srcGetter = srcPd.getReadMethod();

                        if (srcGetter != null) {
                            Object value = srcGetter.invoke(src);

                            if (value != null) {
                                targetSetter.invoke(target, value);
                            }
                        }
                    }
                } catch (Exception e) {
                    System.err.println("Failed to copy property: " + targetPd.getName() + " - " + e.getMessage());
                }
            }
        }
    }

    public static void copyNonNullProperties(Object src, Object target, BiConsumer<Object, Object> handleSpecialFields, String ...excludeDefaultConvert) {
        copyNonNullProperties(src, target, excludeDefaultConvert);
        handleSpecialFields.accept(src, target);
    }
}
