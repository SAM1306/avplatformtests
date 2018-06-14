package com.smartthings.avplatform.api;


import io.restassured.response.ValidatableResponse;
import net.serenitybdd.rest.SerenityRest;
import net.thucydides.core.annotations.Title;
import org.junit.Test;


public class Location extends Properties{


    //TODO Add Get Location, Patch Location

   /* @Title("Record Image with ReusableMethods and Steps")
    @Test
    public void postImageRecordSteps() {
  ValidatableResponse response = steps.recordImage(UserToken,ContentType,SourceId2)
                .statusCode(201)
                .statusLine("HTTP/1.1 201 Created");


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

      String responseStr = response.extract().body().asString();

        Gson gson = new Gson(); //TODO Fix
        JsonParser jp = new JsonParser();
        JsonElement je = jp.parse(responseStr);
        JsonObject responseBodyObject = je.getAsJsonObject();

        JsonElement image = responseBodyObject.get("image");
        JsonObject imageObject = image.getAsJsonObject();

        String imageId = imageObject.get("id").getAsString();
        System.out.println("Image Id: " + imageId);

        String responseBody = response.toString();

        responseBody.contains("ContentType");
        responseBody.contains("OK");
        responseBody.contains("201");

        responseBody.contains("image");
        responseBody.contains("expires_at");
        responseBody.contains("clip_id");
        responseBody.contains("media_url");
        responseBody.contains("start");
        responseBody.contains("id");
        responseBody.contains("state");

       // System.out.println(responseBody);
    }*/

}
