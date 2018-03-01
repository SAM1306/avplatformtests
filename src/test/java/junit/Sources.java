package junit;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.restassured.RestAssured;
import io.restassured.authentication.OAuthSignature;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.serenitybdd.rest.SerenityRest;
import net.thucydides.core.annotations.Manual;
import net.thucydides.core.annotations.Pending;
import net.thucydides.core.annotations.Title;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import testbase.TestBase;

import static org.hamcrest.Matchers.lessThan;

@RunWith(SerenityRunner.class)
public class Sources {


    static String UserToken = "aff6e157-f874-4087-93da-a40b54a7bbe1";
    static String SourceId = "17c9f3cf-973e-4e59-b6cc-61ff20b3d4c3";
    static String offlineSourceId = "ec31e3fa-4609-4c19-9263-000446729196";

    String InvalidUserToken = "affbffcff";
    String InvalidSourceId = "17c9f33d4c3";


    @BeforeClass
    public static void init(){

        RestAssured.baseURI="https://api.s.st-av.net/v1";
    }

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
    public void getASource(){
        SerenityRest.given()
                .auth().oauth2(UserToken)
                .contentType("application/x-www-form-urlencoded")
                .queryParam("source_id", SourceId)
                .when()
                .get("/source")
                .then()
                .log()
                .all()
                .statusCode(200);

    }

    @Title("Get A Source with InvalidAuth")
    @Test
    public void getASourceInvalidAuth(){
        SerenityRest.given()
                .auth().oauth2(InvalidUserToken)
                .contentType("application/x-www-form-urlencoded")
                .queryParam("source_id", SourceId)
                .when()
                .get("/source")
                .then()
                .log()
                .all()
                .statusCode(403);
    };

    @Title("Get A Source with Invalid SourceId")
    @Test
    public void getASourceInvalidSourceId(){
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

       //TODO Add response error message validation
    }

  //TODO fix patch request
    @Title("Update A Source")
    @Test
    public void updateSource() {
        ValidatableResponse response = SerenityRest.given()
                .auth().oauth2(UserToken)
                .contentType("application/json")
                .queryParam("source_id", offlineSourceId)
                .formParam("name", "Rear Door")
                .when()
                .patch("/source")
                .then()
                .log()
                .all()
                .statusCode(200)
                .and().time(lessThan(1000L));

        String responseStr = response.extract().body().asString();

        Gson gson = new Gson(); //TODO Fix
        JsonParser jp = new JsonParser();
        JsonElement je = jp.parse(responseStr);
        JsonObject responseBodyObject = je.getAsJsonObject();

        JsonElement source = responseBodyObject.get("source");
        JsonObject sourceObject = source.getAsJsonObject();

        String name = sourceObject.get("name").getAsString();
        System.out.println("Updated Name: " + name);

      //  if(name.equals("Front Door"))
       //     System.out.println("SourceName succesfully updated");

    }
/*
    @Ignore
    @Test
    public void getAXYZ(){

    }
    @Pending
    @Test
    public void getASourceInvalidAuth(){
        //
    };

    @Manual
    @Test
    public void getASourceInvalidSourceId(){
        //
    };

*/




}
