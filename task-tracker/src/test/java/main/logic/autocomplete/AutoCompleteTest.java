//@@author A0144132W
package main.logic.autocomplete;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;

import main.logic.Logic;
import main.logic.command.DeleteCommand;
import main.logic.command.DoneCommand;
import main.logic.command.EditCommand;
import main.logic.parser.ReferenceList;
import main.model.Model;
import main.model.ModelManager;
import main.model.TaskTracker;
import main.model.UserPrefs;
import main.model.task.ReadOnlyTask;
import main.model.task.Task;
import main.testutil.TypicalTestTasks;

public class AutoCompleteTest {
    AutoComplete autoComplete;
    TaskTracker taskTracker;
    Model model;
    Logic logic;
    
    @Before
    public void setup() {
        TypicalTestTasks typical = new TypicalTestTasks();
        TaskTracker taskTracker = typical.getTypicalTaskTracker();
        model = new ModelManager(taskTracker, new UserPrefs());
        autoComplete = new AutoComplete(model);
    }
    
    /*
     * confirms shown list and suggestions are correct
     */
    private void assertListAndSuggestionsBehavior(List<String> expectedSuggestions, List<? extends ReadOnlyTask> shownList) {
        assertEquals(shownList, model.getFilteredTaskList());
        assertEquals(expectedSuggestions, autoComplete.getSuggestions());

    }
    
    /*
     * update suggestions for each of the edit, done and delete commands, and confirms 
     * the shown list and suggestions are correct.
     */
    private void assertEditDoneDeleteCommands(String input, List<String> expectedSuggestions, List<? extends ReadOnlyTask> shownList) {
        autoComplete.updateSuggestions(EditCommand.COMMAND_WORD + " " + input);
        assertListAndSuggestionsBehavior(expectedSuggestions, shownList);

        autoComplete.updateSuggestions(DoneCommand.COMMAND_WORD + " " + input);
        assertListAndSuggestionsBehavior(expectedSuggestions, shownList);

        autoComplete.updateSuggestions(DeleteCommand.COMMAND_WORD + " " + input);
        assertListAndSuggestionsBehavior(expectedSuggestions, shownList);
    }
    
    /*
     * processes the given set and put objects that starts with the given input 
     * into a sorted list.
     * 
     *  @returns the sorted list where all objects in it matches the input.
     */
    private List<String> setToSortedListMatchingInput(Set<String> set, String input) {
        return set.stream().filter(k -> k.substring(0,input.length()).equals(input)).sorted((k1, k2) -> k1.compareTo(k2)).collect(Collectors.toList());
    }
    
    @Test
    public void updateSuggestions_commands_noMatch() {
        autoComplete.updateSuggestions("asdgsvfc");
        assertEquals(new ArrayList<String>(), autoComplete.getSuggestions());
    }
    
    @Test
    public void updateSuggestions_commands_allMatch() {
        autoComplete.updateSuggestions("");
        assertEquals(ReferenceList.CommandsSetWithRelevantSpaces.stream().sorted((k1, k2) -> k1.compareTo(k2)).collect(Collectors.toList()), autoComplete.getSuggestions());
    }
    
    @Test
    public void updateSuggestions_commands_matchdLowerCase() {
        String input = "d"; 
        autoComplete.updateSuggestions(input);
        assertEquals(setToSortedListMatchingInput(ReferenceList.CommandsSetWithRelevantSpaces, input), autoComplete.getSuggestions());
    }
    
    @Test
    public void updateSuggestions_commands_matchdUpperCase() {
        String input = "D"; 
        autoComplete.updateSuggestions(input);
        assertEquals(setToSortedListMatchingInput(ReferenceList.CommandsSetWithRelevantSpaces, input.toLowerCase()), autoComplete.getSuggestions());
    }
    
    @Test
    public void updateSuggestions_list_matchFirstParam() {
        String input = "list hig";
        autoComplete.updateSuggestions(input);
        assertEquals(Collections.singletonList("high "), autoComplete.getSuggestions());       
    }
    
    @Test
    public void updateSuggestions_list_lowerCaseMatchSecondParam() {
        String input = "list high flo";
        autoComplete.updateSuggestions(input);
        assertEquals(Collections.singletonList("floating "), autoComplete.getSuggestions());
    }
    
    @Test
    public void updateSuggestions_list_noMatch() {
        autoComplete.updateSuggestions("list asdgsvfc");
        assertEquals(new ArrayList<String>(), autoComplete.getSuggestions());
    }
    
    @Test
    public void updateSuggestions_list_upperCaseMatch() {
        autoComplete.updateSuggestions("list EV");
        assertEquals(Collections.singletonList("event "), autoComplete.getSuggestions());
        
    }
    
    @Test
    public void updateSuggestions_editDoneDelete_lowerCaseMatchFirstToken() {
        assertEditDoneDeleteCommands("clea", Arrays.asList("1"), Arrays.asList(new Task(TypicalTestTasks.floating1)));
    }
    
    @Test
    public void updateSuggestions_editDoneDelete_lowerCaseMatchMiddleToken() {
        assertEditDoneDeleteCommands("with", Arrays.asList("1"), Arrays.asList(new Task(TypicalTestTasks.event1)));
    }
    
    @Test
    public void updateSuggestions_editDoneDelete_lowerCaseMatchMultiple() {
        assertEditDoneDeleteCommands("clea with", Arrays.asList("1", "2"), Arrays.asList(new Task(TypicalTestTasks.event1), new Task(TypicalTestTasks.floating1)));
    }
    
    @Test
    public void updateSuggestions_find_lowerCaseMatchOne() {
        autoComplete.updateSuggestions("find clea ");
        assertListAndSuggestionsBehavior(Arrays.asList("1"), Arrays.asList(new Task(TypicalTestTasks.floating1)));
    }
    
    @Test
    public void updateSuggestions_find_upperCaseMatchOne() {
        autoComplete.updateSuggestions("find CLEA ");
        assertListAndSuggestionsBehavior(Arrays.asList("1"), Arrays.asList(new Task(TypicalTestTasks.floating1)));
    }
    
    @Test
    public void updateSuggestions_sort_noMatch() {
        autoComplete.updateSuggestions("sort efad");
        assertEquals(new ArrayList<String>(), autoComplete.getSuggestions());
    }
    
    @Test
    public void updateSuggestions_sort_lowerCaseMatch() {
        autoComplete.updateSuggestions("sort da");
        assertEquals(Arrays.asList("date"), autoComplete.getSuggestions());
    }
    
    @Test
    public void updateSuggestions_sort_upperCaseMatch() {
        autoComplete.updateSuggestions("sort DA");
        assertEquals(Arrays.asList("date"), autoComplete.getSuggestions());
    }    

}
