package cn.bdqn.dao.user;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import cn.bdqn.pojo.User;

public interface UserMapper {
	public User getLoginUser(@Param("userCode")String userCode);
	public List<User> getUserList(Map<String,Object> map);
	public int getUserCount(@Param("userName")String userName,@Param("userRole")int userRole);
}
