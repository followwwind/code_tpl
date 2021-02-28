package com.wind.core.util.ini;

import com.wind.core.model.enums.IniKeyType;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

/**
 * @package com.wind.core.util.ini
 * @className ParseIni
 * @note ini解析
 * @author wind
 * @date 2021/2/27 23:49
 */
public class ParseIni {

    protected HashMap<String, Properties> sections = new HashMap<>(16);
    private final String defaultName = "default";
    private final String POUND_SIGN = "#";
    private final String EQUAL_SIGN = "=";
    private transient String sectionName;
    private transient Properties property;


    /**
     * 构造函数
     *
     * @param filename
     *            文件路径
     * @throws IOException
     */
    public ParseIni(String filename) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        read(reader);
        reader.close();
    }

    /**
     * 构造函数
     *
     * @param inputStream 文件流
     *
     * @throws IOException
     */
    public ParseIni(InputStream inputStream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        read(reader);
        reader.close();
    }

    /**
     * 获取实例对象
     * @param filename
     * @return
     * @throws IOException
     */
    public static ParseIni getInstance(String filename) throws IOException {
        if(filename != null && !filename.startsWith("/")){
            filename = "/" + filename;
        }
        return new ParseIni(ParseIni.class.getResourceAsStream(filename));
    }

    /**
     * 文件读取
     *
     * @param reader
     * @throws IOException
     */
    protected void read(BufferedReader reader) throws IOException {
        String line;
        sectionName = this.defaultName;
        property = new Properties();
        sections.put(sectionName, property);
        while ((line = reader.readLine()) != null) {
            parseLine(line);
        }
    }

    /**
     * 解析每行数据
     *
     * @param line
     */
    protected void parseLine(String line) {
        line = line.trim();
        if (line.indexOf(POUND_SIGN) == 0 || line.indexOf(';') == 0) {
            return;
        }

        if (line.matches("\\[.*\\]")) {
            sectionName = line.replaceFirst("\\[(.*)\\]", "$1").trim();
            property = new Properties();
            if (sectionName.matches(".*:.*")) {
                int pos = sectionName.indexOf(':');
                String child = sectionName.substring(0, pos);
                String parent = sectionName.substring(pos + 1);

                Properties parentObj = this.getSection(parent);
                if (parentObj != null) {
                    property = (Properties) parentObj.clone();
                    sections.put(child, property);
                }
            } else {
                sections.put(sectionName, property);
            }
        } else if (line.matches(".*=.*")) {
            int i = line.indexOf(EQUAL_SIGN);
            String name = line.substring(0, i).trim();
            String value = line.substring(i + 1).trim();

            if (value.indexOf('"') == 0 || value.indexOf('\'') == 0) {
                // 去掉前面符号 " 或 '
                value = value.substring(1);
                // 去掉后面 " 或 '
                int len = value.length();
                if (value.indexOf('"') == len - 1 || value.indexOf('\'') == len - 1) {
                    value = value.substring(0, len - 1);
                }
            }
            property.setProperty(name, value);
        }
    }

    /**
     * 根据节 和 key 获取值
     *
     * @param section
     * @param key
     * @return String
     */
    public String get(String section, String key) {
        if (section == null || "".equals(section)){
            section = this.defaultName;
        }

        Properties property = sections.get(section);
        if (property == null) {
            return null;
        }

        String value = property.getProperty(key);
        if (value == null){
            return null;
        }

        return value;
    }

    /**
     * 根据节 和 key 获取值
     * @param keyType
     * @return
     */
    public String get(IniKeyType keyType){
        return get(keyType.getType(), keyType.getKey());
    }

    /**
     * 获取节下所有key
     * @param section
     * @return Properties
     */
    public Properties getSection(String section) {
        if (section == null || "".equals(section)){
            section = this.defaultName;
        }
        return sections.putIfAbsent(section, new Properties());
    }

    /**
     * 获取import
     * @param section
     * @return
     */
    public List<String> getSectionImport(String section){
        List<String> list = new ArrayList<>();
        Properties properties = getSection(section);
        properties.forEach((k, v) -> {
            String key = k.toString();
            if(key.startsWith("import")){
                list.add(v.toString());
            }
        });
        return list;
    }

    /**
     * 增加节点 及 值
     * @param section
     */
    public void set(String section, String key, String value) {
        if (property == null){
            property = new Properties();
        }
        if (section == null || "".equals(section)){
            section = this.defaultName;
        }
        if (key == null || "".equals(key)) {
            throw new RuntimeException("key is null");
        }
        sections.put(section, property);
        property.setProperty(key, value);
    }

    /**
     * 增加节点
     *
     * @param section
     */
    public void setSection(String section) {
        sections.put(section, property);
    }
}
