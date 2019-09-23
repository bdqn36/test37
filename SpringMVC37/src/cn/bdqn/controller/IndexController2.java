package cn.bdqn.controller;

import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import cn.bdqn.pojo.User;

@Controller
public class IndexController2 {
	Logger log = Logger.getLogger(IndexController2.class);
	@RequestMapping("/index2")
	public String index(String username, String pwd){
		log.info("username=" + username);
		log.info("pwd=" + pwd);
		return "index";
	}
	@RequestMapping("/index3")
	public ModelAndView index3(String username){
		log.info("username=" + username);
		ModelAndView mav = new ModelAndView();
		mav.addObject("username", username);
		mav.setViewName("index");
		return mav;
	}
	@RequestMapping("/index4")
	public String index4(String username,Model model){
		log.info("username=" + username);
		User user = new User();
		user.setUserName(username);
		model.addAttribute("user", user);
		//model.addAttribute(user);
		return "index";
	}
	@RequestMapping("/index5")
	public String index5(String username,Map<String,Object> map){
		log.info("username=" + username);
		User user = new User();
		user.setUserName(username);
		map.put("user5", user);
		map.put("hello", "springMVC");
		return "index";
	}
	
	
	
	
	
}
