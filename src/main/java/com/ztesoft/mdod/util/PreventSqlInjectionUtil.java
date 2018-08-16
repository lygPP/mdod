package com.ztesoft.mdod.util;

import java.util.Map;

public class PreventSqlInjectionUtil {
	
	/**
	 * sql注入检测(Map)
	 * @param params
	 * @return
	 */
	public static boolean IsSqlInjection(Map<String, Object> params) {
		 for (String s : params.keySet()) {
              //params.keySet()返回的是所有key的值
            String str = params.get(s).toString();//得到每个key多对用value的值
            if (sqlValidate(str)) { 
            	// 检测有特殊字符返回true
        		return true;
            }
         }	
		 return false; 
	}
	
	/**
	 * sql注入检测(String)
	 * @param params
	 * @return
	 */
	public static boolean IsSqlInjectionStr(String str) {
		// 检测有特殊字符返回true
		if (sqlValidate(str)) { 
    		return true;
        }
		 return false; 
	}
	
	//效验  
    protected static boolean sqlValidate(String str) {  
        str = str.toLowerCase();//统一转为小写  
        //过滤字段
        String badStr = "'|and|exec|execute|insert|select|delete|update|count|drop|*|%|chr|mid|master|truncate|" +  
                "char|sitename|net user|xp_cmdshell|;|or|like'|create|drop|" +  
                "table|from|grant|use|group_concat|column_name|" +  
                "information_schema.columns|table_schema|union|where|order|by|*|" +  
                "declare|--|+|like|//|%|#";//过滤掉的sql关键字，可以手动添加  
        String[] badStrs = badStr.split("\\|" );
        for (int i = 0; i < badStrs.length; i++) {  
            if (str.indexOf(badStrs[i]) >= 0) {  
                return true;  
            }  
        }  
        return false;  
    }
	
}
