package junit;

        import com.google.gson.Gson;
        import com.google.gson.JsonElement;
        import com.google.gson.JsonObject;
        import com.google.gson.JsonParser;
        import io.restassured.RestAssured;
        import io.restassured.response.ValidatableResponse;
        import net.serenitybdd.junit.runners.SerenityRunner;
        import net.serenitybdd.rest.SerenityRest;
        import net.thucydides.core.annotations.Title;
        import org.junit.BeforeClass;
        import org.junit.Test;
        import org.junit.runner.RunWith;

        import java.lang.reflect.Array;

        import static org.hamcrest.Matchers.lessThan;

@RunWith(SerenityRunner.class)
public class Zones {

    static String UserToken = "aff6e157-f874-4087-93da-a40b54a7bbe1";
    static String SourceId = "17c9f3cf-973e-4e59-b6cc-61ff20b3d4c3";
    static String ZoneId = "Iwn7gQ4ZFY_BezmXPaLoZ";
    String mediaUrl = "";

    @BeforeClass
    public static void init() {

        RestAssured.baseURI = "https://api.s.st-av.net/v1";

    }

    @Title("List all Zones for the given source")
    @Test
    public void getAllZones() {
        SerenityRest.given()
                .auth().oauth2(UserToken)
                .queryParam("source_id", SourceId)
                .when()
                .get("/zones")
                .then()
                .log()
                .all()
                .statusCode(200)
                .and().time(lessThan(1000L));
    }

    @Title("Get a Zones by ID")
    @Test
    public void getAZone() {
        SerenityRest.given()
                .auth().oauth2(UserToken)
                .queryParam("source_id", SourceId)
                .queryParam("zone_id",ZoneId)
                .when()
                .get("/zone")
                .then()
                .log()
                .all()
                .statusCode(200)
                .and().time(lessThan(1000L));
    }


   //TODO Send Coordinates
  /*  @Title("Create a Zone")
    @Test
    public void postZone() {

        int[][] zoneCoordinates = new int[2][2];
       // zoneCoordinates[0][0] =

        SerenityRest.given()
                .auth().oauth2(UserToken)
                .contentType("application/json")
                .queryParam("source_id", SourceId)
                .param("name", "Zone_9")
                .param("coordinates", "{[1,0], [1,1], [2,0], [2,1]}")
                .when()
                .post("/zone")
                .then()
                .log()
                .all()
                .statusCode(201)
                .and().time(lessThan(1000L));
    }*/

 /* //TODO Add Post a zone, get a Zone by Id, delete a Zone, get deleted Zone by ID
    @Title("Get a Zones by ID")
    @Test
    public void getAZone() {
        SerenityRest.given()
                .auth().oauth2(UserToken)
                .queryParam("source_id", SourceId)
                .queryParam("zone_id",ZoneId)
                .when()
                .get("/zone")
                .then()
                .log()
                .all()
                .statusCode(200)
                .and().time(lessThan(1000L));
    }

*/


}

