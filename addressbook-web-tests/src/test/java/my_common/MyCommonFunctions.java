package my_common;

import java.io.File;
import java.nio.file.Paths;
import java.util.Random;

public class MyCommonFunctions {

    public static String randomString(int length) {
        var my_rnd = new Random();
        var my_result = "";
        for (int i = 0; i < length; i++) {
            my_result += (char) ('a' + my_rnd.nextInt(26));
        }
        return my_result;
    }

    public static String randomNumber(int length) {
        var my_rnd = new Random();
        var my_result = "";
        my_result += (char) ('1' + my_rnd.nextInt(8));
        for (int i = 1; i < length; i++) {
            my_result += (char) ('0' + my_rnd.nextInt(9));
        }
        return my_result;
    }

    public static String randomFile(String dir) {
        var my_fileNames = new File(dir).list();
        var my_rnd = new Random();
        var my_index = my_rnd.nextInt(my_fileNames.length);
        var my_result = Paths.get(dir, my_fileNames[my_index]).toString();
        return my_result;
    }
}
