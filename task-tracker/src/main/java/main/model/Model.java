//@@author A0139750B
package main.model;

import main.model.task.Task;
import main.model.task.TaskType;
import main.model.filter.SortCriteria;
import main.model.filter.SortFilter;
import main.model.task.PriorityType;
import main.model.task.ReadOnlyTask;
import main.model.task.UniqueTaskList;
import main.model.task.UniqueTaskList.DuplicateTaskException;
import main.model.task.UniqueTaskList.TaskNotFoundException;
import main.commons.core.UnmodifiableObservableList;
import main.commons.exceptions.DuplicateDataException;

import java.util.ArrayList;
import java.util.Date;
//daryl
import java.util.Set;

import org.apache.commons.lang3.tuple.Triple;

/**
 * The API of the Model component.
 */
public interface Model {
    /** Clears existing backing model and replaces with the provided new data. */
    void resetData(ReadOnlyTaskTracker newData);

    /** Returns the AddressBook */
    ReadOnlyTaskTracker getTaskTracker();

    /** Deletes the given task. */
    void deleteTask(int index) throws UniqueTaskList.TaskNotFoundException;
    
    /** Marks the task at the given index as done **/
    void doneTask(int index) throws UniqueTaskList.TaskNotFoundException;

    /** Adds the given person */
    void addTask(Task task) throws UniqueTaskList.DuplicateTaskException;
    
    /** Replaces the task information at the specific index */
    void editTask(int index, Task newTask) throws TaskNotFoundException, UniqueTaskList.DuplicateTaskException;
    
    /** Returns the task at the corresponding index **/
    
    //@@author A0142686X
    /** Returns the Task at a specific index number inside TaskTracker **/
    Task getTaskfromIndex(int index) throws  UniqueTaskList.TaskNotFoundException, IndexOutOfBoundsException;
    
    /** Returns index number of specific task inside TaskTracker **/
    int getIndexFromTask(ReadOnlyTask task) throws UniqueTaskList.TaskNotFoundException, IndexOutOfBoundsException;
    
    /** Adds task upon undo **/
    void addTaskUndoRedo(Task task) throws DuplicateTaskException;
    
    /** Deletes task upon undo **/
    void deleteTaskUndoRedo(ReadOnlyTask target) throws TaskNotFoundException;
    
    /** Edits task upon undo **/
    void editTaskUndoRedo(int index, Task newTask) throws DuplicateTaskException;
    
    /** Clears Tasks upon undo **/
    void clearTaskUndoRedo(ArrayList<Task> tasks);
    
    /** re-adds a done task upon undo 
     * @throws DuplicateTaskException 
     * @throws TaskNotFoundException **/
    void doneTaskUndoRedo(Task Task) throws DuplicateTaskException, TaskNotFoundException;
    
    //@@author A0144132W
    /** Returns the filtered task list as an {@code UnmodifiableObservableList<ReadOnlyTask>} */
    UnmodifiableObservableList<ReadOnlyTask> getFilteredTaskList();

    /** Updates the filter of the filtered task list to show all pending tasks */
    void updateFilteredListToShowAllPending();

    /** Updates the filter of the filtered task list to show all completed tasks */    
    void updateFilteredListToShowAllDone();
    
    /** Returns the number of tasks today **/
    int getNumToday();

    /** Returns the number of tasks tomorrow **/
    int getNumTmr();
    
    /** Returns the total number of events in the list **/
    int getNumEvent();

    /** Returns the total number of tasks with deadline in the list **/
    int getNumDeadline();
    
    /** Returns the total number of floating tasks in the list **/
    int getNumFloating();

    /** Returns the total number of tasks in the list **/
    int getTotalNum();

    /** Updates the FilteredList based on criterias given **/
    void updateFilteredTaskList(Triple<PriorityType, Date, TaskType> params, boolean isDone);

    /** Sorts the list based on criterias given **/
    void sortBy(SortCriteria criteria);

    /** THe default sorting done at the start **/
    void sortDefault();




    
}
