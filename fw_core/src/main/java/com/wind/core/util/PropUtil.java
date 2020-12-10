package com.wind.core.util;

import java.io.*;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * @package com.wind.core.util
 * @className PropUtil
 * @note properties工具类
 * @author wind
 * @date 2020/12/9 21:16
 */
public class PropUtil {

    private PropUtil(){
        
    }

    /**
     * 解析properties文件
     * @param filePath
     * @return
     */
    public static Properties readProp(String filePath){
        Properties props = new Properties();
        try {
            InputStream in = new BufferedInputStream(new FileInputStream(filePath));
            props.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return props;
    }

    /**
     * 解析properties文件
     * @param in
     * @return
     */
    public static Properties readProp(InputStream in){
        Properties props = new Properties();
        try {
            props.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IoUtil.close(in);
        }

        return props;
    }


    /**
     * 写入properties信息
     * @param filepath
     * @param map
     */
    public static void writeProp(String filepath, Map<String, String> map){
        Properties props = new Properties();
        OutputStream fos = null;
        try {
            fos = new FileOutputStream(filepath);
            Set<Map.Entry<String, String>> setEntry = map.entrySet();
            for(Map.Entry<String, String> entry : setEntry){
                String key = entry.getKey();
                String value = entry.getValue();
                props.setProperty(key, value);
            }
            props.store(fos, "");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IoUtil.close(fos);
        }
    }
}
