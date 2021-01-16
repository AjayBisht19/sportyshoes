package com.sportyshoes.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sportyshoes.dao.ProductRepository;
import com.sportyshoes.dao.UserRepository;
import com.sportyshoes.entities.Product;
import com.sportyshoes.entities.User;

@Controller
@RequestMapping("/user")
public class UserController {
	private int i;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ProductRepository productRepository;
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
	
	@GetMapping("/addtocart/{pid}")
	public String addtocart(@PathVariable("pid") Integer pid,Principal principal, Model model,HttpSession session) {
		Product product = productRepository.getProductById(pid);		
		Product pro=new Product();
		User user = userRepository.getUserByUserName(principal.getName());
		pro.setUser(user);	
		pro.setName(product.getName());
		pro.setBrand(product.getBrand());
		pro.setImageUrl(product.getImageUrl());
		pro.setPurchaseDate(LocalDate.now().toString());
		pro.setDescription(product.getDescription());
		pro.setPrice(product.getPrice());
		productRepository.save(pro);
		session.setAttribute("message","Added to cart successfully");
		return "redirect:/";
	}
	
	@GetMapping("/cart")
	public String cart(Model model, Principal principal){
		User user = userRepository.getUserByUserName(principal.getName());
		System.out.println("the user i wanted"+user);
		List<Product> products = productRepository.getProductByUserId(user.getId());
		model.addAttribute("products",products);
		return ("user/cart");
		}
	
	@GetMapping("/delete/{pid}")
	public String deletefromcart(@PathVariable("pid") Integer pid) {
		Product product = productRepository.getProductById(pid);
		System.out.println(product.getPid()+ "__"+pid); 
		productRepository.deleteByAId(pid);
		return ("redirect:/user/cart/");
	}
}
