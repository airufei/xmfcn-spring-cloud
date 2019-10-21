package com.cn.xmf.util;

import com.aliyun.oss.ClientConfiguration;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.*;
import org.apache.log4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Random;
@SuppressWarnings("all")
public class AliyunOSSClientUtil {

    //log日志
    private static Logger logger = Logger.getLogger(AliyunOSSClientUtil.class);
    //阿里云API的内或外网域名
    //private static String ENDPOINT="https://oss-cn-beijing-internal.aliyuncs.com";

    //阿里云API的内或外网域名
    private static String ENDPOINT = "https://oss-cn-beijing.aliyuncs.com";

    //阿里云API的密钥Access Key ID
    private static String ACCESS_KEY_ID = "LTAIxXmnnjOfiUtY";
    //阿里云API的密钥Access Key Secret
    private static String ACCESS_KEY_SECRET = "eNci7JbDvX0mjNq1fnYoGWxAJKB15e";
    //阿里云API的bucket名称
    private static String BACKET_NAME = "xmf-article-img";
    //阿里云API的文件夹名称
    private static String FOLDER = "xmf_01";


    /**
     * 获取阿里云OSS客户端对象
     *
     * @return ossClient
     */
    public static OSSClient getOSSClient() {
        ClientConfiguration conf = new ClientConfiguration();
        conf.setConnectionTimeout(10000);
        conf.setRequestTimeout(10000);
        conf.setSocketTimeout(10000);
        conf.setConnectionRequestTimeout(5000);
        conf.setMaxErrorRetry(2);
        logger.info("获取连接开始");
        OSSClient ossClient = null;
        try {
            ossClient = new OSSClient(ENDPOINT, ACCESS_KEY_ID, ACCESS_KEY_SECRET, conf);
        } catch (Exception e) {
            logger.info("获取连接 异常：" + e.getMessage());
            e.printStackTrace();
        }
        logger.info("获取连接结束");
        return ossClient;
    }

    /**
     * 创建存储空间
     *
     * @param ossClient  OSS连接
     * @param bucketName 存储空间
     * @return
     */
    public static String createBucketName(OSSClient ossClient, String bucketName) {
        //存储空间
        final String bucketNames = bucketName;
        if (!ossClient.doesBucketExist(bucketName)) {
            //创建存储空间
            Bucket bucket = ossClient.createBucket(bucketName);
            logger.info("创建存储空间成功");
            return bucket.getName();
        }
        return bucketNames;
    }

    /**
     * 删除存储空间buckName
     *
     * @param ossClient  oss对象
     * @param bucketName 存储空间
     */
    public static void deleteBucket(OSSClient ossClient, String bucketName) {
        ossClient.deleteBucket(bucketName);
        logger.info("删除" + bucketName + "Bucket成功");
    }

    /**
     * 获取储空间buckName的文件列表
     *
     * @param ossClient  oss对象
     * @param bucketName 存储空间
     */
    public static void deleteList(OSSClient ossClient, String bucketName, String folder) {
        ObjectListing listObjects = ossClient.listObjects(bucketName);
        // 遍历所有Object

        for (OSSObjectSummary objectSummary : listObjects.getObjectSummaries()) {
            String key = objectSummary.getKey();
            logger.info("key=" + key);
            Random random = new Random();
            boolean nextBoolean =random.nextBoolean();
            if (nextBoolean) {
                deleteFile(ossClient, bucketName, folder, key);
            }
        }
    }

    /**
     * 创建模拟文件夹
     *
     * @param ossClient  oss连接
     * @param bucketName 存储空间
     * @param folder     模拟文件夹名如"qj_nanjing/"
     * @return 文件夹名
     */
    public static String createFolder(OSSClient ossClient, String bucketName, String folder) {
        //文件夹名
        final String keySuffixWithSlash = folder;
        //判断文件夹是否存在，不存在则创建
        if (!ossClient.doesObjectExist(bucketName, keySuffixWithSlash)) {
            //创建文件夹
            ossClient.putObject(bucketName, keySuffixWithSlash, new ByteArrayInputStream(new byte[0]));
            logger.info("创建文件夹成功");
            //得到文件夹名
            OSSObject object = ossClient.getObject(bucketName, keySuffixWithSlash);
            String fileDir = object.getKey();
            return fileDir;
        }
        return keySuffixWithSlash;
    }

    /**
     * 根据key删除OSS服务器上的文件
     *
     * @param ossClient  oss连接
     * @param bucketName 存储空间
     * @param folder     模拟文件夹名 如"qj_nanjing/"
     * @param key        Bucket下的文件的路径名+文件名 如："upload/cake.jpg"
     */
    public static void deleteFile(OSSClient ossClient, String bucketName, String folder, String key) {
        String path=key;
        logger.info("删除" + bucketName + "下的文件"+path);
        ossClient.deleteObject(bucketName, path);
        logger.info("删除" + bucketName + "下的文件" + folder + key + "成功");
    }

    /**
     * 上传图片至OSS
     *
     * @param ossClient  oss连接
     * @param file       上传文件（文件全路径如：D:\\image\\cake.jpg）
     * @param bucketName 存储空间
     * @param folder     模拟文件夹名 如"qj_nanjing/"
     * @return String 返回的唯一MD5数字签名
     */
    public static String uploadObject2OSS(OSSClient ossClient, File file, String bucketName, String folder) {
        String resultStr = null;
        try {
            //以输入流的形式上传文件
            InputStream is = new FileInputStream(file);
            //文件名
            String fileName = file.getName();
            //文件大小
            Long fileSize = file.length();
            //创建上传Object的Metadata
            ObjectMetadata metadata = new ObjectMetadata();
            //上传的文件的长度
            metadata.setContentLength(is.available());
            //指定该Object被下载时的网页的缓存行为
            metadata.setCacheControl("no-cache");
            //指定该Object下设置Header
            metadata.setHeader("Pragma", "no-cache");
            //指定该Object被下载时的内容编码格式
            metadata.setContentEncoding("utf-8");
            //文件的MIME，定义文件的类型及网页编码，决定浏览器将以什么形式、什么编码读取文件。如果用户没有指定则根据Key或文件名的扩展名生成，
            //如果没有扩展名则填默认值application/octet-stream
            String contentType = null;
            try {
                contentType = getContentType(fileName);
            } catch (Exception e) {
                contentType = null;
                logger.info("上传类型错误：" + e.getMessage());
                e.printStackTrace();
            }
            if (StringUtil.isBlank(contentType)) {
                logger.info("上传类型错误：" + fileName);
                return resultStr;
            }
            metadata.setContentType(contentType);
            //指定该Object被下载时的名称（指示MINME用户代理如何显示附加的文件，打开或下载，及文件名称）
            metadata.setContentDisposition("filename/filesize=" + fileName + "/" + fileSize + "Byte.");
            //上传文件   (上传文件流的形式)
            PutObjectResult putResult = ossClient.putObject(bucketName, folder + fileName, is, metadata);
            //解析结果
            resultStr = putResult.getETag();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("上传阿里云OSS服务器异常." + e.getMessage(), e);
        }
        return resultStr;
    }


    public static void close(OSSClient ossClient) {
        if (ossClient != null) {
            try {
                ossClient.shutdown();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 通过文件名判断并获取OSS服务文件上传时文件的contentType
     *
     * @param fileName 文件名
     * @return 文件的contentType
     */
    public static String getContentType(String fileName) {
        //文件的后缀名
        if (StringUtil.isBlank(fileName)) {
            return null;
        }
        int indexOf = fileName.lastIndexOf(".");
        if (indexOf < 0) {
            return null;
        }
        String fileExtension = fileName.substring(indexOf);
        if (".bmp".equalsIgnoreCase(fileExtension)) {
            return "image/bmp";
        }
        if (".gif".equalsIgnoreCase(fileExtension)) {
            return "image/gif";
        }
        if (".jpeg".equalsIgnoreCase(fileExtension) || ".jpg".equalsIgnoreCase(fileExtension) || ".png".equalsIgnoreCase(fileExtension)) {
            return "image/jpeg";
        }
        if (".html".equalsIgnoreCase(fileExtension)) {
            return "text/html";
        }
        if (".txt".equalsIgnoreCase(fileExtension)) {
            return "text/plain";
        }
        if (".vsd".equalsIgnoreCase(fileExtension)) {
            return "application/vnd.visio";
        }
        if (".ppt".equalsIgnoreCase(fileExtension) || "pptx".equalsIgnoreCase(fileExtension)) {
            return "application/vnd.ms-powerpoint";
        }
        if (".doc".equalsIgnoreCase(fileExtension) || "docx".equalsIgnoreCase(fileExtension)) {
            return "application/msword";
        }
        if (".xml".equalsIgnoreCase(fileExtension)) {
            return "text/xml";
        }
        //默认返回类型
        return "image/jpeg";
    }
    /**
     * 上传阿里云OSS
     *
     * @param ossPath
     * @param file
     * @return
     */
    public static String upLoadFileOSS(File file,String ossPath) {
        String BACKET_NAME = "xmf-article-img";
        logger.info("开始上传OSS");
        OSSClient ossClient = null;
        String md5key = null;
        try {
            if (file == null) {
                return md5key;
            }
            String path = ossPath;
            String name = path+"/"+file.getName();
            ossClient = AliyunOSSClientUtil.getOSSClient();
            AliyunOSSClientUtil.createFolder(ossClient, BACKET_NAME, ossPath);
            md5key = AliyunOSSClientUtil.uploadObject2OSS(ossClient, file, BACKET_NAME, ossPath + "/");
            logger.info("上传oss成功：" + name);
        } catch (Exception e) {
            AliyunOSSClientUtil.close(ossClient);
            logger.info("获取连接异常：" + e.getMessage());
            e.printStackTrace();
        }
        if (ossClient == null) {
            logger.info("获取连接为空");
        }
        AliyunOSSClientUtil.close(ossClient);
        return md5key;
    }

    //测试
    public static void main(String[] args) {
        OSSClient ossClient = AliyunOSSClientUtil.getOSSClient();
        /*//初始化OSSClient

        //上传文件
        String file = "E:\\mnt\\images\\pic\\pider08\\wx_act_img3ca44b69364145cfb6c382a432a3bbd1.jpg";
        File filess = new File(file);
        String md5key = AliyunOSSClientUtil.uploadObject2OSS(ossClient, filess, BACKET_NAME, FOLDER);
        logger.info("上传后的文件MD5数字唯一签名:" + md5key);*/
        for (int j= 1; j < 50; j++) {
            String path = "pic/pider0"+j;
            for (int i = 0; i <200 ; i++) {
                deleteList(ossClient, BACKET_NAME,path);
            }
        }


    }
}
