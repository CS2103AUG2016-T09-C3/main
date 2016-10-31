//@@author A0139422J
package main.logic.command;

/**
 * Represents the result of a command execution.
 * @author bey
 */
public class CommandResult {

    public final String feedbackToUser;
    
    public CommandResult(String feedbackToUser) {
        assert feedbackToUser != null;
        this.feedbackToUser = feedbackToUser;
    }

}
