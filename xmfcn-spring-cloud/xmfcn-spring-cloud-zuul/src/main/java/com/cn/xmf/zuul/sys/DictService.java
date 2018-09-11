package com.cn.xmf.zuul.sys;

import com.cn.xmf.common.model.common.Dict;
import com.cn.xmf.common.model.common.Partion;
import com.cn.xmf.zuul.common.HttpBasicAuthConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@FeignClient(value = "${base-service.sys-service}")
@RequestMapping("/server/dict/")
public interface DictService {


    /**
     * 分页查询Dict集合
     *
     * @param map
     * @return
     */
    @RequestMapping(value = "getList", consumes = MediaType.APPLICATION_JSON_VALUE)
    Partion list(@RequestBody Map map);

    /**
     * 不带分页查询Dict集合
     *
     * @param dict
     * @return
     */
    @RequestMapping(value = "getDictList", consumes = MediaType.APPLICATION_JSON_VALUE)
    List<Dict> getDictList(@RequestBody Dict dict);
    /**
     * 逻辑删除数据
     *
     * @return the data return
     */
    @RequestMapping(value = "delete", consumes = MediaType.APPLICATION_JSON_VALUE)
    boolean delete(@RequestBody long id);

    /**
     * 获取字典值
     * @param dictType
     * @param dictKey
     * @return
     */
    @RequestMapping("getDictValue")
    String getDictValue(@RequestParam("dictType") String dictType, @RequestParam("dictKey") String dictKey);

}
