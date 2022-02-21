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

  private static final String API_BASE_PATH = "/api/v1/todo";

  @Test
  @Order(1)
  public void firstGetRequest_noEntries() {
    given().when().get(API_BASE_PATH + "/").then().statusCode(200).body(is("[]"));
  }

  @Test
  @Order(2)
  public void createRequest() {
    given()
        .body("{\"text\": \"some task\"}")
        .contentType(ContentType.JSON)
        .post(API_BASE_PATH)
        .then()
        .statusCode(201);
  }

  @Test
  @Order(3)
  public void secondGetRequest_oneEntries() {
    Response response =
        given()
            .when()
            .get(API_BASE_PATH)
            .then()
            .statusCode(200)
            .body(containsString("some task"))
            .extract()
            .response();

    List<TodoEntity> todoEntities = response.jsonPath().getList("$");
    assertThat(todoEntities, hasSize(1));
  }
}
