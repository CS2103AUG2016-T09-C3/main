//@@author A0139422J
package main.ui;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import main.commons.util.AppUtil;
import main.commons.util.FxViewUtil;
import main.logic.Logic;
import main.model.Model;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

public class ListStatistics extends UiPart {

	private static final String FXML = "ListStatistics.fxml";
	
    @FXML
    private Label floatingtasks;	
    
	@FXML
	private Label alltasks;

	@FXML
	private Label eventtasks;

	@FXML
	private Label tomorrowtasks;

	@FXML
	private Label deadlinetasks;

	@FXML
	private Label todaytasks;

	@FXML
	private ImageView image;
	
	private static final String TODAY_TASK_MESSAGE = " <-: Tasks Due Today ";
	private static final String TOMORROW_TASK_MESSAGE = " <-: Tasks Due Tomorrow ";
	private static final String EVENT_TASK_MESSAGE = " <-: Events ";
	private static final String DEADLINE_TASK_MESSAGE = " <-: Deadlines ";
	private static final String FLOATING_TASK_MESSAGE = " <-: Floating ";
	private static final String ALL_TASK_MESSAGE = " <-: Total ";
	
	private Model model;
	
	private static Logic logic;
	
	private AnchorPane placeHolder;

	private static VBox mainPane;
	
	private static ListStatistics listDisplay;
	
	private StringProperty todaytaskNo = new SimpleStringProperty(""); 
	private StringProperty tomorrowtaskNo  = new SimpleStringProperty("");
	private StringProperty eventtaskNo  = new SimpleStringProperty("");
	private StringProperty deadlinetaskNo  = new SimpleStringProperty("");
	private StringProperty floatingtaskNo  = new SimpleStringProperty("");
	private StringProperty alltaskNo  = new SimpleStringProperty("");
	
	public static ListStatistics load(Stage primaryStage, AnchorPane placeHolder,Logic logic) {
		listDisplay = UiPartLoader.loadUiPart(primaryStage, placeHolder, new ListStatistics()); 
		ListStatistics.logic = logic;
		listDisplay.configure();
		return listDisplay;
	}

	public ListStatistics() {
		todaytasks = new Label();
		tomorrowtasks = new Label();
		eventtasks = new Label();
		deadlinetasks = new Label();
		floatingtasks = new Label();
		alltasks = new Label();
	}

	public void configure() {
		mainPane = new VBox();
		bindingAllStringProperty();
		initializeStringProperty();
		setListIcon();
		mainPane.getChildren().addAll(image, todaytasks, tomorrowtasks, eventtasks, deadlinetasks,floatingtasks, alltasks);
		mainPane.setSpacing(30.0);
		mainPane.setPadding(new Insets(30.0, 0.0, 30.0, 30.0));
		placeHolder.getChildren().add(mainPane);
		FxViewUtil.applyAnchorBoundaryParameters(mainPane, 0.0, 0.0, 0.0, 0.0);
		placeHolder.setMaxWidth(400);
	}

	private void bindingAllStringProperty() {
		todaytasks.textProperty().bind(todaytaskNo);
		tomorrowtasks.textProperty().bind(tomorrowtaskNo);
		eventtasks.textProperty().bind(eventtaskNo);
		deadlinetasks.textProperty().bind(deadlinetaskNo);
		floatingtasks.textProperty().bind(floatingtaskNo);
		alltasks.textProperty().bind(alltaskNo);
	}

	private void setListIcon() {
		image = new ImageView(AppUtil.getImage("/images/statistics.png"));
	}
	
	/**
	 * Remember to change both methods below on Wednesday when you meeet up to recombine.
	 * You would not be using logic to get the data. You will have to use the model component directly. Wait for Ruth
	 */
	private void initializeStringProperty() {
		todaytaskNo.setValue(logic.getNumToday() + TODAY_TASK_MESSAGE);
		tomorrowtaskNo.setValue(logic.getNumTmr() + TOMORROW_TASK_MESSAGE);
		eventtaskNo.setValue(logic.getNumEvent() + EVENT_TASK_MESSAGE);
		deadlinetaskNo.setValue(logic.getNumDeadline() +  DEADLINE_TASK_MESSAGE);
		floatingtaskNo.setValue(logic.getNumFloating() + FLOATING_TASK_MESSAGE);
		alltaskNo.setValue(logic.getFilteredTaskList().size() + ALL_TASK_MESSAGE);
	}
	
	public static void updateStatistics(){
		listDisplay.getTodayTaskNo().setValue(logic.getNumToday() + TODAY_TASK_MESSAGE);
		listDisplay.getTomorrowTaskNo().setValue(logic.getNumTmr() + TOMORROW_TASK_MESSAGE);
		listDisplay.getEventTaskNo().setValue(logic.getNumEvent() + EVENT_TASK_MESSAGE);
		listDisplay.getDeadlineTaskNo().setValue(logic.getNumDeadline() +  DEADLINE_TASK_MESSAGE);
		listDisplay.getFloatingTaskNo().setValue(logic.getNumFloating() + FLOATING_TASK_MESSAGE);
		listDisplay.getAllTaskNo().setValue(logic.getFilteredTaskList().size() + ALL_TASK_MESSAGE);
		
	}
	
	public static void updateListImage(String command){
//		listDisplay.getImage().setImage(AppUtil.getImage("/images/clock.png"));
	}
	
	public StringProperty getTodayTaskNo(){
		return todaytaskNo;
	}
	
	public StringProperty getTomorrowTaskNo(){
		return tomorrowtaskNo;
	}
	
	public StringProperty getEventTaskNo(){
		return eventtaskNo;
	}
	
	public StringProperty getDeadlineTaskNo(){
		return deadlinetaskNo;
	}
	
	public StringProperty getFloatingTaskNo(){
        return floatingtaskNo;
    }
	
	public Label getAllTasksLabel(){
		return alltasks;
	}
	
	public StringProperty getAllTaskNo(){
		return alltaskNo;
	}
	
	public ImageView getImage(){
		return image;
	}
		
	@Override
	public void setNode(Node node) {
		mainPane = (VBox) node;
	}

	@Override
	public void setPlaceholder(AnchorPane placeholder) {
		this.placeHolder = placeholder;
	}

	@Override
	public String getFxmlPath() {
		return FXML;
	}
}
