package com.smartthings.avplatform.api;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.smartthings.avplatform.testbase.TestBase;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.serenitybdd.rest.SerenityRest;
import net.thucydides.core.annotations.Title;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Random;

public class Properties {

    @BeforeClass
    public static void init() {

        RestAssured.baseURI = "https://api.s.st-av.net/v1";
    }

    static String UserToken = "aff6e157-f874-4087-93da-a40b54a7bbe1";
    static String SourceId_1 = "17c9f3cf-973e-4e59-b6cc-61ff20b3d4c3";
    static String SourceId_2 = "12630f5c-b852-4f7b-ac7c-bdd410b44b0f";
    static String offlineSourceId = "ec31e3fa-4609-4c19-9263-000446729196";
    static String InvalidUserToken = "affbffcff";
    static String InvalidSourceId = "17c9f33d4c3";
    static String mediaUrl = "";
    static String StreamID = "TyauHPKdbRbi9MwkmoezS";
    static String ImageId = "SZ-pKIbYS4oMDgKc0Y2nI";
    static String ImageMediaURL = "https://mediaserv.media11.ec2.st-av.net/image?source_id=17c9f3cf-973e-4e59-b6cc-61ff20b3d4c3&image_id=imcL-Pn4ASBI7vO6viw5I";
    static String InvalidImageMediaURL = "https://mediaserv.media11.ec2.st-av.net/image?source_id=17c9f3cb6cc-61ff20b3d4c3&image_id=imcL-Pn4ASBI7vO6viw5I";
    static String InvalidImageId = "SZ-pKIbYSKc0Y2nI";
    static String ContentType = "application/x-www-form-urlencoded";
    static Long ResponseTime = 10000L;
    static String OfflineSourceId = "ec31e3fa-4609-4c19-9263-000446729196";
    static String SourceName = "Cam_" + Properties.getRandomValue();

    static String ZoneId = "Iwn7gQ4ZFY_BezmXPaLoZ";
    static String InvalidZoneId = "xyz";
    static String ZoneName = "Zone_" + Properties.getRandomValue();

    static String ClipID = "5s1RY18-sWq6gipY8i60J";
    static String InvalidClipID = "2f4nKNN0VlW9qURJ";


    public static String getRandomValue() {

        Random random = new Random();
        int randomInt = random.nextInt(1000000);
        return Integer.toString(randomInt);
    }

    public String getResponseAsString(ValidatableResponse response){
        String responseBodyString = response.toString();
        return responseBodyString;
    }


/*

    public String getAllImages(String UserToken, String SourceId) {
        ValidatableResponse response = SerenityRest.given()
                .auth().oauth2(UserToken)
                .contentType("image/jpeg")
                .queryParam("source_id", SourceId)
                .when()
                .get("/images")
                .then()
                .log()
                .all();
        // .statusCode(404);

        String responseStr = response.extract().body().asString();
        return responseStr;
    }
*/

    //Images
    //Extract JsonObject for Image
    public JsonObject getImageObject(ValidatableResponse getImageResponse){
        String getImageStr = getImageResponse.extract().body().asString();
        JsonParser jp = new JsonParser();
        JsonElement je = jp.parse(getImageStr);
        JsonObject responseBodyObject = je.getAsJsonObject();
        JsonElement image = responseBodyObject.get("image");
        JsonObject imageObject = image.getAsJsonObject();
        return imageObject;
    }

    // Extract ImageId from Response
    public String getImageId(ValidatableResponse getImageresponse) {
        JsonObject imageObject = getImageObject(getImageresponse);
        String imageId = imageObject.get("id").getAsString();
        return imageId;
    }

    // Extract Image URL from Response
    public String getImageMediaURL(ValidatableResponse getImageresponse) {
        JsonObject imageObject = getImageObject(getImageresponse);
        String imageURL = imageObject.get("media_url").getAsString();
        return imageURL;
    }


    //Clips
    //Extract JsonObject for Clip
    public JsonObject getClipObject(ValidatableResponse getClipResponse){
        String getClipStr = getClipResponse.extract().body().asString();
        JsonParser jp = new JsonParser();
        JsonElement je = jp.parse(getClipStr);
        JsonObject responseBodyObject = je.getAsJsonObject();
        JsonElement clip = responseBodyObject.get("clip");
        JsonObject clipObject = clip.getAsJsonObject();
        return clipObject;
    }

    // Extract ClipId from Response
    public String getClipId(ValidatableResponse getClipresponse) {
        JsonObject clipObject = getImageObject(getClipresponse);
        String clipId = clipObject.get("id").getAsString();
        return clipId;
    }

    // Extract Clip URL from Response
    public String getClipMediaURL(ValidatableResponse getClipresponse) {
        JsonObject clipObject = getImageObject(getClipresponse);
        String clipURL = clipObject.get("media_url").getAsString();
        return clipURL;
    }

    //TODO Reusable Method
    public String getCurrentStateOfClip(ValidatableResponse getClipResponse) {

        String getClipStr = getClipResponse.extract().body().asString();
        JsonParser jp = new JsonParser();
        JsonElement je = jp.parse(getClipStr);
        JsonObject responseBodyObject = je.getAsJsonObject();

        JsonElement clip = responseBodyObject.get("clip");
        JsonObject clipObject = clip.getAsJsonObject();

        String clipState = clipObject.get("state").getAsString();
        return clipState;
    }

    //General method for clip, image, source, zone

    public JsonObject getResponseObject(ValidatableResponse getResponse, String entity){     //entity can be a Source,Stream,Image,Video clip,Zone,
        String getStr = getResponse.extract().body().asString();
        JsonParser jp = new JsonParser();
        JsonElement je = jp.parse(getStr);
        JsonObject responseBodyObject = je.getAsJsonObject();

        JsonElement element = responseBodyObject.get(entity);
        JsonObject elementObject = element.getAsJsonObject();
        return elementObject;
    }

    public String getId(ValidatableResponse getClipResponse, String entity) {
        JsonObject entityObject = getResponseObject(getClipResponse, entity);
        String entityId = entityObject.get("id").getAsString();
        return entityId;
    }

    //for clip and images
    public String getMediaURL(ValidatableResponse getClipResponse, String entity) {
        JsonObject entityObject = getResponseObject(getClipResponse, entity);
        String entityURL = entityObject.get("media_url").getAsString();
        return entityURL;
    }

    // for clip and images
    public String getCurrentState(ValidatableResponse getClipResponse, String entity) {
        JsonObject entityObject = getResponseObject(getClipResponse, entity);
        String entityState = entityObject.get("state").getAsString();
        return entityState;
    }

}
