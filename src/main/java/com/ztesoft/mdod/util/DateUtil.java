package com.ztesoft.mdod.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 日期工具类
 * 
 * @author zhao.jingang <br/>
 *         2018年1月24日 下午1:08:14
 */
public class DateUtil {

	/** 6位日期格式 */
	public static final String DATE_FORMAT_6 = "yyyyMM";
	/** 8位日期格式 */
	public static final String DATE_FORMAT_8 = "yyyyMMdd";
	/** 10位日期时间格式 */
	public static final String DATE_FORMAT_10 = "yyyy-MM-dd";
	/** 14位日期时间格式 */
	public static final String DATE_TIME_FORMAT_14 = "yyyyMMddHHmmss";
	/** 19位日期时间格式 */
	public static final String DATE_TIME_FORMAT_19 = "yyyy-MM-dd HH:mm:ss";
	/** 22位日期时间格式 */
	public static final String DATE_TIME_FORMAT_22 = "yyyy-MM-dd HH:mm:ss.SSS";

	/**
	 * 得到应用服务器当前日期，按照指定的格式返回。
	 * 
	 * @param pattern
	 *            格式类型，通过系统常量中定义，如：CapConstants.DATE_FORMAT_8
	 * @return
	 */
	public static String formatNowDate(String pattern) {
		Date date = new Date();
		SimpleDateFormat dateFormator = new SimpleDateFormat(pattern);
		String str = dateFormator.format(date);
		return str;
	}

	/**
	 * 转换输入日期 按照指定的格式返回。
	 * 
	 * @param date
	 * @param pattern
	 *            格式类型，通过系统常量中定义，如：CapConstants.DATE_FORMAT_8
	 * @return
	 */
	public static String formatInputDate(Date date, String pattern) {
		if (date == null) {
			return "";
		}
		SimpleDateFormat dateFormator = new SimpleDateFormat(pattern);
		String str = dateFormator.format(date);
		return str;
	}
	
	/**
	 * 验证时间正则
	 * 
	 * @param timeStr
	 * @return
	 */
	public static boolean valiDateTimeFormat19(String timeStr) {
		String format = "((19|20)[0-9]{2})-(0?[1-9]|1[012])-(0?[1-9]|[12][0-9]|3[01]) ([01]?[0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]";
		Pattern pattern = Pattern.compile(format);
		Matcher matcher = pattern.matcher(timeStr);
		if (matcher.matches()) {
			pattern = Pattern.compile("(\\d{4})-(\\d+)-(\\d+).*");
			matcher = pattern.matcher(timeStr);
			if (matcher.matches()) {
				int y = Integer.valueOf(matcher.group(1));
				int m = Integer.valueOf(matcher.group(2));
				int d = Integer.valueOf(matcher.group(3));
				if (d > 28) {
					Calendar c = Calendar.getInstance();
					c.set(y, m-1, 1);
					int lastDay = c.getActualMaximum(Calendar.DAY_OF_MONTH);
					return (lastDay >= d);
				}
			}
			return true;
		}
		return false;
	}
	
	/**
     * 获取前一天时间
     * @return 返回短时间字符串格式yyyyMMdd
     */
    public static String getLastDayString(Date date) {
        SimpleDateFormat formatter = null;
        formatter = new SimpleDateFormat("yyyyMMdd");
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DATE, cal.get(Calendar.DATE) - 1);
        String dateString = formatter.format(cal.getTime());
        return dateString;
    }
    
    /**
	 * 获取当前时间到明天0点的秒数
	 */
	public static int getToNextDaySeconds() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		// 当前时间毫秒数
		Long startLong = calendar.getTime().getTime();
		calendar.add(Calendar.DATE, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		// 明天凌晨0点0时0秒毫秒数
		Long endLong = calendar.getTime().getTime();
		Long res = (endLong - startLong) / 1000;
		return res.intValue();
	}

	/**
	 * 获取当前时间到下个月1号0点的秒数
	 */
	public static int getToNextMonthSeconds() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		// 当前时间毫秒数
		Long startLong = calendar.getTime().getTime();
		calendar.add(Calendar.MONTH, 1);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		// 下月一号凌晨0点0时0秒毫秒数
		Long endLong = calendar.getTime().getTime();
		Long res = (endLong - startLong) / 1000;
		return res.intValue();
	}
	
}
