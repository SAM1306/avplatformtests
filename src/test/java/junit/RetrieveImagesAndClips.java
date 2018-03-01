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
public class RetrieveFromAV {
    static String UserToken = "aff6e157-f874-4087-93da-a40b54a7bbe1";
    static String SourceId = "17c9f3cf-973e-4e59-b6cc-61ff20b3d4c3";
    String mediaUrl ="https://mediaserv.media11.ec2.st-av.net/image?source_id=17c9f3cf-973e-4e59-b6cc-61ff20b3d4c3&image_id=imcL-Pn4ASBI7vO6viw5I";

    @BeforeClass
    public static void init() {

        RestAssured.baseURI = "mediaUrl";
        //    String UserToken = "aff6e157-f874-4087-93da-a40b54a7bbe1";
        //   String SourceId = "17c9f3cf-973e-4e59-b6cc-61ff20b3d4c3";

    }
    @Title("RetrieveAnImage")
    @Test
    public void retrieveAnImage() {
        ValidatableResponse response = SerenityRest.given()
                .auth().oauth2(UserToken)
                .contentType("image/jpeg")
                .when()
                .get(mediaUrl)
                .then()
                .log()
                .all()
                .statusCode(200);


    }
}
