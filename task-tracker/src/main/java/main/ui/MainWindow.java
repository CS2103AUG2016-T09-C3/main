//@@author A0139422J
package main.ui;

import main.commons.core.Config;
import java.util.List;
import java.util.Optional;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import main.commons.core.Config;
import main.commons.core.GuiSettings;
import main.commons.events.ui.ExitAppRequestEvent;
import main.commons.util.FxViewUtil;
import main.logic.Logic;
import main.model.UserPrefs;
import main.model.task.ReadOnlyTask;

/**
 * Instantiates all the individual components for the Gui and interacts with
 * UiManager - CommandBox, HelpWindow, StatusBarFooter, TaskCard, TaskListPanel
 * "person" keyword check done "addressbook" keyword check done
 * 
 * @param AnchorPane
 *            commandBoxPlaceholder
 * @param MenuItem
 *            helpMenuItem
 * @param AnchorPane
 *            taskListPanelPlaceholder
 * @param AnchorPane
 *            resultDisplayPlaceholder
 * @param AnchorPane
 *            statusbarPlaceholder
 * @author bey
 */

public class MainWindow extends UiPart {

	private static final String ICON = "/images/pp.png";
	private static final String FXML = "MainWindow.fxml";
	public static final int MIN_HEIGHT = 620;
	public static final int MIN_WIDTH = 450;

	private Logic logic;

	// Independent Ui parts residing in this Ui container
	private TaskListPanel taskListPanel;
	private ResultDisplay resultDisplay;
	private StatusBarFooter statusBarFooter;
	private CommandBox commandBox;
	private ListStatistics listStatistics;
	private HelpWindow helpWindow;
	private Config config;
	private UserPrefs userPrefs;

	// Handles to elements of this Ui container
	private VBox rootLayout;
	private Scene scene;

	private String taskTrackerName;

	@FXML
	private SplitPane splitpane;

	@FXML
	private AnchorPane commandBoxPlaceholder;

	@FXML
	private AnchorPane taskListPanelPlaceholder;

	@FXML
	private AnchorPane resultDisplayPlaceholder;

	@FXML
	private AnchorPane statusbarPlaceholder;

	@FXML
	private AnchorPane listStatisticsPlaceholder;
	
//	.spiltpane{-fx-background-color: derive(#ff6666, 20%);}
//	.spiltpane{-fx-background-color: derive(#ffffb3, 20%);}
//	.spiltpane{-fx-background-color: derive(#ffa366, 20%);}
	public static final KeyCodeCombination KEY_MINMAX = new KeyCodeCombination(KeyCode.M, KeyCodeCombination.ALT_DOWN);
	public static final String[] colorWheel = {"-fx-background-color: derive(#008000, 20%);", "-fx-background-color: derive(#006080, 20%);", "-fx-background-color: derive(#b34700, 20%);"};
	private static int taskPointer = 0;
	private static int colorPointer = 7;

	public MainWindow() {
		super();
	}

	@Override
	public void setNode(Node node) {
		rootLayout = (VBox) node;
	}

	@Override
	public String getFxmlPath() {
		return FXML;
	}

	public static MainWindow load(Stage primaryStage, Config config, UserPrefs prefs, Logic logic) {

		MainWindow mainWindow = UiPartLoader.loadUiPart(primaryStage, new MainWindow());
		mainWindow.configure(config.getAppTitle(), config.getTaskTrackerName(), config, prefs, logic);
		return mainWindow;
	}

	private void configure(String appTitle, String taskTrackerName, Config config, UserPrefs prefs, Logic logic) {

		// Set dependencies
		this.logic = logic;
		this.taskTrackerName = taskTrackerName;
		this.config = config;
		this.userPrefs = prefs;
		MainWindow.colorPointer = userPrefs.getColourPointer();

		// Configure the UI
		setTitle(appTitle);
		setIcon(ICON);
		setWindowMinSize();
		setWindowDefaultSize(prefs);
		scene = new Scene(rootLayout);
		primaryStage.setScene(scene);
		
		setWindowStyle();
		// setAccelerators();
	}

	// private void setAccelerators() {
	// helpMenuItem.setAccelerator(KeyCombination.valueOf("F1"));
	// }

	void fillInnerParts() {
		taskListPanel = TaskListPanel.load(primaryStage, getTaskListPlaceholder(), logic.getFilteredTaskList());
		resultDisplay = ResultDisplay.load(primaryStage, getResultDisplayPlaceholder());
		statusBarFooter = StatusBarFooter.load(primaryStage, getStatusbarPlaceholder(),
				config.getTaskTrackerFilePath());
		commandBox = CommandBox.load(primaryStage, getCommandBoxPlaceholder(), resultDisplay, logic);
		listStatistics = ListStatistics.load(primaryStage, getListStatisticsPlaceholder(), logic);
		setInitialInputFocus();
		handleAllEvents();
		FxViewUtil.applyAnchorBoundaryParameters(rootLayout, 0.0, 0.0, 0.0, 0.0);
		splitpane.maxWidthProperty().multiply(0.5);
	}

	@FXML
	private void setInitialInputFocus() {
		commandBox.getCommandBoxTextField().requestFocus();
	}

	private AnchorPane getCommandBoxPlaceholder() {
		return commandBoxPlaceholder;
	}

	private AnchorPane getStatusbarPlaceholder() {
		return statusbarPlaceholder;
	}

	private AnchorPane getResultDisplayPlaceholder() {
		return resultDisplayPlaceholder;
	}

	public AnchorPane getTaskListPlaceholder() {
		return taskListPanelPlaceholder;
	}

	public AnchorPane getListStatisticsPlaceholder() {
		return listStatisticsPlaceholder;
	}

	public void hide() {
		primaryStage.hide();
	}

	private void setTitle(String appTitle) {
		primaryStage.setTitle(appTitle);
	}

	/**
	 * Sets the default size based on user preferences.
	 */
	protected void setWindowDefaultSize(UserPrefs prefs) {
		primaryStage.setHeight(prefs.getGuiSettings().getWindowHeight());
		primaryStage.setWidth(prefs.getGuiSettings().getWindowWidth());
		if (prefs.getGuiSettings().getWindowCoordinates() != null) {
			primaryStage.setX(prefs.getGuiSettings().getWindowCoordinates().getX());
			primaryStage.setY(prefs.getGuiSettings().getWindowCoordinates().getY());
		}
	}

	private void setWindowMinSize() {
		// primaryStage.setMinHeight(MIN_HEIGHT);
		// primaryStage.setMaximized(true);
	}

	/**
	 * Returns the current size and the position of the main Window.
	 */
	public GuiSettings getCurrentGuiSetting() {
		return new GuiSettings(primaryStage.getWidth(), primaryStage.getHeight(), (int) primaryStage.getX(),
				(int) primaryStage.getY());
	}

	public void handleHelp() {
		helpWindow = HelpWindow.load(primaryStage);
		helpWindow.show();
	}

	public void closeHelpWindow() {
		if (helpWindow != null)
			helpWindow.closeHelpWindow();
	}

	public void show() {
		primaryStage.show();
	}

	/**
	 * Closes the application.
	 */
	@FXML
	private void handleExit() {
		raise(new ExitAppRequestEvent());
	}

	public TaskListPanel getTaskListPanel() {
		return this.taskListPanel;
	}

	public void handleAllEvents() {
		handleMinimizeWindow();
	    handleChangeColourTheme();
		handleTaskListScrolling();
	}

	private void handleChangeColourTheme() {
	    handleF1Event();
	    handleF2Event();
    }
	
	private void handleMinimizeWindow() {
        rootLayout.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                primaryStage.setIconified(true);
            }
        }); 
    }
	
    private void handleF1Event() {
        rootLayout.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
            if (event.getCode() == KeyCode.F1) {
                if ((colorPointer + 1) > 7)
                    colorPointer = 0;
                else {
                    colorPointer = colorPointer + 1;
                    
                }
                setWindowStyle();
                userPrefs.updateColourPointer(MainWindow.colorPointer);
            }
        }); 
    }
    
    private void handleF2Event() {
        rootLayout.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
            if (event.getCode() == KeyCode.F2) {
                if ((colorPointer - 1) < 0)
                    colorPointer = 7;
                else {
                    colorPointer = colorPointer - 1;
                }
                setWindowStyle();
                userPrefs.updateColourPointer(MainWindow.colorPointer);
            }
        }); 
    }
    
    private void setWindowStyle(){
    	rootLayout.getStylesheets().clear();
    	switch(colorPointer){
    		case 0:	rootLayout.getStylesheets().add(getClass().getResource("/css/RedTheme.css").toExternalForm()); break;
    		
    		case 1:	rootLayout.getStylesheets().add(getClass().getResource("/css/OrangeTheme.css").toExternalForm()); break;
    		
    		case 2:	rootLayout.getStylesheets().add(getClass().getResource("/css/YellowTheme.css").toExternalForm()); break;
    		
    		case 3:	rootLayout.getStylesheets().add(getClass().getResource("/css/GreenTheme.css").toExternalForm()); break;
    		
    		case 4:	rootLayout.getStylesheets().add(getClass().getResource("/css/BlueTheme.css").toExternalForm()); break;
    		
    		case 5:	rootLayout.getStylesheets().add(getClass().getResource("/css/IndigoTheme.css").toExternalForm()); break;
    		
    		case 6:	rootLayout.getStylesheets().add(getClass().getResource("/css/VioletTheme.css").toExternalForm()); break;
    		
    		case 7:	rootLayout.getStylesheets().add(getClass().getResource("/css/DarkTheme.css").toExternalForm()); break;
    		
    		default:rootLayout.getStylesheets().add(getClass().getResource("/css/DarkTheme.css").toExternalForm()); break;
    		
    	}  
    	rootLayout.getStylesheets().add(getClass().getResource("/css/Extensions.css").toExternalForm());
    	
    }

    private void handleTaskListScrolling() {
		ListView<ReadOnlyTask> scrollList = taskListPanel.getTaskListView();
		handlePageUp(scrollList);
		handlePageDown(scrollList);
	}

	public void handlePageDown(ListView<ReadOnlyTask> scrollList) {

		rootLayout.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
			if (event.getCode() == KeyCode.PAGE_DOWN) {
				if ((taskPointer + 1) > logic.getFilteredTaskList().size() - 1)
				    taskPointer = 0;
				else {
                    taskPointer = taskPointer + 1;
				}
				System.out.println(taskPointer);
				scrollList.scrollTo(taskPointer);
			}
		});

	}
	
	//@@author A0142686X   
	public int getColourPointer() {
	    return colorPointer;
	}

	//@@author A0139422J
	public void handlePageUp(ListView<ReadOnlyTask> scrollList) {

		rootLayout.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
			if (event.getCode() == KeyCode.PAGE_UP) {
				if ((taskPointer - 1) < 0)
	                taskPointer = logic.getFilteredTaskList().size() - 1;
				else {
					taskPointer = taskPointer - 1;
				}
				System.out.println(taskPointer);
				scrollList.scrollTo(taskPointer);
			}
		});
	}

}
