package com.smartthings.avplatform.testbase;

import io.restassured.RestAssured;
import net.serenitybdd.junit.runners.SerenityRunner;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;

//@RunWith(SerenityRunner.class)
public class TestBase {

   /* @BeforeClass
    public static void init(){

        RestAssured.baseURI = "https://api.s.st-av.net/v1";

         String UserToken = "aff6e157-f874-4087-93da-a40b54a7bbe1";
         String SourceId = "17c9f3cf-973e-4e59-b6cc-61ff20b3d4c3";
         String offlineSourceId = "ec31e3fa-4609-4c19-9263-000446729196";
         String ImageId ="SZ-pKIbYS4oMDgKc0Y2nI";
         String ImageMediaURL= "https://mediaserv.media11.ec2.st-av.net/image?source_id=17c9f3cf-973e-4e59-b6cc-61ff20b3d4c3&image_id=imcL-Pn4ASBI7vO6viw5I";

         String InvalidImageMediaURL ="https://mediaserv.media11.ec2.st-av.net/image?source_id=17c9f3cb6cc-61ff20b3d4c3&image_id=imcL-Pn4ASBI7vO6viw5I";
         String InvalidImageId ="SZ-pKIbYSKc0Y2nI";

         String ContentType="application/x-www-form-urlencoded";
         Long ResponseTime = 10000L;

        String InvalidUserToken = "affbffcff";
        String InvalidSourceId = "17c9f33d4c3";
    }*/

    static String UserToken = "aff6e157-f874-4087-93da-a40b54a7bbe1";
    static String SourceId_1 = "17c9f3cf-973e-4e59-b6cc-61ff20b3d4c3";
    static String SourceId_2 = "12630f5c-b852-4f7b-ac7c-bdd410b44b0f";

    static String offlineSourceId = "ec31e3fa-4609-4c19-9263-000446729196";
    static String InvalidUserToken = "aff6e157-bbe1";

    String mediaUrl = "";
    static String StreamID = "TyauHPKdbRbi9MwkmoezS";

    @BeforeClass
    public static void init() {

        RestAssured.baseURI = "https://api.s.st-av.net/v1";
    }

}
