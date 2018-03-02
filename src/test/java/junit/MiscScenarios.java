
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

public class MiscScenarios {


        static String UserToken = "aff6e157-f874-4087-93da-a40b54a7bbe1";
        static String SourceId = "17c9f3cf-973e-4e59-b6cc-61ff20b3d4c3";
        static String offlineSourceId = "ec31e3fa-4609-4c19-9263-000446729196";
        static String InvalidUserToken = "aff6e157-bbe1";

        String mediaUrl = "";
        static String StreamID = "TyauHPKdbRbi9MwkmoezS";

        @BeforeClass
        public static void init() {

            RestAssured.baseURI = "https://api.s.st-av.net/v1";
        }

        @Title("Request Live Stream, Record Clip Simulataneously")
        @Test
        public void postStreamRecordClip() {
            ValidatableResponse response = SerenityRest.given()
                    .auth().oauth2(UserToken)
                    .contentType("application/x-www-form-urlencoded")
                    .param("source_id", SourceId)
                    .when()
                    .post("/stream")
                    .then()
                    .log()
                    .all()
                    .time(lessThan(1000L));

            String responseStr = response.extract().body().asString();

            //System.out.println("printing response: "+responseStr);

            Gson gson = new Gson();
            JsonParser jp = new JsonParser();
            JsonElement je = jp.parse(responseStr);
            JsonObject responseBodyObject = je.getAsJsonObject();

            JsonElement stream = responseBodyObject.get("stream");
            JsonObject streamObject = stream.getAsJsonObject();

            String gatewayURL = streamObject.get("gateway_url").getAsString();
            System.out.println("RTSP GatewayURL: " + gatewayURL);

            String running = streamObject.get("running").getAsString();
            System.out.println("Running: " + running);
            running.equals("false");

            String streamID = streamObject.get("id").getAsString();
            System.out.println("StreamID: " + streamID);

            SerenityRest.given()
                    .auth().oauth2(UserToken)
                    .contentType("application/x-www-form-urlencoded")
                    .param("stream_id", streamID)
                    .when()
                    .get("/stream")
                    .then()
                    .log()
                    .all()
                    .statusCode(200)
                    .time(lessThan(1000L));
            
            System.out.println("LiveStream Requested");

            //Record
            ValidatableResponse clipResponse = SerenityRest.given()
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
                    .time(lessThan(1000L));

            System.out.println("Clip Record Requested");


        }
}
