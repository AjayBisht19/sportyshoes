package com.sportyshoes.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
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
		
		List<Product> products = productRepository.getProductByUser();
		model.addAttribute("products",products);
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
		return "redirect:/admin/index";
	}
	
	
	@GetMapping("/delete/{pid}")
	public String deleteitem(@PathVariable("pid") Integer pid) {
		Optional<Product> product = productRepository.findById(pid);
		Product p=product.get();
		productRepository.deleteByDesc(p.getDescription());
		return "redirect:/admin/index";
	}
	
	@GetMapping("/users")
	public String user(Model model) {
		List<User> users = userRepository.findAll();
		model.addAttribute("users",users);
		return "admin/users";
	}

	
	@GetMapping("/report")
	public String report(Model model) {
		
		List<Product> report = productRepository.getProductOfUsers();
		model.addAttribute("report",report);
		return "admin/report";
	}

}
