package com.sportyshoes.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal; 

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.sportyshoes.dao.ProductRepository;
import com.sportyshoes.dao.UserRepository;
import com.sportyshoes.entities.Product;
import com.sportyshoes.entities.User;

@Controller
@RequestMapping("/admin")
public class AdminController {
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
	public String dashboard(Model model) {
		model.addAttribute("product",new Product());
		return "admin/admin_dashboard";
	}
	

	
	@PostMapping("/addproduct")
	public String addproduct(@ModelAttribute("product") Product product,@RequestParam("productImage") MultipartFile file) {
		
		product.setImageUrl(file.getOriginalFilename());
		try {
			File saveFile=new ClassPathResource("static/img").getFile();
			Path path = Paths.get(saveFile.getAbsolutePath()+File.separator+file.getOriginalFilename());
			Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(product);
		System.out.println("asdf");
		this.productRepository.save(product);
		return "admin/admin_dashboard";
	}
}
