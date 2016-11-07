//@@author A0142686X
package main.logic.command;

import main.model.ModelManager;
import main.model.ReadOnlyTaskTracker;
import main.model.TaskTracker;
import main.model.UndoHistory;
import main.model.task.Task;
import main.model.task.UniqueTaskList;
import main.model.task.UniqueTaskList.DuplicateTaskException;
import main.model.task.UniqueTaskList.TaskNotFoundException;

/**
     * Redoes the previous undoed command. 
     * Pops from redo stack and executes appropriate add/del/edit/clr/done commands
     * 
 */
public class RedoCommand extends Command {

    public static final String COMMAND_WORD = "redo";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Reverts the last known command undo.\n" + "eg. "
                                               + COMMAND_WORD;
    public static final String MESSAGE_SUCCESS = "Reverted last undo. ";
    public static final String MESSAGE_EMPTY_HISTORY = "There are no more undos before this.";
    public static final String MESSAGE_FAILIURE = "An error occured while performing redo";

    public static final int ADD = 1;
    public static final int DEL = 2;
    public static final int EDIT = 3;
    public static final int DONE = 4;
    public static final int CLR = 5;
    
    private UndoHistory redoHistory;
    
    @Override
    public CommandResult execute() {
        assert model != null;
        if(ModelManager.redoStack.size() == 0) {
            return new CommandResult(MESSAGE_EMPTY_HISTORY);
        }
        
        redoHistory = ModelManager.redoStack.pop();
        int ID = redoHistory.getID();
               
        switch (ID) {
        case ADD:
            return redoAdd(redoHistory.getTasks().get(0));
        case DEL:
            return redoDelete(redoHistory.getTasks().get(0));
        case EDIT:            
            return redoEdit(redoHistory.getTasks().get(1), redoHistory.getTasks().get(0));
        case CLR:
            return redoClear();
        case DONE:
            return redoDone(redoHistory.getTasks().get(0));
        default:
            return new CommandResult(MESSAGE_FAILIURE);
        }
    }
    
    private CommandResult redoAdd(Task task) {
        assert task != null;
        try {
            model.addTask(task);
        } catch (DuplicateTaskException e) {
            return new CommandResult(MESSAGE_FAILIURE);
        }
        return new CommandResult(MESSAGE_SUCCESS);
    }
    
    private CommandResult redoDelete(Task task) {
        assert task != null;
        try {
            model.deleteTask(model.getIndexFromTask(task));
        } catch (TaskNotFoundException e) {
            return new CommandResult(MESSAGE_FAILIURE);
        }
        return new CommandResult(MESSAGE_SUCCESS);
    }
    
    private CommandResult redoEdit(Task newTask, Task originalTask) {
        assert newTask != null;
        assert originalTask != null;
        try {
            model.editTask(model.getIndexFromTask(originalTask), newTask);
        } catch (DuplicateTaskException | IndexOutOfBoundsException | TaskNotFoundException e) {
            return new CommandResult(MESSAGE_FAILIURE);
        }
        return new CommandResult(MESSAGE_SUCCESS);
    }
    
    private CommandResult redoClear() {
        model.resetData((ReadOnlyTaskTracker) new TaskTracker(new UniqueTaskList()));
        return new CommandResult(MESSAGE_SUCCESS);
    }
    
    private CommandResult redoDone(Task task)  {
        assert task != null;
        try {
            model.doneTask(model.getIndexFromTask(task));
        } catch (IndexOutOfBoundsException | TaskNotFoundException e) {
            return new CommandResult(MESSAGE_FAILIURE);
        }
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
