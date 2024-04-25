package com.github.rodrigofcr.wishlist;

import com.github.rodrigofcr.wishlist.environment.BaseTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.assertj.core.api.Assertions;
import org.bson.Document;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class DeleteWishlistProductTest extends BaseTest {
    @Test
    void shouldDeleteAndReturnNoContentWhenValidRequest() {
        final var productMap = getProductMapMock(3);
        populateWishlist(CUSTOMER_ID_MOCK, productMap);

        final var productId = productMap.keySet().stream().findFirst().get();
        RestAssured.given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .when()
                .delete(WISHLIST_PRODUCT_PATH_FORMAT.formatted(CUSTOMER_ID_MOCK, productId))
                .then()
                .statusCode(204);

        final var wishlist = readWishlist(CUSTOMER_ID_MOCK);

        final ArrayList<Document> products = wishlist.get("products", ArrayList.class);

        Assertions.assertThat(wishlist).isNotNull();
        Assertions.assertThat(products.contains(productId)).isFalse();
    }

    @Test
    void shouldNotDeleteAndReturnBadRequestWhenProductWasNotIsWishlist() {
        final var productMap = getProductMapMock(3);
        populateWishlist(CUSTOMER_ID_MOCK, productMap);

        final var productId = "productIdThatWasNotInWishlist";
        RestAssured.given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .when()
                .delete(WISHLIST_PRODUCT_PATH_FORMAT.formatted(CUSTOMER_ID_MOCK, productId))
                .then()
                .statusCode(400)
                .body("message", Matchers.equalTo("Product not found in wishlist."));

        final var wishlist = readWishlist(CUSTOMER_ID_MOCK);

        final ArrayList<Document> products = wishlist.get("products", ArrayList.class);

        Assertions.assertThat(wishlist.size()).isEqualTo(productMap.size());
        Assertions.assertThat(products.contains(productId)).isFalse();
    }

}
