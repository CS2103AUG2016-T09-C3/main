//@@author A0139422J
package guitests.guihandles;

import guitests.GuiRobot;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import main.TestMain;
import main.model.task.ReadOnlyTask;
import main.model.task.Task;
import main.testutil.TestUtil;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.assertTrue;

/**
 * Provides a handle for the panel containing the person list.
 */
public class TaskListPanelHandle extends GuiHandle {

	public static final int NOT_FOUND = -1;
	public static final String CARD_PANE_ID = "#cardPane";

	private static final String TASKS_LIST_VIEW_ID = "#taskListView";

	public TaskListPanelHandle(GuiRobot guiRobot, Stage primaryStage) {
		super(guiRobot, primaryStage, TestMain.APP_TITLE);
	}

	public List<ReadOnlyTask> getSelectedPersons() {
		ListView<ReadOnlyTask> personList = getListView();
		return personList.getSelectionModel().getSelectedItems();
	}

	public ListView<ReadOnlyTask> getListView() {
		return (ListView<ReadOnlyTask>) getNode(TASKS_LIST_VIEW_ID);
	}

	/**
	 * Returns true if the list is showing the person details correctly and in
	 * correct order.
	 * 
	 * @param persons
	 *            A list of person in the correct order.
	 */
	public boolean isListMatching(ReadOnlyTask... tasks) {
		return this.isListMatching(0, tasks);
	}

	/**
	 * Clicks on the ListView.
	 */
	public void clickOnListView() {
		Point2D point = TestUtil.getScreenMidPoint(getListView());
		guiRobot.clickOn(point.getX(), point.getY());
	}

	/**
	 * Returns true if the {@code tasks} appear as the sub list (in that order)
	 * at position {@code startPosition}.
	 */
	public boolean containsInOrder(int startPosition, ReadOnlyTask... tasks) {
		List<ReadOnlyTask> tasksInList = getListView().getItems();

		return (checkIfListIsTooShort(tasksInList, startPosition, tasks)
				&& checkIfNotMatchingTask(tasksInList, startPosition, tasks) && true);
	}
/**
 * Returns false if any of the tasks doesn't match
 * @param tasksInList
 * @param startPosition
 * @param tasks
 */
	private boolean checkIfNotMatchingTask(List<ReadOnlyTask> tasksInList, int startPosition, ReadOnlyTask[] tasks) {
		for (int i = 0; i < tasks.length - 1; i++) {
			if (!tasksInList.get(startPosition + i).getMessage().equals(tasks[i].getMessage())) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Return false if the list in panel is too short to contain the given list
	 * 
	 * @param tasksInList
	 * 
	 * @param startPosition
	 * @param tasks
	 */
	private boolean checkIfListIsTooShort(List<ReadOnlyTask> tasksInList, int startPosition, ReadOnlyTask... tasks) {
		if (startPosition + tasks.length - 1 > tasksInList.size()) {
			return false;
		}
		return true;
	}

	/**
	 * Returns true if the list is showing the person details correctly and in
	 * correct order.
	 * 
	 * @param startPosition
	 *            The starting position of the sub list.
	 * @param tasks
	 *            A list of person in the correct order.
	 */
	public boolean isListMatching(int startPosition, ReadOnlyTask... tasks) throws IllegalArgumentException {

		if (tasks.length + startPosition != getListView().getItems().size()) {
			throw new IllegalArgumentException(
					"List size mismatched\n" + "Expected " + (getListView().getItems().size()) + " tasks");
		}
		assertTrue(this.containsInOrder(startPosition, tasks));
		return scrollAndCompareTask(startPosition, tasks);
		
	}
/**
 * Engages the guiRobot to scroll to the designated task and compare TaskCard with expected task
 * @param startPosition
 * @param tasks
 * @return
 */
	private boolean scrollAndCompareTask(int startPosition, ReadOnlyTask[] tasks) {
		for (int i = 0; i < tasks.length; i++) {
			final int scrollTo = i + startPosition;
			guiRobot.interact(() -> getListView().scrollTo(scrollTo));
			guiRobot.sleep(200);
			if (!TestUtil.compareCardAndTask(getTaskCardHandle(startPosition + i), tasks[i])) {
				return false;
			}
		}
		return true;
	}

	public TaskCardHandle navigateToTask(String input) {
		guiRobot.sleep(500); // Allow a bit of time for the list to be updated
		final Optional<ReadOnlyTask> task = getListView().getItems().stream().filter(p -> p.getMessage().equals(input))
				.findAny();
		if (!task.isPresent()) {
			throw new IllegalStateException("Task not found: " + input);
		}

		return navigateToTask(task.get());
	}

	/**
	 * Navigates the listview to display and select the task.
	 */
	public TaskCardHandle navigateToTask(ReadOnlyTask task) {
		int index = getTaskIndex(task);

		guiRobot.interact(() -> {
			getListView().scrollTo(index);
			guiRobot.sleep(150);
			getListView().getSelectionModel().select(index);
		});
		guiRobot.sleep(100);
		return getTaskCardHandle(task);
	}

	/**
	 * Returns the position of the person given, {@code NOT_FOUND} if not found
	 * in the list.
	 */
	public int getTaskIndex(ReadOnlyTask targetTask) {
		List<ReadOnlyTask> tasksInList = getListView().getItems();
		for (int i = 0; i < tasksInList.size(); i++) {
			if (tasksInList.get(i).getMessage().equals(targetTask.getMessage())) {
				return i;
			}
		}
		return NOT_FOUND;
	}

	/**
	 * Gets a person from the list by index
	 */
	public ReadOnlyTask getTask(int index) {
		return getListView().getItems().get(index);
	}

	public TaskCardHandle getTaskCardHandle(int index) {
		return getTaskCardHandle(new Task(getListView().getItems().get(index)));
	}

	public TaskCardHandle getTaskCardHandle(ReadOnlyTask task) {
		Set<Node> nodes = getAllCardNodes();
		Optional<Node> taskCardNode = nodes.stream()
				.filter(n -> new TaskCardHandle(guiRobot, primaryStage, n).isSameTask(task)).findFirst();
		if (taskCardNode.isPresent()) {
			return new TaskCardHandle(guiRobot, primaryStage, taskCardNode.get());
		} else {
			return null;
		}
	}

	protected Set<Node> getAllCardNodes() {
		return guiRobot.lookup(CARD_PANE_ID).queryAll();
	}

	public int getNumberOfTasks() {
		return getListView().getItems().size();
	}
}
