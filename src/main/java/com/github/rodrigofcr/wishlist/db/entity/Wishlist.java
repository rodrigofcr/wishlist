package com.github.rodrigofcr.wishlist.db.entity;

import com.github.rodrigofcr.wishlist.validation.MaxSize;
import com.github.rodrigofcr.wishlist.validation.UniqueId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "Wishlist")
public record Wishlist(
        @Id String customerId,
        @MaxSize @UniqueId List<Product> products
) {
    public Wishlist(final String customerId) {
        this(customerId, null);
    }

}
