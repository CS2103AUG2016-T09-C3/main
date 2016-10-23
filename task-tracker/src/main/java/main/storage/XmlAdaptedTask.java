package main.storage;

import main.commons.core.LogsCenter;
import main.commons.exceptions.IllegalValueException;
import main.logic.parser.MainParser;
import main.model.task.*;

import javax.xml.bind.annotation.XmlElement;

import com.joestelmach.natty.generated.DateParser.date_return;

import java.util.Date;
import java.util.IllegalFormatCodePointException;
import java.lang.management.MemoryManagerMXBean;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
/**
 * JAXB-friendly version of the Person.
 */
public class XmlAdaptedTask {
    
    private static final Logger logger = LogsCenter.getLogger(MainParser.class);    //TESTING

    @XmlElement(required = true)
    private String message;
    @XmlElement(required = false)
    private String deadline;
    @XmlElement(required = false)
    private String startTime;
    @XmlElement(required = false)
    private String endTime;
    @XmlElement(required = true)
    private String isFloating;    
    @XmlElement(required = true)
    private String isEvent;
    @XmlElement(required = true)
    private String isDeadline;
    @XmlElement(required = true)
    private String isRecurring;
    @XmlElement(required = true)
    private String priority;
    
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
        deadline = String.valueOf(source.getDeadline().getTime());
        startTime = String.valueOf(source.getStartTime().getTime());
        endTime = String.valueOf(source.getEndTime().getTime());
        isFloating = String.valueOf(source.getIsFloating());
        isEvent = String.valueOf(source.getIsEvent());
        isDeadline = String.valueOf(source.getIsDeadline());
        isRecurring = String.valueOf(source.getIsRecurring());
        priority = source.getPriority().name();
    }

    /**
     * Converts this jaxb-friendly adapted person object into the model's Person object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted person
     */
    //DateTimeFormatter format = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss z yyyy");
    
    
    public Task toModelType(){
        if (isFloating.equals("true"))
            return new Task(message, PriorityType.valueOf(priority));
        else if (isEvent.equals("true"))
            return new Task(message,new Date(Long.valueOf(startTime)),new Date(Long.valueOf(endTime)), PriorityType.valueOf(priority));
        else return new Task(message, new Date(Long.valueOf(deadline)), PriorityType.valueOf(priority));
        
    }
}
