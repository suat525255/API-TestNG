package com.test.run;

import com.test.ProjectMethods.ProjectMethod;
import com.test.TestBase;
import io.restassured.response.Response;
import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.empty;

public class TestRun extends TestBase {

    private JSONObject bookingBody;
    private Response response;
    private String authToken;
    private int bookingId;

    ProjectMethod projectMethod = new ProjectMethod();

    @Test
    public void HealthCheck(){
        response = projectMethod.healthCheck();
        response.then().statusCode(201);

    }

    @Test
    public void RetrieveBooking(){

        bookingId= projectMethod.getBookingIdsFromResponse().get(10);
        response = projectMethod.getBooking(bookingId);
        response.then()
                .statusCode(200)
                .body("firstname", notNullValue())
                .body("lastname", notNullValue());
    }

    @Test
    public void CreateNewBooking(){
        JSONObject bookingDates = new JSONObject();
        bookingDates.put("checkin", "2024-01-01");
        bookingDates.put("checkout", "2024-01-05");

        bookingBody = new JSONObject();
        bookingBody.put("firstname", "Suat");
        bookingBody.put("lastname", "Aydin");
        bookingBody.put("totalprice", 111);
        bookingBody.put("depositpaid", true);
        bookingBody.put("bookingdates", bookingDates);
        bookingBody.put("additionalneeds", "Breakfast");
        response = projectMethod.createBooking(bookingBody);
        response.then().statusCode(200);
        bookingId = response.path("bookingid");
    }

    @Test
    public void UpdateBooking(){
        bookingId = projectMethod.getBookingIdsFromResponse().get(0);
        authToken = projectMethod.getAuthToken("admin", "password123");
        JSONObject bookingDates = new JSONObject();
        bookingDates.put("checkin", "2024-02-01");
        bookingDates.put("checkout", "2024-02-05");

        bookingBody = new JSONObject();
        bookingBody.put("firstname", "SuatUpdate");
        bookingBody.put("lastname", "AydinUpdate");
        bookingBody.put("totalprice", 222);
        bookingBody.put("depositpaid", true);
        bookingBody.put("bookingdates", bookingDates);
        bookingBody.put("additionalneeds", "Lunch");
        response = projectMethod.updateBooking(bookingId, bookingBody, authToken);
        System.out.println("response.prettyPrint() = " + response.prettyPrint());
        System.out.println("response.print() = " + response.print());
        response.then()
                .statusCode(200)
                .body("firstname", equalTo("SuatUpdate"))
                .body("lastname", equalTo("AydinUpdate"))
                .body("totalprice", equalTo(222))
                .body("depositpaid", equalTo(true))
                .body("firstname", equalTo("SuatUpdate"))
                .body("firstname", equalTo("SuatUpdate"))
                .body("bookingdates.checkin", equalTo("2024-02-01"))
                .body("bookingdates.checkout", equalTo("2024-02-05"))
                .body("additionalneeds", equalTo("Lunch"));
    }

    @Test
    public void PartialUpdateBooking(){
        bookingId = projectMethod.getBookingIdsFromResponse().get(0);
        authToken = projectMethod.getAuthToken("admin", "password123");
        bookingBody = new JSONObject();
        bookingBody.put("firstname", "SuatPartialUpdate");
        bookingBody.put("totalprice", 150);
        response = projectMethod.partialUpdateBooking(bookingId, bookingBody, authToken);
        response.then()
                .statusCode(200)
                .body("firstname", equalTo("SuatPartialUpdate"))
                .body("totalprice", equalTo(150));

    }

    @Test
    public void DeleteBooking(){
        bookingId = projectMethod.getBookingIdsFromResponse().get(0);
        authToken = projectMethod.getAuthToken("admin", "password123");
        response = projectMethod.deleteBooking(bookingId, authToken);
        response.then().statusCode(201);

        Assert.assertEquals("Not Found", projectMethod.getBooking(bookingId).print());
    }

    @Test
    public void GetAllBookingIDs(){

        response = projectMethod.getAllBookingIds();
        response.then()
                .statusCode(200)
                .body("$", not(empty()));

        System.out.println("response.path(\"bookingid[0]\") = " + response.path("bookingid[0]"));
        response.print();
    }




}
