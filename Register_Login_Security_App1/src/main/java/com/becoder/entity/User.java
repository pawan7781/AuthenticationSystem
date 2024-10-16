package com.becoder.entity;

import com.becoder.validations.ValidEmail;
import com.becoder.validations.ValidName;
import com.becoder.validations.ValidPassword;
import com.becoder.validations.ValidPhone;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Getter
@AllArgsConstructor
@Builder
@Setter
@NoArgsConstructor
@Data
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

//	@NotNull(message = "Name cannot be null")
//	@Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
	@ValidName
	private String name;

	@ValidPhone
	private String mobileNo;

	@NotBlank(message="email can not be empty! ")
	@Email(message = "Invalid email format")
	@Size(max = 50, message = "Email must be up to 50 characters")
	@ValidEmail
	private String email;

//	@NotBlank(message="password can not be empty! ")
//	@Size(min = 6, max = 12, message = "Password must be between 6 and 12 characters")
	@ValidPassword
	private String password;

//	@NotBlank(message="role can not be empty! ")
//	private String role;

	private boolean enabled;

	private String verificationCode;



}
