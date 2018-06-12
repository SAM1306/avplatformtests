package com.smartthings.avplatform.api;

import com.smartthings.avplatform.testbase.TestBase;
import io.restassured.response.ValidatableResponse;
import net.serenitybdd.rest.SerenityRest;
import net.thucydides.core.annotations.Step;

public class SerenitySteps extends TestBase {

    @Step
    public ValidatableResponse recordImage(final String userToken,final String contentType, final String sourceId){

        return   SerenityRest.given()
                .auth().oauth2(userToken)
                .contentType(contentType)
                .param("source_id", sourceId)
                .when()
                .post("/image/record")
                .then();
    }
}


