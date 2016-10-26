# A0139750Bunused
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
