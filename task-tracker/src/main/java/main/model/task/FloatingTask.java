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
