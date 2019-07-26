package com.cn.xmf.service.dict.service;

import com.alibaba.fastjson.JSON;
import com.cn.xmf.base.model.Partion;
import com.cn.xmf.model.dict.Dict;
import com.cn.xmf.service.dict.dao.DictDao;
import com.cn.xmf.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * Service(数据字典表)
 *
 * @author 数据字典表
 * @version 2017-11-23
 */
@RestController
@RequestMapping(value = "/server/dict/")
@SuppressWarnings("all")
public class DictService {

    @Autowired
    private DictDao dictDao;

    private static Logger logger = LoggerFactory.getLogger(DictService.class);

    /**
     * 删除数据（逻辑删除）
     *
     * @param question
     * @return
     */
    @RequestMapping("delete")
    public boolean delete(Long id) {
        logger.info("删除数据（逻辑删除）开始 id={}", id);
        boolean isSuccess = false;
        if (id < 1) {
            return isSuccess;
        }
        Dict dt = getDictById(id);
        if (dt == null) {
            return isSuccess;
        }
        String dictType = dt.getType();
        String key = dictType;
        dictDao.delete(id);
        isSuccess = true;
        logger.info("删除数据（逻辑删除）结束 id={},isSuccess={}", id, isSuccess);
        return isSuccess;
    }


    /**
     * 保存数据（增加或者修改 ）
     *
     * @param dict
     * @return
     */
    @RequestMapping(value = "save")
    public Dict save(@RequestBody Dict dict) {
        String parms = JSON.toJSONString(dict);
        logger.info("保存数据（增加或者修改 ）开始 parms={}", parms);
        if (dict == null) {
            return dict;
        }
        String dictType = dict.getType();
        String key = dictType;
        if (dict.getId() != null && dict.getId() > 0) {
            dictDao.updateById(dict);
        } else {
            dictDao.add(dict);
        }
        parms = JSON.toJSONString(dict);
        logger.info("保存数据（增加或者修改 ）结束 parms={}", parms);
        return dict;
    }

    /**
     * 获取单条数据实体
     *
     * @param map
     * @return
     */
    @RequestMapping("getDict")
    public Dict getDict(@RequestBody Dict dict) {
        String parms = JSON.toJSONString(dict);
        logger.info("获取单条数据实体 开始 parms={}", parms);
        Dict ret = null;
        if (dict == null) {
            return ret;
        }
        List<Dict> list = getDictList(dict);
        if (list != null && list.size() > 0) {
            ret = list.get(0);
        }
        logger.info("获取单条数据实体 结束 parms={}", parms);
        return ret;
    }


    /**
     * getDictValueByKey:(根据key和Type获取字典值)
     *
     * @param dictType the dict type
     * @param dictKey  the dict key
     * @return the dict value
     */
    @RequestMapping("getDictValue")
    public String getDictValue(String dictType, String dictKey) {
        //logger.info("getDictValueByKey:(根据key和Type获取字典值) 开始 dictType={},dictKey={}", dictType, dictKey);
        String value = "";
        if (StringUtil.isBlank(dictType) || StringUtil.isBlank(dictKey)) {
            logger.info("getDictValueByKey:(根据key和Type获取字典值) 结束 dictType={},dictKey={},value={}", dictType, dictKey, value);
            return value;
        }
        Dict dict = new Dict();
        dict.setType(dictType);
        List<Dict> list = getDictList(dict);
        if (list == null || list.size() < 1) {
            logger.info("getDictValueByKey:(根据key和Type获取字典值) 结束 dictType={},dictKey={},value={}", dictType, dictKey, value);
            return value;
        }
        for (Dict d : list) {
            String dk = d.getDictKey();
            if (dk != null && dk.equals(dictKey)) {
                value = d.getDictValue();
                break;
            }
        }
        //logger.info("getDictValueByKey:(根据key和Type获取字典值) 结束 dictType={},dictKey={},value={}", dictType, dictKey, value);
        return value;
    }


    /**
     * 修改单条数据
     *
     * @param id
     * @return
     */
    @RequestMapping("updateById")
    public boolean updateById(@RequestBody Dict dict) {
        boolean isSuccess = false;
        String parms = JSON.toJSONString(dict);
        logger.info("修改单条数据 开始 parms={}", parms);
        if (dict == null) {
            return isSuccess;
        }
        String dictType = dict.getType();
        String key = dictType;
        dictDao.updateById(dict);
        isSuccess = true;
        logger.info("修改单条数据 结束 parms={},isSuccess={}", parms, isSuccess);
        return isSuccess;
    }

    /**
     * 获取分页数据
     *
     * @param map
     * @return
     */
    @RequestMapping("getList")
    public Partion getList(@RequestBody Map map) {
        String parms = JSON.toJSONString(map);
        logger.info("获取分页数据 开始 parms={}", parms);
        int totalcount = getTotalCount(map);
        List<Dict> list = null;
        if (totalcount > 0) {
            list = dictDao.getList(map);
        }
        Partion pt = new Partion(map, list, totalcount);
        return pt;
    }

    /**
     * 获取集合数据，不带分页
     *
     * @param map
     * @return
     */
    @RequestMapping("getDictList")
    public List<Dict> getDictList(@RequestBody Dict dict) {
        List<Dict> list = null;
        String parms = JSON.toJSONString(dict);
        logger.info("获取集合数据，不带分页 开始 parms={}", parms);
        if (dict == null) {
            return list;
        }
        list = dictDao.getDictList(dict);
        logger.info("获取集合数据，不带分页，结束。");
        return list;
    }


    /**
     * 获取分页总记录数
     *
     * @param map
     * @return
     */
    private int getTotalCount(Map map) {
        int resCount = 0;
        Integer totalCount = dictDao.getTotalCount(map);
        if (totalCount != null) {
            resCount = totalCount;
        }
        return resCount;
    }

    private Dict getDictById(Long id) {
        return dictDao.getDictById(id);
    }

}