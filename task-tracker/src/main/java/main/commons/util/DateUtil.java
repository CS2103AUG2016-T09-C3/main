//@@author A0144132W
package main.commons.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
    
    /**
     * convenient constructor for Date, with time
     * 
     * @returns a Date object
     */
    public static Date getDate(int year, int month, int day, int hour, int min){
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, day, hour, min);
        return cal.getTime();
    }

    /**
     * convenient constructor for Date, without time
     * 
     * @returns a Date object
     */
    public static Date getDate(int year, int month, int day){
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, day);
        return cal.getTime();
    }
    
    /**
     * @returns today's date
     */
    public static Date getToday(){
        Calendar cal = Calendar.getInstance();
        return setTime(cal.getTime(), 8, true);
    }
    
    public static Date getTmr() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getToday());
        cal.add(Calendar.DATE, 1);
        return cal.getTime();
    }
     
    /**
     * @returns true if the given date overlaps with the given the start and end date
     */
    public static boolean dateOverlap(Date start, Date end, Date date) {
        if (start == null || end == null || date == null) {
            return false;
        }
        
        return ((start.before(date) && end.after(date)) 
                || (areSameDay(start, date) && areSameDay(end, date)));

    }
    
    /**
     * compares the 2 dates, not including the time
     * 
     * @returns true if both dates are on the same day
     */
    public static boolean areSameDay(Date date1, Date date2) {
        if (date1 == null | date2 == null) {
            return false;
        }
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        
        if ((cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)) 
            && (cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH)) 
            && (cal1.get(Calendar.DATE) == cal2.get(Calendar.DATE))) {
            return true;
        }
        else return false;
    }
    
    
    /**
     * @returns a date with a nice readable format
     */
    public static String readableDate(Date date, boolean isInferred) {
        DateFormat df;
        if (isInferred) {
            df = new SimpleDateFormat("dd MMM");
        }
        else {
            df = new SimpleDateFormat("dd MMM h:mm a");            
        }
        return df.format(date);
    }
    
    /**
     * takes isInferred as true by default
     * 
     * @returns a a date with a nice readable format, without time
     */
    public static String readableDate(Date date) {
        return readableDate(date, true);
    }
    
    /**
     * changes the time to the hour given (in 24 hours format) and
     * resets minutes, depending on the boolean input
     *       
     * @returns the original date with given time
     */
    public static Date setTime(Date date, int hour, boolean resetMinutes) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, hour);
        if (resetMinutes) {
            cal.clear(Calendar.MINUTE);
            cal.clear(Calendar.SECOND);
            cal.clear(Calendar.MILLISECOND);
        }
        return cal.getTime();
    }
    //@@author A0139750B
    //method to return a date with no time
    public static Date removeTime(Date date){
    	Calendar cal = Calendar.getInstance();
    	cal.setTime(date);
    	cal.clear(Calendar.HOUR_OF_DAY);
    	cal.clear(Calendar.MINUTE);
    	cal.clear(Calendar.SECOND);
    	cal.clear(Calendar.MILLISECOND);
    	return cal.getTime();
    }
    
    //Check if the task date is overdue
    public static boolean checkOverdue(Date taskDate, boolean hasNoTime){
    	Date current = new Date();
    	
    	if(hasNoTime){
    		removeTime(taskDate);
    		removeTime(current);
    	}
    	if(taskDate.before(current)){
    		return true;
    	}
    	else{
    		return false;
    	}
    }
    //@@author A0144132W
    public static Date defaultTime(Date date) {
        return setTime(date, 8, true);
    }
}

