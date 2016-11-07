//@@author A0139422J
package main.ui;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import main.model.task.PriorityType;
import main.model.task.ReadOnlyTask;
import main.model.task.TaskType;

/**
 * Display individual panels within TaskListPanel with the details of each
 * specific task
 */
public class TaskCard extends UiPart {

	private static final String FXML = "TaskListCard.fxml";

	@FXML
	private HBox cardPane;
	@FXML
	private Label id;
	@FXML
	private Label message;
	@FXML
	private Label deadline;

	@FXML
	private Label recurring;

	@FXML
	private Rectangle priorityTab;

	private ReadOnlyTask task;
	private int displayedIndex;

	public TaskCard() {

	}

	public static TaskCard load(ReadOnlyTask task, int displayedIndex) {
		TaskCard card = new TaskCard();
		card.task = task;
		card.displayedIndex = displayedIndex;
		return UiPartLoader.loadUiPart(card);
	}

	@FXML
	public void initialize() {

		configureLayout();
		setTaskCardText();
		setPriorityTabColour();

	}

	private void setTaskCardText() {

		message.setText(task.getMessage());
		message.setWrapText(true);

		id.setText(displayedIndex + ". ");

		if (task.getType() == TaskType.EVENT)
			deadline.setText("Start: " + task.getStartTimeString() + "\nEnd: " + task.getEndTimeString());
		else if (task.getType() == TaskType.DEADLINE)
			deadline.setText("Deadline: " + task.getDeadlineString());
		else
			deadline.setText("");

		if (task.getIsRecurring())
			recurring.setText("Weekly");
		else
			recurring.setText("");
	}

	private void setPriorityTabColour() {

		if (task.getPriority() == PriorityType.HIGH) {
			priorityTab.setFill(Color.RED);
			if (task.getIsDone()) {
	             cardPane.setStyle("-fx-background-color: #bd5252;");
			}
			else {
	             cardPane.setStyle("-fx-background-color: #ff6666;");
			}
		} else if (task.getPriority() == PriorityType.LOW) {
			priorityTab.setFill(Color.YELLOWGREEN);
			if (task.getIsDone()) {
				cardPane.setStyle("-fx-background-color: #cfcf82;");
			}
			else {
				cardPane.setStyle("-fx-background-color: #ffffb3;");
			}
		}
		else {
			priorityTab.setFill(Color.rgb(255, 117, 26));
			if (task.getIsDone()) {
				cardPane.setStyle("-fx-background-color: #d58551;");
			}
			else {
				cardPane.setStyle("-fx-background-color: #ffa366;");
			}
		}

		if (task.isOverdue()) {
			priorityTab.setFill(Color.BLACK);
		}

		priorityTab.setStroke(Color.TRANSPARENT);
	}

	private void configureLayout() {
		cardPane.setMinWidth(0);
	}

	public HBox getLayout() {
		return cardPane;
	}

	@Override
	public void setNode(Node node) {
		cardPane = (HBox) node;
	}

	@Override
	public String getFxmlPath() {
		return FXML;
	}
}
