package com.cn.xmf.base.model;

/**
 * 消息编码和消息内容
 */
public class ResultCodeMessage {


    public static int NO_LOGIN = 201;// 未登录
    public static int SUCCESS = 200;// 成功
    public static int NO_DATA = 202;//暂时无数据
    public static int PARMS_ERROR = 400;//参数错误
    public static int UNAUTHORIZED = 403;//未授权
    public static int DATA_ERROR = 440;//数据错误
    public static int FAILURE = 500;// 失败

    public static String NO_LOGIN_MESSAGE = "未登录";// 未登录
    public static String UNAUTHORIZED_MESSAGE = "访问未授权";// 未登录
    public static String SUCCESS_MESSAGE = "成功";// 成功
    public static String FAILURE_MESSAGE = "失败";// 失败
    public static String EXCEPTION_MESSAGE = "服务器繁忙，请稍后再试";// 服务器繁忙，请稍后再试
    public static String PARMS_ERROR_MESSAGE = "参数错误";//参数错误
    public static String DATA_ERROR_MESSAGE = "数据错误";//数据错误
    public static String NO_DATA_MESSAGE = "暂时无数据";//暂时无数据

}
