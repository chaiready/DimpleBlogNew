package com.dimple.common.utils;


import com.dimple.common.exception.BusinessException;
import org.apache.commons.lang3.math.NumberUtils;

/**
 * ip范围判断工具类
 * 
 * @author caozhejun
 *
 */
public class IpRangeUtil {

	/**
	 * 判断是否在ip范围内
	 * 
	 * @param pattern
	 *            ip模式
	 * @param ip
	 *            具体的ip地址
	 * @return
	 */
	public static boolean isValid(String pattern, String ip) {
		if (pattern.equals(ip)) {
			return true;
		}
		String[] patternSeq = pattern.split("\\.");
		String[] ipSeq = ip.split("\\.");
		if (patternSeq.length != 4 || ipSeq.length != 4) {
			throw new BusinessException("ip格式错误[pattern:" + pattern + "][ip:" + ip + "]");
		}
		for (int i = 0; i < 4; i++) {
			if (!isRange(patternSeq[i], ipSeq[i])) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 是否数字在范围内
	 * 
	 * @param range
	 *            范围标识
	 * @param number
	 *            ip的数字端
	 * @return
	 */
	private static boolean isRange(String range, String number) {
		if ("*".equals(range)) {
			return true;
		}
		if (range.equals(number)) {
			return true;
		}
		String[] ranges = range.split("-");
		if (ranges.length == 2) {
			int start = NumberUtils.toInt(ranges[0]);
			int end = NumberUtils.toInt(ranges[1]);
			int num = NumberUtils.toInt(number);
			if (num <= end && num >= start) {
				return true;
			}
		}
		return false;
	}

}
