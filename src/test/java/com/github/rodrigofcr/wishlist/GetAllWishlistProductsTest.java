package com.github.rodrigofcr.wishlist;

import com.github.rodrigofcr.wishlist.environment.BaseTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

public class GetAllWishlistProductsTest extends BaseTest {
    @Test
    void shouldReadAndReturnOkWhenHasNotEmptyWishlist() {
        final var productMap = getProductMapMock(3);
        populateWishlist(CUSTOMER_ID_MOCK, productMap);

        RestAssured.given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .when()
                .get(WISHLIST_PATH_FORMAT.formatted(CUSTOMER_ID_MOCK))
                .then()
                .statusCode(200)
                .body("products", Matchers.hasSize(3));
    }

    @Test
    void shouldReadAndReturnNoContentWhenHasEmtpyWishlist() {
        RestAssured.given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .when()
                .get(WISHLIST_PATH_FORMAT.formatted(CUSTOMER_ID_MOCK))
                .then()
                .statusCode(204);
    }

}
