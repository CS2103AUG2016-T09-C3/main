package main.model.task;

import java.util.Date;

public class StorageTask {
    private String message;
    private long deadline;
    private long startTime;
    private long endTime;
    private boolean isFloating;
    private boolean isDeadline;
    private boolean isEvent;
    private boolean isRecurring;
    private PriorityType priority;
   
    
    
    
    public StorageTask(String message, long deadline, long startTime, long endTime, boolean isFloating,
            boolean isDeadline, boolean isEvent, boolean isRecurring, PriorityType priority) {
        super();
        this.message = message;
        this.deadline = deadline;
        this.startTime = startTime;
        this.endTime = endTime;
        this.isFloating = isFloating;
        this.isDeadline = isDeadline;
        this.isEvent = isEvent;
        this.isRecurring = isRecurring;
        this.priority = priority;
    }
    //=======GETTERS AND SETTERS=======//
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public long getDeadline() {
        return deadline;
    }
    public void setDeadline(long deadline) {
        this.deadline = deadline;
    }
    public long getStartTime() {
        return startTime;
    }
    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }
    public long getEndTime() {
        return endTime;
    }
    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }
    public boolean isFloating() {
        return isFloating;
    }
    public void setFloating(boolean isFloating) {
        this.isFloating = isFloating;
    }
    public boolean isDeadline() {
        return isDeadline;
    }
    public void setDeadline(boolean isDeadline) {
        this.isDeadline = isDeadline;
    }
    public boolean isEvent() {
        return isEvent;
    }
    public void setEvent(boolean isEvent) {
        this.isEvent = isEvent;
    }
    public boolean isRecurring() {
        return isRecurring;
    }
    public void setRecurring(boolean isRecurring) {
        this.isRecurring = isRecurring;
    }
    public PriorityType getPriority() {
        return priority;
    }
    public void setPriority(PriorityType priority) {
        this.priority = priority;
    }
    
    

}
