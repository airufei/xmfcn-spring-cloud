package com.cn.xmf.job.admin.code.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cn.xmf.base.model.Partion;
import com.cn.xmf.job.admin.code.model.CodeTable;
import com.cn.xmf.job.admin.code.service.CodeTableService;
import com.cn.xmf.job.admin.common.SysCommonService;
import com.cn.xmf.job.core.biz.model.ReturnT;
import com.cn.xmf.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * CodeTableController(数据表信息)
 *
 * @author airufei
 * @version 2018-12-10
 */
@Controller
@RequestMapping("/codeTable")
@SuppressWarnings("all")
public class CodeTableController {

    private static Logger logger = LoggerFactory.getLogger(CodeTableController.class);
    @Autowired
    private CodeTableService codeTableService;
    @Autowired
    private SysCommonService sysCommonService;
    @Value("spring.datasource.name")
    private String dbName;

    @RequestMapping
    public String index() {
        return "code/codeTable-index";
    }

    /**
     * getList:(获取数据表信息分页查询接口)
     *
     * @param request
     * @return
     * @Author airufei
     */
    @RequestMapping("pageList")
    @ResponseBody
    public JSONObject getList(HttpServletRequest request) {
        JSONObject retJon = new JSONObject();
        JSONObject param = null;
        try {
            String startStr = request.getParameter("start");
            String length = request.getParameter("length");
            String name = request.getParameter("name");
            int pageSize = 10;
            int pageNo = 1;
            int start = 0;
            if (StringUtil.isNotBlank(startStr)) {
                start = StringUtil.stringToInt(startStr);
            }
            if (StringUtil.isNotBlank(length)) {
                pageSize = StringUtil.stringToInt(length);
            }
            if (start > 0) {
                pageNo = (start / pageSize) + 1;
            }
            param = StringUtil.getPageJSONObject(pageNo, pageSize);
            logger.info("getList:(获取数据表信息分页查询接口) 开始  param={}", param);

            param.put("name", name);
            Partion pt = codeTableService.getList(param);
            List<CodeTable> list = null;
            int totalCount = 0;
            if (pt != null) {
                list = (List<CodeTable>) pt.getList();
                totalCount = pt.getTotalCount();
            }
            retJon.put("data", list);
            retJon.put("recordsTotal", totalCount);
            retJon.put("recordsFiltered", totalCount);
        } catch (Exception e) {
            String msg = "getList:(获取数据表信息分页查询接口) 异常====>" + StringUtil.getExceptionMsg(e);
            logger.error(msg);
            sysCommonService.sendDingMessage("getList", param.toString(), JSON.toJSONString(retJon), msg, this.getClass());
            e.printStackTrace();
        }
        logger.info("getList:(获取数据表信息分页查询接口) 结束");
        return retJon;
    }

    /**
     * delete:(逻辑删除数据表信息数据接口)
     *
     * @param request
     * @param parms
     * @return
     * @Author airufei
     */
    @RequestMapping("delete")
    @ResponseBody
    public ReturnT<String> delete(HttpServletRequest request) {
        ReturnT<String> returnT = new ReturnT<>(ReturnT.FAIL_CODE, "删除失败");
        String ids = null;
        try {
            ids = request.getParameter("id");
            int id = StringUtil.stringToInt(ids);
            logger.info("delete 开始============>" + id);
            if (id <= 0) {
                returnT.setMsg("参数错误");
                return returnT;
            }
            long newId = id;
            boolean ret = codeTableService.delete(newId);
            if (ret) {
                returnT.setCode(ReturnT.SUCCESS_CODE);
                returnT.setMsg("成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
            returnT = returnT.FAIL;
            String msg = "delete:(逻辑删除数据表信息数据接口) error===>" + StringUtil.getExceptionMsg(e);
            logger.error(msg);
            sysCommonService.sendDingMessage("delete", ids, null, msg, this.getClass());
        }
        logger.info("delete 结束============>" + JSON.toJSONString(returnT));
        return returnT;
    }

    /**
     * 获取当前数据库的所有表信息（不包含系统表）
     *
     * @param dbName
     * @param tableName
     * @return
     */
    @RequestMapping(value = "getTableList")
    @ResponseBody
    public List<CodeTable> getTableList(String tableName) {
        List<CodeTable> tableList = null;
        if (StringUtil.isBlank(tableName)) {
            return tableList;
        }
        try {
            tableList = codeTableService.getTableList(dbName, tableName);
        } catch (Exception e) {
            logger.error(StringUtil.getExceptionMsg(e));
            e.printStackTrace();
        }
        return tableList;
    }

    /**
     * save:(保存数据表信息数据接口)
     *
     * @param request
     * @param parms
     * @return
     * @Author airufei
     */
    @RequestMapping(value = "save")
    @ResponseBody
    public ReturnT<String> save(CodeTable codeTable) {
        ReturnT<String> returnT = new ReturnT<>(ReturnT.FAIL_CODE, "保存数据失败");
        String parms = null;
        try {
            parms = JSON.toJSONString(codeTable);
            logger.info("save:(保存数据表信息数据接口) 开始  parms={}", parms);
            if (codeTable == null) {
                returnT.setMsg("参数为空");
                return returnT;
            }
            if (codeTable == null) {
                returnT.setMsg("参数错误");
                return returnT;
            }
            CodeTable ret = codeTableService.save(codeTable);
            if (ret == null) {
                returnT.setMsg("保存数据失败");
                return returnT;
            }
            returnT.setCode(ReturnT.SUCCESS_CODE);
            returnT.setMsg("成功");
        } catch (Exception e) {
            e.printStackTrace();
            String msg = "save:(保存数据表信息数据接口) error===>" + StringUtil.getExceptionMsg(e);
            logger.error(msg);
            sysCommonService.sendDingMessage("save", parms, JSON.toJSONString(returnT), msg, this.getClass());
            returnT.setMsg("服务器繁忙，请稍后再试");
        }
        logger.info("save 结束============>" + JSON.toJSONString(returnT));
        return returnT;
    }

}