//@@author A0139422J
package main.ui;

import com.google.common.eventbus.Subscribe;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import main.commons.events.ui.AutoCompleteEvent;
import main.commons.events.ui.IncorrectCommandAttemptedEvent;
import main.commons.events.ui.KeyPressEvent;
import main.commons.events.ui.TabPressEvent;
import main.commons.events.ui.updateListStatisticsPictureEvent;
import main.commons.util.FxViewUtil;
import main.logic.Logic;
import main.logic.command.CommandResult;
import main.commons.core.EventsCenter;
import main.commons.core.LogsCenter;

import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * Contains the main input component which would interact with Logic most.
 * "person" key check done "addressbook" keyword check done
 * 
 * @param FXML
 *            TextField commandTextField
 * @param FXML
 *            CommandResult mostRecentResult
 * @@author A0139422J
 */

public class CommandBox extends UiPart {
    private final Logger logger = LogsCenter.getLogger(CommandBox.class);
    private static final String FXML = "CommandBox.fxml";

    private AnchorPane placeHolderPane;
    private AnchorPane commandPane;
    private ResultDisplay resultDisplay;
    String previousCommandTest;

    private Logic logic;
    private static ArrayList<String> commandHistory = new ArrayList<String>();
    private static int historyPointer = 0;
    private static final String EMPTY_COMMAND_HISTORY = "You have not input any commands yet";
    
    @FXML
    private TextField commandTextField;
    private CommandResult mostRecentResult;

    public static CommandBox load(Stage primaryStage, AnchorPane commandBoxPlaceholder, ResultDisplay resultDisplay,
            Logic logic) {
        CommandBox commandBox = UiPartLoader.loadUiPart(primaryStage, commandBoxPlaceholder, new CommandBox());
        commandBox.configure(resultDisplay, logic);
        commandBox.addToPlaceholder();
        commandBox.handleAllEvents();
        return commandBox;
    }

    public void configure(ResultDisplay resultDisplay, Logic logic) {
        this.resultDisplay = resultDisplay;
        this.logic = logic;
        registerAsAnEventHandler(this);
    }

    private void addToPlaceholder() {
        SplitPane.setResizableWithParent(placeHolderPane, false);
        placeHolderPane.getChildren().add(commandTextField);
        FxViewUtil.applyAnchorBoundaryParameters(commandPane, 0.0, 0.0, 0.0, 0.0);
        FxViewUtil.applyAnchorBoundaryParameters(commandTextField, 0.0, 0.0, 0.0, 0.0);
        placeHolderPane.setMaxHeight(50);
    }

    @Override
    public void setNode(Node node) {
        commandPane = (AnchorPane) node;
    }

    @Override
    public String getFxmlPath() {
        return FXML;
    }

    @Override
    public void setPlaceholder(AnchorPane pane) {
        this.placeHolderPane = pane;
    }

    @FXML
    private void handleCommandInputChanged() {
        resultDisplay.clearResultDisplay();
        // Take a copy of the command text
        previousCommandTest = commandTextField.getText();
        CommandBox.getHistory().add(previousCommandTest);
        /*
         * We assume the command is correct. If it is incorrect, the command box
         * will be changed accordingly in the event handling code {@link
         * #handleIncorrectCommandAttempted}
         */
        setStyleToIndicateCorrectCommand();
        mostRecentResult = logic.execute(previousCommandTest);
        String resultMessage = mostRecentResult.feedbackToUser;
        ListStatistics.updateStatistics();
        CommandBox.resetHistoryPointer();
        
        if (saveLocationIsChanged(previousCommandTest, resultMessage)){
            StatusBarFooter.updateSaveLocation(previousCommandTest);
        }
        
        resultDisplay.postMessage(resultMessage);
        logger.info("Result: " + resultMessage);
    }

    private boolean saveLocationIsChanged(String command, String resultMessage) {   
        return ((command.contains("storage")) && (!resultMessage.contains("XML file not found at the specified location."))&& (!command.contains("invalid")));
    }

    private static void resetHistoryPointer() {
        historyPointer = commandHistory.size();

    }

    /**
     * Sets the command box style to indicate a correct command.
     */
    private void setStyleToIndicateCorrectCommand() {
        commandTextField.getStyleClass().remove("error");
        commandTextField.setText("");
    }

    @Subscribe
    private void handleIncorrectCommandAttempted(IncorrectCommandAttemptedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event, "Invalid command: " + previousCommandTest));
        // setStyleToIndicateIncorrectCommand();
        commandTextField.setText("");
    }

    /**
     * Sets the command box style to indicate an error
     */
    private void setStyleToIndicateIncorrectCommand() {
        commandTextField.getStyleClass().add("error");
    }

    public TextField getCommandBoxTextField() {
        return commandTextField;
    }

    public void handleAllEvents() {
        handleUpEvent();
        handleDownEvent();
        handleTabEvent();
    }

    private void handleUpEvent() {

        commandTextField.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
            if (event.getCode() == KeyCode.UP) {
                try{
            	if (historyPointer > 0) {
                    --historyPointer;
                } else
                    historyPointer = 0;
                commandTextField.setText(CommandBox.getHistory().get(historyPointer));
                }
                catch (IndexOutOfBoundsException e){
            		resultDisplay.postMessage(EMPTY_COMMAND_HISTORY);
            		commandTextField.setText("");
            	}
                event.consume();

            }
        });
    }

    private void handleDownEvent() {
        commandTextField.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
            if (event.getCode() == KeyCode.DOWN) {
            	try{
            	if (historyPointer < CommandBox.getHistory().size() - 1) {
                    ++historyPointer;
                } else
                    historyPointer = (CommandBox.getHistory().size()) - 1;
                commandTextField.setText(CommandBox.getHistory().get(historyPointer));
            	}
            	catch (IndexOutOfBoundsException e){
            		resultDisplay.postMessage(EMPTY_COMMAND_HISTORY);
            		commandTextField.setText("");
            	}
                event.consume();
            }
        });
    }

    public static ArrayList<String> getHistory() {
        return commandHistory;
    }

    // @@author A0144132W
    @FXML
    public void handleKeyReleased(KeyEvent event) {
        if (!event.getCode().isDigitKey() && !event.getCode().isLetterKey() && event.getCode() != KeyCode.BACK_SPACE
                && event.getCode() != KeyCode.DELETE)
            return;
        String input = commandTextField.getText();
        EventsCenter.getInstance().post(new KeyPressEvent(event.getCode(), input));
    }

    public void handleTabEvent() {
        commandTextField.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
            if (event.getCode() == KeyCode.TAB) {
                EventsCenter.getInstance().post(new TabPressEvent(event.getCode()));
                event.consume();
            }
        });
    }

    @Subscribe
    public void handleAutoComplete(AutoCompleteEvent event) {
        commandTextField.replaceText(event.getStart(), event.getEnd(), event.getSuggestion());
    }
    // @@author A0139422J
    @Subscribe
    public void handleUpdateListStatisticsPicture(updateListStatisticsPictureEvent event){
    	ListStatistics.updateListImage(event.getImageFilePath());
    }

}
