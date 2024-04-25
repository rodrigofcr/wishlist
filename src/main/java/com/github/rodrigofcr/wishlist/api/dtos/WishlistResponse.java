package com.github.rodrigofcr.wishlist.api.dtos;

import com.github.rodrigofcr.wishlist.db.entity.Product;

import java.util.List;

public record WishlistResponse(
        List<Product> products
) {
}
