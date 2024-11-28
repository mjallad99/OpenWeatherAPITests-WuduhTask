package tests;


import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.number.OrderingComparison.*;

public class WeatherAPITests {
    private static final String BASE_URL = "http://api.weatherapi.com/v1/current.json";
    private static final String API_KEY = "83d53f15575b4d1d8b681630242811"; // Replace with your API key
    private ExtentReports extent;
    private ExtentTest test;
    @BeforeClass
    public void setup() {
        // Set up ExtentReports
        ExtentSparkReporter htmlReporter = new ExtentSparkReporter("extentReports.html");
        extent = new ExtentReports();
        extent.attachReporter(htmlReporter);
    }

    @AfterClass
    public void tearDown() {
        // Flush the report at the end of test execution
        extent.flush();
    }

    @Test
    public void testStatusCode() {
        RestAssured
                .given()
                .queryParam("q", "Dubai")
                .queryParam("key", API_KEY)
                .when()
                .get(BASE_URL)
                .then()
                .statusCode(200);


    }
    @Test
    public void testInvalidAPIKey() {
        RestAssured
                .given()
                .queryParam("key", "53")
                .queryParam("q", "Amman")
                .when()
                .get(BASE_URL)
                .then()
                .statusCode(401) // Unauthorized
                .body("error.message", equalTo("Invalid API key"));
    }
    @Test
    public void testMissingAPIKey() {
        RestAssured
                .given()
                .queryParam("q", "Amman")
                .when()
                .get(BASE_URL)
                .then()
                .statusCode(401) // Unauthorized
                .body("error.message", equalTo("API key is invalid or not provided."));
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
    public void testInvalidCityName() {
        RestAssured
                .given()
                .queryParam("key", API_KEY)
                .queryParam("q", "InvalidCity")
                .when()
                .get(BASE_URL)
                .then()
                .statusCode(400) // Bad Request
                .body("error.message", equalTo("No matching location found."));
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
    @Test
    public void testUnsupportedLanguageHeader() {
        RestAssured
                .given()
                .queryParam("key", API_KEY)
                .queryParam("q", "Amman")
                .header("Accept-Language", "unsupported-lang")
                .when()
                .get(BASE_URL)
                .then()
                .statusCode(200)
                .body("location.name", equalTo("Amman")); // Should default to the correct city name
    }
    @Test
    public void testMultipleCitiesQuery() {
        RestAssured
                .given()
                .queryParam("key", API_KEY)
                .queryParam("q", "Amman,London")
                .when()
                .get(BASE_URL)
                .then()
                .statusCode(200)
                .body("location.name", equalTo("Amman"))
                .body("location.name", equalTo("London"));
    }
    @Test
    public void testMaxQueryLength() {
        String longCityName = "A".repeat(300); // Create a very long city name
        RestAssured
                .given()
                .queryParam("key", API_KEY)
                .queryParam("q", longCityName)
                .when()
                .get(BASE_URL)
                .then()
                .statusCode(400); // Expecting a bad request due to the long query parameter
    }



}

