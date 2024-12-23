package com.test.ProjectMethods;

import com.test.TestBase;
import com.utilities.Driver;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.json.simple.JSONObject;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

import static io.restassured.RestAssured.given;

public class ProjectMethod extends TestBase {

    public ProjectMethod(){
        PageFactory.initElements(Driver.get(),this);
    }

    public Response createBooking(JSONObject bookingBody) {
        return given()
                .contentType(ContentType.JSON)
                .body(bookingBody.toString())
                .when()
                .post("/booking");
    }

    public String getAuthToken(String username, String password) {
        JSONObject authBody = new JSONObject();
        authBody.put("username", username);
        authBody.put("password", password);

        return given()
                .contentType(ContentType.JSON)
                .body(authBody.toString())
                .when()
                .post("/auth")
                .then()
                .statusCode(200)
                .extract()
                .path("token");
    }

    public Response getBooking(int bookingId) {
        return given()
                .contentType(ContentType.JSON)
                .when()
                .get("/booking/" + bookingId);
    }

    public Response updateBooking(int bookingId, JSONObject bookingBody, String token) {
        return given()
                .contentType(ContentType.JSON)
                .header("Cookie", "token=" + token)
                .body(bookingBody.toString())
                .when()
                .put("/booking/" + bookingId);
    }

    public Response partialUpdateBooking(int bookingId, JSONObject bookingBody, String token) {
        return given()
                .contentType(ContentType.JSON)
                .header("Cookie", "token=" + token)
                .body(bookingBody.toString())
                .when()
                .patch("/booking/" + bookingId);
    }

    public Response deleteBooking(int bookingId, String token) {
        return given()
                .contentType(ContentType.JSON)
                .header("Cookie", "token=" + token)
                .when()
                .delete("/booking/" + bookingId);
    }

    public Response getAllBookingIds() {

        return given()
                .contentType(ContentType.JSON)
                .when()
                .get("/booking");
    }

    public Response healthCheck() {
        return given()
                .when()
                .get("/ping");
    }

    public List<Integer> getBookingIdsFromResponse() {
        JsonPath jsonPath = getAllBookingIds().jsonPath();

        return jsonPath.getList("bookingid");
    }


}
