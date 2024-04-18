package cn.moon.lang;


import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormatTool {

    public static String format(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }

    public static String formatDay(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd");

        return sdf.format(date);
    }
    public static String formatDayCn(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年M月d日");
        return sdf.format(date);
    }


}
