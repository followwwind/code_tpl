package com.wind.core.util.ftl;

import com.wind.core.model.db.Table;
import com.wind.core.util.BeanUtil;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

import java.io.*;
import java.util.HashMap;

/**
 * @package com.wind.core.util.ftl
 * @className FtlUtil
 * @note FtlUtil
 * @author wind
 * @date 2020/12/9 22:01
 */
public class FtlUtil {

    private FtlUtil(){

    }

    /**
     * 生成文件
     * @param freeMarker
     */
    public static void genCode(FtlObj freeMarker){
        try {
            File dir = new File(freeMarker.getFileDir());
            boolean sign = true;
            if(!dir.exists()){
                sign = dir.mkdirs();
            }
            if(sign){
                Configuration cfg = new Configuration(Configuration.VERSION_2_3_22);
                cfg.setDirectoryForTemplateLoading(new File(freeMarker.getCfgDir()));
                cfg.setDefaultEncoding("UTF-8");
                cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
                Template temp = cfg.getTemplate(freeMarker.getCfgName());
                OutputStream fos = new FileOutputStream( new File(dir, freeMarker.getFileName()));
                Writer out = new OutputStreamWriter(fos);
                FtlParam obj = freeMarker.getData();
                if(obj == null){
                    obj = new FtlParam();
                }
                temp.process(obj, out);
                fos.flush();
                out.close();
            }
        } catch (IOException | TemplateException e) {
            e.printStackTrace();
        }
    }

    /**
     * 转换model
     * @param table
     * @param c
     * @param <T>
     * @return
     */
    public static <T extends Table> T toModel(Table table, Class<T> c){
        try {
            T t = c.newInstance();
            BeanUtil.copy(table, t);
            return t;
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
