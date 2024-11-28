package tests;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.number.OrderingComparison.*;

public class OpenWeatherAPITests {
    private static final String BASE_URL = "http://api.weatherapi.com/v1/current.json";
    private static final String API_KEY = "83d53f15575b4d1d8b681630242811"; // Replace with your API key

    @Test
    public void testStatusCode() {
        RestAssured
                .given()
                .queryParam("q", "London")
                .queryParam("key", API_KEY)
                .when()
                .get(BASE_URL)
                .then()
                .statusCode(200);
    }
    @Test
    public void viewResponseBody() {
       Response response= RestAssured
                .given()
                .queryParam("q", "Amman")
                .queryParam("key", API_KEY)
                .when()
                .get(BASE_URL);
                response.prettyPrint();

    }
    @Test
    public void testLocationData() {
        RestAssured
                .given()
                .queryParam("key", API_KEY)
                .queryParam("q", "Amman")
                .when()
                .get(BASE_URL)
                .then()
                .body("location.name", equalTo("Amman")) // Validate city name
                .body("location.country", equalTo("Jordan")); // Validate country
    }
    @Test
    public void testCurrentWeatherData() {
        RestAssured
                .given()
                .queryParam("key", API_KEY)
                .queryParam("q", "Amman")
                .when()
                .get(BASE_URL)
                .then()
                .body("current.temp_c", notNullValue()) // Validate temperature is present
                .body("current.condition.text", notNullValue()); // Validate weather condition text is present
    }
    @Test
    public void testResponseDataTypes() {
        RestAssured
                .given()
                .queryParam("key", API_KEY)
                .queryParam("q", "Amman")
                .when()
                .get(BASE_URL)
                .then()
                .body("location.lat", instanceOf(Float.class)) // Latitude should be a Float
                .body("location.lon", instanceOf(Float.class)) // Longitude should be a Float
                .body("current.temp_c", instanceOf(Float.class)); // Temperature should be a Float
    }
    @Test
    public void testResponseTime() {
        RestAssured
                .given()
                .queryParam("key", API_KEY)
                .queryParam("q", "Amman")
                .when()
                .get(BASE_URL)
                .then()
                .time(lessThan(2000L)); // Response time should be less than 2 seconds
    }
    @Test
    public void testPressureAndHumidity() {
        RestAssured
                .given()
                .queryParam("key", API_KEY)
                .queryParam("q", "Amman")
                .when()
                .get(BASE_URL)
                .then()
                .body(("current.pressure_mb"), greaterThanOrEqualTo(900.0F)) // Pressure should be reasonable
                .body("current.humidity", allOf(greaterThanOrEqualTo(0), lessThanOrEqualTo(100))); // Humidity between 0-100%
    }



}

