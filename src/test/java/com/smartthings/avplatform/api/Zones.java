
package com.smartthings.avplatform.api;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.restassured.response.ValidatableResponse;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.serenitybdd.rest.SerenityRest;
import net.thucydides.core.annotations.Title;
import org.json.simple.JSONObject;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.lessThan;

@RunWith(SerenityRunner.class)
public class Zones extends Properties{

    @Title("List all Zones for the given source")
    @Test
    public void getAllZones() {
        SerenityRest.given()
                .auth().oauth2(UserToken)
                .queryParam("source_id", SourceId_1)
                .when()
                .get("/zones")
                .then()
                .log()
                .all()
                .statusCode(200)
                .and().time(lessThan(ResponseTime));
    }

    @Title("List all Zones Invalid Auth")
    @Test
    public void getAllZonesInvalidAuth() {
        SerenityRest.given()
                .auth().oauth2(InvalidUserToken)
                .queryParam("source_id", SourceId_1)
                .when()
                .get("/zones")
                .then()
                .log()
                .all()
                .statusCode(403)
                .and().time(lessThan(ResponseTime));
    }

    @Title("List all Zones Invalid SourceId")
    @Test
    public void getAllZonesInvalidSourceId() {
        SerenityRest.given()
                .auth().oauth2(UserToken)
                .queryParam("source_id", InvalidSourceId)
                .when()
                .get("/zones")
                .then()
                .log()
                .all()
                .statusCode(404)
                .and().time(lessThan(ResponseTime));
    }

    @Title("Get a Zone by ID")
    @Test
    public void getAZone() {
        SerenityRest.given()
                .auth().oauth2(UserToken)
                .queryParam("source_id", SourceId_1)
                .queryParam("zone_id", ZoneId)
                .when()
                .get("/zone")
                .then()
                .log()
                .all()
                .statusCode(200)
                .and().time(lessThan(ResponseTime));
    }

    @Title("Get a Zone with Invalid ZoneID")
    @Test
    public void getAZoneInvalidZoneId() {
        SerenityRest.given()
                .auth().oauth2(UserToken)
                .queryParam("source_id", SourceId_1)
                .queryParam("zone_id", InvalidZoneId)
                .when()
                .get("/zone")
                .then()
                .log()
                .all()
                .statusCode(404)
                .and().time(lessThan(ResponseTime));
    }

    @Title("Get a Zone with Invalid Auth")
    @Test
    public void getAZoneInvalidAuth() {
        SerenityRest.given()
                .auth().oauth2(InvalidUserToken)
                .queryParam("source_id", SourceId_1)
                .queryParam("zone_id", InvalidZoneId)
                .when()
                .get("/zone")
                .then()
                .log()
                .all()
                .statusCode(403)
                .and().time(lessThan(ResponseTime));
    }

    @Title("Get a Zone with Invalid SourceId")
    @Test
    public void getAZoneInvalidSourceId() {
        SerenityRest.given()
                .auth().oauth2(UserToken)
                .queryParam("source_id", InvalidSourceId)
                .queryParam("zone_id", ZoneId)
                .when()
                .get("/zone")
                .then()
                .log()
                .all()
                .statusCode(404)
                .and().time(lessThan(ResponseTime));
    }


    //TODO Send Coordinates
    @Ignore
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

        JSONObject jsonMap = new JSONObject();
        jsonMap.put("name", ZoneName);
        jsonMap.put("coordinates", zoneCoordinates);

        SerenityRest.given()
                .auth().oauth2(UserToken)
                .contentType("application/json")
                .queryParam("source_id", SourceId_1)
                .body(jsonMap.toJSONString())
                .when()
                .post("/zone")
                .then()
                .log()
                .all()
                .statusCode(201)
                .and().time(lessThan(ResponseTime));
    }

    @Title("Create a Zone without Zone Name")     //To be succesfull as name is optional
    @Test
    public void postZoneWithoutZoneName() {

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

        JSONObject jsonMap = new JSONObject();
        jsonMap.put("coordinates", zoneCoordinates);

        SerenityRest.given()
                .auth().oauth2(UserToken)
                .contentType("application/json")
                .queryParam("source_id", SourceId_1)
                .body(jsonMap.toJSONString())
                .when()
                .post("/zone")
                .then()
                .log()
                .all()
                .statusCode(201)
                .and().time(lessThan(ResponseTime));
    }

    @Title("Create a Zone without Zone Coordinates")     //To be succesfull as name is optional
    @Test
    public void postZoneWithoutZoneCoordinates() {

        JSONObject jsonMap = new JSONObject();
        jsonMap.put("name", ZoneName);

        SerenityRest.given()
                .auth().oauth2(UserToken)
                .contentType("application/json")
                .queryParam("source_id", SourceId_1)
                .body(jsonMap.toJSONString())
                .when()
                .post("/zone")
                .then()
                .log()
                .all()
                .statusCode(400)
                .and().time(lessThan(ResponseTime));
    }

    @Test
    public void postZoneGetZoneDeleteZoneGetDeletedZone() throws InterruptedException {


        Gson gson = new Gson();
        // Converting multidimensional array into JSON

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

        System.out.println("Data = " + zoneCoordinates);

        JSONObject jsonMap = new JSONObject();
        jsonMap.put("name", ZoneName);
        jsonMap.put("coordinates", zoneCoordinates);

        ValidatableResponse createZone = SerenityRest.given()
                .auth().oauth2(UserToken)
                .contentType("application/json")
                .queryParam("source_id", SourceId_1)
                .body(jsonMap.toJSONString())
                .when()
                .post("/zone")
                .then()
                .log()
                .all()
                .statusCode(201);

        System.out.println("XXXXXXXXXXXXXXXXXXXXXX");
        System.out.println("Zone Created Succesfully");

        //Alternate way to be added
        Thread.sleep(20000);

        String responseStr = createZone.extract().body().asString();
        //Gson gsonZone = new Gson(); //TODO Fix
        JsonParser jp = new JsonParser();
        JsonElement je = jp.parse(responseStr);
        JsonObject responseBodyObject = je.getAsJsonObject();

        JsonElement zone = responseBodyObject.get("zone");
        JsonObject zoneObject = zone.getAsJsonObject();
        String zoneID = zoneObject.get("id").getAsString();
        System.out.println("ZoneID: " + zoneID);

        SerenityRest.given()
                .auth().oauth2(UserToken)
                .contentType("application/json")
                .queryParam("source_id", SourceId_1)
                .queryParam("zone_id", zoneID)
                .when()
                .get("/zone")
                .then()
                .log()
                .all()
                .statusCode(200);

        System.out.println("XXXXXXXXXXXXXXXXXXXXXX");
        System.out.println("Zone details retrieved");

        SerenityRest.given()
                .auth().oauth2(UserToken)
                .contentType("application/json")
                .queryParam("source_id", SourceId_1)
                .queryParam("zone_id", zoneID)
                .when()
                .delete("/zone")
                .then()
                .log()
                .all()
                .statusCode(204);

        System.out.println("XXXXXXXXXXXXXXXXXXXXXX");
        System.out.println("Zone deleted");
        //.and().time(lessThan(ResponseTime));
    }

    //TODO Create a Zone(or take existing Zone, simulate motion within the zone, verify motion detected event,
    //Check if motion detected record clip




}


