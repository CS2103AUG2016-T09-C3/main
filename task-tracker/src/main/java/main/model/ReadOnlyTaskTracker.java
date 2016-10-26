//@@author A0139750B
package main.model;


import main.model.task.ReadOnlyTask;
import main.model.task.UniqueTaskList;


import java.util.List;

/**
 * Unmodifiable view of an address book
 */
public interface ReadOnlyTaskTracker {

  

    UniqueTaskList getUniqueTaskList();

    /**
     * Returns an unmodifiable view of persons list
     */
    List<ReadOnlyTask> getTaskList();

    
}
