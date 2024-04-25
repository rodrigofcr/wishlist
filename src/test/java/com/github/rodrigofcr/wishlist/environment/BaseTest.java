package com.github.rodrigofcr.wishlist.environment;

import com.github.rodrigofcr.wishlist.WishlistApplication;
import com.github.rodrigofcr.wishlist.property.WishlistProperties;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.common.ConsoleNotifier;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.bson.Document;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@ActiveProfiles("test")
@SpringBootTest(
        classes = {WishlistApplication.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
public class BaseTest extends MongoContainer {
    private static final int WIRE_MOCK_PORT = 2345;
    protected static WireMockServer wireMockServer;
    protected final String CUSTOMER_ID_MOCK = UUID.randomUUID().toString();
    protected final String WISHLIST_PATH_FORMAT = "/wishlist/%s/product";
    protected final String WISHLIST_PRODUCT_PATH_FORMAT = "/wishlist/%s/product/%s";
    protected final String REQUEST_BODY_FORMAT = "{\"name\":\"%s\"}";
    @LocalServerPort
    protected long port;
    @Autowired
    protected MongoTemplate mongoTemplate;
    @Autowired
    protected WishlistProperties wishlistProperties;

    @BeforeAll
    public static void initClass() {
        wireMockServer =
                new WireMockServer(
                        WireMockConfiguration.wireMockConfig()
                                .port(WIRE_MOCK_PORT)
                                .notifier(new ConsoleNotifier(true)));

        wireMockServer.start();
    }

    @AfterAll
    public static void tearDown() {
        wireMockServer.stop();
    }

    @BeforeEach
    public void setUp() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.baseURI = String.format("http://localhost:%s", port);
        wireMockServer.resetAll();
    }

    protected void populateWishlist(final String customerId, final Map<String, String> productMap) {
        productMap.forEach((productId, productName) -> {
            RestAssured.given()
                    .accept(ContentType.JSON)
                    .contentType(ContentType.JSON)
                    .body(REQUEST_BODY_FORMAT.formatted(productName))
                    .when()
                    .put(WISHLIST_PRODUCT_PATH_FORMAT.formatted(customerId, productId))
                    .then()
                    .statusCode(201);
        });
    }

    protected Map<String, String> getProductMapMock(final int size) {
        return IntStream.range(0, size).boxed()
                .collect(Collectors.toMap(this::randomUUIDString, this::randomUUIDString));
    }

    protected String randomUUIDString(final int integer) {
        return UUID.randomUUID().toString();
    }

    protected Document readWishlist(final String customerId) {
        return mongoTemplate.findOne(
                new Query(Criteria.where("_id").is(customerId)),
                Document.class,
                "Wishlist");
    }

}
