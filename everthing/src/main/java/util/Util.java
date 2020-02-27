package util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Creat with IntelliJ IDEA.
 * Description：
 * User:LiuBen
 * Date:2020-01-09
 * Time:10:18
 */
public class Util {

    public static final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private static final String[] SIZENAMES = {"B","KB","MB","GB"};
    private static  DateFormat DATE_FORMAT = new SimpleDateFormat(DATE_PATTERN);

    public static String parseSize(Long size) {
        int i = 0;
        for (; size >= 1024 && i <= 2 ; i++) {
            size = size/1024;
        }
        return size + SIZENAMES[i];
    }

    public static String parseDate(Long lastModified) {
        return DATE_FORMAT.format(new Date(lastModified));
    }

}
