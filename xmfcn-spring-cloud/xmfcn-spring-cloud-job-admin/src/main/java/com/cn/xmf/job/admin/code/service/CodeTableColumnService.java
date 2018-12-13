package com.cn.xmf.job.admin.code.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cn.xmf.base.model.Partion;
import com.cn.xmf.job.admin.code.dao.CodeTableColumnDao;
import com.cn.xmf.job.admin.code.model.CodeTable;
import com.cn.xmf.job.admin.code.model.CodeTableColumn;
import com.cn.xmf.job.admin.common.SysCommonService;
import com.cn.xmf.job.admin.util.TableColumnUtils;
import com.cn.xmf.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;

/**
 * Service(表字段信息)
 *
 * @author airufei
 * @version 2018-12-10
 */
@Service
@SuppressWarnings("all")
public class CodeTableColumnService {

    @Autowired
    private CodeTableColumnDao codeTableColumnDao;
    @Autowired
    private CodeTableColumnHelperService codeTableColumnHelperService;

    @Autowired
    private CodeTableService codeTableService;

    @Autowired
    private SysCommonService sysCommonService;
    private static Logger logger = LoggerFactory.getLogger(CodeTableColumnService.class);

    /**
     * getList(获取表字段信息带分页数据-服务)
     *
     * @param json
     * @return
     * @author airufei
     */
    public Partion getList(@RequestBody JSONObject json) {
        logger.info("getList(获取表字段信息带分页数据-服务) 开始 json={}", json);
        if (json == null || json.size() < 1) {
            return null;
        }
        Partion pt = null;
        try {
            int totalcount = codeTableColumnHelperService.getTotalCount(json);
            List<CodeTableColumn> list = null;
            if (totalcount > 0) {
                list = codeTableColumnDao.getList(json);
            }
            pt = new Partion(json, list, totalcount);
        } catch (Exception e) {
            String msg = "getList(获取表字段信息 异常 " + StringUtil.getExceptionMsg(e);
            logger.error(msg);
            String parms = null;
            if (json != null) {
                parms = json.toString();
            }
            sysCommonService.sendDingMessage("base-service[getList]", parms, null, msg, this.getClass());
            e.printStackTrace();
        }
        logger.info("getList(获取表字段信息带分页数据-服务) 结束 ");
        return pt;
    }

    /**
     * getCodeTableColumnList(获取表字段信息 不带分页数据-服务)
     *
     * @param codeTableColumn
     * @return
     * @author airufei
     */
    public List<CodeTableColumn> getCodeTableColumnList(@RequestBody CodeTableColumn codeTableColumn) {
        String parms = JSON.toJSONString(codeTableColumn);
        List<CodeTableColumn> list = null;
        logger.info("getCodeTableColumnList(获取表字段信息 不带分页数据-服务) 开始 parms={}", parms);
        if (codeTableColumn == null) {
            return list;
        }
        try {
            list = codeTableColumnDao.getCodeTableColumnList(codeTableColumn);
        } catch (Exception e) {
            String msg = "getCodeTableColumnList 异常 " + StringUtil.getExceptionMsg(e);
            logger.error(msg);
            sysCommonService.sendDingMessage("base-service[getCodeTableColumnList]", parms, null, msg, this.getClass());
            e.printStackTrace();
        }
        logger.info("getCodeTableColumnList(获取表字段信息 不带分页数据-服务) 结束");
        return list;
    }


    /**
     * save (保存表字段信息 数据-服务)
     *
     * @param codeTableColumn
     * @return
     * @author airufei
     */
    public CodeTableColumn save(@RequestBody CodeTableColumn codeTableColumn) {
        String parms = JSON.toJSONString(codeTableColumn);
        logger.info("save (保存表字段信息 数据-服务) 开始 parms={}", parms);
        if (codeTableColumn == null) {
            return codeTableColumn;
        }
        try {
            codeTableColumn = codeTableColumnHelperService.save(codeTableColumn);
        } catch (Exception e) {
            String msg = "save (保存表字段信息 数据-服务) " + StringUtil.getExceptionMsg(e);
            logger.error(msg);
            sysCommonService.sendDingMessage("base-service[save]", parms, null, msg, this.getClass());
            e.printStackTrace();
        }
        logger.info("save (保存表字段信息 数据-服务) 结束");
        return codeTableColumn;
    }

    /**
     * getCodeTableColumn(获取表字段信息单条数据-服务)
     *
     * @param codeTableColumn
     * @return
     * @author airufei
     */
    public CodeTableColumn getCodeTableColumn(@RequestBody CodeTableColumn codeTableColumn) {
        CodeTableColumn ret = null;
        String parms = JSON.toJSONString(codeTableColumn);
        List<CodeTableColumn> list = null;
        logger.info("getCodeTableColumn(获取表字段信息单条数据-服务) 开始 parms={}", parms);
        if (codeTableColumn == null) {
            return ret;
        }
        try {
            ret = codeTableColumnHelperService.getSignleCodeTableColumn(codeTableColumn);
        } catch (Exception e) {
            String msg = "getCodeTableColumn(获取表字段信息单条数据-服务) " + StringUtil.getExceptionMsg(e);
            logger.error(msg);
            sysCommonService.sendDingMessage("base-service[getCodeTableColumn]", parms, null, msg, this.getClass());
            e.printStackTrace();
        }
        logger.info("getCodeTableColumn(获取表字段信息单条数据-服务) 结束 ");
        return ret;
    }

    /**
     * delete(逻辑删除表字段信息数据-服务)
     *
     * @param id
     * @return
     * @author airufei
     */
    public boolean delete(Long id) {
        logger.info("delete(逻辑删除表字段信息数据-服务) 开始 id={}", id);
        boolean isSuccess = false;
        if (id < 1) {
            return isSuccess;
        }
        CodeTableColumn dt = codeTableColumnHelperService.getCodeTableColumnById(id);
        if (dt == null) {
            return isSuccess;
        }
        codeTableColumnDao.delete(id);
        isSuccess = true;
        logger.info("delete(逻辑删除表字段信息数据-服务)结束 id={},isSuccess={}", id, isSuccess);
        return isSuccess;
    }

    /**
     * deleteTable(物理删除表字段信息数据-服务)
     *
     * @param id
     * @return
     * @author airufei
     */
    public boolean deleteTable(String tableName) {
        logger.info("deleteTable(物理删除表字段信息数据-服务) 开始 tableName={}", tableName);
        boolean isSuccess = false;
        if (StringUtil.isBlank(tableName)) {
            return isSuccess;
        }
        codeTableColumnDao.deleteTable(tableName);
        isSuccess = true;
        logger.info("delete(物理删除表字段信息数据-服务)结束 tableName={},isSuccess={}", tableName, isSuccess);
        return isSuccess;
    }

    /**
     * deleteTable(物理删除表字段信息数据-服务)
     *
     * @param id
     * @return
     * @author airufei
     */
    public boolean addTrainRecordBatch(List<CodeTableColumn> list) {
        logger.info("deleteTable(物理删除表字段信息数据-服务) 开始 list={}", JSON.toJSONString(list));
        boolean isSuccess = false;
        if (list == null || list.size() <= 0) {
            return isSuccess;
        }
        codeTableColumnDao.addTrainRecordBatch(list);
        isSuccess = true;
        logger.info("delete(物理删除表字段信息数据-服务)结束isSuccess={}", isSuccess);
        return isSuccess;
    }

    /**
     * 获取表字段信息 mysql
     *
     * @param tableName
     * @return
     */
    public List<CodeTableColumn> getTableColumnList(String tableName) {
        List<CodeTableColumn> list = codeTableColumnDao.getTableColumnList(tableName);
        if (list == null || list.size() <= 0) {
            return list;
        }
        CodeTable gt = new CodeTable();
        gt.setName(tableName);
        CodeTable table = codeTableService.getCodeTable(gt);//根据表名查询表Id
        List<CodeTableColumn> resList = new ArrayList();
        for (CodeTableColumn item : list) {
            TableColumnUtils.initColumnField(item);//初始化表列信息
            if (table != null) {
                item.setTableId(String.valueOf(table.getId()));
                item.setTableName(table.getName());
            }
            String name = item.getName();
            if ("Id".equals(name) || "ID".equals(name)) {
                item.setIsPk("1");
            } else {
                item.setIsPk("0");
            }
            resList.add(item);
        }
        List<CodeTableColumn> newList = new ArrayList();
        for (CodeTableColumn con : resList) {
            String sqlType = con.getJdbcType();
            if (sqlType != null && (sqlType.contains("int"))) {
                con.setJavaType("java.lang.Integer");
            } else if (sqlType != null && sqlType.contains("bigint")) {
                con.setJavaType("java.lang.Long");
            } else if (sqlType != null && (sqlType.contains("datetime") || sqlType.contains("date"))) {
                con.setJavaType("java.util.Date");
            } else if (sqlType != null && (sqlType.contains("decimal"))) {
                con.setJavaType("java.math.BigDecimal");
            } else {
                con.setJavaType("java.lang.String");
            }
            newList.add(con);
        }
        return newList;
    }

}