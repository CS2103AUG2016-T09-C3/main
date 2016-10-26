//@@author A0139750B
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
    private boolean isRecurring = false;
    private PriorityType priority = PriorityType.NORMAL; //default priority
    private TaskType type;
    private boolean isDone = false;
    private boolean isInferred = false;
   
    //Floating
    public Task(String message, PriorityType priority) {
        if(message == null){
//          throw new IllegalArgumentException("Please fill in the required fields");
            this.message = "";
        }
        else {
            this.message = message;
        }
        this.priority = priority;
        this.isDone = false;
        this.type = TaskType.FLOATING;
        this.isInferred = false;
        
    }
    
    //Deadline Task
    public Task(String message, Date deadline, PriorityType priority) {
    	assert deadline != null;
        if(message == null){
            throw new IllegalArgumentException("Please fill in the required fields");
        }
        this.message = message;
        this.deadline = deadline;
        this.isDone = false;
        this.priority=priority;
        this.type = TaskType.DEADLINE;
        this.isInferred = false;
       
    }
    
    //Event Task
    public Task(String message, Date startTime, Date endTime, PriorityType priority) {
        assert (startTime != null && endTime != null);
    	if(message == null){
            throw new IllegalArgumentException("Please fill in the required fields");
        }
        this.message = message;
        this.startTime = startTime;
        this.endTime = endTime;
        this.isDone = false;
        this.priority = priority;
        this.type = TaskType.EVENT;
        this.isInferred = false;
    }
     
    public Task(ReadOnlyTask src) {
        this.message = src.getMessage();
        this.deadline = src.getDeadline();
        this.startTime = src.getStartTime();
        this.endTime = src.getEndTime();
        this.isRecurring = src.getIsRecurring();
        this.priority = src.getPriority();
        this.type = src.getType();
        this.isDone = src.getIsDone();
        this.isInferred = src.getIsInferred();
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
		return DateUtil.readableDate(startTime, isInferred);
	}
    
    @Override
    public Date getEndTime(){
    	return this.endTime;
    }
    
    @Override
    public String getEndTimeString() {
		return DateUtil.readableDate(endTime, isInferred);
	}
    
    @Override
    public Date getDeadline(){
    	return this.deadline;
    }
    
    @Override
    public String getDeadlineString() {
		return DateUtil.readableDate(deadline, isInferred);	
	}
    @Override
    public boolean getIsFloating(){
    	return type == TaskType.FLOATING ;
    }
    
    @Override
    public boolean getIsEvent(){
        return type == TaskType.EVENT;
    }
    
    @Override
    public boolean getIsDeadline(){
    	return type == TaskType.DEADLINE;
    }
    
    @Override 
    public boolean getIsRecurring(){
    	return this.isRecurring;
    }
    
    @Override
    public TaskType getType(){
        return this.type;
    }
    
    @Override
    public PriorityType getPriority(){
    	return this.priority;
    }
    
   // public TaskType getType(){
   //	return this.type;
   // }
    
   @Override
    public boolean getIsDone(){
	   return this.isDone;
    }
   
   @Override
   public boolean getIsInferred(){
	   return this.isInferred;
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
    
    public void setIsFloating(){
    	this.type = TaskType.FLOATING;
    }
    
    public void setIsEvent(){
    	this.type = TaskType.EVENT;
    }
    
    public void setIsDeadline(){
    	this.type = TaskType.DEADLINE;
    }
    
    public void setIsRecurring(boolean isRecurring){
    	this.isRecurring = isRecurring;
    }
    
    public void setPriority(PriorityType priority){
    	this.priority = priority;
    }
    
    public void setType(TaskType type){
    	this.type = type;
    }
    
    public boolean setIsDone(){
	    this.isDone = true;
	    return this.isDone;
    }
    
    public boolean setIsUnDone(){
	   this.isDone = false;
	   return this.isDone;
    }
    
    public Task setIsInferred(boolean isInferred){
    	this.isInferred = isInferred;
    	return this;
    }
    
    /*
     * compares the task's time
     * 
     * @returns -1 if this task is due earlier than the given task, 0 if both are 
     *  due the same time, and 1 if this task is due later
     */
    public int compareTime(Task other) {
        if (this.type == TaskType.FLOATING) {
            if (other.type == TaskType.FLOATING) return 0;
            else return 1;
        }
        else {
            Date time;
        
            if (this.type == TaskType.DEADLINE) time = this.deadline;
            else time = this.endTime;
        
            if (other.type == TaskType.FLOATING) return -1;
            else if (other.type == TaskType.DEADLINE) return time.compareTo(other.deadline);
            else return time.compareTo(other.endTime);
        }        
    }
     
    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        
        else if (other instanceof Task) {
            boolean basic = this.message.equals(((Task) other).message) 
                        && this.priority.equals(((Task) other).priority) 
//                        && this.isInferred == ((Task) other).isInferred
                        && this.isDone == ((Task) other).isDone
                        && this.isRecurring == ((Task) other).isRecurring;
                                                                
                        
        	if(this.type == TaskType.FLOATING){ 
        		return basic;
        	}
        	else if(this.type == TaskType.EVENT) {
        	    return (basic && this.startTime.equals(((Task) other).startTime)
        	    && this.endTime.equals(((Task) other).endTime));
        	}
        	else {
                return (basic && this.deadline.equals(((Task) other).deadline));
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
    	if(this.type == TaskType.FLOATING){
    		return getMessage(); 
    	}
    	else if(this.type == TaskType.EVENT){
    		return  getMessage()+ " from " + getStartTimeString() + " to "
    					+ getEndTimeString();
    	}
    	else{
    		return  getMessage() + " due " + getDeadlineString();
    	}
    }
    


}
