package todo;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.h2.H2DatabaseTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import java.util.List;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@QuarkusTest
@QuarkusTestResource(H2DatabaseTestResource.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TodoResourceTest {

  @Test
  @Order(1)
  public void firstGetRequest_noEntries() {
    given().when().get("/api/").then().statusCode(200).body(is("[]"));
  }

  @Test
  @Order(2)
  public void createRequest() {
    given()
        .body("{\"text\": \"some task\"}")
        .contentType(ContentType.JSON)
        .post("/api/")
        .then()
        .statusCode(201);
  }

  @Test
  @Order(3)
  public void secondGetRequest_oneEntries() {
    Response response =
        given()
            .when()
            .get("/api/")
            .then()
            .statusCode(200)
            .body(containsString("some task"))
            .extract()
            .response();

    List<TodoItem> todoItems = response.jsonPath().getList("$");
    assertThat(todoItems, hasSize(1));
  }
}
