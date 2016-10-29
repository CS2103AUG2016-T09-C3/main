//@@author A0139422J
package main.ui;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import main.commons.core.LogsCenter;
import main.commons.events.ui.TaskPanelSelectionChangedEvent;
import main.model.task.ReadOnlyTask;

import java.util.logging.Logger;

/**
 * Panel containing the list of tasks. "person" keyword check done "addressbook"
 * keyword check done
 * 
 * @param ListView<ReadOnlyTask>
 *            taskListView
 * @author bey
 */
public class TaskListPanel extends UiPart {
	private final Logger logger = LogsCenter.getLogger(TaskListPanel.class);
	private static final String FXML = "TaskListPanel.fxml";
	private VBox panel;
	private AnchorPane placeHolderPane;
	private static int currentMaxListSize;

	@FXML
	private ListView<ReadOnlyTask> taskListView;

	public TaskListPanel() {
		super();
	}

	@Override
	public void setNode(Node node) {
		panel = (VBox) node;
	}

	@Override
	public String getFxmlPath() {
		return FXML;
	}

	@Override
	public void setPlaceholder(AnchorPane pane) {
		this.placeHolderPane = pane;
	}

	public static TaskListPanel load(Stage primaryStage, AnchorPane taskListPlaceholder,
			ObservableList<ReadOnlyTask> taskList) {
		TaskListPanel taskListPanel = UiPartLoader.loadUiPart(primaryStage, taskListPlaceholder, new TaskListPanel());
		taskListPanel.configure(taskList);
		return taskListPanel;
	}

	private void configure(ObservableList<ReadOnlyTask> taskList) {
		setConnections(taskList);
		addToPlaceholder();
	}

	private void setConnections(ObservableList<ReadOnlyTask> taskList) {
		taskListView.setItems(taskList);
		taskListView.setCellFactory(listView -> new TaskListViewCell());
		setEventHandlerForSelectionChangeEvent();
	}

	private void addToPlaceholder() {
		SplitPane.setResizableWithParent(placeHolderPane, true);
		placeHolderPane.getChildren().add(panel);
		panel.setMinWidth(450);
		panel.setMaxWidth(450);
		placeHolderPane.setMinWidth(450);
		placeHolderPane.setMaxWidth(450);
	}

	private void setEventHandlerForSelectionChangeEvent() {
		taskListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue != null) {
				logger.fine("Selection in task list panel changed to : '" + newValue + "'");
				raise(new TaskPanelSelectionChangedEvent(newValue));
			}
		});
	}

	public void scrollTo(int index) {
		Platform.runLater(() -> {
			taskListView.scrollTo(index);
			taskListView.getSelectionModel().clearAndSelect(index);
		});
	}

	class TaskListViewCell extends ListCell<ReadOnlyTask> {

		public TaskListViewCell() {
		}

		@Override
		protected void updateItem(ReadOnlyTask task, boolean empty) {
			super.updateItem(task, empty);

			if (empty || task == null) {
				setGraphic(null);
				setText(null);
			} else {
				setGraphic(TaskCard.load(task, getIndex() + 1).getLayout());
			}
		}
	}

	public ListView<ReadOnlyTask> getTaskListView() {
		return taskListView;
	}
}