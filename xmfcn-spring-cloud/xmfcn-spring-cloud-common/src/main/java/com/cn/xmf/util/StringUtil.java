package com.cn.xmf.util;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.LoggingEvent;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cn.xmf.model.ding.EsLogMessage;
import com.cn.xmf.model.ding.MarkdownMessage;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@SuppressWarnings("all")
public class StringUtil extends StringUtils {
    private static Logger logger = LoggerFactory.getLogger(StringUtil.class);

    public static boolean isBlank(final CharSequence cs) {
        if ("null".equals(StringUtils.trim(String.valueOf(cs)))) {
            return true;
        }
        return StringUtils.isBlank(cs);
    }

    public static boolean isNotBlank(final CharSequence cs) {
        if ("null".equals(StringUtils.trim(String.valueOf(cs)))) {
            return false;
        }

        return StringUtils.isNotBlank(cs);
    }

    public static boolean isNotEmpty(final CharSequence cs) {
        if ("null".equals(StringUtils.trim(String.valueOf(cs)))) {
            return false;
        }
        return StringUtils.isNotEmpty(cs);
    }

    /**
     * stringToFloat:(String转float 默认值0)
     *
     * @param
     * @return
     * @author airufei
     * @Date 2017/11/23 15:19
     **/
    public static float stringToFloat(String s) {
        if (isBlank(s)) {
            return 0;
        }
        try {
            return Float.parseFloat(s);
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * stringToDouble:(String 转double 默认值0)
     *
     * @param
     * @return
     * @author airufei
     * @Date 2017/11/23 15:20
     **/
    public static double stringToDouble(String s) {
        try {
            return Double.parseDouble(s);
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * stringToInt:(String 转int 默认值0)
     *
     * @param
     * @return
     * @author airufei
     * @Date 2017/11/23 15:21
     **/
    public static int stringToInt(String strId) {
        int id = 0;
        try {
            id = Integer.parseInt(strId);
        } catch (Exception e) {
        }
        return id;
    }

    /**
     * stringToLong:(String转long，默认值0)
     *
     * @param
     * @return
     * @author airufei
     * @Date 2017/11/23 15:21
     **/
    public static long stringToLong(String strId) {
        long id = 0;
        try {
            id = Long.parseLong(strId);
        } catch (Exception e) {
        }
        return id;
    }

    /**
     * obectToString:(Object 转字符串 默认"")
     *
     * @param
     * @return
     * @author airufei
     * @Date 2017/11/23 15:25
     **/
    public static String obectToString(Object obj) {
        String str = "";
        if (obj != null) {
            str = obj.toString();
        }
        return str;
    }

    public static int StringToId(HttpServletRequest request, String name) {
        int id = 0;
        try {
            id = Integer.parseInt(request.getParameter(name));
        } catch (Exception e) {
        }
        return id;
    }

    /**
     * stringToBoolean:(String转boolean 默认false)
     *
     * @param
     * @return
     * @author airufei
     * @Date 2017/11/23 15:30
     **/
    public static boolean stringToBoolean(String s) {
        boolean b = false;
        try {
            b = Boolean.parseBoolean(s);
        } catch (Exception e) {

        }
        return b;
    }

    /**
     * 字符串转数字
     *
     * @param str 数字字符串
     * @return
     */
    public static int objToInt(Object obj) {
        int result = 0;
        try {
            if (obj != null && obj.toString().length() > 0) {
                result = Integer.parseInt(obj.toString());
            }
        } catch (NumberFormatException e) {

        }
        return result;

    }


    /**
     * getFileExt:(获取文件名后缀 默认值 "")
     *
     * @param
     * @return
     * @author airufei
     * @Date 2017/11/23 15:26
     **/
    public static String getFileExt(String filePath) {
        String fileExt = "";
        if (isBlank(filePath)) {
            return fileExt;
        }
        String path = filePath.trim();
        if (isBlank(path)) {
            return fileExt;
        }
        return path.substring(path.lastIndexOf('.') + 1, path.length());
    }


    /**
     * isNumber:(判断字符是否为数字)
     *
     * @param
     * @return
     * @author airufei
     * @Date 2017/11/23 15:31
     **/
    public static boolean isNumber(String str) {
        if (str.matches("\\d*")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * isMobile :(判断字符是否为手机号)
     *
     * @param
     * @return
     * @author airufei
     * @Date 2017/11/23 15:32
     **/
    public static boolean isMobile(String s) {
        if (s == null) {
            return false;
        }
        if (!s.startsWith("13") && !s.startsWith("15") && !s.startsWith("14") && !s.startsWith("18") && !s.startsWith("17")) {
            return false;
        }

        if (s.length() != 11) {
            return false;
        }

        try {
            Long.parseLong(s);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }


    /**
     * padLeft:(补位左填充)
     *
     * @param
     * @return
     * @author airufei
     * @Date 2017/11/23 15:33
     **/
    public static String padLeft(String input, char c, int length) {
        String output = input;
        while (output.length() < length) {
            output = c + output;
        }
        return output;
    }


    /**
     * getURLDecoderString:(URL解码)
     *
     * @param url
     * @param ENCODE
     * @return
     * @Author airufei
     */
    public static String getURLDecoderString(String url, String encode) {
        String result = "";
        if (null == url) {
            return "";
        }
        try {
            result = java.net.URLDecoder.decode(url, encode);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * getSessionStr:(获取session中的字符串)
     *
     * @param session
     * @param key
     * @return
     * @Author airufei
     */
    public static String getSessionStr(HttpSession session, String key) {
        String str = "";
        Object obj = session.getAttribute(key);
        if (obj != null) {
            str = obj.toString();
        }
        return str;
    }

    /**
     * getSessionStr:(获取session中的对象)
     *
     * @param session
     * @param key
     * @return
     * @Author airufei
     */
    public static String getSessionObj(HttpSession session, String key) {
        String str = "";
        Object obj = session.getAttribute(key);
        if (obj != null) {
            str = obj.toString();
        }
        return str;
    }

    /**
     * getURLEncoderString:(URL编码)
     *
     * @param url
     * @param ENCODE
     * @return
     * @Author airufei
     */
    public static String getURLEncoderString(String url, String ENCODE) {
        String result = "";
        if (null == url) {
            return "";
        }
        try {
            result = java.net.URLEncoder.encode(url, ENCODE);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * getSystemUrl:(获取访问服务器的端口号IP)
     * http://localhost:8081/lcApp 或者http://localhost:8081/lcApp
     *
     * @param request
     * @param isHttps 是否https 链接
     * @return
     * @Author airufei
     */
    public static String getSystemUrl(HttpServletRequest request) {
        // 用于获取服务器IP 端口号 项目名
        int localPort = request.getServerPort();
        String scheme = request.getScheme();
        String serverName = request.getServerName();
        logger.info("ip/domname:" + scheme + "://" + serverName + ":" + localPort + request.getContextPath());
        String url = "";
        if (localPort == 80) {
            url = scheme + "://" + serverName + request.getContextPath();
        } else {
            url = scheme + "://" + serverName + ":" + localPort
                    + request.getContextPath();
        }
        return url;
    }


    /**
     * 显示字符串前N个字
     */
    public static final String getPreInfo(String str, int n) {
        if (str == null) {
            return "";
        }
        if (str.length() <= n) {
            return str;
        }
        str = str.substring(0, n) + "...";
        return str;
    }


    /**
     * getUuId:(获取唯一标识)
     *
     * @return
     * @Author airufei
     */
    public static String getUuId() {

        String uuid = UUID.randomUUID().toString().replace("-", "");
        return uuid;
    }

    /**
     * 将二进制数组转化成字符串
     *
     * @param data
     * @return
     */
    public static String convertToHexString(byte data[]) {
        StringBuffer ret = new StringBuffer();
        for (int i = 0; i < data.length; ++i) {
            int v = data[i] & 255;
            if (v < 16) {
                ret.append('0');
            }
            ret.append(Integer.toString(v, 16).toUpperCase());
        }
        return ret.toString();
    }

    /**
     * getQueryParms:(获取分页参数集合)
     *
     * @param page        当前页
     * @param pageSizeStr 每页记录
     * @return
     * @Author airufei
     */
    public static Map getQueryParms(String page, String pageSizeStr) {
        Map map = new HashMap();
        int currentCount = StringUtil.objToInt(page);
        int pageSize = StringUtil.objToInt(pageSizeStr);
        if (pageSize < 1) {
            pageSize = 10;
        }
        int startIndex = 0;
        if (currentCount <= 1) {
            currentCount = 1;
            startIndex = 0;
        } else {
            startIndex = (currentCount - 1) * pageSize;
        }
        map.put("startIndex", startIndex);
        map.put("pageSize", pageSize);
        map.put("currentCount", currentCount);
        map.put("sortWay", "desc");
        map.put("sortField", "update_time");
        map.put("flag", 1);
        return map;
    }

    /**
     * StrToDouble:(字符转Double)
     *
     * @param amount
     * @return
     * @Author airufei
     */
    public static double StrToDouble(String amount) {
        double result = 0.00;
        try {
            if (StringUtil.isBlank(amount)) {
                return result;
            }
            result = Double.parseDouble(amount);
        } catch (Exception e) {
            logger.error("StrToDouble：服务端异常" + e);
            e.printStackTrace();
        }
        return result;
    }

    /**
     * StrToBoolean:(字符转 boolean)
     *
     * @param amount
     * @return
     * @Author airufei
     */
    public static boolean strToBoolean(String str) {
        boolean result = false;
        try {
            if (isBlank(str)) {
                return result;
            }
            result = Boolean.parseBoolean(str);
        } catch (Exception e) {
            logger.error("StrToBoolean：服务端异常" + e);
            e.printStackTrace();

        }
        return result;
    }

    /**
     * 手机号脱敏.
     *
     * @param phone the phone
     * @return the string
     */
    public static String phoneTuoMin(String phone) {
        if (isBlank(phone)) {
            return phone;
        }
        if (phone.length() == 11) {
            String prifix = phone.substring(0, 3);
            String subfix = phone.substring(7);
            phone = prifix.concat("****").concat(subfix);
        }
        return phone;
    }


    /**
     * 去除数组后小数点后面的0
     *
     * @param strnum
     * @return
     * @author jxli
     */
    public static String formatNum(String strnum) {
        if (!StringUtil.isBlank(strnum) && strnum.indexOf(".") > 0 && "0".equals(strnum.substring(strnum.length() - 1))) {
            String num = Double.parseDouble(strnum) + "";
            if ("0".equals(num.substring(num.length() - 1))) {
                return num.substring(0, num.length() - 2);
            }
            return num;
        }
        return strnum;
    }


    /**
     * redirect:(重定向)
     *
     * @param response
     * @param url
     * @Author airufei
     */
    public static void redirect(HttpServletResponse response, String url) {
        try {
            logger.info("重定向到：url={}", url);
            response.sendRedirect(url);
            return;
        } catch (IOException e) {
            logger.error("redirect:(重定向----跳转) error===>" + e + "========》url：" + url);
            e.printStackTrace();
        }
    }

    /**
     * 创建一个随机数.
     *
     * @return 随机六位数+当前时间yyyyMMddHHmmss
     */
    public static String randomCodeUtil() {
        String cteateTime = DateFormatUtils.format(new Date(), "yyyyMMddHHmmss");
        int num = RandomUtils.nextInt(100000, 1000000);
        String result = cteateTime + StringUtils.leftPad(num + "", 6, "0");
        return result;
    }


    /**
     * getPageMap:(当前页转化为MYSQL所需要的分页参数)
     *
     * @param pageStr
     * @return
     * @Author airufei
     */

    public static Map getPageMap(int currentCount, int pageSize) {
        Map map = new HashMap();
        if (pageSize < 1) {
            pageSize = 10;
        }
        int startIndex = 0;
        if (currentCount <= 1) {
            currentCount = 1;
            startIndex = 0;
        } else {
            startIndex = (currentCount - 1) * pageSize;
        }
        map.put("startIndex", startIndex);
        map.put("pageSize", pageSize);
        map.put("currentCount", currentCount);
        map.put("sortWay", "desc");
        map.put("flag", 1);
        return map;
    }

    /**
     * getPageJSONObject:(当前页转化为MYSQL所需要的分页参数)
     *
     * @param pageStr
     * @return
     * @Author airufei
     */

    public static JSONObject getPageJSONObject(int currentCount, int pageSize) {
        JSONObject map = new JSONObject();
        if (pageSize < 1) {
            pageSize = 10;
        }
        int startIndex = 0;
        if (currentCount <= 1) {
            currentCount = 1;
            startIndex = 0;
        } else {
            startIndex = (currentCount - 1) * pageSize;
        }
        map.put("startIndex", startIndex);
        map.put("pageSize", pageSize);
        map.put("currentCount", currentCount);
        map.put("sortWay", "desc");
        map.put("flag", 1);
        return map;
    }

    /**
     * getPageMap:(当前页转化为MYSQL所需要的分页参数--默认每页10条)
     *
     * @param pageStr
     * @return
     * @Author airufei
     */

    public static Map getPageMap(int currentCount) {
        Map map = new HashMap();
        int pageSize = 10;
        int startIndex = 0;
        if (currentCount <= 1) {
            currentCount = 1;
            startIndex = 0;
        } else {
            startIndex = (currentCount - 1) * pageSize;
        }
        map.put("startIndex", startIndex);
        map.put("pageSize", pageSize);
        map.put("currentCount", currentCount);
        map.put("sortWay", "desc");
        map.put("flag", 0);
        return map;
    }

    /**
     * 输入流转化成字符串
     *
     * @param is
     * @return
     * @throws IOException
     */
    public static String inputStream2String(InputStream is) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int i = -1;
        while ((i = is.read()) != -1) {
            baos.write(i);
        }
        return baos.toString();
    }

    /**
     * getExceptionMsg:(根据异常获取关键异常信息字符串)
     *
     * @param throwable
     * @return
     * @Author airufei
     */
    public static String getExceptionMsg(Throwable throwable) {
        StringBuilder stackMessage = new StringBuilder();
        StackTraceElement[] stackTrace = throwable.getStackTrace();
        if (stackTrace == null || stackTrace.length < 1) {
            return null;
        }
        int len = stackTrace.length;
        for (int i = 0; i < len; i++) {
            if (i > 6) {
                break;
            }
            String moduleName = moduleName = stackTrace[i].getClassName();
            String methodName = stackTrace[i].getMethodName();
            Class<?> aClass = null;
            try {
                aClass = Class.forName(moduleName);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            int lineNumber = stackTrace[i].getLineNumber();
            String simpleName = null;
            if (aClass != null) {
                simpleName = MarkdownMessage.getLinkText("(" + aClass.getSimpleName() + ".java " + lineNumber + ")", null);
            }
            stackMessage.append(moduleName).append(" ").append(methodName).append(simpleName).append("\n\n");
            if (i == 0) {
                stackMessage.append(MarkdownMessage.getBoldText("错误信息:" + throwable.getMessage())).append("\n\n");
            }
        }
        return stackMessage.toString();
    }

    /*
     * getEsLogData(组织日志信息)
     * @param loggingEvent 日志信息
     * @param subSysName 子系统名称
     * @author airufei
     * @date 2018/2/27 11:21
     */
    public static String getEsLogData(LoggingEvent loggingEvent, String subSysName) {
        String message = loggingEvent.getMessage();
        Level level = loggingEvent.getLevel();
        String loggerName = loggingEvent.getLoggerName();
        StackTraceElement[] callerData = loggingEvent.getCallerData();
        StringBuilder stackMessage = null;//堆栈信息
        String methodName = "";
        if (callerData != null && callerData.length > 0) {
            methodName = callerData[0].getMethodName();
            if (level != null && level == Level.ERROR) {
                stackMessage = new StringBuilder();//堆栈信息
                for (int i = 0; i < callerData.length; i++) {
                    if (i > 10) {
                        break;
                    }
                    stackMessage.append(callerData[i] + "\n");
                }
            }

        }
        EsLogMessage esLog = new EsLogMessage();
        esLog.setSubSysName(subSysName);
        esLog.setModuleName(loggerName);
        esLog.setLevel(level.toString());
        esLog.setMethodName(methodName);
        esLog.setMessage(message);
        if (stackMessage != null && stackMessage.toString().length() > 0) {
            esLog.setStackMessage(stackMessage.toString());
        }
        String jsonString = JSON.toJSONString(esLog);
        return jsonString;
    }

}