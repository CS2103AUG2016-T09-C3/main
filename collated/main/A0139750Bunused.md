# A0139750Bunused
###### \java\main\model\task\Deadline.java
``` java
//splitting task into subclasses causes a lot of issues and bugs in other components
package main.model.task;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Deadline extends Task{
	private Date deadline;

	public Deadline(){
		
	}
	
	public Deadline(String message, Date deadline){
		super(message);
		
		if(message == null){
    		throw new IllegalArgumentException("Please fill in the required fields");
    	}
		
		this.deadline = deadline;
		this.setPriority(PriorityType.NORMAL);
		this.setIsFloating(false);
		this.setIsEvent(true);
		
	}
	//getters
	public Date getDeadline(){
    	return this.deadline;
    }
    
    public String getDeadlineString() {
		String dateString = "";
		DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm");
		dateString = df.format(this.deadline);
		return dateString;	
	}
    //setters
    public void setDeadline(Date deadline){
    	this.deadline = deadline;
    }
    
    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        else if (other instanceof Deadline) {
        	 return (this.getDeadline().equals(((Deadline) other).getDeadline()));
        }
        return false;
    }
    
    @Override
    public String toString(){
    	return  getMessage() + " due by " + getDeadlineString();
    }
}
```
###### \java\main\model\task\Event.java
``` java
//splitting task into subclasses causes a lot of issues and bugs in other components 
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
```
###### \java\main\model\task\FloatingTask.java
``` java
//splitting task into subclasses causes a lot of issues and bugs in other components
package main.model.task;

public class FloatingTask extends Task {
	
	public FloatingTask(String message) {
		super(message);
	    if(message == null){
	    	message ="";
	    }
	    else {
	    	this.setIsFloating(true);
	    }
	        
	    this.setPriority(PriorityType.NORMAL);
	    this.setIsRecurring(false);   
	        
	}
	
	@Override
	public boolean equals(Object other) {
        if (this == other) return true;
        else if (other instanceof Task) {
        	if(this.getIsFloating()){ 
        		return (this.getMessage().equals(((Task) other).getMessage()));
        	}
        }
        return false;
	}
	
	@Override
	public String toString(){
			return this.getMessage();
	}		
	
}
```
###### \java\main\model\task\Status.java
``` java
//Did not use OVERDUE, having another class for status is confusing to implement in other components
package main.model.task;

public class Status {
	public enum State{
		DONE, UNDONE, OVERDUE
	}
	private static final String MESSAGE_TASK_DONE = "DONE";
	private static final String MESSAGE_TASK_UNDONE = "UNDONE";
	private static final String MESSAGE_TASK_OVERDUE = "OVERDUE";
	
	private State status;
	
	public Status(State status){
		assert status !=null;
		this.status = status;
	}
	
	public Status(String statusString){
		assert statusString != null;
		this.status = getStatusString(statusString);
	}
	
	
	
	public State getStatusString(String statusString){
		switch(statusString){
		
		case MESSAGE_TASK_DONE:
			return State.DONE;
		
		case MESSAGE_TASK_UNDONE:
			return State.UNDONE;
			
		case MESSAGE_TASK_OVERDUE:
			return State.OVERDUE;
			
		default:
			throw new IllegalArgumentException("Invalid status");
		}
		
	}
	
	@Override
	public boolean equals(Object other){
		return other == this || //same object return true
				 (other instanceof Status// null
				&& this.status.equals(((Status) other).status));
	}
	@Override
	public String toString(){
		
		if(status == State.DONE){
			return "Done";
		}
		else if(status == State.UNDONE){
			return "Undone";
		}
		else{
			return "Overdue";
		}
	}
	
}
```
