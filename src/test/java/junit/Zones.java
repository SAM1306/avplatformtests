package junit;

import io.restassured.RestAssured;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.serenitybdd.rest.SerenityRest;
import net.thucydides.core.annotations.Title;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

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
    @Title("Create a Zone")
    @Test
    public void postZone() {

        List<List<Integer>> zoneCoordinates = new ArrayList<List<Integer>>();

        List<Integer> list1 = new ArrayList<Integer>();
        list1.add(1);
        list1.add(0);

        List<Integer> list2 = new ArrayList<Integer>();
        list2.add(1);
        list2.add(1);

        List<Integer> list3 = new ArrayList<Integer>();
        list3.add(2);
        list3.add(0);

        List<Integer> list4 = new ArrayList<Integer>();
        list4.add(2);
        list4.add(1);

        zoneCoordinates.add(list1);
        zoneCoordinates.add(list2);
        zoneCoordinates.add(list3);
        zoneCoordinates.add(list4);


        SerenityRest.given()
                .auth().oauth2(UserToken)
                .contentType("application/json")
                .queryParam("source_id", SourceId)
                .param("name", "Zone_87")
                .param("coordinates", zoneCoordinates)
                .when()
                .post("/zone")
                .then()
                .log()
                .all()
                .statusCode(201)
                .and().time(lessThan(1000L));
    }

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

