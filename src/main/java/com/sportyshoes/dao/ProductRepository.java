package com.sportyshoes.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sportyshoes.entities.Product;

public interface ProductRepository extends JpaRepository<Product, Integer>{
	@Query("select p from Product p  where p.pid =:pid")
	public Product getProductById(@Param("pid") Integer pid);
	
	@Query("select p from Product p where p.user is null")
	public List<Product> getProductByUser();
	
	@Query("select p from Product p where p.user.id =:userid")
	public List<Product> getProductByUserId(@Param("userid") Integer userid);
	
	@Transactional
	@Modifying
	@Query("delete from Product p where p.pid =:pid")
	public void deleteByAId(@Param("pid") Integer pid);
	
	@Transactional
	@Modifying
	@Query("delete from Product p where p.description =:descr")
	public void deleteByDesc(@Param("descr") String descr);
	
	@Query("select p from Product p where p.user is not null")
	public List<Product> getProductOfUsers();
	
	@Query("select p from Product p where p.purchaseDate =:date")
	public List<Product> getProductByDate(@Param("date") String date);
	
	
	@Query("select p from Product p where p.brand =:cate and p.user is not null")
	public List<Product> getProductByCate(@Param("cate") String cate);
	
	@Query("select distinct brand from Product")
	public List<String> getCategories();
	
	@Query("select p from Product p where p.brand =:brand and p.user is null")
	public List<Product> getProductByCategory(@Param("brand") String brand);

}
