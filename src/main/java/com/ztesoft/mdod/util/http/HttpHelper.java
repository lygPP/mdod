package com.ztesoft.mdod.util.http;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import com.ztesoft.mdod.util.JsonUtil;
import com.ztesoft.mdod.util.StringUtil;

/**
 * @author lei.senlin 网络工具类，用于网络请求、响应、异常处理等
 */
public class HttpHelper {

	private HttpHelper() {

	}

	/**
	 * 设置header的token
	 * 
	 * @return
	 */
	public static String getToken() {
		return "test_token";
	}

	/**
	 * 执行HTTP GET请求。
	 * 
	 * @param url
	 *            请求地址
	 * @param params
	 *            请求参数
	 * @param headers
	 *            请求头
	 * @param cookies
	 *            带cookie
	 * @param charset
	 *            字符集，如UTF-8, GBK, GB2312
	 * @return 响应字符串
	 * @throws IOException
	 */
	public static String doGet(String url, Map<String, String> params,
			Map<String, String> headers, String cookies, String charset)
			throws IOException {
		HttpURLConnection conn = null;
		String rsp = null;

		try {
			String ctype = "application/x-www-form-urlencoded;charset="
					+ charset;
			String query = buildQuery(params, charset);
			try {
				conn = getConnection(buildGetUrl(url, query), ctype, null);
				// 判断并设置Cookie
				if (!StringUtil.isEmpty(cookies)) {
					conn.setRequestProperty("Cookie", cookies);
				}
				// 判断并设置Header
				if (headers != null && headers.size() > 0) {
					boolean isUserAgent = false;
					for (Entry<String, String> header : headers.entrySet()) {
						conn.addRequestProperty(header.getKey(),
								header.getValue());
						if ("user-agent".equals(header.getKey().toLowerCase())) {
							isUserAgent = true;
						}
					}
					if (!isUserAgent) {
						conn.addRequestProperty(
								"User-Agent",
								"Mozilla/5.0 (Windows NT 5.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2311.152 Safari/537.36");
					}
				} else {
					conn.addRequestProperty(
							"User-Agent",
							"Mozilla/5.0 (Windows NT 5.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2311.152 Safari/537.36");
				}

				conn.setRequestMethod(HttpConstants.METHOD_GET);
			} catch (IOException e) {
				// TODO 日志 Auto-generated method stub
				throw new IOException("设置HTTP请求连接异常！", e);
			}

			try {
				rsp = getResponseAsString(conn, false);
			} catch (IOException e) {
				// TODO 日志 Auto-generated method stub
				throw new IOException("设置HTTP请求连接异常！", e);
			}

		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}

		return rsp;
	}

	/**
	 * 执行HTTP Post请求。
	 * 
	 * @param url
	 *            请求地址
	 * @param params
	 *            请求参数
	 * @param headers
	 *            请求头
	 * @param cookies
	 *            带cookie
	 * @param charset
	 *            字符集，如UTF-8, GBK, GB2312
	 * @return 响应字符串
	 * @throws IOException
	 */
	public static String doPost(String url, Map<String, String> params, 
			Map<String, String> header, String charset)throws IOException {
		return doPost(url, JsonUtil.mapToJsonStr(params).getBytes(), null, header, null, 5000, 5000, false, charset);
	}
	
	/**
	 * 执行HTTP POST请求。直接传入byte数组请求方式, 不依赖于实体对象
	 * 
	 * @param url
	 *            请求路径
	 * @param params
	 *            请求参数
	 * @param headers
	 *            请求头
	 * @return
	 * @throws IOException
	 */
	public static String doPost(String url, Map<String, String> params,
			Map<String, String> headers) throws IOException {
		return doPost(url, new byte[] {}, params, headers, null, 5000, 5000,
				false, HttpConstants.CHARSET_UTF8);
	}

	/**
	 * 执行HTTP POST请求。直接传入byte数组请求方式, 不依赖于实体对象
	 *
	 * @param url
	 *            请求路径
	 * @param params
	 *            请求参数
	 * @param headers
	 *            请求头
	 * @return
	 * @throws IOException
	 */
	public static String doPost(String url, byte[] content) throws IOException {
		return doPost(url, content, null, null, null, 5000, 5000,
				false, HttpConstants.CHARSET_UTF8);
	}

	/**
	 * 执行HTTP POST请求。直接传入byte数组请求方式, 不依赖于实体对象
	 * 
	 * @param url
	 *            请求路径
	 * @param content
	 *            请求参数的自己数组(xml参数类型)
	 * @param params
	 *            请求参数
	 * @param headers
	 *            请求头
	 * @param cookies
	 *            待cookie
	 * @param connectTimeout
	 *            连接超时时间
	 * @param readTimeout
	 *            响应超时时间
	 * @param isZip
	 *            是否压缩参数(xml类型)
	 * @param charset
	 *            字符
	 * @return
	 * @throws IOException
	 */
	public static String doPost(String url, byte[] content,
			Map<String, String> params, Map<String, String> headers,
			String cookies, int connectTimeout, int readTimeout, boolean isZip,
			String charset) throws IOException {
		String retMsg = null;

		HttpURLConnection conn = null;
		OutputStream out = null;
		try {
			try {
				Validate.notEmpty(url, "the URL is (" + url
						+ "), please supply a valid URL...");
				String query = buildQuery(params, charset);
				conn = getConnection(buildGetUrl(url, query),
						HttpConstants.CONTENT_TYPE_APP_JSON, null);
				// 判断并设置Cookie
				if (!StringUtil.isEmpty(cookies)) {
					conn.setRequestProperty("Cookie", cookies);
				}
				// 判断并设置Header
				if (headers != null && headers.size() > 0) {
					boolean isUserAgent = false;
					for (Entry<String, String> header : headers.entrySet()) {
						conn.addRequestProperty(header.getKey(),
								header.getValue());
						if ("user-agent".equals(header.getKey().toLowerCase())) {
							isUserAgent = true;
						}
					}
					if (!isUserAgent) {
						conn.addRequestProperty(
								"User-Agent",
								"Mozilla/5.0 (Windows NT 5.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2311.152 Safari/537.36");
					}
				} else {
					conn.addRequestProperty(
							"User-Agent",
							"Mozilla/5.0 (Windows NT 5.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2311.152 Safari/537.36");
				}

				if (isZip) {
					conn.addRequestProperty("Accept-Encoding", "gzip");
				}

				// 设置请求类型
				conn.setRequestMethod(HttpConstants.METHOD_POST);
				conn.setConnectTimeout(connectTimeout);
				conn.setReadTimeout(readTimeout);
			} catch (IOException e) {
				// TODO 日志 Auto-generated method stub
				throw new IOException("设置HTTP请求连接异常！", e);
			}
			try {
				out = conn.getOutputStream();
				if (isZip) {
					try {
						out.write(gzip(content, "utf-8"));
					} catch (Exception ex) {
						throw new IOException("压缩报文异常！", ex);
					}
				} else {
					out.write(content);
					out.flush();
				}
				retMsg = getResponseAsString(conn, isZip);
			} catch (IOException e) {
				// TODO 日志 Auto-generated method stub
				throw new IOException("HTTP数据请求异常！", e);
			}

		} finally {
			if (out != null) {
				out.close();
			}
			if (conn != null) {
				conn.disconnect();
			}
		}
		return retMsg;
	}

	/**
	 * 获取http连接
	 * 
	 * @param url
	 * @param ctype
	 * @param proxy
	 * @return
	 * @throws IOException
	 */
	private static HttpURLConnection getConnection(URL url, String ctype,
			Proxy proxy) throws IOException {
		HttpURLConnection conn = null;
		if ("https".equals(url.getProtocol())) {
			SSLContext ctx = null;
			try {
				ctx = SSLContext.getInstance("TLS");
				ctx.init(new KeyManager[0],
						new TrustManager[] { new DefaultTrustManager() },
						new SecureRandom());
			} catch (Exception e) {
				throw new IOException(e);
			}
			HttpsURLConnection connHttps;
			if (proxy != null) {
				connHttps = (HttpsURLConnection) url.openConnection(proxy);
			} else {
				connHttps = (HttpsURLConnection) url.openConnection();
			}

			if (connHttps != null) {
				connHttps.setSSLSocketFactory(ctx.getSocketFactory());
				connHttps.setHostnameVerifier(new HostnameVerifier() {

					public boolean verify(String hostname, SSLSession session) {
						return false;// 默认认证不通过，进行证书校验。
					}
					
				});
				conn = connHttps;
			}
		} else {
			if (proxy != null) {
				conn = (HttpURLConnection) url.openConnection(proxy);
			} else {
				conn = (HttpURLConnection) url.openConnection();
			}
		}

		conn.setDoInput(true);
		conn.setDoOutput(true);
		conn.setRequestProperty("Accept", HttpConstants.CONTENT_TYPE_APP_JSON);
		// conn.setRequestProperty("Accept", "application/xml");//设置默认接收返回的数据格式
		// conn.setRequestProperty("User-Agent", "aop-sdk-java");
		conn.setRequestProperty("Content-Type", ctype);
		return conn;
	}

	/**
	 * 解析参数成string
	 * 
	 * @param params
	 * @param charset
	 * @return
	 * @throws IOException
	 */
	private static String buildQuery(Map<String, String> params, String charset)
			throws IOException {
		if (params == null || params.isEmpty()) {
			return null;
		}
		StringBuilder query = new StringBuilder();
		Set<Entry<String, String>> entries = params.entrySet();
		boolean hasParam = false;

		for (Entry<String, String> entry : entries) {
			String name = entry.getKey();
			String value = entry.getValue();
			// 忽略参数名或参数值为空的参数
			if (!StringUtil.isEmpty(name)) {
				if (hasParam) {
					query.append("&");
				} else {
					hasParam = true;
				}

				query.append(name).append("=")
						.append(URLEncoder.encode(value, charset));
			}
		}

		return query.toString();
	}

	/**
	 * 拼接url和参数
	 * 
	 * @param strUrl
	 * @param query
	 * @return
	 * @throws IOException
	 */
	private static URL buildGetUrl(String strUrl, String query)
			throws IOException {
		URL url = new URL(strUrl);
		if (StringUtil.isEmpty(query)) {
			return url;
		}

		if (StringUtil.isEmpty(url.getQuery())) {
			if (strUrl.endsWith("?")) {
				strUrl = strUrl + query;
			} else {
				strUrl = strUrl + "?" + query;
			}
		} else {
			if (strUrl.endsWith("&")) {
				strUrl = strUrl + query;
			} else {
				strUrl = strUrl + "&" + query;
			}
		}

		return new URL(strUrl);
	}

	/**
	 * 获取string返回报文
	 * 
	 * @param conn
	 * @param isZip
	 * @return
	 * @throws IOException
	 */
	private static String getResponseAsString(HttpURLConnection conn,
			boolean isZip) throws IOException {
		String charset = getResponseCharset(conn.getContentType());
		InputStream es = conn.getErrorStream();
		if (es == null) {
			return getStreamAsString(conn.getInputStream(), charset, isZip);
		} else {
			String msg = getStreamAsString(es, charset, isZip);
			if (StringUtil.isEmpty(msg)) {
				throw new IOException(conn.getResponseCode() + ":"
						+ conn.getResponseMessage());
			} else {
				throw new IOException(msg);
			}
		}
	}

	/**
	 * 通过流解析报文
	 * 
	 * @param stream
	 * @param charset
	 * @param isZip
	 * @return
	 * @throws IOException
	 */
	private static String getStreamAsString(InputStream stream, String charset,
			boolean isZip) throws IOException {
		try {
			if (isZip) {
				try {
					return ungzip(stream, charset);
				} catch (Exception e) {
					throw new IOException("解压缩异常！", e);
				}
			} else {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(stream, charset));
				StringWriter writer = new StringWriter();

				char[] chars = new char[256];
				int count = 0;
				while ((count = reader.read(chars)) > 0) {
					writer.write(chars, 0, count);
				}

				return writer.toString();
			}
		} finally {
			if (stream != null) {
				stream.close();
			}
		}
	}

	/**
	 * 拼接ctype
	 * 
	 * @param ctype
	 * @return
	 */
	private static String getResponseCharset(String ctype) {
		String charset = HttpConstants.CHARSET_UTF8;

		if (!StringUtil.isEmpty(ctype)) {
			String[] params = ctype.split(";");
			for (String param : params) {
				param = param.trim();
				if (param.startsWith("charset")) {
					String[] pair = param.split("=", 2);
					if (pair.length == 2) {
						if (!StringUtil.isEmpty(pair[1])) {
							charset = pair[1].trim();
						}
					}
					break;
				}
			}
		}

		return charset;
	}

	private static class DefaultTrustManager implements X509TrustManager {
		public X509Certificate[] getAcceptedIssuers() {
			return null;
		}

		public void checkClientTrusted(X509Certificate[] chain, String authType)
				throws CertificateException {
		}

		public void checkServerTrusted(X509Certificate[] chain, String authType)
				throws CertificateException {
		}
	}

	private static byte[] gzip(byte[] input, String charset) throws Exception {
		GZIPOutputStream gzipOS = null;
		try {
			ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
			gzipOS = new GZIPOutputStream(byteArrayOS);
			gzipOS.write(input);
			gzipOS.flush();
			gzipOS.close();
			gzipOS = null;
			return byteArrayOS.toByteArray();
		} catch (Exception e) {
			throw e;
		} finally {
			if (gzipOS != null) {
				try {
					gzipOS.close();
				} catch (Exception ignored) {
				}
			}
		}
	}

	private static String ungzip(InputStream is, String charset)
			throws Exception {
		if (is == null) {
			throw new Exception("待解析返回报文流为空！");
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		try {
			GZIPInputStream gunzip = new GZIPInputStream(is);
			byte[] buffer = new byte[256];
			int n;
			while ((n = gunzip.read(buffer)) >= 0) {
				out.write(buffer, 0, n);
			}
			if(out != null){
				out.close();
			}
			return out.toString(charset);
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			if(out != null){
				out.close();
			}
		}
		return null;
	}

	/**
	 * @author lei.senlin 用于对网络请求中参数的验证并跑出异常
	 */
	static class Validate {

		private Validate() {
		}

		/**
		 * 验证是否为true
		 * 
		 * @param val
		 */
		public static void isTrue(boolean val) {
			if (!val) {
				throw new IllegalArgumentException("Must be true");
			}
		}

		/**
		 * 验证是否为true
		 * 
		 * @param val
		 * @param msg
		 */
		public static void isTrue(boolean val, String msg) {
			if (!val) {
				throw new IllegalArgumentException(msg);
			}
		}

		/**
		 * 验证是否为false
		 * 
		 * @param val
		 */
		public static void isFalse(boolean val) {
			if (val) {
				throw new IllegalArgumentException("Must be false");
			}
		}

		/**
		 * 验证是否为false
		 * 
		 * @param val
		 * @param msg
		 */
		public static void isFalse(boolean val, String msg) {
			if (val) {
				throw new IllegalArgumentException(msg);
			}
		}

		/**
		 * 验证消息是否为空
		 * 
		 * @param string
		 */
		public static void notNull(Object bean) {
			if (bean == null) {
				throw new IllegalArgumentException("Object must not be null");
			}
		}

		/**
		 * 验证字符串是否为空
		 * 
		 * @param string
		 * @param msg
		 */
		public static void notNull(Object bean, String msg) {
			if (bean == null) {
				throw new IllegalArgumentException(msg);
			}
		}

		/**
		 * 验证消息是否为空
		 * 
		 * @param string
		 */
		public static void notEmpty(String string) {
			if (string == null || string.length() == 0) {
				throw new IllegalArgumentException("String must not be empty");
			}
		}

		/**
		 * 验证字符串是否为空
		 * 
		 * @param string
		 * @param msg
		 */
		public static void notEmpty(String string, String msg) {
			if (string == null || string.length() == 0) {
				throw new IllegalArgumentException(msg);
			}
		}

		/**
		 * 失败
		 * 
		 * @param string
		 */
		public static void isFail() {
			throw new IllegalArgumentException("Unknown exception");
		}

		/**
		 * 失败
		 * 
		 * @param string
		 * @param msg
		 */
		public static void isFail(String msg) {
			throw new IllegalArgumentException(msg);
		}

	}

}
