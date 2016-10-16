package main.model.task;

public class FloatingTask extends Task implements ReadOnlyTask {
	
	private boolean isDone;
	
	
	public FloatingTask(){
		super();
		this.isDone = false;
		
	}
	
	public FloatingTask(String message){
		super(message);
		this.isDone = false;
	}
	
	public FloatingTask(FloatingTask task){
		task.setMessage(task.getMessage());
		this.isDone = true;
	}
	
	//getters
	public boolean isComplete(){
		return isDone ;
	}
	
	//setters
	public void setDone(){
		this.isDone = true;
	}
	public void setUndone(){
		this.isDone = false;
	}
	
	@Override
	public String toString(){
			return this.getMessage();
	}
	
	
	
	
	
}
