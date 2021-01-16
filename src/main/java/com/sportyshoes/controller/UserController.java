package com.sportyshoes.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sportyshoes.dao.UserRepository;
import com.sportyshoes.entities.User;

@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired
	private UserRepository userRepository;
	
	@ModelAttribute
	public void commonData(Model model,Principal principal) {
		String username = principal.getName();
		System.out.println(username);
		User user = userRepository.getUserByUserName(username);
		System.out.println(user);
		model.addAttribute(user);
	}
	
	
	@RequestMapping("/index")
	public String dashboard(Model model,Principal principal)
	{	
		
		return "user/user_dashboard";
	}

}
