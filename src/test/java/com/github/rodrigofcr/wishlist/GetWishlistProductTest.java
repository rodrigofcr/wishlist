package com.github.rodrigofcr.wishlist;

import com.github.rodrigofcr.wishlist.environment.BaseTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

public class GetWishlistProductTest extends BaseTest {
    @Test
    void shouldReadAndReturnOkWhenProductIsInWishlist() {
        final var productMap = getProductMapMock(3);
        populateWishlist(CUSTOMER_ID_MOCK, productMap);

        final var productId = productMap.keySet().stream().findFirst().get();
        final var productName = productMap.get(productId);

        RestAssured.given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .when()
                .get(WISHLIST_PRODUCT_PATH_FORMAT.formatted(CUSTOMER_ID_MOCK, productId))
                .then()
                .statusCode(200)
                .body("id", Matchers.equalTo(productId))
                .body("name", Matchers.equalTo(productName));
    }

    @Test
    void shouldReadAndReturnNoContentWhenProductIsNotInWishlist() {
        final var productMap = getProductMapMock(3);
        populateWishlist(CUSTOMER_ID_MOCK, productMap);

        final var productId = "productIdThatWasNotInWishlist";

        RestAssured.given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .when()
                .get(WISHLIST_PRODUCT_PATH_FORMAT.formatted(CUSTOMER_ID_MOCK, productId))
                .then()
                .statusCode(204);
    }

}
