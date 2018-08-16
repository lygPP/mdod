package com.ztesoft.mdod.service.impl.user;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ztesoft.mdod.dao.user.UserDao;
import com.ztesoft.mdod.model.user.User;
import com.ztesoft.mdod.service.inf.user.UserService;
import com.ztesoft.mdod.util.ConstantUtil;
import com.ztesoft.mdod.util.redis.IRedisClient;

@Service("userService")
public class UserServiceImpl implements UserService{
	@Autowired
	IRedisClient redisClient;
	@Autowired
	public UserDao userDao;
	
	@Transactional
	public Map<String, Object> getAllUsers(){
		redisClient.set("test:", "lyg", "lyg");
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> resultObjectMap = new HashMap<String, Object>();
		resultMap.put(ConstantUtil.RESULT_OBJECT, resultObjectMap);
		List<User> users = userDao.getAllUsers();
		resultObjectMap.put("users", users);
		resultMap.put(ConstantUtil.RESULT_CODE, ConstantUtil.RESULT_SUCCESS_CODE);
		resultMap.put(ConstantUtil.RESULT_MSG, ConstantUtil.RESULT_SUCCESS_MSG);
		return resultMap;
	}
}
