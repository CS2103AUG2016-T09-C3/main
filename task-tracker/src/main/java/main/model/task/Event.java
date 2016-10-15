package main.model.task;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Event extends Deadline {
	private Date startDate;
	
	public Event(){
		
	}
	
	public Event(String message, Date startDate, Date deadline) {
		super(message, deadline);
		
    	if(message == null){
    		throw new IllegalArgumentException("Please fill in the required fields");
    	}
        this.startDate = startDate;
        this.setIsFloating(false);
        this.setIsEvent(true);
        this.setPriority(PriorityType.NORMAL);
	    this.setIsRecurring(false);   
    }
	
	//getters
    public Date getStartDate(){
    	return this.startDate;
    }
    
    public String getStartDateString() {
		String dateString = "";
		DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm");
		dateString = df.format(this.startDate);
		return dateString; 
	}
	//setters
    public void setStartDate(Date startDate){
    	this.startDate = startDate;
    } 
    
    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        else if (other instanceof Event) {
        	 return (this.getStartDate().equals(((Event) other).getStartDate()));
        }
        return false;
    }
    
    @Override
    public String toString() {
    		return  getMessage()+ " from " + getStartDateString() + " to "
					+ getDeadline();
	}
}
