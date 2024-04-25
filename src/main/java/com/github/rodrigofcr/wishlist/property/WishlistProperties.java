package com.github.rodrigofcr.wishlist.property;

import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "service.wishlist")
@Validated
public record WishlistProperties(
        @NotNull Integer size
) {
}
