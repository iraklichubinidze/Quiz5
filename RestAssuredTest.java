import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class RestAssuredTest {
    private static final String BASE_URL = "http://ergast.com/api/f1";

    @Test(dataProvider = "circuitData")
    public void verifyCircuitCountry(int circuitIndex, String expectedCountry) {
        Response response = getCircuits();
        String circuitId = response.path("MRData.CircuitTable.Circuits[" + circuitIndex + "].circuitId");
        response = getCircuitDetails(circuitId);
        String country = response.path("MRData.CircuitTable.Circuits[0].Location.country");
        Assert.assertEquals(country, expectedCountry, "Country does not match for circuit index: " + circuitIndex);
    }

    @DataProvider(name = "circuitData")
    public Object[][] getCircuitData() {
        return new Object[][]{
                {1, "Australia"},
                {5, "Spain"}
        };
    }

    private Response getCircuits() {
        RequestSpecification request = RestAssured.given();
        return request.get(BASE_URL + "/2017/circuits.json");
    }

    private Response getCircuitDetails(String circuitId) {
        RequestSpecification request = RestAssured.given();
        return request.get(BASE_URL + "/circuits/" + circuitId + ".json");
    }
}
