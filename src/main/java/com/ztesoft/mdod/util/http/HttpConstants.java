package com.ztesoft.mdod.util.http;

/**
 * @author lei.senlin 用于定义HTTP请求中涉及到的常量
 */
public class HttpConstants {

	/**
	 * HTTP请求方式：POST
	 */
	public static final String METHOD_POST = "POST";

	/**
	 * HTTP请求方式：GET
	 */
	public static final String METHOD_GET = "GET";

	/**
	 * 请求字符编码格式
	 */
	public static final String CHARSET_UTF8 = "UTF-8";

	/**
	 * 请求字符编码格式
	 */
	public static final String CHARSET_GBK = "GBK";

	/**
	 * Content-type application/json;
	 */
	public static final String CONTENT_TYPE_APP_JSON = "application/json;";

	/**
	 * Content-type application/xml;
	 */
	public static final String CONTENT_TYPE_APP_XML = "application/xml;";

	/**
	 * Content-type text/json;
	 */
	public static final String CONTENT_TYPE_TEXT_JSON = "text/json;";

	/**
	 * Content-type text/xml;
	 */
	public static final String CONTENT_TYPE_TEXT_XML = "text/xml;";

	/**
	 * REDIS缓存接口Service对应的URL信息KEY前缀
	 */
	public static final String REDIS_CACHE_KEY_URL_PRE = "INTER:UIF:URL:";

	/**
	 * 正常返回编码
	 */
	public static final String RETURN_0 = "0";

	/**
	 * 元数据-协议查询服务静态编码
	 */
	public static final String META_PROTOCOL = "META_PROTOCOL";

	/**
	 * 元数据-库查询服务静态编码
	 */
	public static final String META_SCHEMA = "META_SCHEMA";

	/**
	 * 元数据-表查询服务静态编码
	 */
	public static final String META_TABLE = "META_TABLE";

	/**
	 * 元数据-ETL任务目录查询服务静态编码
	 */
	public static final String META_ETL_FLOW = "META_ETL_FLOW";

	/**
	 * 元数据-ETL任务查询服务静态编码
	 */
	public static final String META_ETL_TASK = "META_ETL_TASK";

	/**
	 * 元数据-非ETL任务目录查询服务静态编码
	 */
	public static final String META_NOT_ETL_FLOW = "META_NOT_ETL_FLOW";

	/**
	 * 元数据-非ETL任务查询服务静态编码
	 */
	public static final String META_NOT_ELT_TASK = "META_NOT_ELT_TASK";

	/**
	 * 元数据-ETL工单查询服务静态编码
	 */
	public static final String META_ETL_ORDER = "META_ETL_ORDER";

}
