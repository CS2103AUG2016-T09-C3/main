//@@author A0144132W
// adapted from addressbook level 4
package main.model;

import main.commons.core.ComponentManager;
import main.commons.core.EventsCenter;
import main.commons.core.LogsCenter;
import main.commons.core.UnmodifiableObservableList;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Stack;
import java.util.logging.Logger;

import org.apache.commons.lang3.tuple.Triple;

import com.google.common.collect.Lists;
import com.google.common.eventbus.Subscribe;

import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import main.commons.events.model.ChangeSortFilterEvent;
import main.commons.events.model.LoadTaskTrackerEvent;
import main.commons.events.model.TaskTrackerChangedEvent;
import main.commons.events.model.UpdateListWithSuggestionsEvent;
import main.commons.util.DateUtil;

import main.logic.command.UndoCommand;
import main.model.TaskTracker;
import main.model.filter.SortCriteria;
import main.model.filter.SortFilter;
import main.model.task.PriorityType;
import main.model.task.ReadOnlyTask;
import main.model.task.Task;
import main.model.task.TaskType;
import main.model.task.UniqueTaskList.DuplicateTaskException;
import main.model.task.UniqueTaskList.TaskNotFoundException;

public class ModelManager extends ComponentManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);
    
    public static Stack<UndoHistory> undoStack;
    public static Stack<UndoHistory> redoStack;
    
    private TaskTracker taskTracker;
    private final FilteredList<Task> filteredTasks;
    private final SortedList<Task> sortedTasks;
    private Expression baseExpression;
    private Expression save;
    
    public ModelManager(TaskTracker taskTracker, UserPrefs userPref) {
        super();
        assert taskTracker != null;
        assert userPref != null;

        logger.fine("Initializing with task tracker: " + taskTracker + " and user prefs " + userPref);
        EventsCenter.getInstance().registerHandler(this);
        
        this.taskTracker = new TaskTracker(taskTracker);
        filteredTasks = new FilteredList<>(this.taskTracker.getTasks());
        sortedTasks = new SortedList<>(this.filteredTasks);
        sortDefault();
        
        undoStack = new Stack<UndoHistory>();
        redoStack = new Stack<UndoHistory>();
    }
        
    public ModelManager() {
        this(new TaskTracker(), new UserPrefs());
    }

    @Override
    public void resetData(ReadOnlyTaskTracker newData) {
        if(newData.getTaskList().size()==0){
            List<ReadOnlyTask> prevTasks = taskTracker.getTaskList();
            addToUndoStack(UndoCommand.CLR, prevTasks.toArray(new Task [prevTasks.size()]));
        }
        taskTracker.resetData(newData);
        indicateTaskTrackerChanged();
    }
    
    public void setTasks(List<Task> tasks) {
        this.taskTracker.getUniqueTaskList().getInternalList().setAll(tasks);
}

    @Override
    public ReadOnlyTaskTracker getTaskTracker() {
        return taskTracker;
    }

    @Override
    public synchronized void deleteTask(int index) throws TaskNotFoundException {
        ReadOnlyTask target = getTaskfromIndex(index);
        taskTracker.removeTask(target);
        indicateTaskTrackerChanged();
        addToUndoStack(UndoCommand.DEL, (Task)target);
    }
    
    @Override
    public synchronized void doneTask(int index) throws TaskNotFoundException {
        ReadOnlyTask target = getTaskfromIndex(index);
        taskTracker.doneTask(target);
        indicateTaskTrackerChanged();
        addToUndoStack(UndoCommand.DONE,(Task)target);
    }
    
    //@@author A0142686X
    @Override
    public synchronized void editTask(int index, Task newtask) throws TaskNotFoundException, DuplicateTaskException {
        addToUndoStack(UndoCommand.EDIT, getTaskfromIndex(index), newtask);  //NEED TO CHECK ORDER
        //taskTracker.editTask(index, newtask);
        deleteTaskUndo(getTaskfromIndex(index));
        addTaskUndo(newtask);
        updateFilteredListToShowAllPending();
        indicateTaskTrackerChanged();
        
    }
    
    //@@author A0144132W
    /** Raises an event to indicate the model has changed */
    private void indicateTaskTrackerChanged() {
        raise(new TaskTrackerChangedEvent(taskTracker));
}

    @Override
    public synchronized void addTask(Task task) throws DuplicateTaskException {
        taskTracker.addTask(task);
        updateFilteredListToShowAllPending();
        indicateTaskTrackerChanged();
        addToUndoStack(UndoCommand.ADD, task);
    }

    //================== Loading from storage =============================================
    @Override
    @Subscribe
    public void handleLoadTaskTrackerEvent(LoadTaskTrackerEvent event) {
        resetData(event.getTaskTracker());
    }
    
    //=========== Sorting ===================================================================
    @Override
    public void sortBy(SortCriteria criteria) {
        sortedTasks.setComparator(new SortFilter(criteria).getComparator());
    }
    
    @Override
    public void sortDefault() {
        sortBy(SortCriteria.TIME);
    }
    
    @Subscribe
    public void handleChangeSortFilterEvent(ChangeSortFilterEvent event) {
        sortBy(event.getSortCriteria());
    }
    
    //=========== User Friendly Accessors ===================================================================
    
    @Override
    public int getNumOverdue() {
        Expression original = baseExpression;
        updateFilteredTaskList(Triple.of(null, null, null), false, true);
        return getSizeAndReset(original);
    }
    
    @Override
    public int getNumToday() {
        Expression original = baseExpression;
        updateFilteredTaskList(Triple.of(null, DateUtil.getToday(), null), false, false);
        return getSizeAndReset(original);
    }
    
    @Override
    public int getNumTmr() {
        Expression original = baseExpression;
        updateFilteredTaskList(Triple.of(null, DateUtil.getTmr(), null), false, false);
        return getSizeAndReset(original);
    }
    
    @Override
    public int getNumEvent() {
        Expression original = baseExpression;
        updateFilteredTaskList(Triple.of(null, null, TaskType.EVENT), false, false);
        return getSizeAndReset(original);  
    }
    
    @Override
    public int getNumDeadline() {
        Expression original = baseExpression;
        updateFilteredTaskList(Triple.of(null, null, TaskType.DEADLINE), false, false);
        return getSizeAndReset(original);         
    }
    
    @Override
    public int getNumFloating() {
        Expression original = baseExpression;
        updateFilteredTaskList(Triple.of(null, null, TaskType.FLOATING), false, false);
        return getSizeAndReset(original); 
    }
    
    @Override 
    public int getTotalNum() {
        Expression original = baseExpression;
        updateFilteredListToShowAllPending();
        return getSizeAndReset(original);
    }

    private int getSizeAndReset(Expression original) {
        int num = filteredTasks.size();
        if (original == null) updateFilteredListToShowAllPending();
        else updateFilteredTaskList(original);
        return num;
    }
    

    //=========== Filtered Task List Accessors ===============================================================

    @Override
    public UnmodifiableObservableList<ReadOnlyTask> getFilteredTaskList() {
        return new UnmodifiableObservableList<>(sortedTasks);        
    }

    @Override
    public void updateFilteredListToShowAllPending() {
        Expression filter = new PredicateExpression();
        filter.and(new DoneQualifier(false));
        
        updateFilteredTaskList(filter);
        baseExpression = filter;
    }
    
    @Override
    public void updateFilteredListToShowAllDone() {
        Expression filter = new PredicateExpression();
        filter.and(new DoneQualifier(true));
        
        updateFilteredTaskList(filter);
        baseExpression = filter;
    }
    
    
    @Override
    public void updateFilteredTaskList(Triple<PriorityType, Date, TaskType> params, boolean isDone, boolean onlyOverdue) {
        Expression filter = new PredicateExpression();
        
        filter.and(new DoneQualifier(isDone));
        
        if (params.getLeft() != null) {
            filter.and(new PriorityQualifier(params.getLeft()));
        }
        if (params.getMiddle() != null) {
            filter.and(new DateQualifier(params.getMiddle()));        
        }
        if (params.getRight() != null) {
            filter.and(new TypeQualifier(params.getRight()));
        }
        
        if (onlyOverdue) {
            filter.and(new OverdueQualifier());
        }
        
        updateFilteredTaskList(filter);
        baseExpression = filter;
    }
    
    public void updateFilteredTaskList(Expression expression) {
        baseExpression = expression;
        filteredTasks.setPredicate(expression::satisfies);
    }
    
    /*
     * saves a copy of the current filter. This filter is applied 
     * back when revertFilter() is called
     */
    @Override
    public void saveFilter() {
        save = baseExpression;
        logger.info("saved current filter");
    }
    
    /*
     * reverts filter to the one saved previous. If no copy was 
     * saved, the list is unchanged
     */
    @Override
    public void revertFilter() {
        if (save == null) {
            return;
        }
        baseExpression = save;
        updateFilteredTaskList(baseExpression);
        logger.info("reverted to saved filter");
    }
    
    //============= AutoComplete Suggestions ========================================
    
    @Override
    @Subscribe
    public void handleUpdateSuggestionsEvent(UpdateListWithSuggestionsEvent event) {
        List<ReadOnlyTask> suggestions = event.getSuggestions();

        Expression filter = new PredicateExpression();
        filter.and(new MatchQualifier(suggestions));
        updateFilteredTaskList(filter);
    }
    
    //========== Inner classes/interfaces used for filtering =================================================
    
    interface Expression {
        boolean satisfies(ReadOnlyTask task);
        void and(Qualifier qualifier);
        String toString();
    }

    private class PredicateExpression implements Expression {

        private List<Qualifier> qualifier = Lists.newArrayList();
        
        public PredicateExpression() {}
        
        @Override
        public void and(Qualifier qualifier) {
            this.qualifier.add(qualifier);
        }

        @Override
        public boolean satisfies(ReadOnlyTask task) {
            boolean qualify = true;
            
            for (Qualifier q : qualifier) {
                qualify = qualify && q.run(task); 
            }
            
            return qualify;
        }

        @Override
        public String toString() {
            return qualifier.toString();
        }
    }       

    interface Qualifier {
        boolean run(ReadOnlyTask task);
        String toString();
    }

    private class DateQualifier implements Qualifier {
        private Date date;

        DateQualifier(Date date) {
            this.date = date;
        }

        @Override
        public boolean run(ReadOnlyTask task) {
//            return date.stream()
//                    .filter(keyword -> StringUtil.containsWordIgnoreCase(person.getName().fullName, keyword))
//                    .findAny()
//                    .isPresent();
//            return ((compareDate(date, task.getDeadline())) || (compareDate(date, task.getEndTime())) || (compareDate(date, task.getStartTime())));
            if (task.getIsEvent()) return DateUtil.dateOverlap(task.getStartTime(), task.getEndTime(), date);
            else if (!task.getIsFloating() && !task.getIsEvent()) return DateUtil.areSameDay(date, task.getDeadline()); 
            else return false;
        }


        @Override
        public String toString() {
            return ((new SimpleDateFormat("dd MMM")).format(this.date));
        }
    }
    
    private class TypeQualifier implements Qualifier {
        private TaskType type;

        TypeQualifier(TaskType type) {
            this.type = type;
        }

        @Override
        public boolean run(ReadOnlyTask task) {
            return task.getType() == type;
        }

        @Override
        public String toString() {
            return type.name();
        }
    }
    
    private class PriorityQualifier implements Qualifier {
        private PriorityType priority;

        PriorityQualifier(PriorityType priority) {
            this.priority = priority;
        }

        @Override
        public boolean run(ReadOnlyTask task) {
            return priority == task.getPriority();
        }

        @Override
        public String toString() {
            return priority.toString();
        }
    }
    
    private class DoneQualifier implements Qualifier {
        private boolean isDone;

        DoneQualifier(boolean isDone) {
            this.isDone = isDone;
        }

        @Override
        public boolean run(ReadOnlyTask task) {
            return isDone == task.getIsDone();
        }

        @Override
        public String toString() {
            return String.valueOf(isDone);
        }        
    }
    
    private class OverdueQualifier implements Qualifier {

        @Override
        public boolean run(ReadOnlyTask task) {
            return task.isOverdue();
        }

        @Override
        public String toString() {
            return "true";
        }        
    }

    private class MatchQualifier implements Qualifier {
        private List<ReadOnlyTask> matches;

        MatchQualifier(List<ReadOnlyTask> matches) {
            this.matches = matches;
        }

        @Override
        public boolean run(ReadOnlyTask task) {
            return matches.stream().filter(t -> t.equals(task)).findFirst().isPresent();
        }

        @Override
        public String toString() {
            return String.valueOf(matches);
        }        
        
    }
    
  //@@author A0142686X
  //================= Methods for undo ==================================
    
    /**
     * Adds updated tasks to the undo stack
     */
    @Override
    public void addToUndoStack(int ID, Task... tasks) {
        UndoHistory undoHistory = new UndoHistory(ID, tasks);
        undoStack.push(undoHistory);
    }
    
    /**
     * Method used by undo to add tasks without pushing to undo stack
     */
    @Override
    public void addTaskUndo(Task task) throws DuplicateTaskException {
        assert task != null;
        taskTracker.addTask(task);
        indicateTaskTrackerChanged();        
    }
    
    /**
     * Method used by undo to delete tasks without pushing to undo stack
     */
    @Override
    public void deleteTaskUndo(ReadOnlyTask target) throws TaskNotFoundException {
        assert target != null;
        taskTracker.removeTask(target);
        indicateTaskTrackerChanged();        
    }

    /**
     * Method used by undo to edit tasks without pushing to undo stack
     */
    @Override
    public void editTaskUndo(Task originalTask, Task newTask) throws DuplicateTaskException, TaskNotFoundException {
        assert originalTask != null;
        assert newTask != null;
        try {
            deleteTaskUndo(originalTask);
        } catch (TaskNotFoundException e) {
            throw new TaskNotFoundException();
        }
        addTaskUndo(newTask);
        indicateTaskTrackerChanged();        
    }
    
    /**
     * Method used by undo to clear tasks without pushing to undo stack
     */
    @Override
    public void clearTaskUndo(ArrayList<Task> tasks) {
        assert tasks != null;
        TaskTracker prevTaskTracker = new TaskTracker();
        prevTaskTracker.setTasks(tasks);
        taskTracker.resetData(prevTaskTracker);
    }
    
    /**
     * Method used by undo to mark task as undone without pushing to undo stack
     */
    @Override
    public void doneTaskUndo(Task task) throws DuplicateTaskException, TaskNotFoundException {
        assert task != null;
        taskTracker.incompleteTask(task);
        indicateTaskTrackerChanged();
    }
    
    /**
     * Method to get task from Observablelist from specified index
     */
    @Override 
    public Task getTaskfromIndex(int index) throws TaskNotFoundException {
        Task task;        
        try {
            task = sortedTasks.get(index);
        } catch (IndexOutOfBoundsException e) {
            throw new TaskNotFoundException();
        }        
        return task;
    }
    
    /**
     * Method to get index of specified task in ObservableList 
     */
    @Override
    public int getIndexFromTask(ReadOnlyTask task) throws TaskNotFoundException {
        assert task != null;
        int index;
        index=sortedTasks.lastIndexOf(task);
        return index;
    }
}

