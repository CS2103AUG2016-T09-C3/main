//@@author A0144132W
package main.logic.command;

import java.util.Date;
import org.apache.commons.lang3.tuple.Triple;
import main.commons.util.DateUtil;
import main.model.task.PriorityType;
import main.model.task.TaskType;

/**
 * Lists certain tasks in the task tracker to the user.
 */
public class ListCommand extends Command {
    
    public static final String COMMAND_WORD = "list";
    public static final String MESSAGE_USAGE = COMMAND_WORD + "Parameters: [priority] [date] [type] \n"
            												+ "All parameters optional and interchangeable \n"
            												+ "Eg: " + COMMAND_WORD + " high today";
    public static final String MESSAGE_SUCCESS = "Listed all %1$s";
    
    private PriorityType priority;
    private Date date;
    private TaskType type;
    private boolean isDefault = false;
    private boolean isDone = false;
    private boolean onlyOverdue = false;
    
    public ListCommand() {
        isDefault = true;
    }
      
    public ListCommand(PriorityType priority, Date date, TaskType type, boolean isDone, boolean onlyOverdue) {
        this.priority = priority;
        this.date = date;
        this.type = type;
        this.isDone = isDone;
        this.onlyOverdue = onlyOverdue;
    }

	@Override
	public CommandResult execute() {

	    if (isDefault) model.updateFilteredListToShowAllPending();
	    else model.updateFilteredTaskList(Triple.of(priority, date, type), isDone, onlyOverdue);
	    
	    return new CommandResult(String.format(MESSAGE_SUCCESS, getReadableCriteria()));    
	}
	
	/**
	 * @returns String which consolidates the list parameters to something readable 
	 */
	private String getReadableCriteria() {
	    StringBuilder readable = new StringBuilder();
	 
	    String prefix = getPrefix();
	    String task = getReadableTaskType();
	    String priority_str = getPriority();
	    String date_str = getReadableDate();
	    
	    readable.append(prefix).append(priority_str).append(task).append(date_str);
	    
	    return readable.toString();
	    
	}
	
	private String getReadableDate() {
	    String date_str;
	    
	    if (date != null) {
	        date_str = " due " + DateUtil.readableDate(date, true);
	    }
	    else {
	        date_str = "";
	    }
	    return date_str;
	}

    private String getPriority() {
        String priority_str;
        
        if (priority == null) {
	        priority_str = " ";
	    }
	    else if (priority == PriorityType.LOW) {
	        priority_str = " low priority ";
	    }
	    else if (priority == PriorityType.HIGH) {
	        priority_str = " high priority ";
	    }
	    else {
	        priority_str = " normal priority ";
	    }
        
        return priority_str;
    }

    private String getPrefix() {
        String prefix;
        if (isDone) {
	        prefix = "completed";
	    }
	    else if (onlyOverdue) {
	        prefix = "overdue";
	    }
	    else {
	        prefix = "pending";
	    }
        return prefix;
    }

    private String getReadableTaskType() {
        String task;
        if (type == null) {
	        task = "tasks";
	    }
	    else if (type == TaskType.EVENT) {
	        task = "events";
	    }
        else if (type == TaskType.DEADLINE) {
            task = "tasks with deadlines";
        }
        else {
            task = "floating tasks";
        }
        return task;
    }
}
