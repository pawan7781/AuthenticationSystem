package com.becoder.validations;

import com.becoder.repository.UserRepo;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class EmailValidator implements ConstraintValidator<ValidEmail, String>{

        @Autowired
        private UserRepo userRepo;  // Assume you have a UserRepository

        @Override
        public boolean isValid(String email, ConstraintValidatorContext context) {
            return email != null || !userRepo.existsByEmail(email);
        }
    }

