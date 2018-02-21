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

    static String UserToken = "aff6e157-f874-4087-93da-a40b54a7bbe1";
    static String SourceId = "17c9f3cf-973e-4e59-b6cc-61ff20b3d4c3";
    String mediaURL = "";

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

    @Title("Record Another Image")
    @Test
    public void postImageAnotherRecord() {
     ValidatableResponse response =  SerenityRest.given()
                .auth().oauth2(UserToken)
                .contentType("application/x-www-form-urlencoded")
                .param("source_id", SourceId)
                .when()
                .post("/image/record")
                .then()
                .log()
                .all();

// Retrieve the body of the Response
        String responseBody = response.toString();
        responseBody.contains("ContentType");

      //  .assertThat(body);

    }

    @Title("GetAnImage")
    @Test
    public void getAnImage() {
        ValidatableResponse response =    SerenityRest.given()
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

        ValidatableResponse response2 =    SerenityRest.given()
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

        //validating response status code
      // int statusCode = response.getStatusCode();

       // double timeInMs = response.time;


        String responseStr = response.extract().body().asString();

        //System.out.println("printing response: "+responseStr);

        Gson gson = new Gson();
        JsonParser jp = new JsonParser();
        JsonElement je = jp.parse(responseStr);
        JsonObject responseBodyObject = je.getAsJsonObject();
        JsonElement image = responseBodyObject.get("image");
        JsonObject imageObject = image.getAsJsonObject();
        String mediaUrl = imageObject.get("media_url").getAsString();
        System.out.println("media_url: "+mediaUrl);


        String responseBody = response.toString();
        responseBody.contains("ContentType");
        responseBody.contains("OK");
        responseBody.contains("200");

        //  responseBody.contains( );
        responseBody.contains("image");
        responseBody.contains("expires_at");
        responseBody.contains("clip_id");
        responseBody.contains("media_url");
        responseBody.contains("start");
        responseBody.contains("id");
        responseBody.contains("state");

        System.out.println(responseBody);

      //  JsonPath jsonPath = new JsonPath(responseBody);
      //  String mediaUrl = jsonPath.getString("images.media_url");
        //System.out.println(mediaUrl);

    }
}
