/**
 * Project Name:CooxinPro
 * File Name:HttpClientUtil.java
 * Package Name:com.cn.cooxin.spider.domain
 * Date:2016年7月12日下午3:58:55
 * Copyright (c) 2016, hukailee@163.com All Rights Reserved.
 */

package com.cn.xmf.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;
import java.util.Map.Entry;

/**
 * ClassName:HttpClientUtil 发起抓取数据请求
 *
 * @author rufei.cn
 * @Version 1.0
 * @see
 */
@SuppressWarnings("all")
public class HttpClientUtil {

    private static Logger logger = Logger.getLogger(HttpClientUtil.class);

   
    /**
     * httpPost
     *
     * @param url       路径
     * @param jsonParam 参数
     * @return
     */
    public static JSONObject httpPost(String url, JSONObject jsonParam) {
        return httpPost(url, jsonParam, false);
    }

    /**
     * post请求
     *
     * @param url            url地址
     * @param jsonParam      参数
     * @param noNeedResponse 不需要返回结果
     * @return
     */
    public static JSONObject httpPost(String url, JSONObject jsonParam,
                                      boolean noNeedResponse) {
        // post请求返回结果
        DefaultHttpClient httpClient = new DefaultHttpClient();
        JSONObject jsonResult = null;
        HttpPost method = new HttpPost(url);
        try {
            if (null != jsonParam) {
                // 解决中文乱码问题
                StringEntity entity = new StringEntity(jsonParam.toString(),
                        "utf-8");
                entity.setContentEncoding("UTF-8");
                entity.setContentType("application/json");
                method.setEntity(entity);
                method.addHeader("Host", "www.zhihu.com");
                method.addHeader("Origin", "https://www.zhihu.com");
                method.addHeader(
                        "Content-Security-Policy",
                        "default-src *; img-src * data: blob:; frame-src 'self' *.zhihu.com getpocket.com note.youdao.com read.amazon.cn; script-src 'self' *.zhihu.com *.google-analytics.com res.wx.qq.com 'unsafe-eval'; style-src 'self' *.zhihu.com 'unsafe-inline'; connect-src * wss:;");
                method.addHeader("Cookie",
                        "aliyungf_tc=AQAAAD6WbxTVLgQALRF8e9wRU69eKWdg; d_c0=\"AICCNNMJ0QyPTonlp3ntofpNd6oyLAmLwvs=|1512966758\"; _xsrf=8062c503-aea6-4414-af3d-a5a70bef7d9b; q_c1=67914a2cad2c40eebcbdffbccc9e79ab|1512966758000|1512966758000; _zap=34b4c7ca-c253-4c0c-935c-0585b5828d28; l_cap_id=\"ODVmYzAyOGE2YjU2NDIzYmJlYTQ0NDI1MWZkMmQ1ZTA=|1512988659|59e779ed13e4a9cb05dbfcfc02ed0f15141e4873\"; r_cap_id=\"NDE0MWMwYjJlZjlhNDY1ZWJjMTJlZmJhNTdhZmMzMjY=|1512988659|901d16e594ba903e9a8387d40ef4360b39e3984b\"; cap_id=\"ZDVmZWQ5ZjY1NzQzNDkyMTljNGU5YzE3MDQ3ZDhhMWY=|1512988659|f50c09cad080c8051ce2b58aa9c1fde1595e1264\"");
                method.addHeader("Referer",
                        "https://www.zhihu.com/explore/recommendations");
                method.addHeader("X-API-Version",
                        "3.0.91");
                method.addHeader("X-App-Za",
                        "OS=Web");
                method.addHeader("X-UDID",
                        "AICCNNMJ0QyPTonlp3ntofpNd6oyLAmLwvs=");
                method.addHeader("Referer",
                        "https://www.zhihu.com/search?type=content&q=%E5%90%83%E9%B8%A1");
            }
            HttpResponse result = httpClient.execute(method);
            url = URLDecoder.decode(url, "UTF-8");
            /** 请求发送成功，并得到响应 **/
            if (result.getStatusLine().getStatusCode() == 200) {
                String str = "";
                try {
                    /** 读取服务器返回过来的json字符串数据 **/
                    str = EntityUtils.toString(result.getEntity());
                    if (noNeedResponse) {
                        return null;
                    }
                    /** 把json字符串转换成json对象 **/
                    jsonResult = JSONObject.parseObject(str);
                } catch (Exception e) {
                    logger.error("post请求提交失败:" + url, e);
                }
            }
        } catch (IOException e) {
            logger.error("post请求提交失败:" + url, e);
        }
        return jsonResult;
    }

    /**
     * 发送get请求
     *
     * @param url 路径
     * @return
     */
    public static JSONObject httpGet(String url) {
        // get请求返回结果
        JSONObject jsonResult = null;
        try {
            DefaultHttpClient client = new DefaultHttpClient();
            // 发送get请求
            HttpGet request = new HttpGet(url);
            request.addHeader("", "");
            HttpResponse response = client.execute(request);

            /** 请求发送成功，并得到响应 **/
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                /** 读取服务器返回过来的json字符串数据 **/
                String strResult = EntityUtils.toString(response.getEntity());
                /** 把json字符串转换成json对象 **/
                jsonResult = JSONObject.parseObject(strResult);
                url = URLDecoder.decode(url, "UTF-8");
            } else {
                logger.error("get请求提交失败:" + url);
            }
        } catch (IOException e) {
            logger.error("get请求提交失败:" + url, e);
        }
        return jsonResult;
    }


    /**
     * 发送get请求
     *
     * @param url 路径
     * @return
     */
    public static String httpGet(String url, int p) {
        // get请求返回结果
        String strResult = null;
        try {
            DefaultHttpClient client = new DefaultHttpClient();
            // 发送get请求
            HttpGet request = new HttpGet(url);
            HttpResponse response = client.execute(request);

            /** 请求发送成功，并得到响应 **/
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                /** 读取服务器返回过来的json字符串数据 **/
                strResult = EntityUtils.toString(response.getEntity());
                /** 把json字符串转换成json对象 **/

                url = URLDecoder.decode(url, "UTF-8");
            } else {
                logger.error("get请求提交失败:" + url);
            }
        } catch (IOException e) {
            logger.error("get请求提交失败:" + url, e);
        }
        return strResult;
    }

    /**
     * http请求
     *
     * @param param 请求参数
     * @param url   请求地址
     * @return
     */
    public static JSONObject HttpPost(List<NameValuePair> param, String url) {
        String Str = null;
        JSONObject jsonResult = null;
        HttpEntity entity = null;
        try {
            if (url != null) {
                DefaultHttpClient httpclient = new DefaultHttpClient();
                HttpPost post = new HttpPost(url);
                entity = new UrlEncodedFormEntity(param);
                post.setEntity(entity);
                HttpResponse response = httpclient.execute(post);
                if (response != null) {
                    Str = EntityUtils.toString(response.getEntity());
                    int resCode = response.getStatusLine().getStatusCode();
                    if (resCode == 200) {
                        if (Str != null) {
                            jsonResult = JSONObject.parseObject(Str);
                        }
                    } else {
                        throw new Exception(Str);
                    }

                }
            }
        } catch (UnsupportedEncodingException e) {
            logger.error("Post Exception:" + e + "=====url:" + url);
        } catch (ClientProtocolException e) {
            logger.error("Post Exception:" + e + "=====url:" + url);
        } catch (IOException e) {
            logger.error("Post Exception:" + e + "=====url:" + url);
        } catch (Exception e) {
            logger.error("Post Exception:" + e + "=====url:" + url);
        } catch (Error e) {
            logger.error("Post Exception:" + e + "=====url:" + url);
        }
        return jsonResult;
    }

    /**
     * HttpsPost:(HTTPS请求)
     *
     * @param url
     * @param map
     * @param charset
     * @return
     * @author rufei.cn
     */
    public static JSONObject HttpsPost(String url, JSONObject map) {
        HttpClient httpClient = null;
        HttpPost httpPost = null;
        String result = null;
        JSONObject jsonResult = null;
        String charset = "utf-8";
        try {
            if (map == null || map.isEmpty()) {
                return jsonResult;
            }
            httpClient = new SSLClient();
            httpPost = new HttpPost(url);
            // 设置参数
            List<NameValuePair> list = new ArrayList<NameValuePair>();
            Iterator iterator = map.entrySet().iterator();
            while (iterator.hasNext()) {
                Entry<String, String> elem = (Entry<String, String>) iterator
                        .next();
                list.add(new BasicNameValuePair(elem.getKey(), elem.getValue()));
            }
            if (list.size() > 0) {
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list,
                        charset);
                httpPost.setEntity(entity);
            }
            HttpResponse response = httpClient.execute(httpPost);
            if (response != null) {
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    result = EntityUtils.toString(resEntity, charset);
                    if (result != null) {
                        jsonResult = JSONObject.parseObject(result);
                    }
                }
            }
        } catch (Exception ex) {
            logger.error("Https Exception:" + ex + "=====url:" + url);
            ex.printStackTrace();
        } catch (Error ex) {
            logger.error("Https Exception:" + ex + "=====url:" + url);
            ex.printStackTrace();

        }
        return jsonResult;
    }


    /**
     * httpsGet:(发送HTTPS get请求)
     *
     * @param url
     * @return
     * @author rufei.cn
     */
    public static String httpsGet(String url) {
        String charset = "utf-8";
        HttpClient httpClient = null;
        HttpGet httpGet = null;
        String result = null;
        try {
            httpClient = new SSLClient();
            httpGet = new HttpGet(url);
            HttpResponse response = httpClient.execute(httpGet);
            if (response != null) {
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    result = EntityUtils.toString(resEntity, charset);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Https Exception:" + e + "=====url:" + url);
        }

        return result;
    }


    /**
     * setGetHeader:设置 get 请求头
     *
     * @param properties
     * @return
     * @author rufei.cn
     * @Date 2017/11/16 18:13
     **/
    private static void setGetHeader(Map<String, Object> headerList, HttpGet httpGet) {
        if (httpGet == null) {
            return;
        }
        Set<Entry<String, Object>> entries = headerList.entrySet();
        for (Entry<String, Object> entry : entries) {
            String key = entry.getKey();
            Object values = entry.getValue();
            String value = "";
            if (values != null) {
                value = values.toString();
            }
            httpGet.setHeader(key, value);
        }
    }

    /**
     * httpsGet:(发送HTTPS get请求)
     *
     * @param url
     * @return
     * @author rufei.cn
     */
    public static String httpsGet(String url, Map headerList) {
        String charset = "utf-8";
        HttpClient httpClient = null;
        HttpGet httpGet = null;
        String result = null;
        try {
            httpClient = new SSLClient();
            httpGet = new HttpGet(url);
            if (headerList != null && !headerList.isEmpty()) {
                setGetHeader(headerList, httpGet);
            }
            HttpResponse response = httpClient.execute(httpGet);
            if (response != null) {
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    result = EntityUtils.toString(resEntity, charset);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Https Exception:" + e + "=====url:" + url);
        }

        return result;
    }
    
}
