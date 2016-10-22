package main.storage;

import main.commons.exceptions.IllegalValueException;
//import main.model.tag.Tag;
//import main.model.tag.UniqueTagList;
import main.model.ReadOnlyTaskTracker;
import main.model.task.ReadOnlyTask;
import main.model.task.UniqueTaskList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import java.util.ArrayList;
import java.util.List;
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
        tasks.addAll(src.getTaskList().stream().map(XmlAdaptedTask::new).collect(Collectors.toList()));
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
