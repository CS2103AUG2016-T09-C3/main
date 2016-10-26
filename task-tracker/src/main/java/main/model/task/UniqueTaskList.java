//@@author A0139750B
package main.model.task;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import main.commons.util.CollectionUtil;
import main.commons.exceptions.DuplicateDataException;

import java.util.*;

/**
 * A list of tasks that enforces uniqueness between its elements and does not allow nulls.
 *
 * Supports a minimal set of list operations.
 *
 * @see Task#equals(Object)
 * @see CollectionUtil#elementsAreUnique(Collection)
 */
public class UniqueTaskList implements Iterable<Task> {

    /**
     * Signals that an operation would have violated the 'no duplicates' property of the list.
     */
	@SuppressWarnings("serial")
    public static class DuplicateTaskException extends DuplicateDataException {
        protected DuplicateTaskException() {
            super("Operation would result in duplicate tasks");
        }
    }

    /**
     * Signals that an operation targeting a specified task in the list would fail because
     * there is no such matching task in the list.
     */
    @SuppressWarnings("serial")
    public static class TaskNotFoundException extends Exception {}

    private final ObservableList<Task> internalList = FXCollections.observableArrayList();

    /**
     * Constructs empty TaskList.
     */
    public UniqueTaskList() {}

    /**
     * Returns true if the list contains an equivalent task as the given argument.
     */
    public boolean contains(ReadOnlyTask toCheck) {
        assert toCheck != null;
        return internalList.contains(toCheck);
    }

    /**
     * Adds a task to the list.
     *
     * @throws DuplicateTaskException if the task to add is a duplicate of an existing task in the list.
     */
    public void add(Task toAdd) throws DuplicateTaskException {
        assert toAdd != null;
        if (contains(toAdd)) {
            throw new DuplicateTaskException();
        }
        internalList.add(toAdd);
    }
    

    public void replace(int index, Task newtask) throws DuplicateTaskException{
        if (contains(newtask)) {
            throw new DuplicateTaskException();
        }
        internalList.set(index,newtask);

    }
    
    /**
     * Removes the equivalent task from the list.
     *
     * @throws TaskNotFoundException if no such task could be found in the list.
     */
    public boolean remove(ReadOnlyTask toRemove) throws TaskNotFoundException {
        assert toRemove != null;
        final boolean taskFoundAndDeleted = internalList.remove(toRemove);
        if (!taskFoundAndDeleted) {
            throw new TaskNotFoundException();
        }
        return taskFoundAndDeleted;
    }
    /**
     * 
     * Marks the equivalent task as done/completed
     * @return
     * @throws TaskNotFoundException 
     */
    public boolean complete(ReadOnlyTask toComplete) throws TaskNotFoundException {
        assert toComplete != null;
        if (!internalList.contains(toComplete)) {
            throw new TaskNotFoundException();
        }
        Task taskFoundAndCompleted = internalList.get(internalList.indexOf(toComplete));
        return taskFoundAndCompleted.setIsDone();
    }
    
    //@@author A0142686X
    /**
     * method to mark a task not-done in tasktracker
     * @throws TaskNotFoundException 
     */
    public boolean incomplete(ReadOnlyTask toIncomplete) throws TaskNotFoundException {
        assert toIncomplete != null;
        if (!internalList.contains(toIncomplete)) {
            throw new TaskNotFoundException();
        }
        Task taskFoundAndCompleted = internalList.get(internalList.indexOf(toIncomplete));
        return taskFoundAndCompleted.setIsUnDone();
    }
    //@@author A0139750B
    
    public ObservableList<Task> getInternalList() {
        return internalList;
    }

    @Override
    public Iterator<Task> iterator() {
        return internalList.iterator();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof UniqueTaskList // instanceof handles nulls
                && this.internalList.equals(
                ((UniqueTaskList) other).internalList));
    }

    @Override
    public int hashCode() {
        return internalList.hashCode();
    }

}
