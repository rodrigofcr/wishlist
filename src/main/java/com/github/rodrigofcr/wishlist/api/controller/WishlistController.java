package com.github.rodrigofcr.wishlist.api.controller;

import com.github.rodrigofcr.wishlist.api.dtos.WishlistResponse;
import com.github.rodrigofcr.wishlist.api.dtos.GetWishlistProductResponse;
import com.github.rodrigofcr.wishlist.api.dtos.PutWishlistProductRequest;
import com.github.rodrigofcr.wishlist.service.WishlistService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/wishlist", produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
public class WishlistController {

    private final WishlistService wishlistService;

    public WishlistController(final WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }

    @GetMapping("/{customerId}/product")
    @ResponseStatus(HttpStatus.OK)
    public WishlistResponse getAllWishlistProducts(
            @PathVariable @NotBlank final String customerId) {
        return wishlistService.readAllProducts(customerId);
    }

    @GetMapping("/{customerId}/product/{productId}")
    @ResponseStatus(HttpStatus.OK)
    public GetWishlistProductResponse getWishlistProduct(
            @PathVariable @NotBlank final String customerId,
            @PathVariable @NotBlank final String productId) {
        return wishlistService.readProduct(customerId, productId);
    }

    @PutMapping("/{customerId}/product/{productId}")
    @ResponseStatus(HttpStatus.CREATED)
    public WishlistResponse putWishlistProduct(
            @PathVariable @NotBlank final String customerId,
            @PathVariable @NotBlank final String productId,
            @RequestBody @Valid final PutWishlistProductRequest request) {
        return wishlistService.addProduct(customerId, productId, request);
    }

    @DeleteMapping("/{customerId}/product/{productId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteWishlistProduct(
            @PathVariable @NotBlank final String customerId,
            @PathVariable @NotBlank final String productId) {
        wishlistService.removeProduct(customerId, productId);
    }

}
