package com.ztesoft.mdod.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lam on 2017/9/25.
 */
public class StringUtil {

	/**
	 * 拆分
	 * @param msg
	 * @param separate
	 * @return
	 */
    public static String[] split(String msg, String separate){
        ArrayList<String> result = new ArrayList<String>();
        String tmp = msg;
        for(;;) {
            int index = tmp.indexOf(separate);
            if(index >= 0) {
                result.add(tmp.substring(0, index));
                tmp = tmp.substring(index, tmp.length());
                if(tmp.equals(separate)){
                    result.add("");
                }
                tmp = tmp.substring(separate.length(), tmp.length());
            }
            else {
                result.add(tmp);
                tmp = "";
            }
            if(tmp.length() <= 0){
                break;
            }
        }
        return result.toArray(new String[result.size()]);
    }

    /**
     * 下划线分割字符串转为驼峰式
     */
    public static String underline2Camel(String line,boolean smallCamel){
        if(line==null||"".equals(line)){
            return "";
        }
        StringBuffer sb=new StringBuffer();
        Pattern pattern= Pattern.compile("([A-Za-z\\d]+)(_)?");
        java.util.regex.Matcher matcher=pattern.matcher(line);
        while(matcher.find()){
            String word=matcher.group();
            sb.append(smallCamel&&matcher.start()==0?Character.toLowerCase(word.charAt(0)):Character.toUpperCase(word.charAt(0)));
            int index=word.lastIndexOf('_');
            if(index>0){
                sb.append(word.substring(1, index).toLowerCase());
            }else{
                sb.append(word.substring(1).toLowerCase());
            }
        }
        return sb.toString();
    }

	/**
	 * 检查字符串是否为null或空字符串""。
	 * 
	 * @param str
	 *            要检查的字符串
	 * @return
	 */
	public static boolean isEmpty(String str) {
		return ((str == null) || (str.length() == 0) || str.equals("null"));
	}
	
	/**
	 * 检查Long是否为null或空。
	 * 
	 * @param str
	 *            要检查的字符串
	 * @return
	 */
	public static boolean isEmpty(Long id) {
		return isEmpty(id+"");
	}

	/**
	 * 检查字符串是否不是空
	 * 
	 * @param str
	 *            要检查的字符串
	 * @return
	 */
	public static boolean isNotEmpty(String str) {
		return ((str != null) && (str.length() > 0));
	}

	/**
	 * 检查多个字符串是否全部不为空
	 * 
	 * @param params
	 * @return
	 */
	public static boolean allNotEmpty(String... params) {
		for (String temp : params) {
			if (isEmpty(temp)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 检查数组里的字符串是否全部不为空
	 * 
	 * @param params
	 * @return
	 */
	public static boolean arrNotEmpty(String[] params) {
		for (String temp : params) {
			if (isEmpty(temp)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 检查集合里的字符串是否全部不为空
	 * 
	 * @param params
	 * @return
	 */
	public static boolean listNotEmpty(List<String> params) {
		for (String temp : params) {
			if (isEmpty(temp)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 检查字符串是否数字
	 * 
	 * @param num
	 * @return
	 */
	public static boolean isNum(String num) {
		if (num == null || num.equals(""))
			return false;
		Pattern pattern = Pattern.compile("^-{0,1}\\d+$");
		Matcher isNum = pattern.matcher(num);
		return isNum.matches();
	}
	
	/**
	 * 根据key获取map值
	 * @param map
	 * @return
	 */
	public static String getStrForMap(Map<String, Object> map, String key){
		if(map==null || map.size() <=0){
			return "";
		}
		Object value = map.get(key);
		if(value != null) {
			return value.toString();
		}
		else {
			return "";
		}
	}
	
	/**
	 * equals判断
	 */
	public static boolean equals(String str1, String str2){
		if(str1==null || str2==null ){
			return false;
		}
		if(str1.trim().equals(str2.trim())){
			return true;
		}
		return false;
	}
	
}
