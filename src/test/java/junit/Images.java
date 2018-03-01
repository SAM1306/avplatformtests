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
public class Images {

    String mediaUrl = "";

    static String UserToken = "aff6e157-f874-4087-93da-a40b54a7bbe1";
    static String SourceId = "17c9f3cf-973e-4e59-b6cc-61ff20b3d4c3";
    static String offlineSourceId = "ec31e3fa-4609-4c19-9263-000446729196";
   // static String ClipID = "2f4nKNN0P9AiOVlW9qURJ";
   // static String InvalidClipID = "2f4nKNN0VlW9qURJ";
    static String ImageId ="SZ-pKIbYS4oMDgKc0Y2nI";
    static String ImageMediaURL= "https://mediaserv.media11.ec2.st-av.net/image?source_id=17c9f3cf-973e-4e59-b6cc-61ff20b3d4c3&image_id=imcL-Pn4ASBI7vO6viw5I";

    static String InvalidImageMediaURL ="https://mediaserv.media11.ec2.st-av.net/image?source_id=17c9f3cb6cc-61ff20b3d4c3&image_id=imcL-Pn4ASBI7vO6viw5I";
    String InvalidUserToken = "affbffcff";
    String InvalidSourceId = "17c9f33d4c3";

    @BeforeClass
    public static void init() {

        RestAssured.baseURI = "https://api.s.st-av.net/v1";
        //    String UserToken = "aff6e157-f874-4087-93da-a40b54a7bbe1";
        //   String SourceId = "17c9f3cf-973e-4e59-b6cc-61ff20b3d4c3";
    }

    @Title("List all Images for the given source")
    @Test
    public void getAllImages() {
        SerenityRest.given()
                .auth().oauth2(UserToken)
                .contentType("image/jpeg")
                .queryParam("source_id", SourceId)
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
                .queryParam("source_id", SourceId)
                .when()
                .get("/images")
                .then()
                .log()
                .all()
                .statusCode(403);
    }


    @Title("Record, Get, Delete and Get Deleted Image")
    @Test
    public void postGetDeleteImage() {
        ValidatableResponse response = SerenityRest.given()
                .auth().oauth2(UserToken)
                .contentType("application/x-www-form-urlencoded")
                .param("source_id", SourceId)
                .when()
                .post("/image/record")
                .then()
                .log()
                .all()
                .statusCode(201).and().time(lessThan(1000L));

        String responseStr = response.extract().body().asString();
        Gson gson = new Gson();
        JsonParser jp = new JsonParser();
        JsonElement je = jp.parse(responseStr);
        JsonObject responseBodyObject = je.getAsJsonObject();

        JsonElement image = responseBodyObject.get("image");
        JsonObject imageObject = image.getAsJsonObject();
        String imageID = imageObject.get("id").getAsString();

        System.out.println("Image record requested");
        System.out.println("ImageID: " + imageID);


// Retrieve the body of the Response
        String responseBody = response.toString();
        responseBody.contains("ContentType");

        //  .assertThat(body);

    }

    @Title("GetAnImage") // Retrieves details of most recent recorded image
    @Test
    public void getAnImage() {
        ValidatableResponse response = SerenityRest.given()
                .auth().oauth2(UserToken)
                .contentType("image/jpeg")
                .queryParam("source_id", SourceId)
                .when()
                .get("/image")
                .then()
                .log()
                .all()
                .statusCode(200)
                // .extract()//Response status code
                .statusLine("HTTP/1.1 200 OK")          //Status Line
                .contentType("application/json");
        //Header

        // String responseString = response.extract().toString();
        // System.out.println(responseString);

        ValidatableResponse response2 = SerenityRest.given()
                .auth().oauth2(UserToken)
                .contentType("image/jpeg")
                .queryParam("source_id", SourceId)
                //   .queryParam("image_id", ImageId)
                .when()
                .get("/image")
                .then()
                .log()
                .all()
                .statusCode(200)
                // .extract()//Response status code
                .statusLine("HTTP/1.1 200 OK")          //Status Line
                .contentType("application/json");

        String responseStr = response.extract().body().asString();

        //System.out.println("printing response: "+responseStr);

        Gson gson = new Gson();
        JsonParser jp = new JsonParser();
        JsonElement je = jp.parse(responseStr);
        JsonObject responseBodyObject = je.getAsJsonObject();
        JsonElement image = responseBodyObject.get("image");
        JsonObject imageObject = image.getAsJsonObject();
        String mediaUrl = imageObject.get("media_url").getAsString();
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

    @Title("GetAnImage with ImageId")
    @Test
    public void getAnImageById() {
        ValidatableResponse response = SerenityRest.given()
                .auth().oauth2(UserToken)
                .contentType("image/jpeg")
                .queryParam("source_id", SourceId)
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
                .queryParam("source_id", SourceId)
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
                // .extract()//Response status code
    }
        //Status Line


    @Title("Record Image")
    @Test
    public void postImageRecord() {
        SerenityRest.given()
                .auth().oauth2(UserToken)
                .contentType("application/x-www-form-urlencoded")
                .param("source_id", SourceId)
                .when()
                .post("/image/record")
                .then()
                .log()
                .all()
                .statusCode(201).and().time(lessThan(1000L));
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
                .statusCode(403).and().time(lessThan(1000L));
    }

    @Title("Record Image Invalid Auth")
    @Test
    public void postImageInvalidAuth() {
        SerenityRest.given()
                .auth().oauth2(InvalidUserToken)
                .contentType("application/x-www-form-urlencoded")
                .param("source_id", SourceId)
                .when()
                .post("/image/record")
                .then()
                .log()
                .all()
                .statusCode(403).and().time(lessThan(1000L));
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
                .statusCode(404).and().time(lessThan(1000L));
    }


    @Title("RetrieveAnImage") // First Record and then pass mediaURL to retrieve
    @Test
    public void retrieveAnImage() throws InterruptedException {

        ValidatableResponse response = SerenityRest.given()
                .auth().oauth2(UserToken)
                .contentType("application/x-www-form-urlencoded")
                .param("source_id", SourceId)
                .when()
                .post("/image/record")
                .then()
                .log()
                .all()
                .statusCode(201).and().time(lessThan(1000L));

        String responseStr = response.extract().body().asString();

        Gson gson = new Gson(); //TODO Fix
        JsonParser jp = new JsonParser();
        JsonElement je = jp.parse(responseStr);
        JsonObject responseBodyObject = je.getAsJsonObject();

        JsonElement image = responseBodyObject.get("image");
        JsonObject imageObject = image.getAsJsonObject();

        String mediaURL = imageObject.get("media_url").getAsString();
        System.out.println("Image MediaURL: " + mediaURL);

        Thread.sleep(2000);

        ValidatableResponse getResponse = SerenityRest.given()
                .auth().oauth2(UserToken)
                .contentType("image/jpeg")
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

}
