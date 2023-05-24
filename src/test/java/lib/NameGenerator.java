package lib;

import java.util.Random;

public class NameGenerator {
    public static String getShortName(){
        String abc = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        Random r = new Random();
        char name = abc.charAt(r.nextInt(abc.length()));
        return String.valueOf(name);
    }

    public static String getLongName(){
        String abc = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        String longName = "";
        Random r = new Random();


        while (longName.length()<251){
            char name = abc.charAt(r.nextInt(abc.length()));
            longName = longName + name;
        }

        System.out.println(longName);
        return longName;
    }
}
