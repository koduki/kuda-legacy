package cn.orz.pascal.kuda;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class yTest {

    @Test
    public void testHelloEndpoint() {
//        given()
//          .when().get("/y")
//          .then()
//             .statusCode(200)
//             .body(is("hello"));
    }

}