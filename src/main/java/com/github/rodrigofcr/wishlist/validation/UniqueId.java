package com.github.rodrigofcr.wishlist.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Constraint(validatedBy = UniqueIdValidator.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueId {
    String message() default "cannot contain duplicated id.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

