package cn.bdqn.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import cn.bdqn.pojo.User;
import cn.bdqn.tools.Constants;

public class SysInterceptor extends HandlerInterceptorAdapter{
	
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response,Object handler) throws Exception{
		HttpSession session = request.getSession();
		User user = (User)session.getAttribute(Constants.USER_SESSION);
		if (user == null) {
			response.sendRedirect(request.getContextPath() + "/401.jsp");
			return false;
		}
		return true;
	}
	
	
	
	
	
	
}
