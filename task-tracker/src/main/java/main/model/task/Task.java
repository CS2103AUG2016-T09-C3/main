package main.model.task;

import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Objects;

import main.commons.util.DateUtil;

public class Task implements ReadOnlyTask {
    private String message;
    Date deadline;
    Date startTime;
    Date endTime;
    private boolean isFloating;
    private boolean isDeadline;
    private boolean isEvent = false;
    private boolean isRecurring = false;
    private PriorityType priority = PriorityType.NORMAL; //default priority
    //TO-DO
    //ADD RECURRING VARIABLES N METHODS(DAILY, WEEKLY, MONTHLY)
    //private boolean isUpdated; to update the frequency if isRecurring == true
    //private boolean isDone;
    //private boolean isCompleted; 
    //private String frequency;
    public Task(String message) {
    	if(message == null){
//    		throw new IllegalArgumentException("Please fill in the required fields");
    	    this.message = "";
    	}
    	else {
    	    this.message = message;
    	}
        this.isFloating = true; 
        this.isDeadline = false;
    }
    
    public Task(String message, PriorityType priority) {
        if(message == null){
//          throw new IllegalArgumentException("Please fill in the required fields");
            this.message = "";
        }
        else {
            this.message = message;
        }
        this.priority = priority;
        this.isFloating = true; 
        this.isDeadline = false;
    }
    
    public Task(String message, Date deadline) {
    	if(message == null){
    		throw new IllegalArgumentException("Please fill in the required fields");
    	}
        this.message = message;
        this.deadline = deadline;
        this.isFloating = false;
        this.isDeadline = true;
       
    }
    public Task(String message, Date deadline, PriorityType priority) {
        if(message == null){
            throw new IllegalArgumentException("Please fill in the required fields");
        }
        this.message = message;
        this.deadline = deadline;
        this.isFloating = false;
        this.isDeadline = true;
        this.priority=priority;
       
    }
    
    public Task(String message, Date startTime, Date endTime) {
    	if(message == null){
    		throw new IllegalArgumentException("Please fill in the required fields");
    	}
        this.message = message;
        this.startTime = startTime;
        this.endTime = endTime;
        this.isFloating = false;
        this.isDeadline = false; 
        this.isEvent = true;
    }
    public Task(String message, Date startTime, Date endTime, PriorityType priority) {
        if(message == null){
            throw new IllegalArgumentException("Please fill in the required fields");
        }
        this.message = message;
        this.startTime = startTime;
        this.endTime = endTime;
        this.isFloating = false;
        this.isDeadline = false; 
        this.isEvent = true;
        this.priority = priority;
    }
    
    
    
    public Task(ReadOnlyTask src) {
        this.message = src.getMessage();
        this.deadline = src.getDeadline();
        this.startTime = src.getStartTime();
        this.endTime = src.getEndTime(); 
        this.isFloating = src.getIsFloating();
        this.isDeadline = src.getIsDeadline();
        this.isEvent = src.getIsEvent();
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
    
    @Override
    public String getStartTimeString() {
		return DateUtil.readableDate(startTime);
	}
    
    @Override
    public Date getEndTime(){
    	return this.endTime;
    }
    
    @Override
    public String getEndTimeString() {
		return DateUtil.readableDate(startTime);
	}
    
    @Override
    public Date getDeadline(){
    	return this.deadline;
    }
    
    @Override
    public String getDeadlineString() {
		return DateUtil.readableDate(deadline);	
	}
    @Override
    public boolean getIsFloating(){
    	return this.isFloating;
    }
    
    @Override
    public boolean getIsEvent(){
        return this.isEvent;
    }
    
    @Override
    public boolean getIsDeadline(){
    	return this.isDeadline;
    }
    
    @Override 
    public boolean getIsRecurring(){
    	return this.isRecurring;
    }
    
    public PriorityType getPriority(){
    	return this.priority;
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
    
    public void setIsEvent(boolean isEvent){
    	this.isEvent = isEvent;
    }
    
    public void setIsDeadline(boolean isDeadline){
    	this.isDeadline = isDeadline;
    }
    
    public void setIsRecurring(boolean isRecurring){
    	this.isRecurring = isRecurring;
    }
    
    public void setPriority(PriorityType priority){
    	this.priority = priority;
    }
    
    /*
     * compares the task's time
     * 
     * @returns -1 if this task is due earlier than the given task, 0 if both are 
     *  due the same time, and 1 if this task is due later
     */
    public int compareTime(Task other) {
        if (this.isFloating) {
            if (other.isFloating) return 0;
            else return 1;
        }
        else {
            Date time;
        
            if (this.isDeadline) time = this.deadline;
            else time = this.endTime;
        
            if (other.isFloating) return -1;
            else if (other.isDeadline) return time.compareTo(other.deadline);
            else return time.compareTo(other.endTime);
        }        
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
