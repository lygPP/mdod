package com.ztesoft.mdod.util.redis;

import java.util.List;
import java.util.Map;


/**
 * redis接口
 * 
 * @author zhao.jingang <br/>
 *         2018年1月24日 上午11:18:03
 */
public interface IRedisClient {

	/**
	 * 设置字符串值,model为类型
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean set(String model, String key, String value);

	/**
	 * 获取值,model为类型
	 * @param key
	 * @return
	 */
	public String get(String model, String key);

	/**
	 * 设置过期时间,model为类型
	 * @param key
	 * @param expire
	 * @return
	 */
	public boolean expire(String model, String key, long expire);
	
	/**
	 * 保存list,model为类型
	 * @param key
	 * @param list
	 * @return
	 */
	public <T> boolean setList(String model, String key, List<T> list);  
    
	/**
	 * 获取list,model为类型
	 * @param key
	 * @param clz
	 * @return
	 */
    public <T> List<T> getList(String model, String key, Class<T> clz);
    
    /**
	 * 保存map,model为类型
	 * @param key
	 * @param list
	 * @return
	 */
	public boolean setMap(String model, String key, Map<String, Object> map);  
    
	/**
	 * 获取map,model为类型
	 * @param key
	 * @param clz
	 * @return
	 */
    public Map<String, Object> getMap(String model, String key);

	/**
	 * 右端进,model为类型
	 * @param key
	 * @param obj
	 * @return
	 */
	public long rpush(String model, String key, Object obj);

	/**
	 * 左端出,model为类型
	 * @param key
	 * @return
	 */
	public String lpop(String model, String key);
	
	/**
	 * 删除key,model为类型
	 */
	public boolean delKey(String model, String key);

	/**
	 * 模糊查询key,model为类型
	 */
	public Map<String, Object> getKeys(String model, String key, int pageNum, int pageSize);

}
