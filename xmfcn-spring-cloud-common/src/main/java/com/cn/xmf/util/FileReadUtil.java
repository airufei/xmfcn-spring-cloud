package com.cn.xmf.util;

import com.aliyun.oss.OSSClient;
import net.coobird.thumbnailator.Thumbnails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
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

    public static void uploadFile(byte[] file, String filePath, String fileName) {
        File targetFile = new File(filePath);
        if (!targetFile.exists()) {
            targetFile.mkdirs();
        }
        try {
            FileOutputStream out = new FileOutputStream(filePath + fileName);
            out.write(file);
            out.flush();
            out.close();
        } catch (IOException e) {
            String exceptionMsg = StringUtil.getExceptionMsg(e);
            logger.info(exceptionMsg);
        }
    }

    /**
     * 上传OSS并按等比例压缩
     * @param size
     * @param newFilenName
     * @param temp_scale
     * @param temp_path
     * @param ossPath
     */
    public static void uploadOss(long size, String newFilenName, String temp_scale, String temp_path, String ossPath) {
        logger.info("oldfile_size:={}", size);
        try {
            File file = new File(temp_scale);
            if (!file.exists()) {
                file.mkdir();
            }
            //对图片等比例压缩
            double scale = 1;
            double quality = 1;//压缩质量
            if (size > 1024000) {
                scale = 0.8;
                quality = 0.8;
            } else if (size > 4096000) {
                scale = 0.5;
                quality = 0.5;
            }
            else if (size > 6144000) {
                scale = 0.3;
                quality = 0.3;
            }
            Thumbnails.of(temp_path + newFilenName).
                    scale(scale). // 图片缩放80%, 不能和size()一起使用
                    outputQuality(quality). // 图片质量压缩80%
                    toFile(temp_scale + newFilenName);
            File scaleFile = new File(temp_scale + newFilenName);
            AliyunOSSClientUtil.upLoadFileOSS(scaleFile, ossPath);
            File temFile = new File(temp_path + newFilenName);
            if (temFile.exists()) {
                temFile.delete();
                temFile=null;
            }
            if (scaleFile.exists()) {
                scaleFile.delete();
                scaleFile=null;
            }
        } catch (IOException e) {
            String exceptionMsg = StringUtil.getExceptionMsg(e);
            logger.info(exceptionMsg);
        }
    }

    public static void inputStreamToFile(InputStream ins, File file) {
        try {
            OutputStream os = new FileOutputStream(file);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            ins.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
