//@@author A0139750B-unused
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
