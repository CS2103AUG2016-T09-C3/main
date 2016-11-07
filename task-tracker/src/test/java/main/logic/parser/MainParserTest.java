//@@author A0144132W
package main.logic.parser;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import main.commons.util.DateUtil;
import main.logic.command.AddCommand;
import main.logic.command.Command;
import main.logic.command.CommandResult;
import main.model.Model;
import main.model.ModelManager;
import main.model.task.PriorityType;
import main.model.task.Task;

public class MainParserTest {
    
    MainParser mainParser;
    
    @Before
    public void setup() {
        mainParser = new MainParser();
    }
    
    @Test
    public void parse_trickyInput_doubleDates_takeSecond() {
        Command command = mainParser.parse("add xmas party tmr");
        Command expectedCommand = new AddCommand(new Task("xmas party", DateUtil.getTmr(),PriorityType.NORMAL).setIsInferred(true));
        assertCommandBehavior(command, expectedCommand);
    }

    @Test
    public void parse_trickyInput_fullOfNumbers_noDates() {
        Command command = mainParser.parse("add code cs2103");
        Command expectedCommand = new AddCommand(new Task("code cs2103", PriorityType.NORMAL));
        assertCommandBehavior(command, expectedCommand);
    }
    
    private void assertCommandBehavior(Command command, Command expectedCommand) {
        Model actual_model = new ModelManager();
        Model expected_model = new ModelManager();

        command.setData(actual_model);
        expectedCommand.setData(expected_model);
        
        CommandResult actual_result = command.execute();
        CommandResult expected_result = expectedCommand.execute();
        
        assertEquals(actual_model.getTaskTracker(), expected_model.getTaskTracker());
        assertEquals(actual_result.feedbackToUser, expected_result.feedbackToUser);
    }

}
