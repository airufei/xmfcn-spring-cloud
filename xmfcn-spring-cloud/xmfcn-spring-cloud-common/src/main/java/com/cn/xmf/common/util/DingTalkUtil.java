package com.cn.xmf.common.util;

import com.alibaba.fastjson.JSONObject;
import com.cn.xmf.common.model.common.Message;
import com.cn.xmf.common.model.common.SendResult;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import java.io.IOException;

public class DingTalkUtil {
     /*    
      * send:(向钉钉发送信息)
      * @author: airufei  
      * @date:2018/1/3 17:13
      * @param webhook 钉钉地址
      * @param message 发送的消息内容
      * @return:  
      */ 
    public  static SendResult send(String webhook, Message message) throws IOException {
        HttpClient httpclient = HttpClients.createDefault();
        HttpPost httppost = new HttpPost(webhook);
        httppost.addHeader("Content-Type", "application/json; charset=utf-8");
        StringEntity se = new StringEntity(message.toJsonString(), "utf-8");
        httppost.setEntity(se);
        SendResult sendResult = new SendResult();
        HttpResponse response = httpclient.execute(httppost);
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            String result = EntityUtils.toString(response.getEntity());
            JSONObject obj = JSONObject.parseObject(result);
            Integer errcode = obj.getInteger("errcode");
            sendResult.setErrorCode(errcode);
            sendResult.setErrorMsg(obj.getString("errmsg"));
            sendResult.setIsSuccess(errcode.equals(0));
        }
        return sendResult;
    }
}
