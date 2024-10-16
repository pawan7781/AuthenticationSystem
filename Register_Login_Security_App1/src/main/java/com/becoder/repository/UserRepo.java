package com.becoder.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.becoder.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Integer> {


	Optional<User> findByEmail(String email);

	boolean existsByEmail(String email);

	public User findByVerificationCode(String code);

//	@Query("SELECT u from User u where u.email=:email")
//	public User getUserByUserName(@Param("email") String email);
}
