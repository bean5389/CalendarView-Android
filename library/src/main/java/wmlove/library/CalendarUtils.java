package wmlove.library;

import java.util.Calendar;

/**
 * Created by Administrator on 2015/6/17.
 */
public class CalendarUtils {
    public static Calendar getCalenar(){
        return Calendar.getInstance();
    }

    public static void setToFirstDay(Calendar calendar){
        calendar.set(Calendar.DAY_OF_MONTH,1);
        calendar.getTimeInMillis();
    }
}
