package com.github.rodrigofcr.wishlist.service;

import com.github.rodrigofcr.wishlist.api.dtos.WishlistResponse;
import com.github.rodrigofcr.wishlist.api.dtos.GetWishlistProductResponse;
import com.github.rodrigofcr.wishlist.api.dtos.PutWishlistProductRequest;
import com.github.rodrigofcr.wishlist.api.exception.BadRequestException;
import com.github.rodrigofcr.wishlist.api.exception.NoContentException;
import com.github.rodrigofcr.wishlist.db.entity.Product;
import com.github.rodrigofcr.wishlist.db.entity.Wishlist;
import com.github.rodrigofcr.wishlist.db.repository.WishlistRepository;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;

@Service
@Validated
public class WishlistService {
    private final WishlistMapper wishlistMapper;
    private final WishlistRepository wishlistRepository;

    public WishlistService(final WishlistMapper wishlistMapper,
                           final WishlistRepository wishlistRepository) {
        this.wishlistMapper = wishlistMapper;
        this.wishlistRepository = wishlistRepository;
    }

    public WishlistResponse addProduct(
            final String customerId,
            final String productId,
            final PutWishlistProductRequest request) {

        final Wishlist customerWishlist =
                wishlistRepository
                        .findOne(Example.of(new Wishlist(customerId)))
                        .orElse(new Wishlist(customerId, new ArrayList<>()));

        final List<Product> customerWishlistProducts = customerWishlist.products();

        final Product productToAdd = wishlistMapper.toProduct(productId, request);
        customerWishlistProducts.add(productToAdd);

        return wishlistMapper.toWishlistResponse(
                wishlistRepository.save(customerWishlist));
    }

    public void removeProduct(final String customerId, final String productIdToRemove) {
        final Wishlist customerWishlist =
                wishlistRepository
                        .findOne(Example.of(new Wishlist(customerId)))
                        .orElseThrow(() -> new BadRequestException("Wishlist is empty."));

        final List<String> customerWishlistProductIds =
                customerWishlist.products().stream().map(Product::id).toList();

        if (customerWishlistProductIds.contains(productIdToRemove)) {
            final List<Product> updatedCustomerWishlistProducts =
                    customerWishlist.products().stream()
                            .filter(product -> !product.id().equals(productIdToRemove))
                            .toList();

            wishlistRepository.save(new Wishlist(customerId, updatedCustomerWishlistProducts));
        } else {
            throw new BadRequestException("Product not found in wishlist.");
        }
    }

    public WishlistResponse readAllProducts(final String customerId) {
        final Wishlist customerWishlist = wishlistRepository
                .findOne(Example.of(new Wishlist(customerId)))
                .orElseThrow(() -> new NoContentException("Wishlist is empty."));

        return wishlistMapper.toWishlistResponse(customerWishlist);
    }

    public GetWishlistProductResponse readProduct(
            final String customerId, final String productId) {
        final Wishlist customerWishlist = wishlistRepository
                .findOne(Example.of(new Wishlist(customerId)))
                .orElseThrow(() -> new NoContentException("Wishlist is empty."));

        final Product foundProduct = customerWishlist.products().stream()
                .filter(product -> product.id().equals(productId))
                .findAny()
                .orElseThrow(() -> new NoContentException("Product is not in wishlist."));

        return wishlistMapper.toGetWishlistProductResponse(foundProduct);
    }

}
