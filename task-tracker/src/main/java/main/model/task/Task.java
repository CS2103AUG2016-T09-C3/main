package main.model.task;

import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Objects;

public abstract class Task implements ReadOnlyTask {
    private String message;
    Date deadline;
    Date startTime;
    Date endTime;
    private boolean isFloating;
    private boolean isEvent = false;
    private boolean isRecurring = false;

    public Task(){}
    
    public Task(String message) {
    	if(message == null){
    	    this.message = "";
    	}
    	else {
    	    this.message = message;
    	}
        this.isFloating = true; 
    }
    
    public Task(String message, Date deadline) {
    	if(message == null){
    		throw new IllegalArgumentException("Please fill in the required fields");
    	}
        this.message = message;
        this.deadline = deadline;
        this.isFloating = false;
    }
    
    public Task(String message, Date startTime, Date endTime) {
    	if(message == null){
    		throw new IllegalArgumentException("Please fill in the required fields");
    	}
        this.message = message;
        this.startTime = startTime;
        this.endTime = endTime;
        this.isFloating = false;
        this.isEvent = true;
    }
    
    public Task(ReadOnlyTask src) {
        this(src.getMessage());
        if (!src.getIsFloating()) {
            if (!src.getIsEvent()) 
                this.deadline = src.getDeadline();
            else {
                this.startTime = src.getStartTime();
                this.endTime = src.getEndTime();
            }
        }
    }
    //getters
    @Override
    public String getMessage(){
        return this.message;
    }
    @Override
    public Date getStartTime(){
    	return this.startTime;
    }
    
    public String getStartTimeString() {
		String dateString = "";
		DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm");
		dateString = df.format(this.startTime);
		return dateString; 
	}
    
    @Override
    public Date getEndTime(){
    	return this.endTime;
    }
    
    public String getEndTimeString() {
		String dateString = "";
		DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm");
		dateString = df.format(this.endTime);
		return dateString;
	}
    
    @Override
    public Date getDeadline(){
    	return this.deadline;
    }
    
    public String getDeadlineString() {
		String dateString = "";
		DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm");
		dateString = df.format(this.deadline);
		return dateString;	
	}
    @Override
    public boolean getIsFloating(){
    	return this.isFloating;
    }
    
    @Override
    public boolean getIsEvent(){
        return this.isEvent;
    }
    
    //setters
    public void setMessage(String message){
    	this.message = message;
    }
    public void setStartTime(Date startTime){
    	this.startTime = startTime;
    } 
    
    public void setEndTime(Date endTime){
    	this.endTime = endTime;
    }
       
    public void setDeadline(Date deadline){
    	this.deadline = deadline;
    }  
    
    public void setIsFloating(boolean isFloating){
    	this.isFloating = isFloating;
    }
    
    public void setIsRecurring(boolean isRecurring){
    	this.isRecurring = true;
    }
     
    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        else if (other instanceof Task) {
        	if(this.isFloating){ 
        		return (this.message.equals(((Task) other).message));
        	}
        	else if(this.isEvent) {
        	    return (this.message.equals(((Task) other).message)
        	 	&& this.startTime.equals(((Task) other).startTime)
        		&& this.endTime.equals(((Task) other).endTime));
        	}
        	else {
                return (this.message.equals(((Task) other).message)
                && this.deadline.equals(((Task) other).deadline));
        	}
        }
        else return false;
    }
       
    @Override
    public int hashCode() {
        return Objects.hash(message);
    }
    
    @Override
    public String toString() {
    	if(this.isFloating){
    		return  getMessage(); 
    	}
    	else if(this.isEvent){
    		return  getMessage()+ " from " + getStartTimeString() + " to "
    					+ getEndTimeString();
    	}
    	else{
    		return  getMessage() + " due by " + getDeadlineString();
    	}
    }


}
