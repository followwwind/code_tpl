package com.wind.core.model.tpl;


import com.wind.core.model.db.Table;

/**
 * @package com.wind.core.model.ftl
 * @className JavaFtl
 * @note java模型
 * @author wind
 * @date 2020/12/9 21:48
 */
public class JavaModel extends Table {

    /**
     * 包名
     */
    private String packageName;

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
}
