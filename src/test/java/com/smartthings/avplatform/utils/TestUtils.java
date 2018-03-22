package com.smartthings.avplatform.utils;

import java.util.Random;
import java.util.UUID;

public class TestUtils {

    public static String getRandomValue(){

        Random random = new Random();
        int randomInt = random.nextInt(1000000);
        return Integer.toString(randomInt);
    }

   // public static String getRandomUUID() {
   //     UUID ZoneName = UUID.randomUUID();
   //     return Integer.toString(ZoneName);
   // }
}
