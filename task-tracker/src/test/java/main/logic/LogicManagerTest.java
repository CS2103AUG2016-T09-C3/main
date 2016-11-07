//@@author A0144132W
package main.logic;

import main.commons.core.EventsCenter;
import main.commons.core.Messages;
import main.commons.events.model.TaskTrackerChangedEvent;
import main.commons.events.ui.ShowHelpRequestEvent;
import main.commons.util.DateUtil;
import main.logic.command.AddCommand;
import main.logic.command.CommandResult;
import main.logic.command.DeleteCommand;
import main.logic.command.DoneCommand;
import main.logic.command.EditCommand;
import main.logic.command.HelpCommand;
import main.logic.command.ListCommand;
import main.logic.command.RedoCommand;
import main.logic.command.SortCommand;
import main.logic.command.UndoCommand;
import main.logic.command.ExitCommand;
import main.logic.command.ClearCommand;
import main.model.Model;
import main.model.ModelManager;
import main.model.ReadOnlyTaskTracker;
import main.model.TaskTracker;
import main.model.UserPrefs;
import main.model.task.PriorityType;
import main.model.task.ReadOnlyTask;
import main.model.task.Task;
import main.model.task.TaskType;
import main.model.task.UniqueTaskList.DuplicateTaskException;
import static main.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static main.commons.core.Messages.MESSAGE_INVALID_INDEX;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.tuple.Triple;
import org.junit.*;
import com.google.common.eventbus.Subscribe;


public class LogicManagerTest {
    
    private Model model;
    private Logic logic;

    //These are for checking the correctness of the events raised
    private ReadOnlyTaskTracker latestSavedTaskTracker;
    private boolean helpShown;

    @Subscribe
    private void handleLocalModelChangedEvent(TaskTrackerChangedEvent ttce) {
        latestSavedTaskTracker = new TaskTracker(ttce.data);
    }

    @Subscribe
    private void handleShowHelpRequestEvent(ShowHelpRequestEvent she) {
        helpShown = true;
    }

    
    @Before
    public void setUp() {
        model = new ModelManager();
        logic = new LogicManager(model);

        EventsCenter.getInstance().registerHandler(this);

        latestSavedTaskTracker = new TaskTracker(model.getTaskTracker()); // last saved assumed to be up to date
        helpShown = false;
    }
    
    @After
    public void teardown() {
        EventsCenter.clearSubscribers();
    }
    
    private void assertCommandBehavior(String inputCommand, String expectedMessage) throws Exception {
        assertCommandBehavior(inputCommand, expectedMessage, new TaskTracker(), Collections.emptyList());
        String result = logic.execute(inputCommand).feedbackToUser;
        assertEquals(expectedMessage,result);
    }
    
    /**
     * Executes the command and confirms that the result message is correct and
     * also confirms that the following three parts of the LogicManager object's state are as expected:<br>
     *      - the internal task list data are same as those in the {@code expected TaskList} <br>
     *      - the backing list shown by UI matches the {@code shownList} <br>
     *      - {@code expectedTaskList} was saved to the storage file. <br>
     */
    private void assertCommandBehavior(String inputCommand, String expectedMessage,
                                       ReadOnlyTaskTracker expectedTaskTracker,
                                       List<? extends ReadOnlyTask> expectedShownList) throws Exception {

        //Execute the command
        CommandResult result = logic.execute(inputCommand);     

        if (inputCommand.equals("redo") || inputCommand.equals("undo")) {
            model.updateFilteredListToShowAllPending();
        }

        //Confirm the ui display elements should contain the right data
        assertEquals(expectedMessage, result.feedbackToUser);
        assertEquals(expectedShownList, model.getFilteredTaskList());

        //Confirm the state of data (saved and in-memory) is as expected
        assertEquals(expectedTaskTracker, model.getTaskTracker());
        assertEquals(expectedTaskTracker, latestSavedTaskTracker);
    }
    
    private void assertIncorrectIndexFormatBehaviorForCommand(String commandWord, String expectedMessage)
            throws Exception {
        assertCommandBehavior(commandWord , String.format(MESSAGE_INVALID_COMMAND_FORMAT, expectedMessage)); //index missing
        assertCommandBehavior(commandWord + " 0", String.format(MESSAGE_INVALID_INDEX, expectedMessage)); //index cannot be 0
        assertCommandBehavior(commandWord + " not_a_number", String.format(MESSAGE_INVALID_COMMAND_FORMAT, expectedMessage));
    }
    
    private void assertUndoRedoBehavior(TaskTracker expectedTaskTrackerUndo,
                                        TaskTracker expectedTaskTrackerRedo,
                                        List<? extends ReadOnlyTask> expectedShownListUndo,
                                        List<? extends ReadOnlyTask> expectedShownListRedo) throws Exception {
        assertCommandBehavior("undo", UndoCommand.MESSAGE_SUCCESS, expectedTaskTrackerUndo, expectedShownListUndo);
        assertCommandBehavior("redo", RedoCommand.MESSAGE_SUCCESS, expectedTaskTrackerRedo, expectedShownListRedo);
    }
    
    @Test
    public void execute_unknownCommandWord() throws Exception {
        String unknownCommand = "uicfhmowqewca";
        assertCommandBehavior(unknownCommand, String.format(MESSAGE_INVALID_COMMAND_FORMAT, Messages.MESSAGE_UNKNOWN_COMMAND));
    }
    
    @Test
    public void execute_invalid() throws Exception {
        String invalidCommand = "       ";
        assertCommandBehavior(invalidCommand,String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
    }
    
    @Test
    public void execute_help() throws Exception {
        assertCommandBehavior("help", new HelpCommand().execute().feedbackToUser);
        assertTrue(helpShown);
    }
    
    @Test
    public void execute_exit() throws Exception {
        assertCommandBehavior("exit", ExitCommand.MESSAGE_EXIT_ACKNOWLEDGEMENT);
    }
    
    @Test
    public void execute_clear() throws Exception {
        TestDataHelper helper = new TestDataHelper();
        helper.replaceModel(model, 3);

        assertCommandBehavior("clear", ClearCommand.MESSAGE_SUCCESS, new TaskTracker(), Collections.emptyList());
    }

    @Test
    public void execute_edit_invalidArgsFormat_errorMessageShown() throws Exception {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE);
        assertCommandBehavior("edit wrong", expectedMessage);
        assertCommandBehavior("edit Oct", expectedMessage);
        assertCommandBehavior("edit", expectedMessage);        
        assertIncorrectIndexFormatBehaviorForCommand(EditCommand.COMMAND_WORD, EditCommand.MESSAGE_USAGE);

    }

    @Test
    public void execute_done_invalidArgsFormat_errorMessageShown() throws Exception {
        assertIncorrectIndexFormatBehaviorForCommand(DoneCommand.COMMAND_WORD, DoneCommand.MESSAGE_USAGE);
    }
    
    @Test
    public void execute_delete_InvalidArgsFormat_errorMessageShown() throws Exception {
        assertIncorrectIndexFormatBehaviorForCommand(DeleteCommand.COMMAND_WORD, DeleteCommand.MESSAGE_USAGE);
    }
    
    @Test
    public void execute_list_invalidArgsFormat_errorMessageShown() throws Exception {
        String expectedMessage = String.format(Messages.MESSAGE_INVALID_PARAMETERS,"ListCommand", ListCommand.MESSAGE_USAGE);
        assertCommandBehavior("list wrong", expectedMessage);
    }
        
    @Test
    public void execute_sort_InvalidArgsFormat_errorMessageShown() throws Exception {
        String expectedMessage = String.format(Messages.MESSAGE_INVALID_PARAMETERS, SortCommand.COMMAND_WORD, SortCommand.MESSAGE_USAGE);
        assertCommandBehavior("sort", expectedMessage);
        assertCommandBehavior("sort 143", expectedMessage);
        assertCommandBehavior("sort nef", expectedMessage);
    }
    
    @Test
    public void execute_sort_upperCaseValidArgsFormat_successMessageShown() throws Exception {
        assertCommandBehavior("sort DATE", String.format(SortCommand.MESSAGE_SUCCESS, "date"));
        assertCommandBehavior("sort NAME", String.format(SortCommand.MESSAGE_SUCCESS, "name"));
    }
    
    @Test
    public void execute_sort_lowerCaseValidArgsFormat_successMessageShown() throws Exception {
        assertCommandBehavior("sort date", String.format(SortCommand.MESSAGE_SUCCESS, "date"));
        assertCommandBehavior("sort name", String.format(SortCommand.MESSAGE_SUCCESS, "name"));
    }

    @Test
    public void execute_add_emptyDescription_invalidArgsFormat_errorMessageShown() throws Exception {
        String expectedMessage = String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, Messages.MESSAGE_EMPTY_DESCRIPTION);
        assertCommandBehavior("add ", expectedMessage);
    }
    
    @Test
    public void execute_add_multiplePriority_invalidArgsFormat_errorMessageShown() throws Exception {
        String expectedMessage = String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, Messages.MESSAGE_MULTIPLE_PRIORITY);
        assertCommandBehavior("add something -h -l", expectedMessage);
    }
    
    @Test
    public void execute_addFloating_successful() throws Exception {
          
        TestDataHelper helper = new TestDataHelper();
        Task toBeAdded = helper.floating1();
        TaskTracker expectedTT = helper.addToTaskTracker(toBeAdded);
                    
        assertCommandBehavior(helper.generateAddCommand(toBeAdded),
                String.format(AddCommand.MESSAGE_SUCCESS, toBeAdded),
                expectedTT,
                expectedTT.getTaskList());
    }
    
    @Test
    public void execute_addDuplicate_notAllowed() throws Exception {
        // setup expectations
        TestDataHelper helper = new TestDataHelper();
        Task toBeAdded = helper.floating1();
        TaskTracker expectedTT = helper.addToTaskTracker(toBeAdded);
        model.addTask(toBeAdded); // duplicate
    
        // execute command and verify result
        assertCommandBehavior(
                helper.generateAddCommand(toBeAdded),
                AddCommand.MESSAGE_DUPLICATE_TASK,
                expectedTT,
                expectedTT.getTaskList());
    
    }         
      
    @Test
    public void execute_addDeadline_successful() throws Exception {
          
        TestDataHelper helper = new TestDataHelper();
        Task toBeAdded = helper.deadline1();
        TaskTracker expectedTT = helper.addToTaskTracker(toBeAdded);
        
        assertCommandBehavior(helper.generateAddCommand(toBeAdded),
                String.format(AddCommand.MESSAGE_SUCCESS, toBeAdded),
                expectedTT,
                expectedTT.getTaskList());
    }
      
    @Test
    public void execute_addEvent_successful() throws Exception {
          
        TestDataHelper helper = new TestDataHelper();
        Task toBeAdded = helper.event1();
        TaskTracker expectedTT = helper.addToTaskTracker(toBeAdded);
        
        assertCommandBehavior(helper.generateAddCommand(toBeAdded),
                String.format(AddCommand.MESSAGE_SUCCESS, toBeAdded),
                expectedTT,
                expectedTT.getTaskList());
    } 
      
    @Test
    public void execute_list_showsAllPendingTasks() throws Exception{
        TestDataHelper helper = new TestDataHelper();
        TaskTracker expectedTT = helper.generateTaskTracker(2);
        
        Model expectedModel = new ModelManager(expectedTT, new UserPrefs());
        expectedModel.updateFilteredListToShowAllPending();
        
        // prepare task tracker state
        helper.replaceModel(model, 2);
          
        assertCommandBehavior("list",
                String.format(ListCommand.MESSAGE_SUCCESS, "pending tasks"),
                expectedTT,
                expectedModel.getFilteredTaskList());
    }

    @Test
    public void execute_list_showsAllDoneTasks() throws Exception{
        TestDataHelper helper = new TestDataHelper();
        TaskTracker expectedTT = helper.generateTaskTracker(2, Collections.singletonList(helper.done_task()));
        
        Model expectedModel = new ModelManager(expectedTT, new UserPrefs());
        expectedModel.updateFilteredListToShowAllDone();

        // prepare task tracker state
        helper.replaceModel(model, 2, Collections.singletonList(helper.done_task()));
        
        assertCommandBehavior("list done", 
                String.format(ListCommand.MESSAGE_SUCCESS, "completed tasks"), 
                expectedTT,
                expectedModel.getFilteredTaskList());
    }
    
    @Test
    public void execute_list_showsChoosenPriorityTasks() throws Exception{
        TestDataHelper helper = new TestDataHelper();
        TaskTracker expectedTT = helper.generateTaskTracker(2, Collections.singletonList(helper.floating_high_priority()));
        Model expectedModel = new ModelManager(expectedTT, new UserPrefs());
        expectedModel.updateFilteredTaskList(Triple.of(PriorityType.HIGH, null, null), false, false);

        // prepare task tracker state
        helper.replaceModel(model, 2, Collections.singletonList(helper.floating_high_priority()));

        assertCommandBehavior("list high", 
                String.format(ListCommand.MESSAGE_SUCCESS, "pending high priority tasks"), 
                expectedTT,
                expectedModel.getFilteredTaskList());
    }
    
    @Test
    public void execute_list_showsMultipleParams() throws Exception{
        TestDataHelper helper = new TestDataHelper();
        TaskTracker expectedTT = helper.generateTaskTracker(2, Collections.singletonList(helper.deadline_low_priority()));
        Model expectedModel = new ModelManager(expectedTT, new UserPrefs());
        expectedModel.updateFilteredTaskList(Triple.of(PriorityType.LOW, null, TaskType.DEADLINE), false, false);

        // prepare task tracker state
        helper.replaceModel(model, 2, Collections.singletonList(helper.deadline_low_priority()));

        assertCommandBehavior("list low deadline", 
                String.format(ListCommand.MESSAGE_SUCCESS, "pending low priority tasks with deadlines"), 
                expectedTT,
                expectedModel.getFilteredTaskList());
    }
    
    @Test
    public void execute_undoRedoAdd_success() throws Exception {
        TestDataHelper helper = new TestDataHelper();
        TaskTracker expectedTT_undo = helper.generateTaskTracker(1);
        TaskTracker expectedTT_redo = helper.generateTaskTracker(2);

        helper.replaceModel(model, Arrays.asList());
        helper.addToModel(model, 2);
        
        assertUndoRedoBehavior(expectedTT_undo, expectedTT_redo, expectedTT_undo.getTaskList(), expectedTT_redo.getTaskList());
    }
    
    @Test
    public void execute_undoRedoDelete_success() throws Exception {
        TestDataHelper helper = new TestDataHelper();
        TaskTracker expectedTT_undo = helper.generateTaskTracker(3);
        TaskTracker expectedTT_redo = helper.generateTaskTracker(2);
        helper.replaceModel(model, 3);
        model.deleteTask(2);
        
        assertUndoRedoBehavior(expectedTT_undo, expectedTT_redo, expectedTT_undo.getTaskList(), expectedTT_redo.getTaskList());
    }
    
    @Test
    public void execute_undoRedoDone_success() throws Exception {
        TestDataHelper helper = new TestDataHelper();
        TaskTracker expectedTT = helper.generateTaskTracker(3);
        Model expectedModelRedo = new ModelManager(expectedTT, new UserPrefs());
        expectedModelRedo.doneTask(2);
        expectedModelRedo.updateFilteredListToShowAllPending();
        
        helper.replaceModel(model, 3);
        model.doneTask(2);
        assertUndoRedoBehavior(expectedTT, new TaskTracker(expectedModelRedo.getTaskTracker()), expectedTT.getTaskList(), expectedModelRedo.getFilteredTaskList());
    }
    
    @Test
    public void execute_undoRedoEdit_success() throws Exception {
        TestDataHelper helper = new TestDataHelper();
        TaskTracker expectedTT = helper.generateTaskTracker(3);
        Model expectedModelRedo = new ModelManager(expectedTT, new UserPrefs());
        expectedModelRedo.editTask(2, helper.deadline1());
        
        helper.replaceModel(model, 3);
        model.editTask(2, helper.deadline1());
        assertUndoRedoBehavior(expectedTT, new TaskTracker(expectedModelRedo.getTaskTracker()), expectedTT.getTaskList(), expectedModelRedo.getFilteredTaskList());
    }

    /**
     * A utility class to generate test data.
     */
    class TestDataHelper {

        protected Task floating1() {
            return new Task("go cycling", PriorityType.NORMAL);
        }

        protected Task floating_high_priority() {
            return new Task("wash dishes", PriorityType.HIGH);
        }
        
        protected Task deadline_low_priority() {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
            return new Task("collect welfare pack", DateUtil.defaultTime(cal.getTime()), PriorityType.LOW);
        }

        protected Task deadline1() {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
            return new Task("finish book", DateUtil.defaultTime(cal.getTime()), PriorityType.NORMAL);
        }

        protected Task event1() {
            Calendar cal1 = Calendar.getInstance();
            cal1.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
            Calendar cal2 = Calendar.getInstance();
            cal2.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
            return new Task("Jim's birthday", DateUtil.defaultTime(cal1.getTime()), DateUtil.defaultTime(cal2.getTime()),
                    PriorityType.NORMAL).setIsInferred(true);
        }
        
        protected Task done_task() {
            return new Task("sleep 12 hours", PriorityType.NORMAL).setDone(true);
        }

        protected String generateAddCommand(Task toAdd) {
            if (toAdd.getIsFloating()) {
                return "add " + toAdd.getMessage();
            } else if (toAdd.getIsEvent()) {
                return "add " + toAdd.getMessage() + " " + toAdd.getStartTimeString() + " to "
                        + toAdd.getEndTimeString();
            } else {
                return "add " + toAdd.getMessage() + " " + toAdd.getDeadlineString();
            }
        }

        /**
         * Generates a TaskTracker with auto-generated tasks.
         */
        protected TaskTracker generateTaskTracker(int numGenerated) throws Exception {
            TaskTracker taskTracker = new TaskTracker();
            addToTaskTracker(taskTracker, numGenerated);
            return taskTracker;
        }
        
        /**
         * Generates a TaskTracker with a number auto-generated tasks given by
         * numGenerated and adds the list of tasks provided to it.
         */
        protected TaskTracker generateTaskTracker(int numGenerated, List<Task> tasks) throws Exception {
            TaskTracker taskTracker = new TaskTracker();
            addToTaskTracker(taskTracker, numGenerated);
            addToTaskTracker(taskTracker, tasks);
            return taskTracker;
        }

        /**
         * Generates a TaskTracker based on the list of Tasks given.
         */
        protected TaskTracker generateTaskTracker(List<Task> tasks) throws Exception {
            TaskTracker taskTracker = new TaskTracker();
            addToTaskTracker(taskTracker, tasks);
            return taskTracker;
        }

        /**
         * Adds auto-generated Task objects to the given TaskTracker
         * 
         * @param taskTracker
         *            The TaskTracker to which the Tasks will be added
         */
        protected void addToTaskTracker(TaskTracker taskTracker, int numGenerated) throws Exception {
            addToTaskTracker(taskTracker, generateTaskList(numGenerated));
        }

        /**
         * Adds the given list of Tasks to the given TaskTracker
         */
        protected void addToTaskTracker(TaskTracker taskTracker, List<Task> tasksToAdd) throws Exception {
            for (Task p : tasksToAdd) {
                taskTracker.addTask(p);
            }
        }

        protected TaskTracker addToTaskTracker(Task toBeAdded) throws DuplicateTaskException {
            TaskTracker expectedTT = new TaskTracker();
            expectedTT.addTask(toBeAdded);
            return expectedTT;
        }

        /**
         * Adds auto-generated Task objects to the given model
         * 
         * @param model
         *            The model to which the Tasks will be added
         */
        protected void addToModel(Model model, int numGenerated) throws Exception {
            addToModel(model, generateTaskList(numGenerated));
        }

        /**
         * Adds the given list of Tasks to the given model
         */
        protected void addToModel(Model model, List<Task> tasksToAdd) throws Exception {
            for (Task p : tasksToAdd) {
                model.addTask(p);
            }
        }

        /**
         * Adds the given number of auto-generated Task objects and 
         * given list of Tasks to the given model
         */
        protected void addToModel(Model model, int numGenerated, List<Task> tasksToAdd) throws Exception {
            addToModel(model, numGenerated);
            addToModel(model, tasksToAdd);
        }
        
        /**
         * Replaces a Model Object with the given number of auto-generated
         * Task objects
         */
        protected void replaceModel(Model model, int numGenerated) throws Exception {
            model.resetData(generateTaskTracker(numGenerated));
        }
        
        /**
         * Replaces a Model Object with the given list of Tasks
         */
        protected void replaceModel(Model model, List<Task> tasksToAdd) throws Exception {
            model.resetData(generateTaskTracker(tasksToAdd));
        }
        
        /**
         * Replaces the Model data with the given number of auto-generated
         * Task objects and given list of Tasks
         */
        protected void replaceModel(Model model, int numGenerated, List<Task> tasksToAdd) throws Exception {
            model.resetData(generateTaskTracker(numGenerated, tasksToAdd));
        }
        
        /**
         * Generates a valid task using the given seed. Running this function
         * with the same parameter values guarantees the returned task will have
         * the same state. Each unique seed will generate a unique Task object.
         *
         * @param seed
         *            used to generate the task data field values
         */
        protected Task generateTask(int seed) throws Exception {
            return new Task("Task " + seed, PriorityType.NORMAL);
        }

        /**
         * Generates a list of Tasks based on the flags.
         */
        protected List<Task> generateTaskList(int numGenerated) throws Exception {
            List<Task> tasks = new ArrayList<>();
            for (int i = 1; i <= numGenerated; i++) {
                tasks.add(generateTask(i));
            }
            return tasks;
        }

        protected List<Task> generateTaskList(Task... tasks) {
            return Arrays.asList(tasks);
        }
    }

}
