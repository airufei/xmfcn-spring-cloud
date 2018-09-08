package com.cn.xmf.zuul.fallback;

import com.alibaba.fastjson.JSON;
import com.cn.xmf.common.enums.DingMessageType;
import com.cn.xmf.common.model.common.DingMessage;
import com.cn.xmf.common.model.common.RetCode;
import com.cn.xmf.common.model.common.RetData;
import com.cn.xmf.zuul.sys.DingTalkService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.netflix.zuul.filters.route.FallbackProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * APP接口服务熔断处理类
 * 用于下游服务宕机或部署时进行熔断处理
 *
 */
@Component
public class SysLogApiFullbackProvider implements FallbackProvider {

    private static final Logger logger = LoggerFactory.getLogger(SysLogApiFullbackProvider.class);

    @Value("${zuul.routes.log-api.serviceId}")
    String serviceName;

    @Autowired
    private DingTalkService dingTalkService;
    /**
     * 获取下游服务名
     *
     * @return
     */
    @Override
    public String getRoute() {
        return serviceName;
    }

    @Override
    public ClientHttpResponse fallbackResponse(String route, Throwable cause) {
        return new ClientHttpResponse() {
            @Override
            public HttpStatus getStatusCode() throws IOException {
                return HttpStatus.OK;
            }

            @Override
            public int getRawStatusCode() throws IOException {
                return 200;
            }

            @Override
            public String getStatusText() throws IOException {
                return "OK";
            }

            @Override
            public void close() {

            }

            @Override
            public InputStream getBody() throws IOException {
                logger.info(serviceName + "服务触发熔断");
                RetData dataReturn = new RetData();
                dataReturn.setCode(RetCode.FAILURE);
                dataReturn.setMessage("服务繁忙，请稍后再试！");
                dataReturn.setData(null);
                SendMarkdownMessage();
                return new ByteArrayInputStream(JSON.toJSONString(dataReturn).getBytes("utf-8"));
            }

            @Override
            public HttpHeaders getHeaders() {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                return headers;
            }
        };
    }

    /**
     * 发送钉钉消息
     *
     * @return
     */
    public void SendMarkdownMessage() {
        DingMessage dingMessage=new DingMessage();
        dingMessage.setDingMessageType(DingMessageType.MARKDWON);
        dingMessage.setSysName("base-zuul");
        dingMessage.setModuleName("UserApiFullbackProvider");
        dingMessage.setMethodName("fullback()");
        dingMessage.setExceptionMessage(serviceName+"接口超时或者服务中断，启动了熔断机制，请相关人员立即检查，谢谢。");
        dingTalkService.sendMessageToDingTalk(dingMessage);
    }
}
