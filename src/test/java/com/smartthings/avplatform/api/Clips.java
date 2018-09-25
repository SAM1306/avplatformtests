/*


package com.smartthings.avplatform.api;

import io.restassured.response.ValidatableResponse;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.serenitybdd.rest.SerenityRest;
import net.thucydides.core.annotations.Title;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.lessThan;


@RunWith(SerenityRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class Clips extends Properties {


    @Title("Request Clip Record, Get a Clip by Id, Delete a clip and Get Deleted clip")
    @Test
    public void _01postClipGetClipDeleteClipGetDeletedClip() throws InterruptedException {

        //Record a clip
        ValidatableResponse response = SerenityRest.given()
                .auth().oauth2(UserToken)
                .contentType("application/x-www-form-urlencoded")
                .param("source_id", SourceId_1)
                .param("duration", 10)
                .when()
                .post("/clip/record")
                .then()
                .log()
                .all()
                .statusCode(201)
                .time(lessThan(ResponseTime));

        System.out.println("Clip Record Requested");

        Thread.sleep(20000);

        String actualState = getCurrentStateOfClip(response);
        System.out.println("Actual State of clip : " +actualState);
        String expectedState = "present";
        assertThat(actualState.equalsIgnoreCase(expectedState));
        System.out.println("Clip record is successful");

        String clipID = getId(response,"clip");
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
                .statusCode(200)
                .time(lessThan(ResponseTime));

        System.out.println("Clip details retrieved");

        //Delete a Clip
        SerenityRest.given()
                .auth().oauth2(UserToken)
                .queryParam("source_id", SourceId_1)
                .queryParam("clip_id", clipID)
                .when()
                .delete("/clip")
                .then()
                .log()
                .all()
                .statusCode(204)
                .time(lessThan(ResponseTime));

        System.out.println("Clip Deleted");

        //Get Deleted Clip
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
                .statusCode(404)
                .time(lessThan(ResponseTime));

        System.out.println("Clip not found");

    }

    @Title("Get a Clip with InvalidAuth")
    @Test
    public void _02getClipInvalidAuth() {
        ValidatableResponse getResponse = SerenityRest.given()
                .auth().oauth2(InvalidUserToken)
                .contentType("application/x-www-form-urlencoded")
                .queryParam("source_id", SourceId_1)
                .queryParam("clip_id", ClipID)
                .when()
                .get("/clip")
                .then()
                .log()
                .all()
                .statusCode(403)
                .time(lessThan(ResponseTime));

        System.out.println("Invalid Auth");
    }

    @Title("Get a Clip by  InvalidClipId")
    @Test
    public void _03getClipInvalidClipId() {
        ValidatableResponse getResponse = SerenityRest.given()
                .auth().oauth2(InvalidUserToken)
                .contentType("application/x-www-form-urlencoded")
                .queryParam("source_id", SourceId_1)
                .queryParam("clip_id", InvalidClipID)
                .when()
                .get("/clip")
                .then()
                .log()
                .all()
                .statusCode(403)
                .time(lessThan(ResponseTime));

        System.out.println("Invalid Auth");
    }

    @Title("Get a Clip without ClipId")  //ClipId is optional
    @Test
    public void _04getClipWithoutClipId() {
        ValidatableResponse getResponse = SerenityRest.given()
                .auth().oauth2(UserToken)
                .contentType("application/x-www-form-urlencoded")
                .queryParam("source_id", SourceId_1)
                .when()
                .get("/clip")
                .then()
                .log()
                .all()
                .statusCode(200)
                .time(lessThan(ResponseTime));

        System.out.println("Most recent clip is retrieved");
    }

    @Title("Request Clip Record duration>120sec")
    @Test
    public void _05postClipLongDuration() {
        ValidatableResponse response = SerenityRest.given()
                .auth().oauth2(UserToken)
                .contentType("application/x-www-form-urlencoded")
                .param("source_id", SourceId_1)
                .param("duration", 121)
                .when()
                .post("/clip/record")
                .then()
                .log()
                .all()
                .statusCode(400)
                .time(lessThan(ResponseTime));

        System.out.println("Lengthy Clip Requested");
        String responseStr = response.extract().body().asString();
        responseStr.contains("Duration must be between 10 and 120 seconds");
    }

    @Title("Request Clip Record duration=120sec")
    @Test
    public void _06postClipMaxDuration() throws InterruptedException {
        ValidatableResponse response = SerenityRest.given()
                .auth().oauth2(UserToken)
                .contentType("application/x-www-form-urlencoded")
                .param("source_id", SourceId_1)
                .param("duration", 120)
                .when()
                .post("/clip/record")
                .then()
                .log()
                .all()
                .statusCode(201);
        //.time(lessThan(ResponseTime));
        System.out.println(" Clip Requested");
        Thread.sleep(140000);

        String actualState = getCurrentStateOfClip(response);
        System.out.println("Actual State of clip : " +actualState);
        String expectedState = "present";
        assertThat(actualState.equalsIgnoreCase(expectedState));
        System.out.println("Clip record is successful");

        String clipID = getId(response,"clip");
        System.out.println("ClipID: " + clipID);

        ValidatableResponse getClipResponse = SerenityRest.given()
                .auth().oauth2(UserToken)
                .contentType("application/x-www-form-urlencoded")
                .queryParam("source_id", SourceId_1)
                .queryParam("clip_id", clipID)
                .when()
                .get("/clip")
                .then()
                .log()
                .all()
                .statusCode(200)
                .time(lessThan(ResponseTime));
    }

    @Title("List All Clips")
    @Test
    public void _07getClips() {
        ValidatableResponse getResponse = SerenityRest.given()
                .auth().oauth2(UserToken)
                .contentType("application/x-www-form-urlencoded")
                .queryParam("source_id", SourceId_1)
                .when()
                .get("/clips")
                .then()
                .log()
                .all()
                .statusCode(200)
                .time(lessThan(ResponseTime));

        System.out.println("List of all clips is retrieved");
    }

    @Title("RetrieveAClip")  // Record a new clip and retrieve the clip using mediaURL
    @Test
    public void _08retrieveAClip() throws InterruptedException {
        ValidatableResponse response = SerenityRest.given()
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

        System.out.println(" Clip Requested");
        Thread.sleep(20000);

        String clipID = getId(response,"clip");
        System.out.println("ClipID: " + clipID);

        String mediaURL = getMediaURL(response,"clip");
        System.out.println("Clip Media URL: " + mediaURL);

        ValidatableResponse getResponse = SerenityRest.given()
                .auth().oauth2(UserToken)
                .when()
                .get(mediaURL)
                .then()
                .log()
                .all()
                .statusCode(200).contentType("video/mp4");
    }

    @Title("Get Clip Images")
    @Test
    public void _09getClipImages() throws InterruptedException {

        ValidatableResponse response = SerenityRest.given()
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

        System.out.println(" Clip Requested");
        Thread.sleep(30000);

        String clipID = getId(response,"clip");
        System.out.println("ClipID: " + clipID);

        ValidatableResponse getResponse = SerenityRest.given()
                .auth().oauth2(UserToken)
                .contentType("application/x-www-form-urlencoded")
                .queryParam("source_id", SourceId_1)
                .queryParam("clip_id", clipID)
                .when()
                .get("/clip_images")
                .then()
                .log()
                .all()
                .statusCode(200);
        System.out.println("Clip Images retrieved");
    }

    @Title("Request Clip Record - Camera Offline")
    @Test
    public void _10postClipCameraOffline() {
        ValidatableResponse response = SerenityRest.given()
                .auth().oauth2(UserToken)
                .contentType("application/x-www-form-urlencoded")
                .param("source_id", offlineSourceId)
                .param("duration", 20)
                .when()
                .post("/clip/record")
                .then()
                .log()
                .all()
                .statusCode(403)
                .time(lessThan(ResponseTime));

        System.out.println("Clip record requested from an Offline Camera");

        String responseStr = response.extract().body().asString();
        responseStr.contains("source: ec31e3fa-4609-4c19-9263-000446729196 is offline");
    }

    @Title("Request Clip Record - Invalid UserToken")
    @Test
    public void _11postClipInvalidAuth() {
        ValidatableResponse response = SerenityRest.given()
                .auth().oauth2(InvalidUserToken)
                .contentType("application/x-www-form-urlencoded")
                .param("source_id", SourceId_1)
                .param("duration", 20)
                .when()
                .post("/clip/record")
                .then()
                .log()
                .all()
                .statusCode(403)
                .time(lessThan(ResponseTime));

        System.out.println("Clip record requested with Invalid Auth");
    }

    @Title("Request Clip Record - Invalid SourceId")
    @Test
    public void _12postClipInvalidSourceId() {
        ValidatableResponse response = SerenityRest.given()
                .auth().oauth2(UserToken)
                .contentType("application/x-www-form-urlencoded")
                .param("source_id", InvalidSourceId)
                .param("duration", 20)
                .when()
                .post("/clip/record")
                .then()
                .log()
                .all()
                .statusCode(404)
                .time(lessThan(ResponseTime));

        System.out.println("Clip record requested with Invalid SourceId");
    }

    @Title("Request to record a Clip before the earlier request is completed")
    @Test
    public void _13postClipRequest() throws InterruptedException {
       ValidatableResponse response = SerenityRest.given()
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

        System.out.println("Clip 1 record requested");

        String clipState = getCurrentState(response,"clip");
        System.out.println("Clip State: " + clipState);

        String expectedState = "pending";
        assertThat(clipState.equalsIgnoreCase(expectedState));

         ValidatableResponse response2 = SerenityRest.given()
                    .auth().oauth2(UserToken)
                    .contentType("application/x-www-form-urlencoded")
                    .param("source_id", SourceId_1)
                    .param("duration", 10)
                    .when()
                    .post("/clip/record")
                    .then()
                    .log()
                    .all()
                    .statusCode(403);
         Thread.sleep(20000); //To let the previous request complete before running next Clip Record Request
        }

   */
/* @Title("Record 10 clips and verify if they are recorded successfully")
    @Test
    public void _14postClipsRecord() throws InterruptedException {

        for (int k = 1; k <= 10; k++) {
            ValidatableResponse response = SerenityRest.given()
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

            System.out.println("Clip " +k+ " requested");

            String actualState1 = getCurrentStateOfClip(response);
            System.out.println("Actual State of clip : " +actualState1);
            String expectedState1 = "pending";
            assertThat(actualState1.equalsIgnoreCase(expectedState1));
            Thread.sleep(25000);

            ValidatableResponse getClipResponse = SerenityRest.given()
                    .auth().oauth2(UserToken)
                    .contentType("application/x-www-form-urlencoded")
                    .queryParam("source_id", SourceId_1)
                    .when()
                    .get("/clip")
                    .then()
                    .log()
                    .all()
                    .statusCode(200);

           String actualState2 = getCurrentStateOfClip(getClipResponse);
           System.out.println("Actual State of clip : " +actualState2);
           String expectedState2 = "present";
           assertThat(actualState2.equalsIgnoreCase(expectedState2));
           System.out.println("Clip " +k+ " recorded");
        }

    }*//*


    }

*/
