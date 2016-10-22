package main.model;

import main.model.task.Task;
import main.model.filter.SortCriteria;
import main.model.filter.SortFilter;
import main.model.task.PriorityType;
import main.model.task.ReadOnlyTask;
import main.model.task.UniqueTaskList;
import main.model.task.UniqueTaskList.TaskNotFoundException;
import main.commons.core.UnmodifiableObservableList;

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

    /** Deletes the given person. */
    void deleteTask(ReadOnlyTask target) throws UniqueTaskList.TaskNotFoundException;

    /** Adds the given person */
    void addTask(Task task) throws UniqueTaskList.DuplicateTaskException;
    
    /** Replaces the task information at the specific index */
    void editTask(int index, Task newTask) throws TaskNotFoundException, UniqueTaskList.DuplicateTaskException;
    
    /** Returns the task at the corresponding index **/
    Task getTaskfromIndex(int index) throws  UniqueTaskList.TaskNotFoundException, IndexOutOfBoundsException;

    /** Returns the filtered person list as an {@code UnmodifiableObservableList<ReadOnlyPerson>} */
    UnmodifiableObservableList<ReadOnlyTask> getFilteredTaskList();

    /** Updates the filter of the filtered person list to show all persons */
    void updateFilteredListToShowAll();

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
    void updateFilteredTaskList(Triple<PriorityType, Date, String> params);

    /** Sorts the list based on criterias given **/
    void sortBy(SortCriteria criteria);

    /** THe default sorting done at the start **/
    void sortDefault();



    
}
