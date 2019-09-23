package cn.bdqn.dao.role;

import java.sql.Connection;
import java.util.List;
import cn.bdqn.pojo.Role;

public interface RoleDao {
	
	public List<Role> getRoleList(Connection connection)throws Exception;

}
