package com.cn.xmf.util;

import java.io.InputStream;
import java.util.Properties;

public class FileReadUtil {

    private static FileReadUtil instance = null;
    private Properties props = null;
    private static String FILEPATH = "";

    private static FileReadUtil getInstatance(String filePath) {
        instance = new FileReadUtil("/" + filePath);
        return instance;
    }

    private FileReadUtil(String filePath) {
        loadProps(filePath);
    }

    private void loadProps(String filePath) {
        props = new Properties();
        InputStream in = null;
        try {
            in = getClass().getResourceAsStream(filePath);
            props.load(in);
        } catch (Exception e) {
            //此处可根据你的日志框架进行记录
            System.err.println("Error reading conf properties in PropertyManager.loadProps() " + e);

        } finally {
            try {
                in.close();
            } catch (Exception e) {

                //此处可根据你的日志框架进行记录
            }
        }
    }

    private String getProp(String key) {
        String value = props.getProperty(key);
        return value == null ? "" : value.trim();
    }

    /**
     * 根据key获取对应value
     *
     * @param filePath 文件路径
     * @param key
     * @return
     */
    public static String getValue(String filePath, String key) {
        return getInstatance(filePath).getProp(key);
    }

    /**
     * 根据key获取对应value，如果为空则返回默认的value
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public static String getValue(String filePath, String key, String defaultValue) {
        String value = getInstatance(filePath).getProp(key);
        return "".equals(value) ? defaultValue : value;
    }

}
