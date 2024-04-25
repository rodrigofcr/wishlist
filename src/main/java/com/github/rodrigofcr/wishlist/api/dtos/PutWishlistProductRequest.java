package com.github.rodrigofcr.wishlist.api.dtos;

import jakarta.validation.constraints.NotBlank;

public record PutWishlistProductRequest(
        @NotBlank String name
) {
}
