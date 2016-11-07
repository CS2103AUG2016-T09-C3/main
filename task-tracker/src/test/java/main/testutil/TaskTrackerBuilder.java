package main.testutil;

import main.model.TaskTracker;
import main.model.task.Task;
import main.model.task.UniqueTaskList;

/**
 * A utility class to help with building TaskTracker objects.
 * Example usage: <br>
 *     {@code TaskTracker tt = new TaskTrackerBuilder().withTask("CS2103 Meeting").build();}
 */
public class TaskTrackerBuilder {

    private TaskTracker taskTracker;

    public TaskTrackerBuilder(TaskTracker taskTracker){
        this.taskTracker = taskTracker;
    }

    /*
     * Task can be FloatingTask, Deadline or Event
     * 
     */
    public TaskTrackerBuilder withTask(Task task) throws UniqueTaskList.DuplicateTaskException {
        taskTracker.addTask(task);
        return this;
    }

    public TaskTracker build(){
        return taskTracker;
    }
}
