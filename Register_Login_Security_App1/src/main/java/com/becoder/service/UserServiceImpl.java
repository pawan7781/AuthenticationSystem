package com.becoder.service;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;

import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.becoder.entity.User;
import com.becoder.repository.UserRepo;

import jakarta.servlet.http.HttpSession;

import java.util.List;
import java.util.UUID;


@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	@Autowired
	private JavaMailSender mailSender;
//    @Autowired
//    private JavaMailSenderImpl mailSender;

	@Override
	public User saveUser(User user , String url) {

		String password=passwordEncoder.encode(user.getPassword());
		user.setPassword(password);
//		user.setRole("ROLE_USER");
		user.setEnabled(false);
		user.setVerificationCode(UUID.randomUUID().toString());


		User newuser = userRepo.save(user);

		sendVerificationMail(user, url);
		return newuser;
	}


//	@Override
//	public void removeSessionMessage() {
//
//		HttpSession session = ((ServletRequestAttributes) (RequestContextHolder.getRequestAttributes())).getRequest()
//				.getSession();
//
//		session.removeAttribute("msg");
//
//	}
//	@Override
//	public boolean isUserExist(String userId) {
//		User user2 = userRepo.findById(userId).orElse(null);
//		return user2 != null ? true : false;
//	}

	public void sendVerificationMail(User user , String url) {
		String From="pawanmagarde124@gmail.com";
		String to= user.getEmail();
		String subject="Account Verification";
		String content="Dear [[name]],<br>"
				+ "Please click the link below to verify your registration:<br>"
				+ "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
				+ "Thank you,<br>"
				+ "EliteMart";

		try {
			MimeMessage message =mailSender.createMimeMessage();
			MimeMessageHelper helper=new MimeMessageHelper(message);

			helper.setFrom(From, "MyCompany");
			helper.setTo(to);
			helper.setSubject(subject);

			content= content.replace("[[name]]", user.getName());
			String siteUrl=url+"/verify?code="+user.getVerificationCode();

//			System.out.println(siteUrl);
			content = content.replace("[[URL]]", siteUrl);

			helper.setText(content, true);
			mailSender.send(message);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	public void sendOtpForPassword(String email, int otp) {
		String From="pawanmagarde124@gmail.com";
		String to=email;
		String subject="OTP For Reset Password ";
		String content="Dear [[name]],<br>"
				+ "Please note the otp below to change your password:<br>"
				+ "OTP : otp "+otp+"<br>"
				+ "Thank you,<br>"
				+ "MyCompany";

		try {
			MimeMessage message =mailSender.createMimeMessage();
			MimeMessageHelper helper=new MimeMessageHelper(message);

			helper.setFrom(From, "MyCompany");
			helper.setTo(to);
			helper.setSubject(subject);

//			content= content.replace("[[name]]", user.getName());

//			String siteUrl=url+"/verify?code="+user.getVerificationCode();

//			System.out.println(siteUrl);
//			content = content.replace("[[URL]]", siteUrl);

			helper.setText(content, true);
			mailSender.send(message);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

    }

	@Override
	public boolean isUserExistByEmail(String email) {
		User user = userRepo.findByEmail(email).orElse(null);
		return user != null ? true : false;
	}



	@Override
	public User getUserByEmail(String email) {
		return userRepo.findByEmail(email).orElse(null);
	}

	@Override
	public boolean verifyAccount(String code) {

		User user= userRepo.findByVerificationCode(code);
		if(user!=null)
		{
			user.setEnabled(true);
			user.setVerificationCode(null);
			userRepo.save(user);
			return true;
		}

		return false;
	}


}
