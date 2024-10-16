package com.becoder.controller;

import com.becoder.entity.User;

import com.becoder.repository.UserRepo;
import com.becoder.service.UserService;

import com.becoder.service.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

@Controller
public class HomeController {
	@Autowired
	UserService userService;
	@Autowired
	UserServiceImpl userServiceImpl;
	@Autowired
	UserRepo userRepo;
	@Autowired
	BCryptPasswordEncoder bcrypt;

	Random r= new Random(100000);
	@GetMapping("/register")
	public ModelAndView register() {

		String viewName = "register";

		Map<String, Object> model = new HashMap<>();

		model.put("userForm", new User());


		return new ModelAndView(viewName, model);
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public ModelAndView processRegister(@Valid @ModelAttribute("userForm") User user, BindingResult result, HttpServletRequest request) {
		String url = request.getRequestURL().toString();
		url = url.replace(request.getServletPath(), "");

		if (result.hasErrors()) {
			// Return to the registration form view with error messages
			return new ModelAndView("register", "userForm", user);
		}

		User usersaved = userService.saveUser(user, url);
		System.out.println("user saved");


		RedirectView rd = new RedirectView();
		rd.setUrl("/register");
		return new ModelAndView(rd);
	}

	@GetMapping("/signin")
	public ModelAndView login() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("login");
		return modelAndView;
	}

	@GetMapping("/verify")
	public String verify(@Param("code") String code) {

		if(userService.verifyAccount(code)) {
			return "verify_success";
		}
		else
		{
			return "failed";
		}
	}
	@RequestMapping("/forgot")
	public String openEmailForm()
	{
		return "forgot_email_form";
	}

	@PostMapping ("/send-otp")
	public String sendOTP(@RequestParam("email") String email, HttpSession session)
	{
		int otp=r.nextInt(999999);
		System.out.println(email+" "+otp);


		if(userRepo.findByEmail(email).isPresent())
		{
			userServiceImpl.sendOtpForPassword(email,otp);
			session.setAttribute("myotp",otp);
			session.setAttribute("email", email);
			return "verifyOTP";

		}
		else {
			session.setAttribute("message","check your email id!!");
			return "forgot_email_form";
		}

	}
	@PostMapping("/verify-otp")
	public String VerifyOTP(@RequestParam("otp") int otp, HttpSession session)
	{

		int myotp= (int)session.getAttribute("myotp");
		String email= (String)session.getAttribute("email");
		if(myotp==otp)
		{
			Optional<User> user=userRepo.findByEmail(email);
			if(user.isEmpty())
			{
				session.setAttribute("message","user doesn't exist with this email id!!");
				return "forgot_email_form";
			}
			else {
				return "password_change_form";
			}

		}
		else
		{
			session.setAttribute("message ","you have entered wrong otp");
			return "verifyOTP";
		}
	}
	@PostMapping("/change-password")
	public String changepassword(@RequestParam("newpassword") String newpassword, HttpSession session)
	{
		String email= (String)session.getAttribute("email");
		User user= userRepo.findByEmail(email).orElse(null);
		user.setPassword(bcrypt.encode(newpassword));
		userRepo.save(user);
		return "redirect:/signin?change=password changed successfully";
	}
}