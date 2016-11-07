//@@author A0144132W
package main.logic.command;

public class FindCommand extends Command {
    
    public static final String COMMAND_WORD = "find";
    public static final String MESSAGE_USAGE = COMMAND_WORD + "Parameters: [keywords] \n"
            												+ "Type in keywords found in the desired task's description \n"
            												+ "Eg: " + COMMAND_WORD + " cs2103";
    public static final String MESSAGE_SUCCESS = "Found %1$s matching tasks";
        
    public FindCommand() {}
    
    @Override
    public CommandResult execute() {
        return new CommandResult(String.format(MESSAGE_SUCCESS, model.getFilteredTaskList().size()));
    }
    
}
