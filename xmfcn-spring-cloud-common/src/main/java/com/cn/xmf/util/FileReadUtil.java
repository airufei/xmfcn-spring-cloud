package com.cn.xmf.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Properties;

/**
 * 读取文件
 */
public class FileReadUtil {
    private static Logger logger = LoggerFactory.getLogger(FileReadUtil.class);
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
            logger.error(StringUtil.getExceptionMsg(e));
        } finally {
            try {
                in.close();
            } catch (Exception e) {
                logger.error(StringUtil.getExceptionMsg(e));
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
