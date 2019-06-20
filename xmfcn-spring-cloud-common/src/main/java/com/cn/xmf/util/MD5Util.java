/**
 * Project Name:CooxinPro
 * File Name:MD5Util.java
 * Package Name:com.cn.cooxin.util
 * Date:2017年2月13日下午10:45:54
 * Copyright (c) 2017, hukailee@163.com All Rights Reserved.
 *
*/

package com.cn.xmf.util;

import java.math.BigInteger;
import java.security.MessageDigest;

/**
 * ClassName:MD5Util (MD5加密)
 * Date:     2017年2月13日 下午10:45:54
 * @Author   airufei
 * @Version  1.0
 * @see 	 
 */
public class MD5Util {

	/**
	 * 对字符串md5加密
	 *
	 * @param str
	 * @return
	 * @throws Exception 
	 */
	public static String getMD5(String str) throws Exception {
	    try {
	        // 生成一个MD5加密计算摘要
	        MessageDigest md = MessageDigest.getInstance("MD5");
	        // 计算md5函数
	        md.update(str.getBytes());
	        // digest()最后确定返回md5 hash值，返回值为8为字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
	        // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
	        return new BigInteger(1, md.digest()).toString(16);
	    } catch (Exception e) {
	        throw new Exception("MD5加密出现错误");
	    }
	}
}

