package main.storage;

import main.commons.exceptions.IllegalValueException;
//import main.model.tag.Tag;
//import main.model.tag.UniqueTagList;
import main.model.ReadOnlyTaskTracker;
import main.model.task.PriorityType;
import main.model.task.ReadOnlyTask;
import main.model.task.StorageTask;
import main.model.task.UniqueTaskList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.logging.Log;

import edu.emory.mathcs.backport.java.util.Arrays;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * An Immutable AddressBook that is serializable to XML format
 */
@XmlRootElement(name = "tasktracker")
public class XmlSerializableTaskTracker implements ReadOnlyTaskTracker {

    @XmlElement
    private List<XmlAdaptedTask> tasks;
    //@XmlElement
    //private List<Tag> tags;

    {
        tasks = new ArrayList<>();
        //tags = new ArrayList<>();
    }

    /**
     * Empty constructor required for marshalling
     */
    public XmlSerializableTaskTracker() {}

    /**
     * Conversion
     */
    
    public XmlSerializableTaskTracker(ReadOnlyTaskTracker src) {
        
        ArrayList<StorageTask> tempTasks = new ArrayList<StorageTask>();
        int i=0;
        String message;
        long deadline;
        long startTime;
        long endTime;
        boolean isFloating;
        boolean isDeadline;
        boolean isEvent;
        boolean isRecurring;
        PriorityType priorityType;
       
        
        for(i=0;i<src.getTaskList().size();i++){
            deadline=00000;
            startTime=000000;
            endTime=000000;
            
            
            message=src.getTaskList().get(i).getMessage();
            isFloating=src.getTaskList().get(i).getIsFloating();
            isDeadline=src.getTaskList().get(i).getIsFloating();
            isEvent=src.getTaskList().get(i).getIsEvent();
            isRecurring=src.getTaskList().get(i).getIsRecurring();
            priorityType=src.getTaskList().get(i).getPriority();
            
            if(isEvent==true){
              startTime=(src.getTaskList().get(i).getStartTime()).getTime();
              endTime=(src.getTaskList().get(i).getEndTime()).getTime();
            }
            if(isDeadline==true){
                deadline=(src.getTaskList().get(i).getDeadline()).getTime(); 
            }
            tempTasks.add(new StorageTask(message, deadline, startTime, endTime, isFloating, isDeadline, isEvent, isRecurring, priorityType));        
        
        }
        tasks.addAll(tempTasks.stream().map(XmlAdaptedTask::new).collect(Collectors.toList()));
        //tags = src.getTagList();
    }

    //@Override
    //public UniqueTagList getUniqueTagList() {
      //  try {
        //    return new UniqueTagList(tags);
        //} catch (UniqueTagList.DuplicateTagException e) {
          //  //TODO: better error handling
           // e.printStackTrace();
            //return null;
        //}
   // }

    @Override
    public UniqueTaskList getUniqueTaskList() {
        UniqueTaskList lists = new UniqueTaskList();
        for (XmlAdaptedTask t : tasks) {
            try {
                lists.add(t.toModelType());
            } catch (IllegalValueException e) {
                //TODO: better error handling
            }
        }
        return lists;
    }

    @Override
    public List<ReadOnlyTask> getTaskList() {
        return tasks.stream().map(t -> {
            return t.toModelType();
        }).collect(Collectors.toCollection(ArrayList::new));
    }

    //@Override
    //public List<Tag> getTagList() {
     //   return Collections.unmodifiableList(tags);
    //}

}
