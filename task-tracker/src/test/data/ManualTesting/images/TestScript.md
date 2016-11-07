## **Manual Testing**

### Prerequisites

1. **JDK `1.8.0_60`**  or later<br>

    > Having any Java 8 version is not enough. <br>
    This app will not work with earlier versions of Java 8.
    
### Opening **T-T**
   1. Navigate to our [release page](https://github.com/CS2103AUG2016-T09-C3/main/releases)
   2. Download the jar file from the lastest release.
   3. Run the jar file to start up **T-T**

### Loading Sample Data

1. With **T-T** opened, you should see something like this <br>
<img src="docs/images/empty.png" <br>
2. To load the data, type `storage src/test/data/ManualTesting/SampleData.xml`

### Testing Commands
<br>
### 1.**ADD**

**Purpose**: Adding a new floating task <br>
**Action**: type `add question life`<br>
**Expected**:<br>
- Display Area will show message: `New task added: question life`.
- TaskCard corresponding to this task is orange as no priority was indicated.
- TaskCard will have no date shown.
- Task will be inserted near the end of the list, as it is a floating task.
<br>

<br>
**Purpose**: Adding a new deadline task <br>
**Action**: type `add fix my laptop by 20 nov`<br>
**Expected**:<br>
- Display Area will show message: `add fix my laptop due 20 nov`
- TaskCard corresponding to this task is orange as no priority was indicated.
- TaskCard will have the date shown at the side
<br>

<br>
**Purpose**: Adding a new event task <br>
**Action**: type `add jim's birthday 1 dec 6pm to 11pm`<br>
**Expected**:<br>
- Display Area will show message: `New task added: jim's birthday from 01 Dec 6:00 PM to 01 Dec 11:00 PM`
- TaskCard corresponding to this task is orange as no priority was indicated.
- TaskCard will have both dates shown at the side
<br>

<br>
**Purpose**: Adding a new high priority task <br>
**Action**: type `add brush teeth today -h`<br>
**Expected**:<br>
- Display Area will show message: `New task added: brush teeth due 07 Nov`.
- TaskCard corresponding to this task is red as high priority was indicated.
- TaskCard will show today's date at the side.
<br>

<br>
**Purpose**: Adding a new low priority task <br>
**Action**: type `add be nice -l`<br>
**Expected**:<br>
- Display Area will show message: `New task added: be nice`.
- TaskCard corresponding to this task is greenish white as low priority was indicated.
<br>

<br>
**Purpose**: Adding a overdue task <br>
**Action**: type `add halloween party 31 oct`<br>
**Expected**:<br>
- Display Area will show message: `New task added: halloween party due 31 Oct`.
- Rectangle at the side of TaskCard will be black to indicate overdue task.
<br>

<br>
**Purpose**: Adding a recurring task <br>
**Action**: type `add teach tution every wed`<br>
**Expected**:<br>
- Display Area will show message: `New task added: teach tution due 14 Nov`.
- The side of TaskCard will display `Weekly` to indicate a recurring task.
<br>

<br>
**Purpose**: Adding a duplicate task <br>
**Action**: type `add clean room -l`<br>
**Expected**:<br>
- Display Area will show message: `This task is already in the to-do list`<br>

<br>
<br>
### 2.**LIST** <br>

**Purpose**: Lists all pending tasks<br>
**Action**: type `list`<br>
**Expected**:<br>
- Display Area will show message: `Listed all pending tasks`<br>
- Image in List Statistics Panel will change.<br>
- Task-list panel will display only task cards with two dates specified.<br>

<br>

**Purpose**: Lists all events<br>
**Action**: type `list event`<br>
**Expected**:<br>
- Display Area will show message: `Listed all pending events`<br>
- Image in List Statistics Panel will change.<br>
- Task-list panel will display only task cards with two dates specified.<br>

<br>
**Purpose**: Lists all deadlines<br>
**Action**: type `list deadlines`<br>
**Expected**:<br>
- Display Area will show message: `Listed all pending tasks with deadlines`<br>
- Image in List Statistics Panel will change.<br>
- Task-list panel will display only task cards with one date specified.<br>

**Purpose**: Lists all floating tasks<br>
**Action**: type `list floating`<br>
**Expected**:<br>
- Display Area will show message: `Listed all pending floating tasks`<br>
- Image in List Statistics Panel will change.<br>
- Task-list panel will display only task cards with no date specified.<br>

**Purpose**: Lists all low priority tasks<br>
**Action**: type `list low`<br>
**Expected**:<br>
-   Display Area will show message: `Listed all pending low priority tasks`<br>
- Image in List Statistics Panel will change.<br>
- Task-list panel will display only green task cards.<br>
<br>

<br>
**Purpose**: Lists all normal priority tasks<br>
**Action**: type `list normal`<br>
**Expected**:<br>
- Display Area will show message: `Listed all pending normal priority tasks`<br>
- Image in List Statistics Panel will change.<br>
- Task-list panel will display only orange task cards.<br>
<br>

<br>
**Purpose**: Lists all high priority tasks<br>
**Action**: type `list high`<br>
**Expected**:<br>
- Display Area will show message: `Listed all pending low priority tasks`<br>
- Image in List Statistics Panel will change.<br>
- Task-list panel will display only red task cards.<br>
<br>

<br>
**Purpose**: Lists all done tasks<br>
**Action**: type `list done`<br>
**Expected**:<br>
- Display Area will show message: `Listed all completed tasks`<br>
- Image in List Statistics Panel will change.<br>
- Task-list panel will display only completed tasks.<br>
<br>

<br>
**Purpose**: Lists all overdue tasks<br>
**Action**: type `list overdue`<br>
**Expected**:<br>
- Display Area will show message: `Listed all overdue tasks`<br>
- Image in List Statistics Panel will change.<br>
- Task-list panel will display only tasks with a black bar at the side.<br>
<br>

<br>
**Purpose**: Lists all tasks on a specific date<br>
**Action**: type `list 31 oct`<br>
**Expected**:<br>
- Display Area will show message: `Listed all pending tasks due 31 Oct`<br>
- Image in List Statistics Panel will change.<br>
- Task-list panel will display only tasks on 31 oct.<br>
<br>

<br>
**Purpose**: Lists all low priority floating tasks<br>
**Action**: type `list low floating`<br>
**Expected**:<br>
- Display Area will show message: `Listed all pending low priority floating tasks` <br>
- Image in List Statistics Panel will change.<br>
- Task-list panel will display only floating tasks which have low priority.<br>
<br>

<br>
**Purpose**: Lists all events on a specific date<br>
**Action**: type `list 31 oct event`<br>
**Expected**:<br>
- Display Area will show message: `Listed all pending events due 31 Oct` <br>
- Image in List Statistics Panel will change.<br>
- Task-list panel will display only events on that specific date.<br>
<br>

<br>
**Purpose**: Lists all high priority events on a specific date<br>
**Action**: type `list high 31 oct event`<br>
**Expected**:<br>
- Display Area will show message: `Listed all pending high priority events due 31 Oct` <br>
- Image in List Statistics Panel will change.<br>
- Task-list panel will display only high priority events on that specific date.<br>
<br>

<br>
**Purpose**: Autocomplete list parameters<br>
**Action**: type `list hi`, press <kbd>tab</kbd><br>
**Expected**:<br>
- Command Box will become `list high `<br>
<br>

<br>
**Purpose**: More autocomplete list parameters<br>
**Action**: type `list high e`, press <kbd>tab</kbd><br>
**Expected**:<br>
- Command Box will become `list high event `<br>
<br>

<br>
<br>
<br>
### 3. **EDIT**

**Purpose**: Edit an existing task<br> 
**Action**: type `edit 1 check out 01 sep 2016 -l`<br>
**Expected**:<br>
- Display Area will show message: `Task changed to:check out due 01 Sep`<br>

<br>
<br>
<br>

### 4. **DELETE**

**Purpose**: Delete the task in the list <br>
**Action**: type `list`, followed by `delete 2`<br>
**Expected**:<br>
- Display Area will show message: `Delete Task: 2`<br>

**Purpose**: Delete the non existent task in the list <br>
**Action**: type `delete 100`<br>
**Expected**:<br>
- Display Area will show message: `Task does not exist in Task-Tracker`<br>
<br>

<br>
**Purpose**: Delete the task in specificied list <br>
**Action**: type `list floating', followed by `delete 1`<br>
**Expected**:<br>
- Display Area will show message: `Deleted Task: 1`<br>
- Current list view stays the same, except the deleted task is gone. <br>
<br>

<br>
<br>
### 5. **DONE**

**Purpose**: Mark a task as done<br>
**Action**: type `list`, followed by `done 5`<br>

**Expected**:<br>
- Display Area will show message: `The following task is done: 5`<br>

**Purpose**: Mark an invalid task as done<br>
**Action**: type `list`, followed by `done 70`<br>

**Expected**:<br>
- Display Area will show message: `Task does not exist in task-tracker`<br>

<br>
<br>
<br>

### 6. **SORT**

**Purpose**: Sort the list of tasks by date<br>
**Action**: type `list`, followed by `sort date`<br>
**Expected**:<br>
- Display Area will show message: `Sorting by date`<br>
- Task card in Task-List Panel are arranged such that the closest deadlines are displayed at the top of the list.<br>

**Purpose**: Sort the list of tasks lexicographically<br>
**Action**: type `list`, followed by `sort name`<br>
**Expected**:<br>
- Display Area will show message: `Sorting by name`<br>
- Task card in Task-List Panel are arranged alphebatically.<br>
<br>

**Purpose**: Sort the specificied list of tasks by date<br>
**Action**: type `list deadline`, followed by `sort date`<br>
**Expected**:<br>
- Display Area will show message: `Sorting by date`<br>
- Currenly displayed Task cards in Task-List Panel are arranged such that the closest deadlines are displayed at the top of the list.<br>
<br>

**Purpose**: Autocomplete sort parameteres<br>
**Action**: type `sort na`, then press <kbd>tab</kbd><br>
**Expected**:<br>
- Command Box becomes `sort name`<br>
<br>

<br>
<br>

### 7. **FIND**

**Purpose**: Find any tasks whose messages start with the letter "w"<br>
**Action**: type `find w` (without pressing <kbd>enter</kbd>)<br> 
**Expected**:<br>
- Task-list panel will displays in real time only task cards whose messages contains the letter 'w'.<br>

**Purpose**: Find tasks matching either of the 2 tokens<br>
**Action**: type `find w bu` (without pressing <kbd>enter</kbd>)<br> 
**Expected**:<br>
- Task-list panel will displays in real time only task cards whose messages contains the letter 'w', and task cards whose message starts with "w" or "bu".<br>

**Purpose**: Manipulate the list of found tasks<br>
**Action**: type `find bu`, press <kbd>enter</kbd>, then type `done 1`<br>
**Expected**:<br>
- Task-list panel will display task cards whose message starts with "bu".<br>
- The first task in the list will be gone when it is marked as done. <br>
- The rest of the task remains. <br>
<br>

<br>
<br>

### 8. **CLEAR**

**Purpose**: Clears T-T of all tasks<br>
**Action**: type `clear`<br>

**Expected**:<br>
- Display Area will show message: `Task-Tracker has been cleared!`
- Task-list panel will display 0 task cards.<br>

<br>
<br>
<br>

### 9. **UNDO**

**Purpose**: Reverts previous command<br>
**Action**: type `undo`<br>
**Expected**:<br>
- Display Area will show message: `Reverted last command`<br>
- Task cards in the Task List panel will be restored to its previous state. <br>

<br>
<br>
<br>

### 10. **STORAGE**

**Purpose**: Change the current storage file path.<br>
**Action**: type `storage src/test/data/sandbox/newfile.xml`<br>
**Expected**:<br>
- Display Area will show message: `Successfully changed storaged path`<br>
- A new xml file named `newfile` will appear under the specified project directory.<br>

<br>
<br>
<br>

### 11. **HELP**

**Purpose**: Input wrong command <br>
**Action**: type `bleh wrongcommandinput`<br>
**Expected**:<br>
- Display Area will show message: `Invalid command format Unknown command`<br>
<br>

<br>

**Purpose**: Open up the help window<br>
**Action**: type `help`<br>
**Expected**:<br>
- A help window should have opened with details of what available commands there are for the users to execute as well as what hot keys they can use to access T-T's features. <br>
- Display Area will show message: `Opened help window`<br>

<br>
<br>
<br>

### 12. **EXIT**

**Purpose**: Exits the program<br>
**Action**: type `exit`<br>
**Expected**:<br>
- The program closes.<br>

<br>
<br>
<br>

### Testing Hot-Keys

<br>

### 1. **Command History Toggle**

**Purpose**: Navigating up the command history to the oldest command input<br>
**Action**:
- type `list`<br>
- type `find`<br>
- Press `UP` ARROW KEY <br>

**Expected**:<br>
- The command box should display the recent command input `find`.<br>

**Purpose**: Navigating down command history to the most recent command input<br>
**Action**:<br>
- Execute `list`<br>
- Execute `find`<br>
- Execute `list high`<br>
- Press `UP` ARROW KEY 3 times<br>
- Press `DOWN` ARROW KEY <br>

**Expected**:<br>
- The command box should display the recent command input `find`.<br>

<br>
<br>
<br>

### 2. **Color Toggling**

**Purpose**: Change color theme<br>
**Action**:<br>
Press `F1` <br>
**Expected**:<br>
- The color of the entire UI should have changed.<br>

**Purpose**: Revert color theme<br>
**Action**:<br>
Press `F2` <br>
**Expected**:<br>
- The color of the entire UI should have returned to the initial color.<br>

<br>
<br>
<br>

### 3. **Task-List Navigation**

**Purpose**: Scroll down the task list panel<br>
**Action**:<br>
- Execute `list`<br>
- Press `PgDn` <br>

**Expected**:<br>
- The scroll bar in the Task List panel will have moved down by 1 card<br>
- If the scroll bar is already at the bottom of the list, it will jump back to the top of the list.<br>

**Purpose**: Scroll up the task list panel<br>
**Action**:<br>
- Execute `list`<br>
- Press `PgUp` <br>

**Expected**:<br>
- The scroll bar in the Task List panel will have moved up by 1 card<br>
- If the scroll bar is already at the top, it will jump down to the bottom of the list.<br>

<br>
<br>
<br>

### 4. **Autocompletion**

**Purpose**: Autocompletes user command input<br>
**Action**:<br>
- Type `d`<br>
- Press `Tab` <br>
- Press `Tab` <br>

**Expected**:<br>
- The command input will have been autocompleted to `delete` then `done` <br>

**Purpose**: Shows list of available commands<br>
**Action**:<br>
- Clear the command box of any text<br>
- Press `Tab` <br>

**Expected**:<br>
- The command input will show a list of commands, starting with `add`.<br>

<br>
<br>
<br>

### 5. **Minimize Window**<br>

**Purpose**: Minimizes the window<br>
**Action**:<br>
Press `Esc` <br>
**Expected**:<br>
- The window should be minimized.<br>

<br>
<br>
<br>
