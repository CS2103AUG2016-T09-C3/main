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
    //private boolean isFloating;
    //private boolean isDeadline;
    //private boolean isEvent = false;
    private boolean isRecurring = false;
    private PriorityType priority = PriorityType.NORMAL; //default priority
    private TaskType type;
    private Status status;
   
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
       // this.isDone = false;
        this.type = TaskType.FLOATING;
        
        
    }
    
    //Deadline Task
    public Task(String message, Date deadline, PriorityType priority, Status status) {
        if(message == null){
            throw new IllegalArgumentException("Please fill in the required fields");
        }
        this.message = message;
        this.deadline = deadline;
        //this.isDone = false;
        this.priority=priority;
        this.type = TaskType.DEADLINE;
       
    }
    
    //Event Task
    public Task(String message, Date startTime, Date endTime, PriorityType priority, Status status) {
        if(message == null){
            throw new IllegalArgumentException("Please fill in the required fields");
        }
        this.message = message;
        this.startTime = startTime;
        this.endTime = endTime;
      //  this.isDone = false;
        this.priority = priority;
        this.type = TaskType.EVENT;
    }
    
    
    
    public Task(ReadOnlyTask src) {
        this(src.getMessage(), src.getPriority());
        if (!(src.getIsFloating().equals(TaskType.FLOATING))) {
            if (!(src.getIsEvent().equals(TaskType.DEADLINE))) 
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
    public TaskType getIsFloating(){
    	return TaskType.FLOATING;
    }
    
    @Override
    public TaskType getIsEvent(){
        return TaskType.EVENT;
    }
    
    @Override
    public TaskType getIsDeadline(){
    	return TaskType.DEADLINE;
    }
    
    @Override 
    public boolean getIsRecurring(){
    	return this.isRecurring;
    }
    
    @Override
    public PriorityType getPriority(){
    	return this.priority;
    }
    
   // public TaskType getType(){
   //	return this.type;
   // }
    
 //  @Override
 //  public boolean getIsDone(){
 //	   return this.isDone;
 //  }
    @Override
    public Status getStatus(){
    	return this.status;
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
    
   // public void setType(TaskType type){
   // 	this.type = type;
   // }
    
  //  public void setIsDone(){
  //  	this.isDone = true;
  //   }
    
  //  public void setIsUnDone(){
  //  	this.isDone = false;
  // }
    public void setStatus(Status status){
    	this.status = status;
    }
    
    /*
     * compares the task's time
     * 
     * @returns -1 if this task is due earlier than the given task, 0 if both are 
     *  due the same time, and 1 if this task is due later
     */
    public int compareTime(Task other) {
        if (this.type.equals(TaskType.FLOATING)) {
            if (other.equals(TaskType.FLOATING)) return 0;
            else return 1;
        }
        else {
            Date time;
        
            if (this.type.equals(TaskType.DEADLINE)) time = this.deadline;
            else time = this.endTime;
        
            if (other.equals(TaskType.FLOATING)) return -1;
            else if (other.equals(TaskType.DEADLINE)) return time.compareTo(other.deadline);
            else return time.compareTo(other.endTime);
        }        
    }
     
    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        
        else if (other instanceof Task) {
        	if(this.type.equals(TaskType.FLOATING)){ 
        		return (this.message.equals(((Task) other).message)) 
        		&& this.priority.equals(((Task) other).priority);
        	}
        	
        	else if(this.type.equals(TaskType.EVENT)) {
        	    return (this.message.equals(((Task) other).message)
        	 	&& this.startTime.equals(((Task) other).startTime)
        		&& this.endTime.equals(((Task) other).endTime))
        	   	&& this.priority.equals(((Task) other).priority);
        	}
        	
        	else {
                return (this.message.equals(((Task) other).message)
                && this.deadline.equals(((Task) other).deadline))
                && this.priority.equals(((Task) other).priority);
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
    	if(this.type.equals(TaskType.FLOATING)){
    		return  getMessage(); 
    	}
    	else if(this.type.equals(TaskType.EVENT)){
    		return  getMessage()+ " from " + getStartTimeString() + " to "
    					+ getEndTimeString();
    	}
    	else{
    		return  getMessage() + " due by " + getDeadlineString();
    	}
    }
    


}
