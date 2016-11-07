//@@author A0144132W
package guitests;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.google.common.eventbus.Subscribe;

import main.commons.events.ui.AutoCompleteEvent;
import main.commons.events.ui.KeyPressEvent;
import main.commons.events.ui.TabPressEvent;
import main.logic.Logic;
import main.logic.autocomplete.AutoComplete;
import main.model.Model;
import main.model.ModelManager;
import main.model.TaskTracker;
import main.model.UserPrefs;
import main.testutil.TypicalTestTasks;

public class AutoCompleteGUITest extends TaskTrackerGuiTest{
    Model model = new ModelManager(td.getTypicalTaskTracker(), new UserPrefs());
    AutoComplete autoComplete = new AutoComplete(model);

    
    //These are for checking the correctness of the events raised
    int start;
    int end;
    String suggestion;

    @Subscribe
    private void handleAutoCompleteEvent(AutoCompleteEvent ace) {
        start = ace.getStart();
        end = ace.getEnd();
        suggestion = ace.getSuggestion();
    }

    @Test
    public void tabPressEvent_commandSuggestionLowerCase_changeCommandBoxInput(){
        invokeAutoComplete("li");
        assertEquals("list ", commandBox.getCommandInput());
    }
    @Test
    public void tabPressEvent_commandSuggestionUpperCase_changeCommandBoxInput(){
        invokeAutoComplete("LI");
        assertEquals("list ", commandBox.getCommandInput());
    }
    
    @Test
    public void tabPressEvent_oneTaskSuggestion_changeCommandBoxInput(){
        assertAutoCompleteEditDeleteDone("delete with");
        
        autoComplete.handleTabPressEvent(new TabPressEvent());
        assertEquals("delete 1", commandBox.getCommandInput());
    }

    @Test
    public void tabPressEvent_multipleTaskSuggestion_changeCommandBoxInput(){
        assertAutoCompleteEditDeleteDone("delete b");
        
        autoComplete.handleTabPressEvent(new TabPressEvent());
        assertEquals("delete 2", commandBox.getCommandInput());
    }
    
    @Test
    public void tabPressEvent_listSuggestionFirst_changeCommandBoxInput(){
        invokeAutoComplete("list fl");
        assertEquals("list floating ", commandBox.getCommandInput());
    }
    
    @Test
    public void tabPressEvent_listSuggestionSecond_changeCommandBoxInput(){
        invokeAutoComplete("list floating l");
        assertEquals("list floating low ", commandBox.getCommandInput());
    }
    
    @Test
    public void tabPressEvent_sortSuggestion_changeCommandBoxInput(){
        invokeAutoComplete("sort na");
        assertEquals("sort name", commandBox.getCommandInput());
    }

    private void invokeAutoComplete(String input) {
        commandBox.enterCommand(input);
        autoComplete.handleKeyPressEvent(new KeyPressEvent(input));
        autoComplete.handleTabPressEvent(new TabPressEvent());
    }

    private void assertAutoCompleteEditDeleteDone(String input) {
        invokeAutoComplete(input);
        assertEquals("delete 1", commandBox.getCommandInput());
    }
    
}
