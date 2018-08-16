package com.ztesoft.mdod.util;

import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;

/**
 * 加载params.properties配置文件的数据到缓存
 * 
 * @author kingkong
 *
 */
public class PropertiesUtil {

	private String fileName = "";
	private Properties params = new Properties();

	public PropertiesUtil(String filePath) {
		this.fileName = filePath;
		initParams();
	}

	/**
	 * 根据路径解析properties配置文件exp:
	 * @param filePath config/cache_config.properties
	 */
	@SuppressWarnings("rawtypes")
	public void initParams() {
		try {
			params = this.getConfigFileProperties(fileName);
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		Enumeration enu = params.keys();
		String key = "";
		String val = "";
		while (enu.hasMoreElements()) {
			key = (String) enu.nextElement();
			val = params.getProperty(key);
			if (val != null && !"".equals(val)) {
				params.put(key, val.trim());
			}
		}
	}

	public String getParamValue(String name) {
		return params.getProperty(name);
	}

	@SuppressWarnings("rawtypes")
	public void updateProperty(String paramCode) throws Exception {
		Properties tempProperty = this.getConfigFileProperties(fileName);
		Enumeration enu = tempProperty.keys();
		String key = "";
		String val = "";
		while (enu.hasMoreElements()) {
			key = (String) enu.nextElement();
			val = tempProperty.getProperty(key);
			if (val != null && !"".equals(val)) {
				tempProperty.put(key, val.trim());
			}
		}
		String paramValue = tempProperty.getProperty(paramCode);
		if (paramValue != null) {
			params.setProperty(paramCode, paramValue);
		}
	}

	private InputStream getFileInputStream(String fileName) throws Exception {
		return this.getClass().getClassLoader().getResourceAsStream(fileName);
	}

	public Properties getConfigFileProperties(String fileName) throws Exception {
		InputStream is = getFileInputStream(fileName);
		Properties configFile = new Properties();
		configFile.load(is);
		is.close();
		return configFile;
	}
}
