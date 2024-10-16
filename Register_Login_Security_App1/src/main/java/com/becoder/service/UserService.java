package com.becoder.service;

import com.becoder.entity.User;
import org.springframework.ui.Model;

import java.util.List;

public interface UserService {

	public User saveUser(User user, String url );

	//public void removeSessionMessage();

	//boolean isUserExist(String userId);

	boolean isUserExistByEmail(String email);

	User getUserByEmail(String email);

	public boolean verifyAccount(String code);


}
