package com.cn.xmf.service.dict.dao;

import com.cn.xmf.model.dict.Dict;

import java.util.List;
import java.util.Map;

/**
 * 数据字典表DAO接口
 *
 * @author 数据字典表
 * @version 2017-11-23
 */
@SuppressWarnings("all")
public interface DictDao {

    /**
     * 删除数据（逻辑删除）
     *
     * @param question
     * @return
     */
    public void delete(Long id);

    /**
     * 单条数据增加
     *
     * @param dict
     * @return
     */
    public void add(Dict dict);

    /**
     * 批量数据增加
     *
     * @param dict
     * @return
     */
    public void addTrainRecordBatch(List<Dict> list);

    /**
     * 获取单条数据
     *
     * @param id
     * @return
     */
    public Dict getDictById(Long id);

    /**
     * 修改单条数据
     *
     * @param id
     * @return
     */
    public void updateById(Dict dict);

    /**
     * 获取分页数据
     *
     * @param map
     * @return
     */
    public List<Dict> getList(Map map);


    /**
     * 获取集合数据，不带分页
     *
     * @param map
     * @return
     */
    public List<Dict> getDictList(Dict dict);

    /**
     * 获取分页记录总数
     *
     * @param map
     * @return
     */
    public Integer getTotalCount(Map map);

}