//@@author A0139422J
package main.logic.command;

import main.model.task.UniqueTaskList.TaskNotFoundException;
import main.ui.ListStatistics;

/**
 * Deletes a task identified using it's last displayed index from the task tracker.
 * Prints success message and the corresponding deleted index.
 * @author bey
 */

public class DeleteCommand extends Command {
	public static final String COMMAND_WORD = "delete";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": " 
            + "Deletes the existing task by the index number shown in the list above.\n"
            + "Parameters: <task index>\n"
            + "Eg: " + COMMAND_WORD + " 1";


    public static final String MESSAGE_DELETE_TASK_SUCCESS = "Deleted Task: ";
    public static final String MESSAGE_DELETE_TASK_INVALID = "The task index provided is invalid";
    public static final String MESSAGE_DELETE_NO_TASK = "Task does not exist in Task-Tracker";
    
    private int taskIndex;

    public DeleteCommand(int targetVisibleIndex) {
        super(targetVisibleIndex);
        
        this.taskIndex = targetVisibleIndex;
    }


    @Override
    public CommandResult execute() {
        try {
            model.deleteTask(taskIndex);
            return new CommandResult(String.format(MESSAGE_DELETE_TASK_SUCCESS + (taskIndex+1) + "", taskIndex));

        } catch (IndexOutOfBoundsException ie) {
            return new CommandResult(MESSAGE_DELETE_TASK_INVALID);
        } catch (TaskNotFoundException pnfe) {
            return new CommandResult(MESSAGE_DELETE_NO_TASK);
        }
    }
}
