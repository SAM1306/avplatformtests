package com.smartthings.avplatform.testbase;

import io.restassured.RestAssured;
import org.junit.BeforeClass;

public class TestBase {

    @BeforeClass
    public static void init(){

        RestAssured.baseURI = "https://api.s.st-av.net/v1";
    }
}
