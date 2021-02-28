package com.wind.core.util;

import com.wind.core.common.CommonConst;

/**
 * @package com.wind.core.util
 * @className StringUtil
 * @note 字符串工具类
 * @author wind
 * @date 2020/12/9 20:33
 */
public class StringUtil {

    private StringUtil(){

    }

    /**
     * 判断字符串是否为null或空字符串
     * @param str
     * @return
     */
    public static boolean isEmpty(String str){
        return str == null || "".equals(str.trim());
    }

    /**
     * 判断字符串不为null且不为空字符串
     * @param str
     * @return
     */
    public static boolean isNotBlank(String str){
        return !isEmpty(str);
    }

    /**
     * 驼峰命名 hello_world => HelloWorld
     * @param name 数据库列名
     * @param isFirst 首字母小写为false helloWorld， 大写为true HelloWorld
     * @return
     */
    public static String getCamelCase(String name, boolean isFirst){
        StringBuilder sb = new StringBuilder();
        if(isNotBlank(name)){
            String[] strArr = name.split(CommonConst.UNDERLINE);
            for(int i = 0; i < strArr.length; i++){
                String s = strArr[i];
                if(i == 0){
                    sb.append(getCap(s, isFirst));
                }else{
                    sb.append(getCap(s, true));
                }
            }
        }
        return sb.toString();
    }

    /**
     * 将单词首字母变大小写
     * @param str
     * @param flag true变大写， false变小写
     * @return
     */
    public static String getCap(String str, boolean flag){
        StringBuilder sb = new StringBuilder();
        int length = str != null ? str.length() : 0;
        if(length >= 1){
            if(flag){
                sb.append(str.substring(0, 1).toUpperCase());
            }else{
                sb.append(str.substring(0, 1).toLowerCase());
            }
            if(length > 1){
                sb.append(str.substring(1));
            }
        }
        return sb.toString();
    }
}
