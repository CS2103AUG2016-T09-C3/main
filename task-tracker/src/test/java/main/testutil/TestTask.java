//@@author A0139750B
package main.testutil;

import java.util.Date;
import java.util.Objects;

import main.commons.util.DateUtil;
import main.model.task.PriorityType;
import main.model.task.ReadOnlyTask;
import main.model.task.Task;
import main.model.task.TaskType;

/**
 * A mutable task object. For testing only.
 */
public class TestTask implements ReadOnlyTask {

	private String message;
    Date deadline;
    Date startTime;
    Date endTime;
    private boolean isRecurring = false;
    private PriorityType priority = PriorityType.NORMAL; //default priority
    private TaskType type;
    private boolean isDone;
    private boolean isInferred;
   
    //Floating
    public TestTask(String message, PriorityType priority) {
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
    public TestTask(String message, Date deadline, PriorityType priority) {
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
    public TestTask(String message, Date startTime, Date endTime, PriorityType priority) {
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
     
    public TestTask(ReadOnlyTask src) {
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
    
    public void setIsInferred(boolean isInferred){
    	this.isInferred = isInferred;
    }
    
    /*
     * compares the task's time
     * 
     * @returns -1 if this task is due earlier than the given task, 0 if both are 
     *  due the same time, and 1 if this task is due later
     */
    public int compareTime(TestTask other) {
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
        
        else if (other instanceof ReadOnlyTask) {
        	if(this.type.equals(TaskType.FLOATING)){ 
        		return (this.message.equals(((TestTask) other).message)) 
        		&& this.priority.equals(((TestTask) other).priority);
        	}
        	
        	else if(this.type.equals(TaskType.EVENT)) {
        	    return (this.message.equals(((TestTask) other).message)
        	 	&& this.startTime.equals(((TestTask) other).startTime)
        		&& this.endTime.equals(((TestTask) other).endTime))
        	   	&& this.priority.equals(((TestTask) other).priority);
        	}
        	
        	else {
                return (this.message.equals(((TestTask) other).message)
                && this.deadline.equals(((TestTask) other).deadline))
                && this.priority.equals(((TestTask) other).priority);
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
    
    public String getAddCommand() {
        StringBuilder sb = new StringBuilder();
        sb.append("add " + this.getMessage());
        
        if (type == TaskType.EVENT) {
            sb.append(" " + getStartTimeString() + " ")
              .append("to")
              .append(" " + getEndTimeString() + " ");
        }
        else if (type == TaskType.DEADLINE) {
            sb.append(" " + getDeadlineString());
        }
        
        String priorityInput;
        if (getPriority()==PriorityType.HIGH)
        	priorityInput = "-h";
        else if (getPriority()==PriorityType.LOW)
        	priorityInput = "-l";
        else
        	priorityInput = "-m";
        
        sb.append(" " + priorityInput + " ");
        return sb.toString();
    }

  //method to check if task is overdue
    @Override
    public boolean isOverdue(){
    	
    	//Event
    	if(this.type == TaskType.EVENT){
    		return DateUtil.checkOverdue(this.getEndTime(), this.isInferred);
    	}
    	//Deadline
    	else if(this.type == TaskType.DEADLINE){
    		return DateUtil.checkOverdue(this.getDeadline(), this.isInferred);
    	}
    	//Floating
    	else{
    		return false;
    	}
    }

}
