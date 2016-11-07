//@@author A0144132W
package main.commons.events.storage;

import main.commons.events.BaseEvent;
import main.model.ReadOnlyTaskTracker;

public class FilePathChangedEvent extends BaseEvent{
    String filePath;
    ReadOnlyTaskTracker taskTracker;

    public FilePathChangedEvent(String filePath, ReadOnlyTaskTracker taskTracker) {
        this.filePath = filePath;
        this.taskTracker = taskTracker;
    }
    
    public String getFilePath() {
        return filePath;
    }
    
    public ReadOnlyTaskTracker getTaskTracker() {
        return taskTracker;
    }

    @Override
    public String toString() {
        return filePath;
    }
}
