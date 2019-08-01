package com.cn.xmf.util;

/**
 * 常量类
 */
@SuppressWarnings("all")
public class ConstantUtil {

    /**
     * 缓存前缀相关
     */
    public static final String CACHE_SYS_BASE_DATA_ = "sys_base_data";//缓存前缀

    public static final String CACHE_KEY_PREFIX_MENU_="cache_key_prefix_menu_";

    /**
     * kafka主题相关
     */
    public static final String XMF_KAFKA_TOPIC_LOG="xmf_kafka_topic_log";//系统日志

    /**
     * 字典类型相关
     */
    public static  final String DICT_TYPE_CONFIG_KAFKA="dict_type_kafka_config";//kafka连接配置
    public static final String DICT_TYPE_BASE_CONFIG ="dict_type_base_config" ;//基础配置类型
    public static final String DICT_TYPE_SYS_NAME="dict_type_sys_name" ;//系统名称组

    /**
     * ES相关
     */
    public static final String ES_SYS_LOG_INDEX ="xmfcn_sys_log_index";//索引名称
    public static final String ES_SYS_LOG_TYPE ="xmfcn_sys_log_type";//文档类型（Type）
}
