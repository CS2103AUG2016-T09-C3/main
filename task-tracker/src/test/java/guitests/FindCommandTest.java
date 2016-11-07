//@@author A0144132W
package guitests;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import main.commons.events.ui.KeyPressEvent;
import main.logic.command.FindCommand;
import main.testutil.TestTask;
import main.testutil.TypicalTestTasks;

public class FindCommandTest extends TaskTrackerGuiTest {
    
    private void assertFindBehavior(String input, int expectedListLength, TestTask[] expectedList) {
        commandBox.enterCommand(input);
        raise(new KeyPressEvent(input));
        commandBox.pressEnter();
        
        assertResultMessage(String.format(FindCommand.MESSAGE_SUCCESS, expectedListLength));
  
        // confirms if the list view matches the expected list given.
        assertTrue(taskListPanel.isListMatching(expectedList));
    }

	@Test
	public void find_oneKeyword_success() {	    
		assertFindBehavior("find" + " frie", 1, new TestTask[] {TypicalTestTasks.event1});
	}
	
    @Test
    public void find_twoKeyword_success() {  
        assertFindBehavior("find" + " frie" + " b", 3, new TestTask[] {TypicalTestTasks.deadline2, TypicalTestTasks.deadline1, TypicalTestTasks.event1});
    }
    
    @Test
    public void find_middleParam_success() {
        assertFindBehavior("find" + " with", 1, new TestTask[] {TypicalTestTasks.event1});
    }
    
    @Test
    public void find_upperCase_success() {     
        assertFindBehavior("find" + " FRIE", 1, new TestTask[] {TypicalTestTasks.event1});
    }
    
    @Test
    public void find_nonexistentTask_empty() {     
        assertFindBehavior("find" + " wdsc", 0, new TestTask[] {});
    }   

}
