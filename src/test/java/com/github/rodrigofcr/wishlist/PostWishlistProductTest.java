package com.github.rodrigofcr.wishlist;

import com.github.rodrigofcr.wishlist.environment.BaseTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.assertj.core.api.Assertions;
import org.bson.Document;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class PostWishlistProductTest extends BaseTest {
    @Test
    void shouldSaveAndReturnCreatedWhenValidRequest() {
        final var productMap = getProductMapMock(5);

        populateWishlist(CUSTOMER_ID_MOCK, productMap);

        final var productId = productMap.keySet().stream().findFirst().get();
        final var productName = productMap.get(productId);

        final var wishlist = readWishlist(CUSTOMER_ID_MOCK);

        final ArrayList<Document> products = wishlist.get("products", ArrayList.class);

        Assertions.assertThat(wishlist).isNotNull();
        Assertions.assertThat(products.size()).isNotEqualTo(0);
        Assertions.assertThat(products.get(0).get("_id")).isEqualTo(productId);
        Assertions.assertThat(products.get(0).get("name")).isEqualTo(productName);
    }

    @Test
    void shouldNotSaveAndReturnBadRequestWhenWishlistIsFull() {
        final var wishlistProductsMaxSize = wishlistProperties.size();

        populateWishlist(CUSTOMER_ID_MOCK, getProductMapMock(wishlistProductsMaxSize));

        final var productId = String.valueOf(wishlistProductsMaxSize + 1);
        final var productName = String.valueOf(wishlistProductsMaxSize + 1);

        RestAssured.given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(REQUEST_BODY_FORMAT.formatted(productName))
                .when()
                .put(WISHLIST_PRODUCT_PATH_FORMAT.formatted(CUSTOMER_ID_MOCK, productId))
                .then()
                .statusCode(400)
                .body("message", Matchers.equalTo("products: size must be between 0 and %s".formatted(wishlistProductsMaxSize)));

        final var wishlist = readWishlist(CUSTOMER_ID_MOCK);

        final ArrayList<Document> products = wishlist.get("products", ArrayList.class);
        Assertions.assertThat(wishlist).isNotNull();
        Assertions.assertThat(products.size()).isEqualTo(wishlistProductsMaxSize);

        final List<String> productIds = products.stream().map(product -> product.get("_id", String.class)).toList();
        Assertions.assertThat(!productIds.contains(productId));
    }

    @Test
    void shouldNotSaveAndReturnBadRequestWhenProductIdAlreadyInWishlist() {
        final var productMap = getProductMapMock(1);

        populateWishlist(CUSTOMER_ID_MOCK, productMap);

        final var productId = productMap.keySet().stream().findFirst().get();
        final var productName = productMap.get(productId);

        RestAssured.given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(REQUEST_BODY_FORMAT.formatted(productName))
                .when()
                .put(WISHLIST_PRODUCT_PATH_FORMAT.formatted(CUSTOMER_ID_MOCK, productId))
                .then()
                .statusCode(400)
                .body("message", Matchers.equalTo("products: cannot contain duplicated id."));

        final var wishlist = readWishlist(CUSTOMER_ID_MOCK);

        final ArrayList<Document> products = wishlist.get("products", ArrayList.class);
        Assertions.assertThat(wishlist).isNotNull();
        Assertions.assertThat(products.size()).isEqualTo(productMap.size());
    }

}
