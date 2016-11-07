//@@author A0142686X
package main.logic.command;

import java.util.ArrayList;
import main.model.ModelManager;
import main.model.UndoHistory;
import main.model.task.Task;
import main.model.task.UniqueTaskList.DuplicateTaskException;
import main.model.task.UniqueTaskList.TaskNotFoundException;
    
/**
     *Undoes the previous command. 
     *Maintains a stack of changes made by last entered command
     *Pushes the undo'd command to redo stack
     *
 */ 
public class UndoCommand extends Command {

    public static final String COMMAND_WORD = "undo";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Reverts the last known command input.\n" + "eg. "
            + COMMAND_WORD;

    public static final String MESSAGE_SUCCESS = "Reverted last command. ";
    public static final String MESSAGE_EMPTY_HISTORY = "There are no more inputs before this.";
    public static final String MESSAGE_FAILIURE = "An error occured while performing undo.";

    public static final int ADD = 1;
    public static final int DEL = 2;
    public static final int EDIT = 3;
    public static final int DONE = 4;
    public static final int CLR = 5;
    
    private UndoHistory undoHistory;

    @Override
    public CommandResult execute() {
        assert model != null;
        if(ModelManager.undoStack.size() == 0) {
            return new CommandResult(MESSAGE_EMPTY_HISTORY);
        }
        
        undoHistory=ModelManager.undoStack.pop();
        ModelManager.redoStack.push(undoHistory);
        int ID=undoHistory.getID();
        
        switch (ID) {
        case ADD:
            return undoAdd(undoHistory.getTasks().get(0));
        case DEL:
            return undoDelete(undoHistory.getTasks().get(0));
        case EDIT:            
            return undoEdit(undoHistory.getTasks().get(0), undoHistory.getTasks().get(1));
        case CLR:
            return undoClear(undoHistory.getTasks());
        case DONE:
            return undoDone(undoHistory.getTasks().get(0));
        default:
            return new CommandResult(MESSAGE_FAILIURE);
        }
    }
    
    private CommandResult undoAdd(Task task) {
        assert task != null;
        try {
            model.deleteTaskUndo(task);
        } catch (TaskNotFoundException e) {
            return new CommandResult(MESSAGE_FAILIURE);
        }
        return new CommandResult(MESSAGE_SUCCESS);
    }
    
    private CommandResult undoDelete(Task task) {
        assert task != null;
        try {
            model.addTaskUndo(task);
        } catch (DuplicateTaskException e) {
            return new CommandResult(MESSAGE_FAILIURE);
        }
        return new CommandResult(MESSAGE_SUCCESS);
    }
    
    private CommandResult undoEdit(Task newTask, Task originalTask) {
        assert newTask != null;
        assert originalTask != null;
        try {
            model.editTaskUndo(originalTask, newTask);
        } catch (DuplicateTaskException | TaskNotFoundException e) {
            return new CommandResult(MESSAGE_FAILIURE);
        }
        return new CommandResult(MESSAGE_SUCCESS);
    }
    
    private CommandResult undoClear(ArrayList<Task> tasks) {
        assert tasks != null;
        model.clearTaskUndo(tasks);
        return new CommandResult(MESSAGE_SUCCESS);
    }
    
    private CommandResult undoDone(Task task) {
        assert task != null;
        try {
            model.doneTaskUndo(task);
        } catch (DuplicateTaskException | TaskNotFoundException e) {
            return new CommandResult(MESSAGE_FAILIURE);
        }
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
