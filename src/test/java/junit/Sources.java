package junit;

import io.restassured.RestAssured;
import io.restassured.authentication.OAuthSignature;
import io.restassured.response.Response;
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

@RunWith(SerenityRunner.class)
public class Sources {

    @BeforeClass
    public static void init(){

        RestAssured.baseURI="https://api.s.st-av.net/v1";
//        String UserToken = "aff6e157-f874-4087-93da-a40b54a7bbe1";
    }

    @Title("List all sources for the given user")
    @Test
    public void getAllSources() {
        SerenityRest.given()
                .auth().oauth2("aff6e157-f874-4087-93da-a40b54a7bbe1")
                //          .auth().oauth2("aff6e157-f874-4087-93da-a40b54a7bbe1")
                .contentType("application/x-www-form-urlencoded")
                .when()
                .get("/sources")
                .then()
                .log()
                .all()
                .statusCode(200);
    }

    @Ignore
    @Test
    public void getASource(){
           //
    };
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






}
