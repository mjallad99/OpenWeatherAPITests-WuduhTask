This project is a collection of automated test cases for the WeatherAPI that ensures the correct functionality of various endpoints related to weather data retrieval. It is developed using Java, RestAssured for API testing, and TestNG for managing test cases. ExtentReports is used to generate detailed HTML reports of the test execution.


Dependencies
This project uses the following dependencies:

RestAssured
TestNG
ExtentReports


Test Cases
1. testStatusCode
Verifies that the status code for a valid city (Dubai) is 200.
2. testInvalidAPIKey
Verifies that an invalid API key returns a 401 Unauthorized status.
3. testMissingAPIKey
Verifies that the absence of an API key returns a 401 Unauthorized status.
4. viewResponseBody
Prints the response body for inspection.
5. testInvalidCityName
Verifies that providing an invalid city name results in a 400 Bad Request status.
6. testLocationData
Validates the location data for a valid city (Amman), checking for correct city name and country.
7. testCurrentWeatherData
Verifies that current weather data (temperature and condition) is returned for a valid city.
8. testResponseDataTypes
Ensures that the response contains correct data types for latitude, longitude, and temperature.
9. testResponseTime
Verifies that the response time is under 2 seconds.
10. testPressureAndHumidity
Verifies that pressure and humidity values are within acceptable ranges.
11. testUnsupportedLanguageHeader
Verifies the handling of an unsupported language header.
12. testMultipleCitiesQuery
Verifies querying for multiple cities returns correct data for each.
13. testMaxQueryLength
Verifies that a long city name results in a 400 Bad Request.
Report Generation
After running the tests, an HTML report will be generated as extentReports.html in the project directory. This report provides a detailed summary of the test execution, including passed, failed, and skipped tests.
