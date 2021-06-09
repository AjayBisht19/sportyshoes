package com.sportyshoes.controller;

import java.security.Principal;
import java.util.List;

import javax.servlet.http.HttpSession; 

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sportyshoes.dao.ProductRepository;
import com.sportyshoes.dao.UserRepository;
import com.sportyshoes.entities.Product;
import com.sportyshoes.entities.User;
import com.sportyshoes.helper.Message;

@Controller
public class HomeController {
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ProductRepository productRepository;
	
	@GetMapping("/")
	public String products(Model model,Principal principal) {
		
		if(principal ==null) {
			User admin=new User();
			admin.setName("Admin");
			admin.setEmail("admin@gmail.com");
			admin.setPassword(passwordEncoder.encode("admin123"));
			admin.setRole("ROLE_admin");
			System.out.println(admin);
			try {
				userRepository.save(admin);
			} catch (Exception e) {
				System.out.println("Exception  to be handled here");
				return "redirect:/login";
			}
			
			return "redirect:/login";
		}
		
		List<String> categories = productRepository.getCategories();
		System.out.println(categories);
		model.addAttribute("categories",categories);
		
		String username = principal.getName();
		System.out.println(username);
		User user = userRepository.getUserByUserName(username);
		System.out.println(user);
		model.addAttribute(user);
		List<Product> products = productRepository.getProductByUser();
		model.addAttribute("products",products);
		return "/products/products";
	}
	
	@GetMapping("/{brand}")
	public String brand(@PathVariable("brand") String brand,Model model,Principal principal) {
		String username = principal.getName();
		System.out.println(username);
		User user = userRepository.getUserByUserName(username);
		System.out.println(user);
		model.addAttribute(user);
		List<Product> products = productRepository.getProductByCategory(brand);
		model.addAttribute("products",products);
		System.out.println("main brands are"+products.toString());
		return "products/products";
	}
	
	@GetMapping("/login")
	public String test() {
		
		return "home";
	}
	
	
	
	@GetMapping("/signup")
	public String sigup(Model model) {
		model.addAttribute("user",new User());
	
		return "signup";
	}
	
	@PostMapping("/doregister")
	public String register(@ModelAttribute("user") User user,Model model,HttpSession session ) {
		try {
			System.out.println(user);
			user.setRole("ROLE_user");
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			this.userRepository.save(user);
			model.addAttribute("user",new User());
			session.setAttribute("message", new Message("Successfully registered! Login Now","alert-success"));
			return "home";

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			model.addAttribute("user",user);
			session.setAttribute("message", new Message("Something went wrong!! Try different Email", "alert-danger"));
		}
		
	
		return "signup";
	}
	
}
