//@@author A0139422J
package main.logic.command;

import java.util.Date;

import main.commons.exceptions.IllegalValueException;
import main.model.task.Task;
import main.model.task.UniqueTaskList.DuplicateTaskException;
import main.model.task.UniqueTaskList.TaskNotFoundException;

/**
 * Edits an existing task in task-tracker
 * @author 
 *
 */
//@@author A0139422J
public class EditCommand extends Command {
public static final String COMMAND_WORD = "edit";
	
	public static final String MESSAGE_USAGE = COMMAND_WORD + ": " + "Edits a task in the Task-Tracker.\n"
												+ "Parameters: [task] [date] [time]\n"
												+ "Eg: " + COMMAND_WORD
												+ "2 wash dishes tuesday 6pm -h";
	
	public static final String MESSAGE_SUCCESS = "Task changed to: %1$s";
	public static final String MESSAGE_NO_SUCH_TASK = "This task does not exist in the Task-Tracker";
	
	private Task toEdit;
	private final int editNum;
	private Task newTask;
	
	public EditCommand(int targetVisibleIndex, Task newTask) {
		super(targetVisibleIndex);
		editNum = targetVisibleIndex;
		this.newTask = newTask;
	}	
	
	@Override
	public CommandResult execute() {
		try {
		    model.deleteTask(editNum);
			model.addTask(newTask);
			return new CommandResult(String.format(MESSAGE_SUCCESS, newTask));
		}catch (TaskNotFoundException e){
            return new CommandResult("Task does not exist in task-tracker");
		}catch (IndexOutOfBoundsException ie) {
			return new CommandResult("The task index provided is invalid");
		}catch (DuplicateTaskException e) {
            return new CommandResult(AddCommand.MESSAGE_DUPLICATE_TASK);
		}
	}
	
	
}
