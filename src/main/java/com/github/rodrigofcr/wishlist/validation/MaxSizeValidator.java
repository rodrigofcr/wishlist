package com.github.rodrigofcr.wishlist.validation;

import com.github.rodrigofcr.wishlist.property.WishlistProperties;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Collection;

public class MaxSizeValidator implements ConstraintValidator<MaxSize, Collection> {

    private final WishlistProperties wishlistProperties;
    private String message;

    public MaxSizeValidator(final WishlistProperties wishlistProperties) {
        this.wishlistProperties = wishlistProperties;
    }

    @Override
    public void initialize(MaxSize constraintAnnotation) {
        message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(final Collection collection,
                           final ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message.formatted(wishlistProperties.size()))
                .addConstraintViolation();

        return collection.size() <= wishlistProperties.size();
    }
}
