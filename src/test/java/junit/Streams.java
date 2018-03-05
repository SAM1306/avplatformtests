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

import static org.hamcrest.Matchers.lessThan;

@RunWith(SerenityRunner.class)

public class Streams {

    static String UserToken = "aff6e157-f874-4087-93da-a40b54a7bbe1";
    static String SourceId = "17c9f3cf-973e-4e59-b6cc-61ff20b3d4c3";
    static String offlineSourceId = "ec31e3fa-4609-4c19-9263-000446729196";

    static String InvalidUserToken = "aff6e157-bbe1";
    static String InvalidSourceId = "1e3fa-4609-4c19-";

    String mediaUrl = "";
    static String StreamID = "TyauHPKdbRbi9MwkmoezS";

    @BeforeClass
    public static void init() {

        RestAssured.baseURI = "https://api.s.st-av.net/v1";
    }


    @Title("Request Live Stream")
    @Test
    public void postStream() {
        ValidatableResponse response = SerenityRest.given()
                .auth().oauth2(UserToken)
                .contentType("application/x-www-form-urlencoded")
                .param("source_id", SourceId)
                .when()
                .post("/stream")
                .then()
                .log()
                .all()
                .time(lessThan(1000L));

        String responseStr = response.extract().body().asString();

        //System.out.println("printing response: "+responseStr);

        Gson gson = new Gson();
        JsonParser jp = new JsonParser();
        JsonElement je = jp.parse(responseStr);
        JsonObject responseBodyObject = je.getAsJsonObject();

        JsonElement stream = responseBodyObject.get("stream");
        JsonObject streamObject = stream.getAsJsonObject();

        String gatewayURL = streamObject.get("gateway_url").getAsString();
        System.out.println("RTSP GatewayURL: " + gatewayURL);

        String running = streamObject.get("running").getAsString();
        System.out.println("Running: " + running);
        running.equals("false");

        String streamID = streamObject.get("id").getAsString();
        System.out.println("StreamID: " + streamID);

        SerenityRest.given()
                .auth().oauth2(UserToken)
                .contentType("application/x-www-form-urlencoded")
                .param("stream_id", streamID)
                .when()
                .get("/stream")
                .then()
                .log()
                .all()
                .time(lessThan(1000L));

     /*   JsonElement eventLog = streamObject.get("event_log").getAsJsonArray();
        JsonObject eventObject = eventLog.getAsJsonObject();
        String name = eventObject.get("name").getAsString();
        System.out.println("Name : " + name);
   */
        String responseBody = response.toString();

        // Response Body Validation
        responseBody.contains("ContentType");
        responseBody.contains("Created");
        responseBody.contains("201");

        responseBody.contains("stream");
        responseBody.contains("user_id");
        responseBody.contains("event_log");
        responseBody.contains("ts");
        responseBody.contains("name");
        responseBody.contains("running");
        responseBody.contains("id");

        responseBody.contains("mode");
        responseBody.contains("profile");
        responseBody.contains("source_id");
        responseBody.contains("modified");
        responseBody.contains("client_id");
        responseBody.contains("gateway_url");
        responseBody.contains("created");


        //Validate running = "false"
        //Validate  event log "name": "stream_announced"
        System.out.println(responseBody);

    }

    @Title("Get Streams")
    @Test
    public void getStreams() {
        ValidatableResponse response = SerenityRest.given()
                .auth().oauth2(UserToken)
                //    .contentType("application/x-www-form-urlencoded")
                .queryParam("source_id", SourceId)
                .when()
                .get("/streams")
                .then()
                .log()
                .all()
                .time(lessThan(1000L));

        String responseBody = response.toString();

        // Response Body Validation
        responseBody.contains("ContentType");
        responseBody.contains("OK");
        responseBody.contains("200");
    }

    @Title("Get a Stream by ID")
    @Test
    public void getAStream() {
        ValidatableResponse response = SerenityRest.given()
                .auth().oauth2(UserToken)
                .contentType("application/x-www-form-urlencoded")
                .queryParam("stream_id", StreamID)       //Declared constant
                .when()
                .get("/stream")
                .then()
                .log()
                .all()
                .time(lessThan(1000L));

        String responseBody = response.toString();

        // Response Body Validation
        responseBody.contains("ContentType");
        responseBody.contains("OK");
        responseBody.contains("200");
    }


    @Title("Delete a Stream by ID")
    @Test
    public void deleteAStream() {
        ValidatableResponse response = SerenityRest.given()
                .auth().oauth2(UserToken)
                .queryParam("source_id", SourceId)
                .when()
                .delete("/stream")
                .then()
                .log()
                .all()
                .time(lessThan(1000L));

        String responseBody = response.toString();

        // Response Body Validation
        responseBody.contains("ContentType");
        responseBody.contains("OK");
        responseBody.contains("204");
    }

    //TODO Remove hard coded values for Stream ID, pass parameters to other functions

    @Title("Request Live Stream, Get Stream, Delete Stream and Get Deleted Stream")
    @Test
    public void postStreamGetStreamDeleteStreamGetDeletedStream() {
        ValidatableResponse response = SerenityRest.given()
                .auth().oauth2(UserToken)
                .contentType("application/x-www-form-urlencoded")
                .param("source_id", SourceId)
                .when()
                .post("/stream")
                .then()
                .log()
                .all()
                .statusCode(201);
               // .time(lessThan(1000L));

        System.out.println("Stream Requested");


        String responseStr = response.extract().body().asString();
        Gson gson = new Gson();
        JsonParser jp = new JsonParser();
        JsonElement je = jp.parse(responseStr);
        JsonObject responseBodyObject = je.getAsJsonObject();

        JsonElement stream = responseBodyObject.get("stream");
        JsonObject streamObject = stream.getAsJsonObject();
        String streamID = streamObject.get("id").getAsString();
        System.out.println("StreamID: " + streamID);

        ValidatableResponse getResponse = SerenityRest.given()
                .auth().oauth2(UserToken)
                .contentType("application/x-www-form-urlencoded")
                .queryParam("stream_id", streamID)
                .when()
                .get("/stream")
                .then()
                .log()
                .all()
                .statusCode(200)
                .time(lessThan(1000L));

        System.out.println("Stream details retrieved");

        String getResponseStr = getResponse.extract().body().asString();

        Gson gson2 = new Gson();
        JsonParser jp2 = new JsonParser();
        JsonElement je2 = jp.parse(getResponseStr);
        JsonObject getResponseBodyObject = je.getAsJsonObject();

        JsonElement getStream = getResponseBodyObject.get("stream");
        JsonObject getStreamObject = getStream.getAsJsonObject();
        String running = getStreamObject.get("running").getAsString();
        System.out.println("Running: " + running);

        SerenityRest.given()
                .auth().oauth2(UserToken)
                .queryParam("source_id", SourceId)
                .when()
                .delete("/stream")
                .then()
                .log()
                .all()
                .statusCode(204)
                .time(lessThan(1000L));

        System.out.println("Stream Deleted");


        ValidatableResponse getResponse2 = SerenityRest.given()
                .auth().oauth2(UserToken)
                .contentType("application/x-www-form-urlencoded")
                .queryParam("stream_id", streamID)
                .when()
                .get("/stream")
                .then()
                .log()
                .all()
                .statusCode(404)
             //   .statusLine(No such stream)
                .time(lessThan(1000L));

        System.out.println("Attempted to retrieve deleted Stream details");


    }


    @Title("Request Live Stream when Camera is Offline")
    @Test
    public void postStreamOfflineCamera() {
        ValidatableResponse response = SerenityRest.given()
                .auth().oauth2(UserToken)
                .contentType("application/x-www-form-urlencoded")
                .param("source_id", offlineSourceId)
                .when()
                .post("/stream")
                .then()
                .log()
                .all()
                .statusCode(403)
                .time(lessThan(1000L));

        String responseStr = response.extract().body().asString();

        responseStr.contains("source: ec31e3fa-4609-4c19-9263-000446729196 is offline");
    }

    @Title("Request Live Stream with Invalid UserToken")
    @Test
    public void postStreamInvalidUserToken() {
        ValidatableResponse response = SerenityRest.given()
                .auth().oauth2(InvalidUserToken)
                .contentType("application/x-www-form-urlencoded")
                .param("source_id", offlineSourceId)
                .when()
                .post("/stream")
                .then()
                .log()
                .all()
                .statusCode(403)
                .time(lessThan(1100L));

        String responseStr = response.extract().body().asString();

    }

    @Title("Request Live Stream with Invalid SourceId")
    @Test
    public void postStreamInvalidSourceId() {
        ValidatableResponse response = SerenityRest.given()
                .auth().oauth2(UserToken)
                .contentType("application/x-www-form-urlencoded")
                .param("source_id", InvalidSourceId)
                .when()
                .post("/stream")
                .then()
                .log()
                .all()
                .statusCode(404)
                .time(lessThan(1000L));

        String responseStr = response.extract().body().asString();

    }

}
