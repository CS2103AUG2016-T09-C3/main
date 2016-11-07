//@@author A0144132W
package main.logic.parser;

import java.util.Map;
import java.util.Set;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

import main.logic.command.*;
import main.model.task.PriorityType;
import main.model.task.TaskType;

public class ReferenceList {
    
    public static final Map<String,String> commandsDictionary = ImmutableMap.<String, String>builder()
            .put("add", AddCommand.COMMAND_WORD)
            .put("edit", EditCommand.COMMAND_WORD)
            .put("change", EditCommand.COMMAND_WORD)
            .put("delete", DeleteCommand.COMMAND_WORD)                        
            .put("remove", DeleteCommand.COMMAND_WORD)
            .put("rm", DeleteCommand.COMMAND_WORD)
            .put("trash", DeleteCommand.COMMAND_WORD)
            .put("done", DoneCommand.COMMAND_WORD)
            .put("finish", DoneCommand.COMMAND_WORD)
            .put("complete", DoneCommand.COMMAND_WORD)               
            .put("mark", DoneCommand.COMMAND_WORD)
            .put("undo", UndoCommand.COMMAND_WORD)
            .put("redo", RedoCommand.COMMAND_WORD)
            .put("exit", ExitCommand.COMMAND_WORD)
            .put("close", ExitCommand.COMMAND_WORD)
            .put("find", FindCommand.COMMAND_WORD)
            .put("search", FindCommand.COMMAND_WORD)
            .put("list", ListCommand.COMMAND_WORD)
            .put("show", ListCommand.COMMAND_WORD)
            .put("help", HelpCommand.COMMAND_WORD)
            .put("T.T", HelpCommand.COMMAND_WORD)
            .put("clear", ClearCommand.COMMAND_WORD)
            .put("storage", StorageCommand.COMMAND_WORD)
            .put("sort", SortCommand.COMMAND_WORD)
            .build(); 
    
    public static final Map<String,PriorityType> priorityDictionary = ImmutableMap.<String, PriorityType>builder()
            .put("high", PriorityType.HIGH)
            .put("impt", PriorityType.HIGH)
            .put("important", PriorityType.HIGH)
            .put("medium", PriorityType.NORMAL)
            .put("med", PriorityType.NORMAL)
            .put("normal", PriorityType.NORMAL)
            .put("low", PriorityType.LOW)
            .build();
    
    public static final Map<String,TaskType> typeDictionary = ImmutableMap.<String, TaskType>builder()
            .put("event", TaskType.EVENT)
            .put("events", TaskType.EVENT)
            .put("deadline", TaskType.DEADLINE)
            .put("deadlines", TaskType.DEADLINE)
            .put("floating", TaskType.FLOATING)
            .put("floatings", TaskType.FLOATING)
            .build();
    
    public static final String overdue = "overdue";
    
    //prepare list command
    public static final Set<String> doneSet = ImmutableSet.<String>builder()
            .add("done")
            .add("complete")
            .add("finish")
            .build();
    
    // connectors to be discarded
    public static final Set<String> connectorSet = ImmutableSet.<String>builder()
            .add("by")
            .add("from")
            .add("on")
            .add("due")
            .add("during")
            .add("at")
            .add("every")
            .build();
    
    // for autocomplete
    public static final Set<String> listSet = ImmutableSet.<String>builder()
            .add("high ")
            .add("low ")
            .add("normal ")
            .add("medium ")
            .add("event ")
            .add("deadline ")
            .add("floating ")
            .add("done ")
            .add("overdue ")
            .add("january ")
            .add("february ")
            .add("march ")
            .add("april ")
            .add("may ")
            .add("june ")
            .add("july ")
            .add("august ")
            .add("september ")
            .add("october ")
            .add("november ")
            .add("december ")
            .build();
    
    // for autocomplete
    public static final Set<String> sortSet = ImmutableSet.<String>builder()
            .add("name")
            .add("date")
            .build();
    
    // for autocomplete
    public static final Set<String> CommandsSetWithRelevantSpaces = ImmutableSet.<String>builder()
            .add(AddCommand.COMMAND_WORD + " ")
            .add(EditCommand.COMMAND_WORD + " ")
            .add(DeleteCommand.COMMAND_WORD + " ")
            .add(DoneCommand.COMMAND_WORD + " ")
            .add(UndoCommand.COMMAND_WORD)
            .add(RedoCommand.COMMAND_WORD)
            .add(ExitCommand.COMMAND_WORD)
            .add(FindCommand.COMMAND_WORD + " ")
            .add(ListCommand.COMMAND_WORD + " ")
            .add(HelpCommand.COMMAND_WORD)
            .add(ClearCommand.COMMAND_WORD)
            .add(StorageCommand.COMMAND_WORD + " ")
            .add(SortCommand.COMMAND_WORD + " ")
            .build();
}