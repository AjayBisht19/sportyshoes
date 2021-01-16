package com.sportyshoes.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sportyshoes.entities.User;

public interface UserRepository extends JpaRepository<User, Integer>{
	@Query("select u from User u where u.email =:email")
	public User getUserByUserName(@Param("email") String email);
	
	@Query("select u from User u where u.name =:name")
	public List<User> findByName(@Param("name") String name);
	
}
