package com.cn.xmf.job.admin.code.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cn.xmf.base.model.Partion;
import com.cn.xmf.job.admin.code.model.CodeTableColumn;
import com.cn.xmf.job.admin.code.service.CodeTableColumnService;
import com.cn.xmf.job.admin.common.SysCommonService;
import com.cn.xmf.job.core.biz.model.ReturnT;
import com.cn.xmf.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * CodeTableColumnController(表字段信息)
 *
 * @author airufei
 * @version 2018-12-10
 */
@Controller
@RequestMapping("/codeTableColumn")
@SuppressWarnings("all")
public class CodeTableColumnController {

    private static Logger logger = LoggerFactory.getLogger(CodeTableColumnController.class);
    @Autowired
    private CodeTableColumnService codeTableColumnService;
    @Autowired
    private SysCommonService sysCommonService;

    @RequestMapping
    public String index(HttpServletRequest request, Model model) {
        String tableName = request.getParameter("tableName");
        if (StringUtil.isBlank(tableName)) {
            model.addAttribute("errorMsg", "表名tableName不能为空");
            return "common/common-error";
        }
        CodeTableColumn column = new CodeTableColumn();
        column.setTableName(tableName);
        List<CodeTableColumn> list = codeTableColumnService.getCodeTableColumnList(column);
        model.addAttribute("colist", list);
        return "code/codeTableColumn-index";
    }

    /**
     * getList:(获取表字段信息分页查询接口)
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
            String tableId = request.getParameter("tableId");
            String tableName = "t_mgt_user";//request.getParameter("tableName");
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
            logger.info("getList:(获取表字段信息分页查询接口) 开始  param={}", param);
            param.put("flag", 0);
            param.put("tableName", tableName);
            // param.put("name", name);
            Partion pt = codeTableColumnService.getList(param);
            List<CodeTableColumn> list = null;
            int totalCount = 0;
            if (pt != null) {
                list = (List<CodeTableColumn>) pt.getList();
                totalCount = pt.getTotalCount();
            }
            retJon.put("data", list);
            retJon.put("recordsTotal", totalCount);
            retJon.put("recordsFiltered", totalCount);
        } catch (Exception e) {
            String msg = "getList:(获取表字段信息分页查询接口) 异常====>" + StringUtil.getExceptionMsg(e);
            logger.error(msg);
            sysCommonService.sendDingMessage("getList", param.toString(), JSON.toJSONString(retJon), msg, this.getClass());
            e.printStackTrace();
        }
        logger.info("getList:(获取表字段信息分页查询接口) 结束");
        return retJon;
    }

    /**
     * delete:(逻辑删除表字段信息数据接口)
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
            boolean ret = codeTableColumnService.delete(newId);
            if (ret) {
                returnT.setCode(ReturnT.SUCCESS_CODE);
                returnT.setMsg("成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
            returnT = returnT.FAIL;
            String msg = "delete:(逻辑删除表字段信息数据接口) error===>" + StringUtil.getExceptionMsg(e);
            logger.error(msg);
            sysCommonService.sendDingMessage("delete", ids, null, msg, this.getClass());
        }
        logger.info("delete 结束============>" + JSON.toJSONString(returnT));
        return returnT;
    }

    /**
     * save:(保存表字段信息数据接口)
     *
     * @param request
     * @param parms
     * @return
     * @Author airufei
     */
    @RequestMapping(value = "save")
    @ResponseBody
    public ReturnT<String> save(CodeTableColumn codeTableColumn) {
        ReturnT<String> returnT = new ReturnT<>(ReturnT.FAIL_CODE, "保存数据失败");
        String parms = null;
        try {
            parms = JSON.toJSONString(codeTableColumn);
            logger.info("save:(保存表字段信息数据接口) 开始  parms={}", parms);
            if (codeTableColumn == null) {
                returnT.setMsg("参数为空");
                return returnT;
            }
            if (codeTableColumn == null) {
                returnT.setMsg("参数错误");
                return returnT;
            }
            CodeTableColumn ret = codeTableColumnService.save(codeTableColumn);
            if (ret == null) {
                returnT.setMsg("保存数据失败");
                return returnT;
            }
            returnT.setCode(ReturnT.SUCCESS_CODE);
            returnT.setMsg("成功");
        } catch (Exception e) {
            e.printStackTrace();
            String msg = "save:(保存表字段信息数据接口) error===>" + StringUtil.getExceptionMsg(e);
            logger.error(msg);
            sysCommonService.sendDingMessage("save", parms, JSON.toJSONString(returnT), msg, this.getClass());
            returnT.setMsg("服务器繁忙，请稍后再试");
        }
        logger.info("save 结束============>" + JSON.toJSONString(returnT));
        return returnT;
    }


    /**
     * saveList:(保存表字段信息数据接口)
     *
     * @param request
     * @param parms
     * @return
     * @Author airufei
     */
    @RequestMapping(value = "saveList")
    @ResponseBody
    public ReturnT<String> saveList(@RequestParam(value = "list", required = false) List<CodeTableColumn> list) {
        ReturnT<String> returnT = new ReturnT<>(ReturnT.FAIL_CODE, "保存数据失败");
        String parms = null;
        try {
            parms = JSON.toJSONString(list);
            logger.info("saveList:(保存表字段信息数据接口) 开始  parms={}", parms);
            if (list == null || list.size() <= 0) {
                returnT.setMsg("参数为空");
                return returnT;
            }
            CodeTableColumn codeTableColumn = list.get(0);
            if (codeTableColumn == null) {
                returnT.setMsg("参数为空");
                return returnT;
            }
            String tableName = codeTableColumn.getTableName();
            if (StringUtil.isBlank(tableName)) {
                returnT.setMsg("表名称为空");
                return returnT;
            }
            boolean batch = false;
            boolean b = codeTableColumnService.deleteTable(tableName);
            if (b) {
                batch = codeTableColumnService.addTrainRecordBatch(list);
            }
            CodeTableColumn ret = codeTableColumnService.save(codeTableColumn);
            if (!batch) {
                returnT.setMsg("保存数据失败");
                return returnT;
            }
            returnT.setCode(ReturnT.SUCCESS_CODE);
            returnT.setMsg("成功");
        } catch (Exception e) {
            e.printStackTrace();
            String msg = "saveList:(保存表字段信息数据接口) error===>" + StringUtil.getExceptionMsg(e);
            logger.error(msg);
            sysCommonService.sendDingMessage("save", parms, JSON.toJSONString(returnT), msg, this.getClass());
            returnT.setMsg("服务器繁忙，请稍后再试");
        }
        logger.info("save 结束============>" + JSON.toJSONString(returnT));
        return returnT;
    }


}