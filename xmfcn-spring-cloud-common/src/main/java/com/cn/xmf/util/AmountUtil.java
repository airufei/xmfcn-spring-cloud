package com.cn.xmf.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

public class AmountUtil {

    private static Logger logger = LoggerFactory.getLogger(AmountUtil.class);

    /*
     * getStrDecimal(金额保留N位小数-返回字符串类型)
     * @param tags
     * @author rufei.cn
     * @date 2018/3/7 16:07
     */
    public static String getStrDecimal(String amount, int num) {
        String retAmount = "0.00";
        if (StringUtil.isBlank(amount)) {
            return retAmount;
        }
        try {
            BigDecimal am = new BigDecimal(amount);
            retAmount = am.setScale(num, BigDecimal.ROUND_HALF_UP).toString();
        } catch (Exception e) {
            logger.error("getStrDecimal(金额保留N位小数-返回字符串类型)：" + e);

        }
        return retAmount;
    }

    /*
     * getDecimal(金额保留N位小数-返回数字类型)
     * @param tags
     * @author rufei.cn
     * @date 2018/3/7 16:07
     */
    public static BigDecimal getDecimal(String amount, int num) {
        BigDecimal retAmount = new BigDecimal(0.00);
        if (StringUtil.isBlank(amount)) {
            return retAmount.setScale(num,BigDecimal.ROUND_HALF_UP);
        }
        try {
            BigDecimal am = new BigDecimal(amount);
            retAmount = am.setScale(num, BigDecimal.ROUND_HALF_UP);
        } catch (Exception e) {
            logger.error("getDecimal(金额保留N位小数-返回数字类型)：" + e);

        }
        return retAmount;
    }

    /*
     * getString(金额保留N位小数-返回数字类型)
     * @param tags
     * @author rufei.cn
     * @date 2018/3/7 16:07
     */
    public static String getString(BigDecimal amount, int num) {
        BigDecimal retAmount = new BigDecimal(0.00);
        if (amount==null) {
            return retAmount.setScale(num,BigDecimal.ROUND_HALF_UP).toString();
        }
        try {
            retAmount = amount.setScale(num, BigDecimal.ROUND_HALF_UP);
        } catch (Exception e) {
            logger.error("getDecimal(金额保留N位小数-返回数字类型)：" + e);

        }
        return retAmount.toString();
    }
    /*
     * getDecimal(金额保留N位小数-返回数字类型)
     * @param tags
     * @author rufei.cn
     * @date 2018/3/7 16:07
     */
    public static BigDecimal getDecimal(BigDecimal amount, int num) {
        BigDecimal retAmount = new BigDecimal(0.00);
        if (amount == null) {
            return retAmount.setScale(num,BigDecimal.ROUND_HALF_UP);
        }
        try {
            retAmount = amount.setScale(num, BigDecimal.ROUND_HALF_UP);
        } catch (Exception e) {
            logger.error("getDecimal(金额保留N位小数-返回数字类型)：" + e);

        }
        return retAmount;
    }

    /*
     * getDecimal(字符串转金额类型)
     * @param tags
     * @author rufei.cn
     * @date 2018/3/7 16:07
     */
    public static BigDecimal getDecimal(String amount) {
        BigDecimal retAmount = new BigDecimal(0.00);
        if (StringUtil.isBlank(amount)) {
            return retAmount;
        }
        try {
            retAmount = new BigDecimal(amount);
        } catch (Exception e) {
            logger.error("getDecimal(金额保留N位小数-返回数字类型)：" + e);

        }
        return retAmount;
    }
}
