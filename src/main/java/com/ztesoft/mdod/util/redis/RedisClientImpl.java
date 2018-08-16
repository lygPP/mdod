package com.ztesoft.mdod.util.redis;

import java.util.*;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Service;

import com.ztesoft.mdod.util.JsonUtil;
import com.ztesoft.mdod.util.page.ResultPageInfo;

/**
 * redis实现类
 * 
 * @author zhao.jingang <br/>
 *         2018年1月24日 上午11:22:10
 */
@Service
public class RedisClientImpl implements IRedisClient {

	@Resource
	private RedisTemplate<String, ?> redisTemplate;

	public boolean set(final String model, final String key, final String value) {
		boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {
			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
				RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
				connection.set(serializer.serialize(RedisKeyConstant.PROJECT_NAME+model+key),serializer.serialize(value));
				return true;
			}
		});
		return result;
	}

	public String get(final String model, final String key) {
		String result = redisTemplate.execute(new RedisCallback<String>() {
			public String doInRedis(RedisConnection connection) throws DataAccessException {
				RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
				byte[] value = connection.get(serializer.serialize(RedisKeyConstant.PROJECT_NAME+model+key));
				return serializer.deserialize(value);
			}
		});
		return result;
	}

	public boolean expire(final String model, final String key, long expire) {
		return redisTemplate.expire(RedisKeyConstant.PROJECT_NAME+model+key, expire, TimeUnit.SECONDS);
	}
	
	public <T> boolean setList(String model, String key, List<T> list) {  
        String value = JsonUtil.object2Json(list);  
        return set(model,key,value);  
    }  
	  
    @SuppressWarnings("unchecked")
	public <T> List<T> getList(String model, String key, Class<T> clz) {  
        String json = get(model,key);  
        if(json!=null){  
            return JsonUtil.json2Object(json, List.class);
        }  
        return null;  
    }
    
    public boolean setMap(String model, String key, Map<String, Object> map) {
        String value = JsonUtil.object2Json(map);  
        return set(model,key,value);  
    }  
	  
	@SuppressWarnings("unchecked")
	public Map<String, Object> getMap(String model, String key) {  
        String json = get(model,key);  
        if(json!=null){  
            return JsonUtil.json2Object(json, Map.class);
        }  
        return null;  
    }

	public long rpush(final String model, final String key, Object obj) {
		final String value = JsonUtil.object2Json(obj);
		long result = redisTemplate.execute(new RedisCallback<Long>() {
			public Long doInRedis(RedisConnection connection) throws DataAccessException {
				RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
				long count = connection.rPush(serializer.serialize(RedisKeyConstant.PROJECT_NAME+model+key),serializer.serialize(value));
				return count;
			}
		});
		return result;
	}

	public String lpop(final String model, final String key) {
		String result = redisTemplate.execute(new RedisCallback<String>() {
			public String doInRedis(RedisConnection connection) throws DataAccessException {
				RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
				byte[] res = connection.lPop(serializer.serialize(RedisKeyConstant.PROJECT_NAME+model+key));
				return serializer.deserialize(res);
			}
		});
		return result;
	}
	
	public boolean delKey(final String model, final String key){
		boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {
			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
				RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
				connection.del(serializer.serialize(RedisKeyConstant.PROJECT_NAME+model+key));
				return true;
			}
		});
		return result;
	}

	@SuppressWarnings("rawtypes")
	public Map<String, Object> getKeys(final String model, final String key, int pageNum, int pageSize) {
		final int start = (pageNum-1) * pageSize; //起始页下标
		final int end = pageNum * pageSize;
		final int[] count = {0};
		final List<Map> list = new ArrayList<Map>();
		redisTemplate.execute(new RedisCallback<List<Map>>() {
			@SuppressWarnings("unchecked")
			public List<Map> doInRedis(RedisConnection connection) throws DataAccessException {
				RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
				ScanOptions scanOptions = ScanOptions.scanOptions().count(1000).match(RedisKeyConstant.PROJECT_NAME+model+key).build();
				Cursor<byte[]> cursor = connection.scan(scanOptions);
				while(cursor.hasNext()){
					String res = serializer.deserialize(cursor.next());
					if(count[0] < end && count[0] >= start){
						Map map = new HashMap();
						res = res.replace(RedisKeyConstant.PROJECT_NAME + model, "");
						map.put("key", res);
						map.put("value", get(model, res));
						list.add(map);
					}
					count[0]++;
				}
				return list;
			}
		});
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("dataList", list);
		ResultPageInfo pageInfo = new ResultPageInfo();
		pageInfo.setPageNum(pageNum);
		pageInfo.setPageSize(pageSize);
		pageInfo.setTotalRow(count[0]);
		resultMap.put("pageInfo", pageInfo);
		return resultMap;
	}

}
