package cn.bdqn.controller;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.catalina.startup.SetAllPropertiesRule;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONArray;

import cn.bdqn.pojo.User;
import cn.bdqn.service.role.RoleService;
import cn.bdqn.service.user.UserService;
import cn.bdqn.service.user.UserServiceImpl;
import cn.bdqn.tools.Constants;
import cn.bdqn.pojo.Role;
//import cn.bdqn.service.role.RoleService;
//import cn.bdqn.service.role.RoleServiceImpl;
import cn.bdqn.tools.PageSupport;

@Controller
@RequestMapping("/user")
public class UserController {
	Logger log = Logger.getLogger(UserController.class);
	@Resource
	UserService userService;
	@Resource
	RoleService roleService;
	@RequestMapping("/login")
	public String login(){
		//System.out.println(1/0);
		return "login";
	}
	@RequestMapping("/login.do")
	public String doLogin(String userCode, String userPassword,
			HttpSession session, HttpServletRequest request){
		System.out.println("login ============ " );
		//����service�����������û�ƥ��
		User user = userService.login(userCode,userPassword);
		if(null != user){//��¼�ɹ�
			//����session
			session.setAttribute(Constants.USER_SESSION, user);
			//ҳ����ת��frame.jsp��
			//response.sendRedirect("jsp/frame.jsp");
			return "redirect:/user/main.html";
		}else{
			//ҳ����ת��login.jsp��������ʾ��Ϣ--ת��
			request.setAttribute("error", "�û��������벻��ȷ");
			//request.getRequestDispatcher("login.jsp").forward(request, response);
			return "login";
		}
	}
	@RequestMapping("/main.html")
	public String main(HttpSession session){
		if (session.getAttribute(Constants.USER_SESSION) == null) {
			return "redirect:/user/login";
		}
		return "frame";
	}
	@RequestMapping("/user.do")
	public String userList(String queryname,String queryUserRole,
			String pageIndex,HttpServletRequest request){
		//��ѯ�û��б�
		String queryUserName = queryname;
		String temp = queryUserRole;
		int queryUserRoleInt = 0;
		List<User> userList = null;
		//����ҳ������
    	int pageSize = Constants.pageSize;
    	//��ǰҳ��
    	int currentPageNo = 1;
		/**
		 * http://localhost:8090/SMBMS/userlist.do
		 * ----queryUserName --NULL
		 * http://localhost:8090/SMBMS/userlist.do?queryname=
		 * --queryUserName ---""
		 */
		System.out.println("queryUserName servlet--------"+queryUserName);  
		System.out.println("queryUserRole servlet--------"+queryUserRole);  
		System.out.println("query pageIndex--------- > " + pageIndex);
		if(queryUserName == null){
			queryUserName = "";
		}
		if(temp != null && !temp.equals("")){
			queryUserRoleInt = Integer.parseInt(temp);
		}
		
    	if(pageIndex != null){
    		try{
    			currentPageNo = Integer.valueOf(pageIndex);
    		}catch(NumberFormatException e){
    			//response.sendRedirect("error.jsp");
    			return "redirect:/user/error";
    		}
    	}	
    	//����������	
    	int totalCount	= userService.getUserCount(queryUserName,queryUserRoleInt);
    	//��ҳ��
    	PageSupport pages=new PageSupport();
    	pages.setCurrentPageNo(currentPageNo);
    	pages.setPageSize(pageSize);
    	pages.setTotalCount(totalCount);
    	
    	int totalPageCount = pages.getTotalPageCount();
    	
    	//������ҳ��βҳ
    	if(currentPageNo < 1){
    		currentPageNo = 1;
    	}else if(currentPageNo > totalPageCount){
    		currentPageNo = totalPageCount;
    	}
		
		
		userList = userService.getUserList(queryUserName,queryUserRoleInt,currentPageNo, pageSize);
		request.setAttribute("userList", userList);
//		List<Role> roleList = null;
//		RoleService roleService = new RoleServiceImpl();
//		roleList = roleService.getRoleList();
//		request.setAttribute("roleList", roleList);
		request.setAttribute("queryUserName", queryUserName);
		request.setAttribute("queryUserRole", queryUserRole);
		request.setAttribute("totalPageCount", totalPageCount);
		request.setAttribute("totalCount", totalCount);
		request.setAttribute("currentPageNo", currentPageNo);
//		request.getRequestDispatcher("userlist.jsp").forward(request, response);
		return "userlist";
	}
	@RequestMapping("/error")
	public String error(){
		return "error";
	}
	@RequestMapping("/logout.do")
	public String logout(HttpSession session){
		session.removeAttribute(Constants.USER_SESSION);
		return "login";
	}
	@RequestMapping("/sys/useradd")
	public String useradd(@ModelAttribute("user")User user){
		return "useradd";
	}
	
//	public String useradd1(User user, Model model){
//		model.addAttribute("user", user);
//		return "useradd";
//	}
	@RequestMapping("/userSave")
	public String userSave(User user,HttpSession session,
			HttpServletRequest request,
			@RequestParam(value="a_idPicPath",required=false) MultipartFile attach){
		String idPicPath = null;
		//�ж��ļ��Ƿ�Ϊ��
		if (!attach.isEmpty()) {
			//�ϴ���·��
			String path = request.getSession().getServletContext()
					.getRealPath("statics" + File.separator + "uploadfiles");
			log.info("path==========" + path);
			//ԭ�ļ���
			String oldFileName = attach.getOriginalFilename();
			log.info("oldFileName==========" + oldFileName);
			//ԭ�ļ�����׺��
			String suffix = FilenameUtils.getExtension(oldFileName);
			log.info("suffix==========" + suffix);
			int fileSize = 500000;
			log.info("oldFileSize==========" + attach.getSize());
			if (attach.getSize() > fileSize) {
				//�ϴ��ļ���С����Ҫ��
				request.setAttribute("uploadFileError", "�ϴ��ļ���С����500KB");
				return "useradd";
			} else if (suffix.equalsIgnoreCase("jpg")
					|| suffix.equalsIgnoreCase("png") 
					|| suffix.equalsIgnoreCase("jpeg")
					|| suffix.equalsIgnoreCase("pneg")){
				String fileName = System.currentTimeMillis() + RandomUtils.nextInt(100) + "_Pic.jpg";
				log.info("fileName========" + fileName);
				File targetFile = new File(path,fileName);
				if (!targetFile.exists()) {
					targetFile.mkdirs();
				}
				//�ļ�����
				try {
					attach.transferTo(targetFile);
				} catch (Exception e) {
					e.printStackTrace();
					request.setAttribute("uploadFileError", "�ϴ�ʧ��");
					return "useradd";
				} 
				idPicPath = fileName;
			} else {
				request.setAttribute("uploadFileError", "�ϴ��ļ���ʽ����ȷ");
				return "useradd";
			}
		}
		user.setCreationDate(new Date());
		User sessionUser = (User)session.getAttribute(Constants.USER_SESSION);
		user.setCreatedBy(sessionUser.getId());
		user.setIdPicPath(idPicPath);
		if (userService.add(user)) {
			return "redirect:/user/user.do";
		} else {
			return "error";
		}
	}
	@RequestMapping("/userModify")
	public String userModify(String uid,Model model){
		User user = userService.getUserById(uid);
		model.addAttribute("user", user);
		return "usermodify";
	}
	
	@RequestMapping("/userModifySave")
	public String userModifySave(User user,HttpSession session){
		User sessionUser = (User)session.getAttribute(Constants.USER_SESSION);
		user.setModifyBy(sessionUser.getId());
		user.setModifyDate(new Date());
		if (userService.modify(user)) {
			return "redirect:/user/user.do";
		} else {
			return "error";
		}
	}
	@RequestMapping("/userView/{id}")
	public String userView(@PathVariable String id,Model model){
		User user = userService.getUserById(id);
		model.addAttribute("user", user);
		return "userview";
	}
	
	@RequestMapping("/userCodeExist")
	@ResponseBody
	public Object userCodeExist(String userCode){
		log.info("userCode=======" + userCode);
		Map<String,String> map = new HashMap<String, String>();
		if (userCode == null || "".equals(userCode)) {
			map.put("userCode", "exist");
		} else {
			User user = userService.selectUserCodeExist(userCode);
			if (user != null) {
				map.put("userCode", "exist");
			} else {
				map.put("userCode", "noexist");
			}
		}
		return JSONArray.toJSONString(map);
	}
	@RequestMapping("/getRoleList")
	@ResponseBody
	public Object getRoleList(){
		List<Role> list =  roleService.getRoleList();
		return JSONArray.toJSONString(list);
	}
	
	
	
	
	
}
