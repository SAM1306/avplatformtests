
package com.smartthings.avplatform.api;

import com.google.gson.JsonObject;
import io.restassured.response.ValidatableResponse;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.serenitybdd.rest.SerenityRest;
import net.thucydides.core.annotations.Title;
import org.json.simple.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.lessThan;

@RunWith(SerenityRunner.class)
public class Sources extends Properties {

    @Title("List all sources for the given user")
    @Test
    public void getAllSources() {
        SerenityRest.given()
                .auth().oauth2(UserToken)
                .contentType("application/x-www-form-urlencoded")
                .when()
                .get("/sources")
                .then()
                .log()
                .all()
                .statusCode(200);
    }

    @Title("Get A Source By Id")
    @Test
    public void getASource() {
        SerenityRest.given()
                .auth().oauth2(UserToken)
                .contentType("application/x-www-form-urlencoded")
                .queryParam("source_id", SourceId_1)
                .when()
                .get("/source")
                .then()
                .log()
                .all()
                .statusCode(200);
    }

    @Title("Get A Source with InvalidAuth")
    @Test
    public void getASourceInvalidAuth() {
        SerenityRest.given()
                .auth().oauth2(InvalidUserToken)
                .contentType("application/x-www-form-urlencoded")
                .queryParam("source_id", SourceId_1)
                .when()
                .get("/source")
                .then()
                .log()
                .all()
                .statusCode(403);
    }

    @Title("Get A Source with Invalid SourceId")
    @Test
    public void getASourceInvalidSourceId() {
        SerenityRest.given()
                .auth().oauth2(UserToken)
                .contentType("application/x-www-form-urlencoded")
                .queryParam("source_id", InvalidSourceId)
                .when()
                .get("/source")
                .then()
                .log()
                .all()
                .statusCode(404);
    }

    @Title("Update A Source")
    @Test
    public void updateSource() {

        JSONObject jsonMap = new JSONObject();
        jsonMap.put("name", SourceName);

        ValidatableResponse updateResponse = SerenityRest.given()
                .auth().oauth2(UserToken)
                .contentType("application/json")
                .queryParam("source_id", SourceId_2)
                .body(jsonMap.toJSONString())
                .when()
                .patch("/source")
                .then()
                .log()
                .all()
                .statusCode(200)
                .and().time(lessThan(ResponseTime));

        JsonObject sourceObject = getResponseObject(updateResponse, "source");
        String name = sourceObject.get("name").getAsString();
        System.out.println("Updated Name: " + name);

    }
}


