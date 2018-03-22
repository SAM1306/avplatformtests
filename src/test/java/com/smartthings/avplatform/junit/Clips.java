

package com.smartthings.avplatform.junit;

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

        import javax.validation.constraints.AssertTrue;

        import static org.hamcrest.Matchers.lessThan;

@RunWith(SerenityRunner.class)
public class Clips {

    static String UserToken = "aff6e157-f874-4087-93da-a40b54a7bbe1";
    static String SourceId = "17c9f3cf-973e-4e59-b6cc-61ff20b3d4c3";
    static String offlineSourceId = "ec31e3fa-4609-4c19-9263-000446729196";
    static String ClipID = "5s1RY18-sWq6gipY8i60J";
    static String InvalidClipID = "2f4nKNN0VlW9qURJ";

    static String InvalidUserToken = "affbffcff";
    static String InvalidSourceId = "17c9f33d4c3";
    static Long ResponseTime = 10000L;



    @BeforeClass
    public static void init() {

        RestAssured.baseURI = "https://api.s.st-av.net/v1";
    }


    @Title("Request Clip Record, Get a Clip by Id, Delete a clip and Get Deleted clip")
    @Test
    public void postClipGetClipDeleteClipGetDeletedClip() throws InterruptedException {

        //Record
        ValidatableResponse response = SerenityRest.given()
                .auth().oauth2(UserToken)
                .contentType("application/x-www-form-urlencoded")
                .param("source_id", SourceId)
                .param("duration", 20)
                .when()
                .post("/clip/record")
                .then()
                .log()
                .all()
                .statusCode(201)
                .time(lessThan(ResponseTime));

        System.out.println("Clip Record Requested");

        //Alternate way to be added
        Thread.sleep(20000);

        String responseStr = response.extract().body().asString();
      //  System.out.println(responseStr);
        Gson gson = new Gson(); //TODO Fix
        JsonParser jp = new JsonParser();
        JsonElement je = jp.parse(responseStr);
        JsonObject responseBodyObject = je.getAsJsonObject();

        JsonElement clip = responseBodyObject.get("clip");
        JsonObject clipObject = clip.getAsJsonObject();
        String clipID = clipObject.get("id").getAsString();
        System.out.println("ClipID: " + clipID);

       //Get a Clip
       SerenityRest.given()
                .auth().oauth2(UserToken)
                .contentType("application/x-www-form-urlencoded")
                .queryParam("source_id", SourceId)
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
                .queryParam("source_id", SourceId)
                .queryParam("clip_id", clipID)
                .when()
                .delete("/clip")
                .then()
                .log()
                .all()
                .statusCode(204)
                .time(lessThan(ResponseTime));

        System.out.println("Clip Deleted");

    }

    @Title("Get a Clip with InvalidAuth")
    @Test
    public void getClipInvalidAuth() {
        ValidatableResponse getResponse = SerenityRest.given()
                .auth().oauth2(InvalidUserToken)
                .contentType("application/x-www-form-urlencoded")
                .queryParam("source_id", SourceId)
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
    public void getClipInvalidClipId() {
        ValidatableResponse getResponse = SerenityRest.given()
                .auth().oauth2(InvalidUserToken)
                .contentType("application/x-www-form-urlencoded")
                .queryParam("source_id", SourceId)
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
    public void getClipWithoutClipId() {
        ValidatableResponse getResponse = SerenityRest.given()
                .auth().oauth2(UserToken)
                .contentType("application/x-www-form-urlencoded")
                .queryParam("source_id", SourceId)
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
    public void postClipLongDuration() {
        ValidatableResponse response = SerenityRest.given()
                .auth().oauth2(UserToken)
                .contentType("application/x-www-form-urlencoded")
                .param("source_id", SourceId)
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
    public void postClipMaxDuration() throws InterruptedException {
        ValidatableResponse response = SerenityRest.given()
                .auth().oauth2(UserToken)
                .contentType("application/x-www-form-urlencoded")
                .param("source_id", SourceId)
                .param("duration", 10)
                .when()
                .post("/clip/record")
                .then()
                .log()
                .all()
                .statusCode(201);
                //.time(lessThan(ResponseTime));

        System.out.println(" Clip Requested");
        Thread.sleep(20000);

        String responseStr = response.extract().body().asString();
        String clipResponseBody = response.extract().body().asString();
        JsonParser jp2 = new JsonParser();
        JsonElement je2 = jp2.parse(responseStr);
        JsonObject clipResponseBodyObject = je2.getAsJsonObject();

        //Store Clip Ids in Array List

        JsonElement clip = clipResponseBodyObject.get("clip");
        JsonObject clipObject = clip.getAsJsonObject();

        String state = clipObject.get("state").getAsString();
        System.out.println( "Clip State: " + state);
        state.equals("present");


           // System.out.println("Clip is present");



    }
    @Title("List All Clips")
    @Test
    public void getClips() {
        ValidatableResponse getResponse = SerenityRest.given()
                .auth().oauth2(UserToken)
                .contentType("application/x-www-form-urlencoded")
                .queryParam("source_id", SourceId)
                .when()
                .get("/clips")
                .then()
                .log()
                .all()
                .statusCode(200)
                .time(lessThan(ResponseTime));

        System.out.println("List of all clips is retrieved");


        //TODO Add "start time validation". Get "start" into array and validate it is not more than
        // Clip Retention settings/default - 7 days


/*   String responseStr = getResponse.extract().body().asString();
        responseStr.contains("start");
        Gson gson2 = new Gson();
        JsonParser jp2 = new JsonParser();
        JsonElement je2 = jp2.parse(responseStr);
        JsonObject getResponseBodyObject = je2.getAsJsonObject();

        JsonElement getClips = getResponseBodyObject.get("clips");
        JsonObject getClipsObject = getClips.getAsJsonObject();
        String start = getClipsObject.get("start").getAsString();
        System.out.println("start: " + start);

       */


    }
    @Title("RetrieveAClip")
    @Test
    public void retrieveAClip() {
        ValidatableResponse response = SerenityRest.given()
                .auth().oauth2(UserToken)
                .when()
                .get("https://mediaserv.media12.ec2.st-av.net/image?source_id=17c9f3cf-973e-4e59-b6cc-61ff20b3d4c3&image_id=yioPzdAxbNqhmzVOKCkVI")
                .then()
                .log()
                .all()
                .statusCode(200);

        String responseBody = response.toString();

        responseBody.contains("ContentType");
        boolean contentType = responseBody.contains("video/mp4");

        System.out.println(responseBody);
              //.and().contentType("video/mp4");
        //TODO Fix
    }

    @Title("Get Clip Images")
    @Test
    public void getClipImages() {
        ValidatableResponse getResponse = SerenityRest.given()
                .auth().oauth2(UserToken)
                .contentType("application/x-www-form-urlencoded")
                .queryParam("source_id", SourceId)
                .queryParam("clip_id", ClipID)
                .when()
                .get("/clip")
                .then()
                .log()
                .all()
                .statusCode(200);
                //.time(lessThan(ResponseTime));

        System.out.println("Clip Images retrieved");
    }

    @Title("Request Clip Record - Camera Offline")
    @Test
    public void postClipCameraOffline() {
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
    public void postClipInvalidAuth() {
        ValidatableResponse response = SerenityRest.given()
                .auth().oauth2(InvalidUserToken)
                .contentType("application/x-www-form-urlencoded")
                .param("source_id", SourceId)
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
    public void postClipInvalidSourceId() {
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
}