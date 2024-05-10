package com.tamstudio.learning.validator;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class AdultAgeValidator implements ConstraintValidator<AdultAge, LocalDate> {
    private int min;

    @Override
    public void initialize(AdultAge constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        this.min = constraintAnnotation.min();
    }

    @Override
    public boolean isValid(LocalDate dob, ConstraintValidatorContext context) {
        if (dob == null) {
            return false; // Null values are considered invalid
        }

        // Calculate age
        LocalDate now = LocalDate.now();
        int age = now.minusYears(dob.getYear()).getYear();

        // Check if age is at least 18
        return age >= min;
    }
}
