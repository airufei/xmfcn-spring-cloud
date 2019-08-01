package com.cn.xmf.job.admin.sys.controller;

import com.alibaba.fastjson.JSONObject;
import com.cn.xmf.base.model.ResultCodeMessage;
import com.cn.xmf.job.admin.sys.KafKaProducerService;
import com.cn.xmf.job.core.biz.model.ReturnT;
import com.cn.xmf.util.ConstantUtil;
import com.cn.xmf.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * KafKaProducerController(发送数据到kafka)
 *
 * @author airufei
 * @version 2019-05-27
 */
@Controller
@RequestMapping("/kafka")
@SuppressWarnings("all")
public class KafKaProducerController {

    private static Logger logger = LoggerFactory.getLogger(KafKaProducerController.class);

    @Autowired
    private KafKaProducerService kafKaProducerService;

    @RequestMapping
    public String index(HttpServletRequest request, Model model) {
        return "kafka/kafka-index";
    }


    /**
     * send(发送数据到kafka)
     *
     * @return
     */
    @RequestMapping("send")
    @ResponseBody
    public ReturnT<String> send(HttpServletRequest request) {
        ReturnT<String> retData = new ReturnT<>(ResultCodeMessage.FAILURE, "执行失败，没有数据");
        try {
            String topic = request.getParameter("topic");
            String kafkaData = request.getParameter("kafkaData");
            if (StringUtil.isBlank(topic)) {
                retData.setMsg("请输入topic");
                return retData;
            }
            if (StringUtil.isBlank(kafkaData)) {
                retData.setMsg("请输入kafkaData");
                return retData;
            }
            JSONObject sendJson = new JSONObject();
            sendJson.put("topic", topic);
            sendJson.put("value", kafkaData);
            boolean sendKafka = kafKaProducerService.sendKafka(sendJson);
            if (sendKafka) {
                retData.setCode(ResultCodeMessage.SUCCESS);
                retData.setMsg(ResultCodeMessage.SUCCESS_MESSAGE);
            }
        } catch (Exception e) {

            retData = ReturnT.FAIL;
            String msg = "send(发送数据到kafka) error===>" + StringUtil.getExceptionMsg(e);
            logger.error(msg);
            retData.setMsg(msg);
        }
        return retData;
    }
}