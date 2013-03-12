package checkers.gui.controll.play.entity;

import java.util.Calendar;
import java.util.Date;

public class DateInfo {

    private Calendar calendar;
    
    public DateInfo(Date date) {
        calendar = Calendar.getInstance();
        calendar.setTime(date);
    }
    
    public int getYear() {
        return calendar.get(Calendar.YEAR);
    }
    
    public int getMonth() {
        return calendar.get(Calendar.MONTH);
    }
    
    public int getDay() {
        return calendar.get(Calendar.DATE);
    }
    
}