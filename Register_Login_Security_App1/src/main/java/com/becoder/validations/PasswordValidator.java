package com.becoder.validations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<ValidPassword, String> {

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (password == null) {
            return false;
        }

        boolean hasUppercase = password.chars().anyMatch(Character::isUpperCase);
        boolean hasSpecialChar = password.chars().anyMatch(ch -> !Character.isLetterOrDigit(ch));
        boolean hasDigit = password.chars().anyMatch(Character::isDigit);
        boolean isAtLeast8 = password.length() >= 8;

        return hasUppercase && hasSpecialChar && hasDigit && isAtLeast8;
    }
}