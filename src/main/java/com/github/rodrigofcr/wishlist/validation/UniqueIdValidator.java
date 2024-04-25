package com.github.rodrigofcr.wishlist.validation;

import com.github.rodrigofcr.wishlist.db.entity.Product;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Collection;
import java.util.Set;

public class UniqueIdValidator implements ConstraintValidator<UniqueId, Collection<Product>> {

    @Override
    public boolean isValid(final Collection<Product> list, final ConstraintValidatorContext context) {
        return list.size() == Set.copyOf(list.stream().map(Product::id).toList()).size();
    }

}
