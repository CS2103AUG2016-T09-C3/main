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

import org.apache.commons.lang3.BooleanUtils;
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
        String deadline;
        String startTime;
        String endTime;
        String isFloating;
        String isDeadline;
        String isEvent;
        String isRecurring;
        String priorityType;
       
        
        for(i=0;i<src.getTaskList().size();i++){
            deadline = "0";
            startTime = "0";
            endTime = "0";
            
            
            message = src.getTaskList().get(i).getMessage();
            isFloating = String.valueOf(src.getTaskList().get(i).getIsFloating());
            isDeadline = String.valueOf(src.getTaskList().get(i).getIsDeadline());
            isEvent = String.valueOf(src.getTaskList().get(i).getIsEvent());
            isRecurring = String.valueOf(src.getTaskList().get(i).getIsRecurring());
            priorityType= src.getTaskList().get(i).getPriority().name();
            
            if(isEvent.equals("true")){
              startTime = String.valueOf(src.getTaskList().get(i).getStartTime().getTime());
              endTime = String.valueOf((src.getTaskList().get(i).getEndTime()).getTime());
            }
            else if(isDeadline.equals("true")){
                deadline = String.valueOf(src.getTaskList().get(i).getDeadline().getTime()); 
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
                System.out.println(lists.getInternalList().get(lists.getInternalList().size() - 1 ));
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
