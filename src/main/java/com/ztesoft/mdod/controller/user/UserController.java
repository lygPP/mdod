package com.ztesoft.mdod.controller.user;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ztesoft.mdod.model.user.User;
import com.ztesoft.mdod.service.inf.user.UserService;
import com.ztesoft.mdod.util.ConstantUtil;

@RestController
@RequestMapping("/user")
public class UserController {
	@Autowired
    private UserService userService;
    
    @RequestMapping(path="/getAllUsers",method=RequestMethod.GET)
    public Map<String, Object> getAllUsers(){
    	return userService.getAllUsers();
    }
    
    @RequestMapping(path="/login",method=RequestMethod.POST)
    public Map<String, Object> login(@RequestParam Map<String, Object> params){
    	Map<String, Object> resultMap = new HashMap<String, Object>();
    	Map<String, Object> resultObjectMap = new HashMap<String, Object>();
    	String username = (String)params.get("username");
    	String password = (String)params.get("password");
    	if("test".equals(username) && "test".equals(password)){
    		User user = new User();
    		user.setName(username);
    		user.setAge(22);
    		resultMap.put(ConstantUtil.RESULT_CODE, ConstantUtil.RESULT_SUCCESS_CODE);
    		resultMap.put(ConstantUtil.RESULT_MSG, "登录成功！");
    		resultObjectMap.put("user", user);
    		resultMap.put(ConstantUtil.RESULT_OBJECT, resultObjectMap);
    	} else{
    		resultMap.put(ConstantUtil.RESULT_CODE, ConstantUtil.RESULT_FAIL_CODE);
    		resultMap.put(ConstantUtil.RESULT_MSG, "用户名或密码错误！");
    	}
    	return resultMap;
    }
}
