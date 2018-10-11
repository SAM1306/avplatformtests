
package com.smartthings.avplatform.api;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.restassured.response.ValidatableResponse;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.serenitybdd.rest.SerenityRest;
import net.thucydides.core.annotations.Title;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import static org.hamcrest.Matchers.lessThan;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(SerenityRunner.class)

public class Streams extends Properties {

    @Title("Request Live Stream")
    @Test
    public void postStream() {
        ValidatableResponse response = SerenityRest.given()
                .auth().oauth2(UserToken)
                .contentType("application/x-www-form-urlencoded")
                .param("source_id", SourceId_1)
                .when()
                .post("/stream")
                .then()
//                .log()
//                .all()
                .statusCode(201)
                .time(lessThan(ResponseTime));

        String responseStr = response.extract().body().asString();

        //System.out.println("printing response: "+responseStr);
        //Gson gson = new Gson();
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
                .time(lessThan(ResponseTime));
      /*  JsonElement eventLog = streamObject.get("event_log").getAsJsonArray();
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
    public void getStreams () {
        ValidatableResponse response = SerenityRest.given()
                .auth().oauth2(UserToken)
                //    .contentType("application/x-www-form-urlencoded")
                .queryParam("source_id", SourceId_1)
                .when()
                .get("/streams")
                .then()
                .log()
                .all()
                .statusCode(200)
                .time(lessThan(ResponseTime));

        String responseBody = response.toString();
        // Response Body Validation
        responseBody.contains("ContentType");
        responseBody.contains("OK");
        responseBody.contains("200");
    }

    @Ignore
    @Title("Get a Stream by ID")
    @Test
    public void getAStream () {
        ValidatableResponse response = SerenityRest.given()
                .auth().oauth2(UserToken)
                .contentType("application/x-www-form-urlencoded")
                .queryParam("stream_id", StreamID)
                .when()
                .get("/stream")
                .then()
                .log()
                .all()
                .statusCode(200)
                .time(lessThan(ResponseTime));

        String responseBody = response.toString();

        // Response Body Validation
        responseBody.contains("ContentType");
        responseBody.contains("OK");
        responseBody.contains("200");
    }

    @Ignore
    @Title("Delete a Stream by ID")
    @Test
    public void deleteAStream () {
        ValidatableResponse response = SerenityRest.given()
                .auth().oauth2(UserToken)
                .queryParam("source_id", SourceId_1)
                .when()
                .delete("/stream")
                .then()
                .log()
                .all()
                .statusCode(204)
                .time(lessThan(ResponseTime));

        String responseBody = response.toString();

        // Response Body Validation
        responseBody.contains("ContentType");
        responseBody.contains("OK");
        responseBody.contains("204");
    }

    @Title("Request Live Stream, Get Stream, Delete Stream and Get Deleted Stream")
    @Test
    public void postStreamGetStreamDeleteStreamGetDeletedStream () throws InterruptedException {
        ValidatableResponse response = SerenityRest.given()
                .auth().oauth2(UserToken)
                .contentType("application/x-www-form-urlencoded")
                .param("source_id", SourceId_1)
                .when()
                .post("/stream")
                .then()
//                .log()
//                .all()
                .statusCode(201);

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
                .statusCode(200);

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
                .queryParam("source_id", SourceId_1)
                .when()
                .delete("/stream")
                .then()
                .log()
                .all()
                .statusCode(204);

        System.out.println("Stream Deleted");

        Thread.sleep(2000);

        ValidatableResponse getResponse2 = SerenityRest.given()
                .auth().oauth2(UserToken)
                .contentType("application/x-www-form-urlencoded")
                .queryParam("stream_id", streamID)
                .when()
                .get("/stream")
                .then()
                .log()
                .all()
                .statusCode(404);

        System.out.println("Attempted to retrieve deleted Stream details");
    }

    @Title("Request Live Stream when Camera is Offline")
    @Test
    public void postStreamOfflineCamera () {
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
                .time(lessThan(ResponseTime));

        String responseStr = response.extract().body().asString();
        responseStr.contains("source: ec31e3fa-4609-4c19-9263-000446729196 is offline");
    }

    @Title("Request Live Stream with Invalid UserToken")
    @Test
    public void postStreamInvalidUserToken () {
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
                .time(lessThan(ResponseTime));

        String responseStr = response.extract().body().asString();
    }

    @Title("Request Live Stream with Invalid SourceId")
    @Test
    public void postStreamInvalidSourceId () {
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
                .time(lessThan(ResponseTime));

        String responseStr = response.extract().body().asString();

    }

}

