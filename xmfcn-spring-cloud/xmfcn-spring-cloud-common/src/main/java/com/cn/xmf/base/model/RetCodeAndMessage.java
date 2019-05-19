package com.cn.xmf.base.model;

public class RetCodeAndMessage {


	public static int NO_LOGIN = 201;// 未登录
    public static int SUCCESS = 200;// 成功
    public static int NO_DATA = 202;//暂时无数据
    public static int PARMS_ERROR = 400;//参数错误
    public static int DATA_ERROR = 440;//数据错误
    public static int SYS_ERROR = 500;// 失败

    public static String NO_LOGIN_MESSAGE = "未登录";// 未登录
    public static String SUCCESS_MESSAGE = "成功";// 成功
    public static String SYS_ERROR_MESSAGE = "服务器繁忙，请稍后再试";// 失败
    public static String PARMS_ERROR_MESSAGE = "参数错误";//参数错误
    public static String DATA_ERROR_MESSAGE = "参数错误";//参数错误
    public static String NO_DATA_MESSAGE = "暂时无数据";//暂时无数据


}
