package com.github.rodrigofcr.wishlist.service;

import com.github.rodrigofcr.wishlist.api.dtos.WishlistResponse;
import com.github.rodrigofcr.wishlist.api.dtos.GetWishlistProductResponse;
import com.github.rodrigofcr.wishlist.api.dtos.PutWishlistProductRequest;
import com.github.rodrigofcr.wishlist.db.entity.Product;
import com.github.rodrigofcr.wishlist.db.entity.Wishlist;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface WishlistMapper {

    @Mapping(target = "name", source = "putRequest.name")
    Product toProduct(final String id, final PutWishlistProductRequest putRequest);

    WishlistResponse toWishlistResponse(final Wishlist wishlist);

    GetWishlistProductResponse toGetWishlistProductResponse(final Product product);
}
