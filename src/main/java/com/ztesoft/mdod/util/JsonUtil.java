package com.ztesoft.mdod.util;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * 
 * @author liang.weitong
 * 
 * JSON操作工具类，引用在MapUtil里和ListUtil里
 *
 */
public class JsonUtil {
	
	public static final String EMPTY_JSON = "{}";
	public static final String EMPTY_JSON_ARRAY = "[]";
	public static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss SSS";
	public static final double SINCE_VERSION_10 = 1.0d;
	public static final double SINCE_VERSION_11 = 1.1d;
	public static final double SINCE_VERSION_12 = 1.2d;
	public static final double UNTIL_VERSION_10 = SINCE_VERSION_10;
	public static final double UNTIL_VERSION_11 = SINCE_VERSION_11;
	public static final double UNTIL_VERSION_12 = SINCE_VERSION_12;

	public JsonUtil() {
		super();
	}

	public static String toJson(Object target, Type targetType, boolean isSerializeNulls, Double version, String datePattern, boolean excludesFieldsWithoutExpose) {
		if (target == null)
			return EMPTY_JSON;
		GsonBuilder builder = new GsonBuilder();
		if (isSerializeNulls)
			builder.serializeNulls();
		if (version != null)
			builder.setVersion(version.doubleValue());
		if (isBlank(datePattern))
			datePattern = DEFAULT_DATE_PATTERN;
		builder.setDateFormat(datePattern);
		if (!excludesFieldsWithoutExpose) {
			builder.excludeFieldsWithoutExposeAnnotation();
		}
		return toJson(target, targetType, builder);
	}

	public static String toJson(Object target) {
		return toJson(target, null, false, null, null, true);
	}

	public static String toJson(Object target, String datePattern) {
		return toJson(target, null, false, null, datePattern, true);
	}

	public static String toJson(Object target, Double version) {
		return toJson(target, null, false, version, null, true);
	}

	public static String toJson(Object target, boolean excludesFieldsWithoutExpose) {
		return toJson(target, null, false, null, null, excludesFieldsWithoutExpose);
	}

	public static String toJson(Object target, Double version, boolean excludesFieldsWithoutExpose) {
		return toJson(target, null, false, version, null, excludesFieldsWithoutExpose);
	}

	public static String toJson(Object target, Type targetType) {
		return toJson(target, targetType, false, null, null, true);
	}

	public static String toJson(Object target, Type targetType, Double version) {
		return toJson(target, targetType, false, version, null, true);
	}

	public static String toJson(Object target, Type targetType, boolean excludesFieldsWithoutExpose) {
		return toJson(target, targetType, false, null, null, excludesFieldsWithoutExpose);
	}

	public static String toJson(Object target, Type targetType, Double version, boolean excludesFieldsWithoutExpose) {
		return toJson(target, targetType, false, version, null, excludesFieldsWithoutExpose);
	}

	@SuppressWarnings("unchecked")
	public static <T> T fromJson(String json, TypeToken<T> token, String datePattern) {
		if (isBlank(json)) {
			return null;
		}
		GsonBuilder builder = new GsonBuilder();
		if (isBlank(datePattern)) {
			datePattern = DEFAULT_DATE_PATTERN;
		}
		Gson gson = builder.create();
		try {
			return (T) gson.fromJson(json, token.getType());
		} catch (Exception ex) {
			System.out.println(json + " 无法转换为 " + token.getRawType().getName() + " 对象!");
			return null;
		}
	}

	public static <T> T fromJson(String json, TypeToken<T> token) {
		return (T) fromJson(json, token, null);
	}

	public static <T> T fromJson(String json, Class<T> clazz, String datePattern) {
		if (isBlank(json)) {
			return null;
		}
		GsonBuilder builder = new GsonBuilder();
		if (isBlank(datePattern)) {
			datePattern = DEFAULT_DATE_PATTERN;
		}
		Gson gson = builder.create();
		try {
			return (T) gson.fromJson(json, clazz);
		} catch (Exception ex) {
			System.out.println(json + " 无法转换为 " + clazz.getName() + " 对象!");
			return null;
		}
	}

	public static <T> T fromJson(String json, Class<T> clazz) {
		return (T) fromJson(json, clazz, null);
	}

	public static Object parserJsonToMap(String json) {
		JsonParser jsonParser = new JsonParser();
		JsonElement jsonElement = jsonParser.parse(json);
		if (jsonElement.isJsonArray()) {
			return parserArrays(jsonElement.getAsJsonArray());
		} else if (jsonElement.isJsonNull()) {
			return null;
		} else if (jsonElement.isJsonObject()) {
			return parserJsonObjecet(jsonElement.getAsJsonObject());
		} else if (jsonElement.isJsonPrimitive()) {
			return jsonElement.getAsString();
		}
		return null;
	}

	private static Map<String, Object> parserJsonObjecet(JsonObject jsonObject) {
		Map<String, Object> map = new HashMap<String, Object>();
		Set<Map.Entry<String, JsonElement>> set = jsonObject.entrySet();
		for (Map.Entry<String, JsonElement> entry : set) {
			if (entry.getValue().isJsonPrimitive()) {
				map.put(entry.getKey(), entry.getValue().getAsString());
			} else if (entry.getValue().isJsonNull()) {
				map.put(entry.getKey(), "null");
			} else if (entry.getValue().isJsonArray()) {
				map.put(entry.getKey(), parserArrays(entry.getValue().getAsJsonArray()));
			} else if (entry.getValue().isJsonObject()) {
				map.put(entry.getKey(), parserJsonObjecet(entry.getValue().getAsJsonObject()));
			}
		}
		return map;
	}

	private static List<Object> parserArrays(JsonArray jsonArray) {
		List<Object> list = new ArrayList<Object>();
		for (int i = 0; i < jsonArray.size(); i++) {
			JsonElement jsonElement = jsonArray.get(i);
			if (jsonElement.isJsonNull()) {
				list.add("null");
			} else if (jsonElement.isJsonArray()) {
				list.add(parserArrays(jsonElement.getAsJsonArray()));
			} else if (jsonElement.isJsonObject()) {
				list.add(parserJsonObjecet(jsonElement.getAsJsonObject()));
			} else if (jsonElement.isJsonPrimitive()) {
				list.add(jsonElement.getAsString());
			}
		}
		return list;
	}

	public static String toJson(Object target, Type targetType, GsonBuilder builder) {
		if (target == null)
			return EMPTY_JSON;
		Gson gson = null;
		if (builder == null) {
			gson = new Gson();
		} else {
			gson = builder.create();
		}
		String result = EMPTY_JSON;
		try {
			if (targetType == null) {
				result = gson.toJson(target);
			} else {
				result = gson.toJson(target, targetType);
			}
		} catch (Exception ex) {
			System.out.println("目标对象 " + target.getClass().getName() + " 转换 JSON 字符串时，发生异常！");
			if (target instanceof Collection<?> || target instanceof Iterator<?> || target instanceof Enumeration<?> || target.getClass().isArray()) {
				result = EMPTY_JSON_ARRAY;
			}
		}
		return result;
	}

	public static boolean isBlank(String str) {
		if (null == str || str.trim().length() == 0) {
			return true;
		} else {
			return false;
		}
	}

	public static String getSafeString(String str) {
		if (str == null)
			return "";
		return str;
	}

	/*******************************************************************************************/
	
	/**
	 * json字符串转成Map
	 * 
	 * @param str
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static Map jsonStrToMap(String str) {
		if (str == null || str.trim().length() == 0) {
			return new HashMap();
		}
		return JsonUtil.fromJson(str, Map.class);
	}

	/**
	 * map转成json字符串
	 * 
	 * @param map
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static String mapToJsonStr(Map map) {
		if (map==null || map.size()==0) {
			return "";
		}
		return JsonUtil.toJson(map, "yyyy-MM-dd HH:mm:ss");
	}
	
	/**
	 *  list转json字符串
	 * @param list
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static String listToJsonStr(List list) {
		if (list==null || list.size()==0) {
			return "";
		}
		return JsonUtil.toJson(list);
	}

	/**
	 *  json字符串转List
	 * @param str
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static List jsonStrToList(String str) {
		if (str == null || str.trim().length() == 0) {
			return new ArrayList();
		}
		return JsonUtil.fromJson(str, List.class);
	}

	/**
	 * map转成bean
	 */
	public static Object mapToObject(Map<String, Object> map, Class<?> beanClass) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			if (map == null) {
				return null;
			}
			Object obj = beanClass.newInstance();
			Field[] fields = obj.getClass().getDeclaredFields();
			for (Field field : fields) {
				int mod = field.getModifiers();
				if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
					continue;
				}
				field.setAccessible(true);
				if (field.getGenericType().toString().equals("class java.lang.Long")) {
					if (map.get(field.getName()) != null && !map.get(field.getName()).equals("")) {
						Double douubleObject = new Double(Math.floor(Double.parseDouble(map.get(field.getName()).toString())));
						field.set(obj, douubleObject.longValue());
					}
				} else if (field.getGenericType().toString().equals("class java.lang.Short")) {
					if (map.get(field.getName()) != null && !map.get(field.getName()).equals("")) {
						field.set(obj, Short.parseShort(map.get(field.getName()).toString()));
					}
				} else if (field.getGenericType().toString().equals("class java.util.Date")) {
					if (map.get(field.getName()) != null && !map.get(field.getName()).equals("")) {
						field.set(obj, sdf.parse(map.get(field.getName()).toString()));
					}
				} else if (field.getGenericType().toString().equals("class java.lang.Integer") ||
						field.getGenericType().toString().equals("int")) {
					if (map.get(field.getName()) != null && !map.get(field.getName()).equals("")) {
						field.set(obj, Integer.parseInt(map.get(field.getName()).toString()));
					}
				}else if (field.getGenericType().toString().equals("class java.lang.Double")) {
					if (map.get(field.getName()) != null && !map.get(field.getName()).equals("")) {
						field.set(obj, Double.parseDouble(map.get(field.getName()).toString()));
					}
				}else if (field.getGenericType().toString().equals("class [B")) {
					if (map.get(field.getName()) != null && !map.get(field.getName()).equals("")) {
						byte[] b = map.get(field.getName()).toString().getBytes();
						field.set(obj, b);
					}
				}else {
					field.set(obj, map.get(field.getName()));
				}
			}
			return obj;
		} catch (Exception e) {
			System.out.println("map to bean exception...");
			e.printStackTrace();
		}
		return null;
	}
	
	/**
     * bean转成map（该方法不适用于继承了父类的子类，使用时会忽略父类属性）
     */
    public static Map<String, Object> objectToMap(Object obj) {
        try {
            if (obj == null) {
                return null;
            }
            Map<String, Object> map = new HashMap<String, Object>();
            Field[] declaredFields = obj.getClass().getDeclaredFields();
            for (Field field : declaredFields) {
                field.setAccessible(true);
                map.put(field.getName(), field.get(obj));
            }
            return map;
        } catch (Exception e) {
            System.out.println("bean to map exception...");
            e.printStackTrace();
        }
        return null;
    }
	
	/**
     * JSON转Object对象
     */
    public static <T> T json2Object(String json, Class<T> cls) {
        T result = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            result = objectMapper.readValue(json, cls);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
	
    /**
     * 对象转JSON字符串
     */
    public static String object2Json(Object obj) {
        String result = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            result = objectMapper.writeValueAsString(obj);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
    
	/*
	 * 将父类所有的属性COPY到子类中。 类定义中child一定要extends father；
	 * 而且child和father一定为严格javabean写法，
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void fatherToChild(Object father, Object child) {
		if (!(child.getClass().getSuperclass() == father.getClass())) {
			System.err.println("child不是father的子类");
		}
		Class fatherClass = father.getClass();
		Class childClass = child.getClass();
		Field fields[] = fatherClass.getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			Field f = fields[i];// 取出每一个属性
			try {
				Method fatherMethod = getMethod(fatherClass, "get" + upperHeadChar(f.getName()));//获取方法
				Object obj = null;
				if(fatherMethod != null) {
					obj = fatherMethod.invoke(father);// 取出属性值
				}
				Method childMethod = getMethod(childClass, "set" + upperHeadChar(f.getName()));//设置方法
				if(childMethod != null) {
					childMethod.invoke(child, obj);// 设置属性值
				}
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 获取指定字段名称查找在class中的对应的方法(包括查找父类)
	 * 
	 * @param clazz
	 *            指定的class
	 * @param methodName
	 *            字段名称
	 * @return Method对象
	 */
	private static Method getMethod(Class<?> clazz, String methodName) {
		if (Object.class.getName().equals(clazz.getName())) {
			return null;
		}
		
		Method[] declaredMethods = clazz.getMethods();
		for (Method method : declaredMethods) {
			if (method.getName().equals(methodName)) {
				return method;
			}
		}
		Class<?> superClass = clazz.getSuperclass();
		if (superClass != null) {// 简单的递归一下
			return getMethod(superClass, methodName);
		}
		return null;
	}

	/**
	 * 首字母大写，in:deleteDate，out:DeleteDate
	 */
	private static String upperHeadChar(String in) {
		String head = in.substring(0, 1);
		String out = head.toUpperCase() + in.substring(1, in.length());
		return out;
	}
    
}