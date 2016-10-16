package main.storage;

import main.commons.exceptions.IllegalValueException;
import main.model.task.*;

import javax.xml.bind.annotation.XmlElement;

import com.joestelmach.natty.generated.DateParser.date_return;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * JAXB-friendly version of the Person.
 */
public class XmlAdaptedTask {

    @XmlElement(required = true)
    private String message;
    @XmlElement(required = false)
    private Date deadline;
    @XmlElement(required = false)
    private Date startTime;
    @XmlElement(required = false)
    private Date endTime;
    @XmlElement(required = true)
    private boolean isFloating;    
    @XmlElement(required = true)
    private boolean isEvent;
    
    //@XmlElement
    //private List<XmlAdaptedTag> tagged = new ArrayList<>();

    /**
     * No-arg constructor for JAXB use.
     */
    public XmlAdaptedTask() {}


    /**
     * Converts a given Person into this class for JAXB use.
     *
     * @param source future changes to this will not affect the created XmlAdaptedPerson
     */
    public XmlAdaptedTask(ReadOnlyTask source) {
        message = source.getMessage();
        deadline = source.getDeadline();
        startTime = source.getStartTime();
        endTime = source.getEndTime();
        isFloating = source.getIsFloating();
        isEvent = source.getIsEvent();
    }

    /**
     * Converts this jaxb-friendly adapted person object into the model's Person object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted person
     */
    public Task toModelType() throws IllegalValueException {
            
        if (isFloating) return new Task(message);
        else if (isEvent) return new Task(message, startTime, endTime);
        else return new Task(message, deadline);
    }
}
