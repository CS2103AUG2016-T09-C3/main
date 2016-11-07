//@@author A0144132W
package main.commons.events.ui;

import java.util.Map;

import com.google.common.collect.ImmutableMap;

import main.commons.events.BaseEvent;
import main.model.task.PriorityType;
import main.model.task.TaskType;

public class updateListStatisticsPictureEvent extends BaseEvent{
    
    private String parameter;
    private static final Map<String, String> linkToFilePath = ImmutableMap.<String, String>builder()
            .put(PriorityType.HIGH.name(), "/images/high.png")
            .put(PriorityType.NORMAL.name(), "/images/normal.png")
            .put(PriorityType.LOW.name(), "/images/low.png")
            .put(TaskType.DEADLINE.name(), "/images/deadlines.png")
            .put(TaskType.EVENT.name(), "/images/events.png")
            .put(TaskType.FLOATING.name(), "/images/floating.png")
            .put("date", "/images/calendar.png")            
            .put("list", "/images/statistics.png")
            .put("overdue", "/images/overdue.png")
            .build();
    
    /*
     * updates the picture in list statistics according to the parameter passed
     * 
     * parameter is limited to the keys in linkToFilePath 
     */
    public updateListStatisticsPictureEvent(String parameter) {
        this.parameter = parameter;
    }
    
    public String getImageFilePath() {
        if (!linkToFilePath.containsKey(parameter)) {
            return linkToFilePath.get("list"); // in case parameter is passed wrongly
        }
        else {
            return linkToFilePath.get(parameter);
        }
    }

    @Override
    public String toString() {
        return parameter;
    }
}
