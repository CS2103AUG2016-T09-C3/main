//@@author A0144132W
package main.commons.events.model;

import main.commons.events.BaseEvent;
import main.model.ReadOnlyTaskTracker;

public class LoadTaskTrackerEvent extends BaseEvent{
    private ReadOnlyTaskTracker taskTracker;
    
    public LoadTaskTrackerEvent(ReadOnlyTaskTracker taskTracker) {
        this.taskTracker = taskTracker;
    }
    
    public ReadOnlyTaskTracker getTaskTracker() {
        return taskTracker;
    }

    @Override
    public String toString() {
        return taskTracker.toString();
    }
    
    

}
