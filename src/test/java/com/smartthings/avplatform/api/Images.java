

package com.smartthings.avplatform.api;

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
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasValue;
import static org.hamcrest.Matchers.lessThan;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SerenityRunner.class)
public class Images extends Properties{

    @Title("List all Images for the given source")
    @Test
    public void getAllImages() {
        SerenityRest.given().auth().oauth2(UserToken).contentType("image/jpeg")
                .queryParam("source_id", SourceId_1)
                .when()
                .get("/images")
                .then()
                .log()
                .all()
                .statusCode(200);
    }

    @Title("List all Images Invalid sourceId")
    @Test
    public void getAllImagesInvalidSourceId() {
        SerenityRest.given()
                .auth().oauth2(UserToken)
                .contentType("image/jpeg")
                .queryParam("source_id", InvalidSourceId)
                .when()
                .get("/images")
                .then()
                .log()
                .all()
                .statusCode(404);
    }

    @Title("List all Images Invalid Auth")
    @Test
    public void getAllImagesInvalidAuth() {
        SerenityRest.given()
                .auth().oauth2(InvalidUserToken)
                .contentType("image/jpeg")
                .queryParam("source_id", SourceId_1)
                .when()
                .get("/images")
                .then()
                .log()
                .all()
                .statusCode(403);
    }

    @Title("Record, Get, Delete and Get Deleted Image")
    @Test
    public void postGetDeleteImage() throws InterruptedException {
        ValidatableResponse response = SerenityRest.given()
                .auth().oauth2(UserToken)
                .contentType("application/x-www-form-urlencoded")
                .param("source_id", SourceId_1)
                .when()
                .post("/image/record")
                .then()
                .log()
                .all()
                .statusCode(201);

        String imageID = getId(response,"image");

        System.out.println("Image record requested");
        System.out.println("ImageID: " + imageID);

        Thread.sleep(2000);

        SerenityRest.given()
                .auth().oauth2(UserToken)
                .contentType("image/jpeg")
                .queryParam("source_id", SourceId_1)
                .queryParam("image_id",imageID)
                .when()
                .get("/image")
                .then()
                .log()
                .all()
                .statusCode(200)
                .statusLine("HTTP/1.1 200 OK")          //Status Line
                .contentType("application/json");

        SerenityRest.given()
                .auth().oauth2(UserToken)
                .contentType("image/jpeg")
                .when()
                .queryParam("source_id", SourceId_1)
                .queryParam("image_id",imageID)
                .delete("/image")
                .then()
                .log()
                .all()
                .statusCode(204);

        SerenityRest.given()
                .auth().oauth2(UserToken)
                .contentType("image/jpeg")
                .queryParam("source_id", SourceId_1)
                .queryParam("image_id",imageID)
                .when()
                .get("/image")
                .then()
                .log()
                .all()
                .statusCode(404);

        System.out.println("Image deleted");
    }

    @Title("GetAnImage")// Retrieves details of most recent recorded image
    @Test
    public void getAnImage() {
        ValidatableResponse response = SerenityRest.given()
                .auth().oauth2(UserToken)
                .contentType("image/jpeg")
                .queryParam("source_id", SourceId_1)
                .when()
                .get("/image")
                .then()
                .log()
                .all()
                .statusCode(200)
                .statusLine("HTTP/1.1 200 OK")
                .contentType("application/json");

        ValidatableResponse response2 = SerenityRest.given()
                .auth().oauth2(UserToken)
                .contentType("image/jpeg")
                .queryParam("source_id", SourceId_1)
                .when()
                .get("/image")
                .then()
                .log()
                .all()
                .statusCode(200)
                .statusLine("HTTP/1.1 200 OK")          //Status Line
                .contentType("application/json");

        String mediaUrl = getMediaURL(response2,"image");
        System.out.println("media_url: " + mediaUrl);

        String responseBody = response.toString();

        responseBody.contains("ContentType");
        responseBody.contains("OK");
        responseBody.contains("200");

        responseBody.contains("image");
        responseBody.contains("expires_at");
        responseBody.contains("clip_id");
        responseBody.contains("media_url");
        responseBody.contains("start");
        responseBody.contains("id");
        responseBody.contains("state");

        System.out.println(responseBody);

    }

    @Ignore
    @Title("GetAnImage with ImageId") //Record and pass ImageId. Covered in above test case
    @Test
    public void getAnImageById() {
        ValidatableResponse response = SerenityRest.given()
                .auth().oauth2(UserToken)
                .contentType("image/jpeg")
                .queryParam("source_id", SourceId_1)
                .queryParam("image_id", ImageId)
                .when()
                .get("/image")
                .then()
                .log()
                .all()
                .statusCode(200)
                // .extract()//Response status code
                .statusLine("HTTP/1.1 200 OK")          //Status Line
                .contentType("application/json");

        System.out.println("Image Details Retrieved");
    }

    @Title("GetAnImage InvalidAuth")
    @Test
    public void getAnImageInvalidAuth() {
        ValidatableResponse response = SerenityRest.given()
                .auth().oauth2(InvalidUserToken)
                .contentType("image/jpeg")
                .queryParam("source_id", SourceId_1)
                .when()
                .get("/image")
                .then()
                .log()
                .all()
                .statusCode(403);
    }

    @Title("GetAnImage Invalid SourceId")
    @Test
    public void getAnImageInvalidSourceId() {
        ValidatableResponse response = SerenityRest.given()
                .auth().oauth2(UserToken)
                .contentType("image/jpeg")
                .queryParam("source_id", InvalidSourceId)
                .when()
                .get("/image")
                .then()
                .log()
                .all()
                .statusCode(404);
    }

    @Title("GetAnImage InvalidImageId")
    @Test
    public void getAnImageInvalidImageId() {
        ValidatableResponse response = SerenityRest.given()
                .auth().oauth2(UserToken)
                .contentType("image/jpeg")
                .queryParam("source_id", SourceId_1)
                .queryParam("image_id", InvalidImageId)
                .when()
                .get("/image")
                .then()
                .log()
                .all()
                .statusCode(404);
    }

    @Title("Record Image")
    @Test
    public void postImageRecord() {
        SerenityRest.given()
                .auth().oauth2(UserToken)
                .contentType("application/x-www-form-urlencoded")
                .param("source_id", SourceId_1)
                .when()
                .post("/image/record")
                .then()
                .log()
                .all()
                .statusCode(201).and().time(lessThan(ResponseTime));
    }

    @Title("Record Image Offline Source")
    @Test
    public void postImageRecordOfflineSource() {
        SerenityRest.given()
                .auth().oauth2(UserToken)
                .contentType("application/x-www-form-urlencoded")
                .param("source_id", offlineSourceId)
                .when()
                .post("/image/record")
                .then()
                .log()
                .all()
                .statusCode(403).and().time(lessThan(ResponseTime));
    }

    @Title("Record Image Invalid Auth")
    @Test
    public void postImageInvalidAuth() {
        SerenityRest.given()
                .auth().oauth2(InvalidUserToken)
                .contentType("application/x-www-form-urlencoded")
                .param("source_id", SourceId_1)
                .when()
                .post("/image/record")
                .then()
                .log()
                .all()
                .statusCode(403);
    }

    @Title("Record Image Invalid SourceId")
    @Test
    public void postImageInvalidSourceId() {
        SerenityRest.given()
                .auth().oauth2(UserToken)
                .contentType("application/x-www-form-urlencoded")
                .param("source_id", InvalidSourceId)
                .when()
                .post("/image/record")
                .then()
                .log()
                .all()
                .statusCode(404);
    }

    @Title("RetrieveAnImage") // First Record and then pass mediaURL to retrieve
    @Test
    public void retrieveAnImage() throws InterruptedException {

        ValidatableResponse response = SerenityRest.given()
                .auth().oauth2(UserToken)
                .contentType("application/x-www-form-urlencoded")
                .param("source_id", SourceId_1)
                .when()
                .post("/image/record")
                .then()
                .log()
                .all()
                .statusCode(201);

        Thread.sleep(5000);
       String mediaURL =getImageMediaURL(response);
        System.out.println("Image MediaURL: " + mediaURL);

        ValidatableResponse getResponse = SerenityRest.given()
                .auth().oauth2(UserToken)
                .when()
                .get(mediaURL)
                .then()
                .log()
                .all()
                .statusCode(200)
                .and().contentType("image/jpeg");
    }

    @Title("RetrieveAnImage InvalidMediaURL")
    @Test
    public void retrieveAnImageInvalidMediaURL() {
        ValidatableResponse response = SerenityRest.given()
                .auth().oauth2(UserToken)
                .contentType("image/jpeg")
                .when()
                .get(InvalidImageMediaURL)
                .then()
                .log()
                .all()
                .statusCode(404);
    }

    @Title("Delete Image Invalid ImageId") // First record, then pass imageId to delete
    @Test
    public void deleteImage() throws InterruptedException {

        ValidatableResponse response = SerenityRest.given()
                .auth().oauth2(UserToken)
                .contentType("application/x-www-form-urlencoded")
                .param("source_id", SourceId_1)
                .when()
                .post("/image/record")
                .then()
                .log()
                .all()
                .statusCode(201)
                .contentType("application/json");

        String imageId = getId(response,"image");
        System.out.println("Image Id: " + imageId);

        Thread.sleep(2000);

        ValidatableResponse getResponse = SerenityRest.given()
                .auth().oauth2(UserToken)
                .contentType("image/jpeg")
                .when()
                .queryParam("source_id", SourceId_1)
                .queryParam("image_id",InvalidImageId)
                .delete("/image")
                .then()
                .log()
                .all()
                .statusCode(404);
    }

    @Title("Delete Image Invalid SourceId") // First Record and then pass imageId to delete
    @Test
    public void deleteImageInvalidSourceId() throws InterruptedException {

        ValidatableResponse getResponse = SerenityRest.given()
                .auth().oauth2(UserToken)
                .contentType("image/jpeg")
                .when()
                .queryParam("source_id", InvalidSourceId)
                .queryParam("image_id",ImageId)
                .delete("/image")
                .then()
                .log()
                .all()
                .statusCode(404);
    }

    @Title("Delete Image Invalid Auth") // First Record and then pass imageId to delete
    @Test
    public void deleteImageInvalidAuth() throws InterruptedException {

        ValidatableResponse getResponse = SerenityRest.given()
                .auth().oauth2(InvalidUserToken)
                .contentType("image/jpeg")
                .when()
                .queryParam("source_id", SourceId_1)
                .queryParam("image_id",ImageId)
                .delete("/image")
                .then()
                .log()
                .all()
                .statusCode(403);
    }


    @Title("RetrieveAnImage validate ResponseBody") // First Record and then pass mediaURL to retrieve
    @Test
    public void retrieveAnImageResponseFieldValidation() throws InterruptedException {

        ValidatableResponse response = SerenityRest.given()
                .auth().oauth2(UserToken)
                .contentType("application/x-www-form-urlencoded")
                .param("source_id", SourceId_1)
                .when()
                .post("/image/record")
                .then()
                .log()
                .all()
                .statusCode(201);

        Thread.sleep(5000);

        String imageId = getImageId(response);
        System.out.println("Image Id: " + imageId);

        ValidatableResponse getResponse = SerenityRest.given()
                .auth().oauth2(UserToken)
                .when()
                .contentType("image/jpeg")
                .queryParam("source_id", SourceId_1)
                .queryParam("image_id", imageId)
                .when()
                .get("/image")
                .then()
                .log()
                .all()
                .statusCode(200);

        String responseString = getResponseAsString(getResponse);

        assertThat(responseString.contains("id"));
        System.out.println("Response contains Image Id");
        assertThat(responseString.contains("media_url"));
        System.out.println("Response contains MediaURL");
        assertThat(responseString.contains("expires_at"));
        System.out.println("Response contains Image Expiration Date");
        assertThat(responseString.contains("clip_id"));
        System.out.println("Response contains clip_id");
        assertThat(responseString.contains("state"));
        System.out.println("Response contains Clip State");
    }

    @Title("Retrieve the most recent image")
    @Test
    public void retrieveAnImageInvalidSourceId() {
        ValidatableResponse response = SerenityRest.given()
                .auth().oauth2(UserToken)
                .contentType("image/jpeg")
                .queryParam("source_id", SourceId_1)
                .when()
                .get("/image")
                .then()
                .log()
                .all()
                .statusCode(200);

       String imageId = getId(response,"image");
       System.out.println("Image Id is " +imageId);

       String imageURL = getMediaURL(response, "image");
       System.out.println("Image URL is " +imageURL);

    }

}

