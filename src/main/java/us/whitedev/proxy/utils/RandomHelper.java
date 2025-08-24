package us.whitedev.proxy.utils;

import java.util.Random;

public class RandomHelper {
    private final Random random = new Random();
    private static RandomHelper randomHelper;

    public static RandomHelper getRandomHelper(){
        if(randomHelper == null) randomHelper = new RandomHelper();
        return randomHelper;
    }

    public String getRandomChar(boolean upperCase) {
        char randomChar = (char) ('a' + random.nextInt(26));
        return upperCase ? String.valueOf(Character.toUpperCase(randomChar)) : String.valueOf(randomChar);
    }

    public String getRandomString(int length, boolean UpperCase) {
        StringBuilder randomString = new StringBuilder();

        for (int i = 0; i <= length - 1; ++i) {
            randomString.append(this.getRandomChar(UpperCase));
        }

        return randomString.toString();
    }

    public int getRandomInt(int min, int max) {
        return (int) ((double) min + Math.random() * (double) (max - min + 1));
    }
}
