//@@author A0144132W
package main.logic.parser;

import main.commons.core.EventsCenter;
import main.commons.core.LogsCenter;
import main.commons.core.Messages;
import main.commons.events.ui.updateListStatisticsPictureEvent;
import main.commons.exceptions.IllegalValueException;
import main.commons.exceptions.MultiplePriorityException;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;

import main.model.task.PriorityType;
import main.model.task.Task;
import main.model.task.TaskType;
import main.logic.command.AddCommand;
import main.logic.command.ClearCommand;
import main.logic.command.Command;
import main.logic.command.DeleteCommand;
import main.logic.command.DoneCommand;
import main.logic.command.EditCommand;
import main.logic.command.ExitCommand;
import main.logic.command.FindCommand;
import main.logic.command.HelpCommand;
import main.logic.command.IncorrectCommand;
import main.logic.command.ListCommand;
import main.logic.command.RedoCommand;
import main.logic.command.SortCommand;
import main.logic.command.StorageCommand;
import main.logic.command.UndoCommand;

public class MainParser {
    
    private static final Pattern BASIC_COMMAND_FORMAT = Pattern.compile("(?<commandWord>\\S+)(?<task>.*)");
    private static final Pattern EDIT_FORMAT = Pattern.compile("(?<index>\\d)(?<task>.*)");
    private static final Logger logger = LogsCenter.getLogger(MainParser.class);

    public MainParser() {}
    
    /***
     * parses the given input
     * 
     * @returns a Command object to be executed.
     */
    public Command parse(String input){
        final Matcher matcher = BASIC_COMMAND_FORMAT.matcher(input.trim());
        
        if (!matcher.matches()) {
            return commandIncorrectPlusHelp(Messages.MESSAGE_INVALID_COMMAND_FORMAT);
        }
        String commandWord = matcher.group("commandWord");
        String task = matcher.group("task");
        
        logger.fine("command word: " + commandWord);
        if (!ReferenceList.commandsDictionary.containsKey(commandWord.toLowerCase())) {
            return commandIncorrectPlusHelp(String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, Messages.MESSAGE_UNKNOWN_COMMAND));
        }

        return getCommand(input, commandWord, task);
    }

    /***
     * Calls the appropriate prepare function according to the commandWord given.
     * 
     * @returns a Command object
     */
    private Command getCommand(String input, String commandWord, String task) {
        switch (ReferenceList.commandsDictionary.get(commandWord.toLowerCase())) {
            case AddCommand.COMMAND_WORD:
                return prepareAdd(task);
            case EditCommand.COMMAND_WORD:
                return prepareEdit(task);
            case DeleteCommand.COMMAND_WORD:
                return prepareDelete(task);
            case ListCommand.COMMAND_WORD:
                return prepareList(task);
            case HelpCommand.COMMAND_WORD:
                return new HelpCommand();
            case ExitCommand.COMMAND_WORD:
                return new ExitCommand();
            case DoneCommand.COMMAND_WORD:
                return prepareDone(task);
            case FindCommand.COMMAND_WORD:
                return new FindCommand();
            case ClearCommand.COMMAND_WORD:
                return new ClearCommand();
            case UndoCommand.COMMAND_WORD:
                return prepareUndo();
            case RedoCommand.COMMAND_WORD:
                return prepareRedo();
            case StorageCommand.COMMAND_WORD:
                return prepareStorage(input);
            case SortCommand.COMMAND_WORD:
                return prepareSort(task);
            default: 
                return commandIncorrectPlusHelp(String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, Messages.MESSAGE_UNKNOWN_COMMAND));
        }
    }

    /**
     * @returns an IncorrectCommand object with help message
     */
    private Command commandIncorrectPlusHelp(String message) {
        return new IncorrectCommand(String.format(message, HelpCommand.MESSAGE_USAGE));
    }
    
    /**
     * Extracts task to be added from the given input 
     */    
    public Command prepareAdd(String task) {
        try {
            Task newTask = extractTask(task);
            return new AddCommand(newTask);
        } catch (MultiplePriorityException e) {
            return new IncorrectCommand(String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, Messages.MESSAGE_MULTIPLE_PRIORITY));
        } catch (IllegalArgumentException e) {
            return new IncorrectCommand(String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, Messages.MESSAGE_EMPTY_DESCRIPTION));
        }                      
    }     
    
    /**
     * Extracts relevant parameters for the EditCommamd, consisting of the index to be
     * replaced and Task to take its place. Appropriate IncorrectCommand when input
     * is invalid.
     */
    public Command prepareEdit(String input) {
        final Matcher edit_matcher = EDIT_FORMAT.matcher(input.trim());
        
        if (!edit_matcher.matches()) {
            return new IncorrectCommand(String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE));
        }
        
        int index;
        
        try {
            index = extractValidIndex(edit_matcher.group("index"));
        } catch (NumberFormatException e) {
            return new IncorrectCommand(String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
        } catch (IllegalValueException e) {
            return new IncorrectCommand(String.format(e.getMessage(), EditCommand.MESSAGE_USAGE));
        } 
                
        String task = edit_matcher.group("task");
      
        try {
            Task newTask = extractTask(task);
            return new EditCommand(index, newTask);
        } catch (IllegalArgumentException e) {
            return new IncorrectCommand(String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE));
        }  
    }
    
    /**
     * Extracts relevant parameters for the DeleteCommamd, and returns appropriate
     * IncorrectCommand when input is invalid
     */
    public Command prepareDelete(String input) {
        int index;
        
        try {
            index = extractValidIndex(input);
        } catch (NumberFormatException e) {
            return new IncorrectCommand(String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
        } catch (IllegalValueException e) {
            return new IncorrectCommand(String.format(e.getMessage(), DeleteCommand.MESSAGE_USAGE));
        } 
   
        return new DeleteCommand(index);
    }

    /**
     * Extracts relevant parameters for the DoneCommamd, and returns appropriate
     * IncorrectCommand when input is invalid
     */
    public Command prepareDone(String input) {
        int index;
        
        try {
            index = extractValidIndex(input);
        } catch (NumberFormatException e) {
            return new IncorrectCommand(String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, DoneCommand.MESSAGE_USAGE));
        } catch (IllegalValueException e) {
            return new IncorrectCommand(String.format(e.getMessage(), DoneCommand.MESSAGE_USAGE));
        } 
   
        return new DoneCommand(index);
    }

    /**
     * Extracts a valid index from the given input. 
     *
     * @throws IllegalValueException when input is empty, or index derived is less than 0
     * @throws NumberFormatException if input given cannot be parsed as an integer.
     */
    private int extractValidIndex(String input) throws IllegalValueException, NumberFormatException{
        if (input.trim().equals(""))  {
            throw new IllegalValueException(Messages.MESSAGE_INVALID_COMMAND_FORMAT); 
        }
        
        int index = Integer.valueOf(input.trim()) - 1;
        
        if (index < 0) {
            throw new IllegalValueException(Messages.MESSAGE_INVALID_INDEX); 
        }

        return index;
    }
    
  //@@author A0142686X
    public Command prepareUndo() {
        return new UndoCommand();
    }
    
    public Command prepareRedo() {
        return new RedoCommand();
    }
    
    public Command prepareStorage(String input) {
        if(input.trim() == "") {
            return new IncorrectCommand(String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, StorageCommand.MESSAGE_USAGE));
        }
        String path = extractPath(input.trim());
        return new StorageCommand(path);
    }
    
    public String extractPath(String input) {
        return input.substring(8);
    }
    
    //@@author A0144132W
    /**
     * Extracts list parameters from the given input.
     * 
     * @returns appropriate ListCommand or IncorrectCommand if input is invalid.
     */
    public Command prepareList(String input) {
        if (input.trim().equals("")) {
            defaultListPicture();
            return new ListCommand();
        }
        
        Triple<String, List<Date>, List<Boolean>> info = TimeParser.extractTime(input.trim());

        String left = info.getLeft();
        List<Date> dates = info.getMiddle();
        PriorityType priority = null;
        TaskType type = null;
        boolean isDone = false;
        boolean onlyOverdue = false;
     
        for (String param: left.trim().split(" ")) {
            if (ReferenceList.priorityDictionary.containsKey(param) && priority == null) {
                priority = ReferenceList.priorityDictionary.get(param);
            }
            else if (ReferenceList.typeDictionary.containsKey(param) && type == null) {
                type = ReferenceList.typeDictionary.get(param);
            }
            else if (ReferenceList.doneSet.contains(param)) {
                isDone = true;
            }
            else if (param.equals(ReferenceList.overdue)) {
                onlyOverdue = true;
            }
            else if (!param.trim().equals("")) {
                return new IncorrectCommand(String.format(Messages.MESSAGE_INVALID_PARAMETERS,"ListCommand", ListCommand.MESSAGE_USAGE));
            }           
        }  
        
        Date date;
        date = getDate(dates);

        indicateListParamsChanged(priority, date, type, onlyOverdue);
        return new ListCommand(priority, date, type, isDone, onlyOverdue);
    }

    /**
     * Gets the first date if there are more than 1. If there are none, null is returned.
     * 
     * for prepareList function
     * 
     * @params list of dates given cannot be null
     */
    private Date getDate(List<Date> dates) {
        assert dates != null;
        
        Date date;
        if (dates.size() > 0) {
            date = dates.get(0);
        }
        else {
            date = null;
        }
        return date;
    }
    
    /**
     * Checks whether the input given matches the 2 sort parameter.
     * 
     * @returns SortCommand if input matches
     * @returns IncorrectCommand if input does not match
     */
    public Command prepareSort(String input) {
        String trimmed = input.trim().toLowerCase();
        
        if ("date".equals(trimmed) || "name".equals(trimmed)) {
            return new SortCommand(trimmed);
        }
        else {
            return new IncorrectCommand(String.format(Messages.MESSAGE_INVALID_PARAMETERS, "sort", SortCommand.MESSAGE_USAGE));
        }
    }

    /**
     * Checks whether task has a deadline, is an event, or is floating,
     * and uses the appropriate constructors accordingly.
     * 
     * @throws MultiplePriorityException if there are multiple priority indicated
     * @throws IllegalArgumentException if string input is invalid
     * @returns a Task object extracted from string given
     */
    private Task extractTask(String raw) throws MultiplePriorityException, IllegalArgumentException {

        Pair<PriorityType, String> proc = getPriority(raw.trim());
        PriorityType priority = proc.getKey();
        String input = proc.getValue();    
        
        Triple<String, List<Date>, List<Boolean>> info = TimeParser.extractTime(input.trim());
        List<Date> dates = info.getMiddle();
        String description = info.getLeft();
        Boolean isInferred = info.getRight().get(0);
        Boolean isRecurring = info.getRight().get(1);
        
        if (description.trim().equals("")) {
            throw new IllegalArgumentException();
        }
        
        if (dates.isEmpty()) {
            return (new Task(description, priority)).setIsInferred(isInferred).setIsRecurring(isRecurring);
        }
        else if (dates.size() == 1) { 
            return new Task(description,dates.get(0), priority).setIsInferred(isInferred).setIsRecurring(isRecurring);
        }
        // compare dates if there are 2 dates
        else {
            if (dates.get(0).before(dates.get(1)))
                return new Task(description,dates.get(0),dates.get(1), priority).setIsInferred(isInferred).setIsRecurring(isRecurring);
            else 
                return new Task(description,dates.get(1),dates.get(0), priority).setIsInferred(isInferred).setIsRecurring(isRecurring);
        }
    }
    
    /**
     * Extracts the priority from the given input. Priority is indicated by the 
     * PriorityType enum class.
     * 
     * @throws MultiplePriorityException if multiple priority is detected.
     * @returns a pair consisting PriorityType and truncated message (without 
     * priority indicator).
     */
    private Pair<PriorityType,String> getPriority(String input) throws MultiplePriorityException{
        String [] levels = {"-h", "-m", "-l" };
        String [] enum_array = {"HIGH", "NORMAL", "LOW"};
        PriorityType priority = null;
        int index = input.length();
        int find;
        
        for (int i = 0; i < levels.length; i++) {
            if ((find = argumentIndexInString(input,levels[i])) != -1) {
                if (priority != null) {
                    throw new MultiplePriorityException();
                }
                priority = PriorityType.valueOf(enum_array[i]);
                index = find;
            }
        }

        if (priority == null) {
            priority = PriorityType.NORMAL;
        }
        
        String truncate = input.substring(0, index);
        
        return Pair.of(priority, truncate);
    }
    
    /**
     * @returns the last index at which arg given appears in str given
     */
    private int argumentIndexInString(String str, String arg) {
        return (str.toLowerCase().lastIndexOf(arg.toLowerCase()));
    }
    
    /**
     * Processes the list parameters and chooses one to be shown in the list statistics
     * by posting an event
     */
    private void indicateListParamsChanged(PriorityType priority, Date date, TaskType type, boolean onlyOverdue) {
        String paramToShow = "";
        if (onlyOverdue) {
            paramToShow = "overdue";
        }
        else if (priority != null) {
            paramToShow = priority.name();
        }
        else if (date != null) {
            paramToShow = "date";
        }
        else if (type != null) {
            paramToShow = type.name();
        }
        else {
            paramToShow = "list";
        }
            
        EventsCenter.getInstance().post(new updateListStatisticsPictureEvent(paramToShow));
    }
    
    /**
     * Changes the ListStatistics picture to the default one
     */
    private void defaultListPicture() {
        indicateListParamsChanged(null, null, null, false);
    }

    
}
