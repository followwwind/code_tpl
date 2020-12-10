package com.wind.core.util;

import org.springframework.cglib.beans.BeanCopier;

/**
 * @package com.wind.core.util
 * @className BeanUtil
 * @note 对象拷贝复制
 * @author wind
 * @date 2020/12/9 20:59
 */
public class BeanUtil {

    /**
     * 对象拷贝复制
     * @param source
     * @param target
     */
    public static void copy(Object source, Object target){
        if(source != null && target != null){
            BeanCopier copier = BeanCopier.create(source.getClass(), target.getClass(), false);
            copier.copy(source, target, null);
        }
    }
}
