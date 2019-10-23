package com.cn.xmf.job.admin.redis;

import com.alibaba.fastjson.JSONObject;
import com.cn.xmf.job.admin.common.SysCommonService;
import com.cn.xmf.job.admin.menu.service.JobMenuService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Set;

/**
 * RedisController(redis-运行信息)
 *
 * @author rufei.cn
 * @version 2018-10-17
 */
@Controller
@RequestMapping("/redis")
@SuppressWarnings("all")
public class RedisController {

    private static Logger logger = LoggerFactory.getLogger(JobMenuService.class);
    @Autowired
    private SysCommonService sysCommonService;

    @RequestMapping
    public String index(HttpServletRequest request, Model model) {
        JSONObject redisInfo = sysCommonService.getRedisInfo();
        JSONObject jsonObject =null;
        if(redisInfo!=null)
        {
            jsonObject=redisInfo.getJSONObject("info");
        }
        Set<Map.Entry<String, Object>> entries =null;
        if(jsonObject!=null)
        {
            entries = jsonObject.entrySet();
        }
        model.addAttribute("list", entries);
        return "redis/redis-index";
    }
}