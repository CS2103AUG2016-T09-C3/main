//@@author A0139422J
package main.logic.command;

/**
 * Represents the result of a command execution.
 * @author bey
 */
public class CommandResult {

    public final String feedbackToUser;
//    private TTNotification ttbot = new TTNotification();
    
    public CommandResult(String feedbackToUser) {
        assert feedbackToUser != null;
        this.feedbackToUser = feedbackToUser;
    }
    
//    public boolean isCorrectCommand(){
//    	return !feedbackToUser.contains("Invalid");
//    }
//    
//    public void showNotification(){
//    	ttbot.setTitle(feedbackToUser);
//    	ttbot.getTTbot().showAndDismiss(Duration.millis(500)); 	
//    }

}
