//@@author A0139422J
package guitests;

import guitests.guihandles.TaskCardHandle;
import main.commons.core.Messages;
import main.logic.command.AddCommand;
import main.testutil.TestTask;
import main.testutil.TestUtil;
import main.testutil.TypicalTestTasks;

import org.junit.Test;


import static org.junit.Assert.assertTrue;

public class AddCommandTest extends TaskTrackerGuiTest {

    @Test
    public void add() {
//        //add one task
        TestTask[] currentList = td.getTypicalTasks();
//        TestTask taskToAdd = td.event2;
//        assertAddSuccess(taskToAdd, currentList);
//        currentList = TestUtil.addTasksToList(currentList, taskToAdd);

//        //add another task
        TestTask taskToAdd = TypicalTestTasks.floating3;
        assertAddSuccess(taskToAdd, currentList);
        currentList = TestUtil.addTasksToList(currentList, taskToAdd);
        
////        //add duplicate floating task
        commandBox.runCommand(TypicalTestTasks.floating2.getAddCommand());
        assertResultMessage(AddCommand.MESSAGE_DUPLICATE_TASK);
        assertTrue(taskListPanel.isListMatching(currentList));
        
//        //add to empty list
        commandBox.runCommand("clear");
        assertAddSuccess(TypicalTestTasks.floating1);

//        
//        //invalid command
        commandBox.runCommand("adds wrongcommandinput");
        assertResultMessage("Invalid command format! \nUnknown command");
    }

    private void assertAddSuccess(TestTask taskToAdd, TestTask... currentList) {
        commandBox.runCommand(taskToAdd.getAddCommand());

        //confirm the new card contains the right data
        TaskCardHandle addedCard = taskListPanel.navigateToTask(taskToAdd.getMessage());
        assertMatching(taskToAdd, addedCard);

        //confirm the list now contains all previous persons plus the new person
        TestTask[] expectedList = TestUtil.addTasksToList(currentList, taskToAdd);
        assertTrue(taskListPanel.isListMatching(expectedList));
    }

}
