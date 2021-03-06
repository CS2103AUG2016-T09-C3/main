package main.testutil;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
//import com.google.common.io.Files;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
//import org.testfx.api.FxToolkit;
import junit.framework.AssertionFailedError;
import main.TestMain;
import main.commons.exceptions.IllegalValueException;
import main.commons.util.FileUtil;
import main.commons.util.XmlUtil;
import main.model.TaskTracker;
import main.model.filter.SortCriteria;
import main.model.filter.SortFilter;
import main.model.task.PriorityType;
//import main.model.task.Deadline;
//import main.model.task.Event;
//import main.model.task.FloatingTask;
import main.model.task.ReadOnlyTask;
import main.model.task.Task;
import main.model.task.UniqueTaskList;
import main.storage.XmlSerializableTaskTracker;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

import com.google.common.io.Files;

import guitests.guihandles.TaskCardHandle;

/**
 * A utility class for test cases.
 */
public class TestUtil {

	public static String LS = System.lineSeparator();

	public static void assertThrows(Class<? extends Throwable> expected, Runnable executable) {
		try {
			executable.run();
		} catch (Throwable actualException) {
			if (!actualException.getClass().isAssignableFrom(expected)) {
				String message = String.format("Expected thrown: %s, actual: %s", expected.getName(),
						actualException.getClass().getName());
				throw new AssertionFailedError(message);
			} else
				return;
		}
		throw new AssertionFailedError(
				String.format("Expected %s to be thrown, but nothing was thrown.", expected.getName()));
	}

	/**
	 * Folder used for temp files created during testing. Ignored by Git.
	 */
	public static String SANDBOX_FOLDER = FileUtil.getPath("./src/test/data/sandbox/");

	public static final Task[] sampleTaskData = getSampleTaskData();

	private static Task[] getSampleTaskData() {
		// try {
		return new Task[] { new Task("Get a girlfriend", PriorityType.HIGH),
				new Task("Get a boyfriend", PriorityType.HIGH), new Task("Take a long nap", PriorityType.HIGH),
				new Task(("Plan for SEP"), setDate(2016, 10, 17, 23, 59), PriorityType.NORMAL),
				new Task("Complete code", setDate(2016, 11, 7, 23, 30), PriorityType.LOW),
				new Task("birthday party", setDate(2016, 11, 20, 12, 0), setDate(2016, 11, 20, 20, 0),
						PriorityType.NORMAL),
				new Task("hackathon", setDate(2016, 12, 11, 8, 0), setDate(2016, 12, 13, 18, 0), PriorityType.NORMAL),
				new Task("travel Japan", setDate(2016, 12, 20, 0, 0), setDate(2017, 1, 5, 0, 0), PriorityType.NORMAL) };
		// } catch (IllegalValueException e) {
		// assert false;
		// //not possible
		// return null;
		// }
	}

	public static Date setDate(int year, int month, int day, int hour, int minute) {
		Calendar cal = Calendar.getInstance();
		cal.set(year, month, day, hour, minute);
		return cal.getTime();
	}

	public static List<Task> generateSampleTaskData() {
		return Arrays.asList(sampleTaskData);
	}

	/**
	 * Appends the file name to the sandbox folder path. Creates the sandbox
	 * folder if it doesn't exist.
	 * 
	 * @param fileName
	 * @return
	 */
	public static String getFilePathInSandboxFolder(String fileName) {
		try {
			FileUtil.createDirs(new File(SANDBOX_FOLDER));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return SANDBOX_FOLDER + fileName;
	}

	public static void createDataFileWithSampleData(String filePath) {
		createDataFileWithData(generateSampleStorageTaskTracker(), filePath);
	}

	public static <T> void createDataFileWithData(T data, String filePath) {
		try {
			File saveFileForTesting = new File(filePath);
			FileUtil.createIfMissing(saveFileForTesting);
			XmlUtil.saveDataToFile(saveFileForTesting, data);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static void main(String... s) {
		createDataFileWithSampleData(TestMain.SAVE_LOCATION_FOR_TESTING);
	}

	public static TaskTracker generateEmptyTaskTracker() {
		return new TaskTracker(new UniqueTaskList());
	}

	public static XmlSerializableTaskTracker generateSampleStorageTaskTracker() {
		return new XmlSerializableTaskTracker(generateEmptyTaskTracker());
	}

	/**
	 * Tweaks the {@code keyCodeCombination} to resolve the
	 * {@code KeyCode.SHORTCUT} to their respective platform-specific keycodes
	 */
	public static KeyCode[] scrub(KeyCodeCombination keyCodeCombination) {
		List<KeyCode> keys = new ArrayList<>();
		if (keyCodeCombination.getAlt() == KeyCombination.ModifierValue.DOWN) {
			keys.add(KeyCode.ALT);
		}
		if (keyCodeCombination.getShift() == KeyCombination.ModifierValue.DOWN) {
			keys.add(KeyCode.SHIFT);
		}
		if (keyCodeCombination.getMeta() == KeyCombination.ModifierValue.DOWN) {
			keys.add(KeyCode.META);
		}
		if (keyCodeCombination.getControl() == KeyCombination.ModifierValue.DOWN) {
			keys.add(KeyCode.CONTROL);
		}
		keys.add(keyCodeCombination.getCode());
		return keys.toArray(new KeyCode[] {});
	}

	public static boolean isHeadlessEnvironment() {
		String headlessProperty = System.getProperty("testfx.headless");
		return headlessProperty != null && headlessProperty.equals("true");
	}

	// public static void captureScreenShot(String fileName) {
	// File file = GuiTest.captureScreenshot();
	// try {
	// Files.copy(file, new File(fileName + ".png"));
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// }

	public static String descOnFail(Object... comparedObjects) {
		return "Comparison failed \n"
				+ Arrays.asList(comparedObjects).stream().map(Object::toString).collect(Collectors.joining("\n"));
	}

	public static void setFinalStatic(Field field, Object newValue)
			throws NoSuchFieldException, IllegalAccessException {
		field.setAccessible(true);
		// remove final modifier from field
		Field modifiersField = Field.class.getDeclaredField("modifiers");
		modifiersField.setAccessible(true);
		// ~Modifier.FINAL is used to remove the final modifier from field so
		// that its value is no longer
		// final and can be changed
		modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
		field.set(null, newValue);
	}

	/*
	 * public static void initRuntime() throws TimeoutException {
	 * FxToolkit.registerPrimaryStage(); FxToolkit.hideStage(); }
	 * 
	 * public static void tearDownRuntime() throws Exception {
	 * FxToolkit.cleanupStages(); }
	 */

	/**
	 * Gets private method of a class Invoke the method using
	 * method.invoke(objectInstance, params...)
	 *
	 * Caveat: only find method declared in the current Class, not inherited
	 * from supertypes
	 */
	public static Method getPrivateMethod(Class objectClass, String methodName) throws NoSuchMethodException {
		Method method = objectClass.getDeclaredMethod(methodName);
		method.setAccessible(true);
		return method;
	}

	public static void renameFile(File file, String newFileName) {
		try {
			Files.copy(file, new File(newFileName));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * Gets mid point of a node relative to the screen.
	 * 
	 * @param node
	 * @return
	 */
	public static Point2D getScreenMidPoint(Node node) {
		double x = getScreenPos(node).getMinX() + node.getLayoutBounds().getWidth() / 2;
		double y = getScreenPos(node).getMinY() + node.getLayoutBounds().getHeight() / 2;
		return new Point2D(x, y);
	}

	/**
	 * Gets mid point of a node relative to its scene.
	 * 
	 * @param node
	 * @return
	 */
	public static Point2D getSceneMidPoint(Node node) {
		double x = getScenePos(node).getMinX() + node.getLayoutBounds().getWidth() / 2;
		double y = getScenePos(node).getMinY() + node.getLayoutBounds().getHeight() / 2;
		return new Point2D(x, y);
	}

	/**
	 * Gets the bound of the node relative to the parent scene.
	 * 
	 * @param node
	 * @return
	 */
	public static Bounds getScenePos(Node node) {
		return node.localToScene(node.getBoundsInLocal());
	}

	public static Bounds getScreenPos(Node node) {
		return node.localToScreen(node.getBoundsInLocal());
	}

	public static double getSceneMaxX(Scene scene) {
		return scene.getX() + scene.getWidth();
	}

	public static double getSceneMaxY(Scene scene) {
		return scene.getX() + scene.getHeight();
	}

	public static Object getLastElement(List<?> list) {
		return list.get(list.size() - 1);
	}

	/**
	 * Removes a subset from the list of tasks.
	 * 
	 * @param tasks
	 *            The list of tasks
	 * @param tasksToRemove
	 *            The subset of tasks.
	 * @return The modified tasks after removal of the subset from tasks.
	 */
	public static TestTask[] removeTasksFromList(final TestTask[] tasks, TestTask... tasksToRemove) {
		List<TestTask> listOfTasks = asList(tasks);
		listOfTasks.removeAll(asList(tasksToRemove));
		listOfTasks = sortList(listOfTasks,0);
		return listOfTasks.toArray(new TestTask[listOfTasks.size()]);
	}

	/**
	 * Returns a copy of the list with the task at specified index removed.
	 * 
	 * @param list
	 *            original list to copy from
	 * @param targetIndexInOneIndexedFormat
	 *            e.g. if the first element to be removed, 1 should be given as
	 *            index.
	 */
	public static TestTask[] removeTaskFromList(final TestTask[] list, int targetIndexInOneIndexedFormat) {
		return removeTasksFromList(list, list[targetIndexInOneIndexedFormat - 1]);
	}

	/**
	 * Replaces tasks[i] with a task.
	 * 
	 * @param tasks
	 *            The array of tasks.
	 * @param task
	 *            The replacement task
	 * @param index
	 *            The index of the task to be replaced.
	 * @return
	 */
	public static TestTask[] replaceTaskFromList(TestTask[] tasks, TestTask task, int index) {
		tasks[index] = task;
		return tasks;
	}

	/**
	 * Appends tasks to the array of tasks.
	 * 
	 * @param tasks
	 *            A array of tasks.
	 * @param tasksToAdd
	 *            The tasks that are to be appended behind the original array.
	 * @return The modified array of tasks.
	 */
	public static TestTask[] addTasksToList(final TestTask[] tasks, TestTask... tasksToAdd) {
		List<TestTask> listOfTasks = asList(tasks);
		listOfTasks.addAll(asList(tasksToAdd));
		listOfTasks = sortList(listOfTasks,0);
		return listOfTasks.toArray(new TestTask[listOfTasks.size()]);
	}
	
	private static List<TestTask> sortList(List<TestTask> tasks, int indicator){
		
		Comparator<TestTask> byTime = (t1, t2) -> t1.compareTime(t2);
		
		Comparator<TestTask> byName = (t1, t2) -> t1.getMessage().toLowerCase().compareTo(t2.getMessage().toLowerCase());
		
		switch(indicator){
			case 0: Collections.sort(tasks, byTime.thenComparing(byName)); break;
			
			case 1: Collections.sort(tasks,byName); break;
			
			case 2: Collections.sort(tasks,byTime);break;
		
		}
		return tasks;
		
	}
	
	public static TestTask[] sortTasksByName(final TestTask[] tasks) {
		List<TestTask> listOfTasks = asList(tasks);
		listOfTasks = sortList(listOfTasks,1);
		return listOfTasks.toArray(new TestTask[listOfTasks.size()]);
	}
	
	public static TestTask[] sortTasksByDate(final TestTask[] tasks) {
		List<TestTask> listOfTasks = asList(tasks);
		listOfTasks = sortList(listOfTasks,2);
		return listOfTasks.toArray(new TestTask[listOfTasks.size()]);
	}

    private static <T> List<T> asList(T[] objs) {
        List<T> list = new ArrayList<>();
        for(T obj : objs) {
            list.add(obj);
        }
        return list;
    }

	// TODO when guitests are up

	public static boolean compareCardAndTask(TaskCardHandle card, ReadOnlyTask task) {
		return card.isSameTask(task);
	}

}
