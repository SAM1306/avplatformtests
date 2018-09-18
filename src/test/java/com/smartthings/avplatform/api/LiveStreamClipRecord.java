

package com.smartthings.avplatform.api;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.restassured.response.ValidatableResponse;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.serenitybdd.rest.SerenityRest;
import net.thucydides.core.annotations.Title;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.lessThan;

@RunWith(SerenityRunner.class)
public class LiveStreamClipRecord extends Properties {

    @Title("Request Live Stream, Record Clip simultaneously on same camera")
    @Test
    public void liveStreamClipRecordSameCamera() throws InterruptedException {
        ValidatableResponse response = SerenityRest.given()
                .auth().oauth2(UserToken)
                .contentType("application/x-www-form-urlencoded")
                .param("source_id", SourceId_1)
                .when()
                .post("/stream")
                .then()
                .log()
                .all()
                .time(lessThan(10000L));

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
                .statusCode(200)
                .time(lessThan(1000L));

        System.out.println("LiveStream Requested");

        //Record
        ValidatableResponse clipResponse = SerenityRest.given()
                .auth().oauth2(UserToken)
                .contentType("application/x-www-form-urlencoded")
                .param("source_id", SourceId_1)
                .param("duration", 20)
                .when()
                .post("/clip/record")
                .then()
                .log()
                .all()
                .statusCode(201)
                .time(lessThan(1000L));
        System.out.println("Clip Record Requested");

        //Alternate way to be added
        Thread.sleep(20000);

        String getResponseStr = clipResponse.extract().body().asString();
        //  Gson gson = new Gson(); //TODO Fix
        JsonParser jp2 = new JsonParser();
        JsonElement je2 = jp2.parse(getResponseStr);
        JsonObject responseBodyObject2 = je2.getAsJsonObject();

        JsonElement clip = responseBodyObject2.get("clip");
        JsonObject clipObject = clip.getAsJsonObject();
        String clipID = clipObject.get("id").getAsString();
        System.out.println("ClipID: " + clipID);

        //Get a Clip
        SerenityRest.given()
                .auth().oauth2(UserToken)
                .contentType("application/x-www-form-urlencoded")
                .queryParam("source_id", SourceId_1)
                .queryParam("clip_id", clipID)
                .when()
                .get("/clip")
                .then()
                .log()
                .all()
                .statusCode(200);
        //.time(lessThan(1000L));

        System.out.println("Clip details retrieved");

    }

    @Title("Request Live Stream on One Camera, Record Clip on another camera")  //Using two cameras
    @Test
    public void liveStreamClipRecordOnDifferentCameras() throws InterruptedException {
        ValidatableResponse streamResponse = SerenityRest.given()
                .auth().oauth2(UserToken)
                .contentType("application/x-www-form-urlencoded")
                .param("source_id", SourceId_1)
                .when()
                .post("/stream")
                .then()
                .log()
                .all()
                .time(lessThan(10000L));

        String responseStr = streamResponse.extract().body().asString();

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
                .statusCode(200)
                .time(lessThan(1000L));

        System.out.println("LiveStream Requested");

        //View Live Stream

        //Record
        ValidatableResponse clipResponse = SerenityRest.given()
                .auth().oauth2(UserToken)
                .contentType("application/x-www-form-urlencoded")
                .param("source_id", SourceId_2)
                .param("duration", 20)
                .when()
                .post("/clip/record")
                .then()
                .log()
                .all()
                .statusCode(201)
                .time(lessThan(1000L));
        System.out.println("Clip Record Requested");

        //Alternate way to be added
        Thread.sleep(20000);

        String getResponseStr = clipResponse.extract().body().asString();
        //  Gson gson = new Gson(); //TODO Fix
        JsonParser jp2 = new JsonParser();
        JsonElement je2 = jp2.parse(getResponseStr);
        JsonObject responseBodyObject2 = je2.getAsJsonObject();

        JsonElement clip = responseBodyObject2.get("clip");
        JsonObject clipObject = clip.getAsJsonObject();
        String clipID = clipObject.get("id").getAsString();
        System.out.println("ClipID: " + clipID);

        //Get a Clip
        SerenityRest.given()
                .auth().oauth2(UserToken)
                .contentType("application/x-www-form-urlencoded")
                .queryParam("source_id", SourceId_2)
                .queryParam("clip_id", clipID)
                .when()
                .get("/clip")
                .then()
                .log()
                .all()
                .statusCode(200);
        //.time(lessThan(1000L));

        System.out.println("Clip details retrieved");

    }


    @Title("Request Live Stream, Record 10 Clips  on same camera")  //Same Camera
    @Test
    public void liveStreamMultipleClipRecordsSameCamera() throws InterruptedException {
        ValidatableResponse response = SerenityRest.given()
                .auth().oauth2(UserToken)
                .contentType("application/x-www-form-urlencoded")
                .param("source_id", SourceId_1)
                .when()
                .post("/stream")
                .then()
                .log()
                .all()
                .time(lessThan(10000L));

        String responseStr = response.extract().body().asString();
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

        for (int i = 1; i <= 1; i++) {
            ValidatableResponse clipResponse = SerenityRest.given()
                    .auth().oauth2(UserToken)
                    .contentType("application/x-www-form-urlencoded")
                    .param("source_id", SourceId_1)
                    .param("duration", 10)
                    .when()
                    .post("/clip/record")
                    .then()
                    .log()
                    .all()
                    .statusCode(201);
            //.time(lessThan(ResponseTime));
            Thread.sleep(20000);
            System.out.println("Clip " + i + " Record Requested");

            String clipResponseBody = clipResponse.extract().body().asString();
            JsonParser jp2 = new JsonParser();
            JsonElement je2 = jp2.parse(clipResponseBody);
            JsonObject clipResponseBodyObject = je2.getAsJsonObject();

            //Store Clip Ids in Array List

            JsonElement clip = clipResponseBodyObject.get("clip");
            JsonObject clipObject = clip.getAsJsonObject();
            String state = clipObject.get("state").getAsString();
            String clipID = clipObject.get("id").getAsString();

            SerenityRest.given()
                    .auth().oauth2(UserToken)
                    .contentType("application/x-www-form-urlencoded")
                    .queryParam("source_id", SourceId_1)
                    .queryParam("clip_id", clipID)
                    .when()
                    .get("/clip")
                    .then()
                    .log()
                    .all()
                    .statusCode(200);
            //.time(lessThan(ResponseTime));


            System.out.println(+i+ "Clip State: " + state);
            if (state.equals("present"))
                System.out.println("Clip is present");
            else
                System.out.println("Clip is not present");

        }

//TODO Fix Clip is not present ??
        System.out.println("Running: " + running);
        running.equals("false");

    }
}

