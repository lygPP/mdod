package com.ztesoft.mdod.dao.user;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.ztesoft.mdod.model.user.User;

@Mapper
public interface UserDao {
	
	public int insertUser(@Param("user") User user);

	public List<User> getAllUsers();
}
