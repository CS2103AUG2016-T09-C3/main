package main.model.task;

import java.util.Date;

import org.apache.commons.lang3.BooleanUtils;

public class StorageTask {
    private String message;
    private String deadline;
    private String startTime;
    private String endTime;
    private String isFloating;
    private String isDeadline;
    private String isEvent;
    private String isRecurring;
    private String priority;
   
    
    
    
    public StorageTask(String message, String deadline, String startTime, String endTime, String isFloating,
            String isDeadline, String isEvent, String isRecurring, String priority) {
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
        return Long.valueOf(deadline);
    }
    public void setDeadline(long deadline) {
        this.deadline = String.valueOf(deadline);
    }
    public long getStartTime() {
        return Long.valueOf(startTime);
    }
    public void setStartTime(long startTime) {
        this.startTime = String.valueOf(startTime);
    }
    public long getEndTime() {
        return Long.valueOf(endTime);
    }
    public void setEndTime(long endTime) {
        this.endTime = String.valueOf(endTime);
    }
    public boolean isFloating() {
        return BooleanUtils.toBoolean(isFloating);
    }
    public void setFloating(boolean isFloating) {
        this.isFloating = String.valueOf(isFloating);
    }
    public boolean isDeadline() {
        return BooleanUtils.toBoolean(isDeadline);
    }
    public void setDeadline(boolean isDeadline) {
        this.isDeadline = String.valueOf(isDeadline);
    }
    public boolean isEvent() {
        return BooleanUtils.toBoolean(isEvent);
    }
    public void setEvent(boolean isEvent) {
        this.isEvent = String.valueOf(isEvent);
    }
    public boolean isRecurring() {
        return BooleanUtils.toBoolean(isRecurring);
    }
    public void setRecurring(boolean isRecurring) {
        this.isRecurring = String.valueOf(isRecurring);
    }
    public PriorityType getPriority() {
        return PriorityType.valueOf(priority);
    }
    public void setPriority(PriorityType priority) {
        this.priority = priority.name();
    }
    
    

}
