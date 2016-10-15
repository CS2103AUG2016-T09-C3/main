package main.model.task;

/*import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;*/
import java.util.Objects;

public class Task implements ReadOnlyTask {
    private String message;
    private PriorityType priority = PriorityType.NORMAL; //default priority
    private boolean isFloating;
    private boolean isEvent = false;//default floating task
    private boolean isRecurring = false;// default non repeating

    public Task(){
    	
    }
    public Task(String message){
    	this.message = message ;
    }
    
  
    /*public Task(String message, Date deadline) {
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
        this.endTime = endTime;x
        this.isFloating = false;
        this.isEvent = true;
       
    }*/
    
   /* public Task(ReadOnlyTask src) {
        this(src.getMessage());
        if (!src.getIsFloating()) {
            if (!src.getIsEvent()) 
                this.deadline = src.getDeadline();
            else {
                this.startTime = src.getStartTime();
                this.endTime = src.getEndTime();
            }
        }
    }*/
    
    //getters
  
    public String getMessage(){
        return this.message;
    }
    
    public boolean getIsFloating(){
    	return this.isFloating;
    }
    
    public boolean getIsEvent(){
        return this.isEvent;
    }
    public boolean getIsRecurring(){
        return this.isRecurring;
    }
    
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
   
    public void setIsFloating(boolean isFloating){
    	this.isFloating = isFloating;
    }
    
    public void setIsEvent(boolean isEvent){
    	this.isEvent = isEvent;
    }
    
    public void setIsRecurring(boolean isRecurring){
    	this.isRecurring = true;
    }
    
    public void setPriority(PriorityType priority){
    	this.priority = priority;
    }
    
    
    
    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        else if (other instanceof Task) {
        	if(this.isFloating){ 
        		return (this.message.equals(((Task) other).message));
        	}
        }
		return false;
    }
       
    @Override
    public int hashCode() {
        return Objects.hash(message);
    }
    
    @Override
    public String toString() {
    		return getMessage();
		
    }

}
