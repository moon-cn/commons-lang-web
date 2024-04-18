package cn.moon.lang;


import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormatTool {

    public static String format(Date date){
        return format(date,"yyyy-MM-dd HH:mm:ss");
    }

    public static String formatDay(Date date){
        return format(date,"yyyy-MM-dd");
    }
    public static String formatDayCn(Date date){
        return format(date,"yyyy年M月d日");
    }

    public static String format(Date date,String fmt){
        if(date == null){
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat( fmt);
        return sdf.format(date);
    }

}
