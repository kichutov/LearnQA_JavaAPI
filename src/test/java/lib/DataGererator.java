package lib;

import java.text.SimpleDateFormat;

public class DataGererator {
    public static String getRandomEmail(){
        String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date());
        return "learnqa"+timestamp+"@example.com";
    }

    public static String getUncorrectRandomEmail(){
        String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date());
        return "learnqa"+timestamp+"example.com";
    }
}
