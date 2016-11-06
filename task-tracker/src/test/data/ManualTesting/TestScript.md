<!--@@author A0139422J -->
# **Manual Testing**

## Setting up
### Prerequisites

1. **JDK `1.8.0_60`**  or later<br>

    > Having any Java 8 version is not enough. <br>
    This app will not work with earlier versions of Java 8.
    
2. **Eclipse** IDE
3. **e(fx)clipse** plugin for Eclipse (Do the steps 2 onwards given in
   [this page](http://www.eclipse.org/efxclipse/install.html#for-the-ambitious))
4. **Buildship Gradle Integration** plugin from the Eclipse Marketplace


### Importing the project into Eclipse

1. Fork this repo, and clone the fork to your computer
2. Open Eclipse (Note: Ensure you have installed the **e(fx)clipse** and **buildship** plugins as given in the prerequisites above)
3. Click `File` > `Import`
4. Click `Gradle` > `Gradle Project` > `Next` > `Next`
5. Click `Browse`, then locate the project's directory
6. Click `Finish`

  > * If you are asked whether to 'keep' or 'overwrite' config files, choose to 'keep'.
  > * Depending on your connection speed and server load, it can even take up to 30 minutes for the set up to finish 
      (This is because Gradle downloads library files from servers during the project set up process)
  > * If Eclipse auto-changed any settings files during the import process, you can discard those changes.
  
### Troubleshooting project setup

**Problem: Eclipse reports compile errors after new commits are pulled from Git**<br>

>Reason: Eclipse fails to recognize new files that appeared due to the Git pull.

**Solution**: Refresh the project in Eclipse:
Right click on the project (in Eclipse package explorer), choose Gradle -> Refresh Gradle Project.

**Problem: Eclipse reports some required libraries missing**
>Reason: Required libraries may not have been downloaded during the project import.

**Solution**: Run tests using Gradle once (to refresh the libraries).
 
 


### Loading Sample Data

1. Start up **T-T** in Eclipse.
2. Ensure that the current file path is src/test/data/ManualTesting/SampleData.xml. The file path is displayed below the Command Box in the Status Footer Bar. 
   If the file path is incorrect, please do the following:
   1. Type in 'storage src/test/data/ManualTesting/SampleData.xml'
   2. Close the program.
   3. Navigate the aforementioned file directory within the project and delete SampleData.xml.
   4. Rename the existing `SampleData1.xml` file in the folder to `SampleData.xml`
   5. Restart the program.


### Testing Commands
<br>
<br>
<br>
### 1.**ADD**

**Purpose**: Adding a new task 
**Action**:
Execute `add question life`
**Expected**:
Display Area will show message: `New task added: question life`

**Purpose**: Adding a duplicate floating task 
**Action**:
Execute `add clean room -l`
**Expected**:
Display Area will show message: `This task is already in the to-do list`

**Purpose**: Input wrong command 
**Action**:
Execute `adds wrongcommandinput`
**Expected**:
Display Area will show message: `Invalid command format Unknown command`
<br>
<br>
<br>
### 2.**LIST**

**Purpose**: Lists all events
**Action**:
Execute `list events`
**Expected**:
    -   Display Area will show message: `Listed all pending events`
    -   Image in List Statistics Panel will change.
    -   Task-list panel will display only 4 task cards.

**Purpose**: Lists all deadlines
**Action**:
Execute `list deadlines`
**Expected**:
    -   Display Area will show message: `Listed all pending tasks with deadlines`
    -   Image in List Statistics Panel will change.
    -   Task-list panel will display only 34 task cards.

**Purpose**: Lists all floating tasks
**Action**:
Execute `list floating`
**Expected**:
    -   Display Area will show message: `Listed all pending floating tasks`
    -   Image in List Statistics Panel will change.
    -   Task-list panel will display only 13 task cards.

**Purpose**: Lists all low priority tasks
**Action**:
Execute `list low`
**Expected**:
    -   Display Area will show message: `Listed all pending low priority tasks`
    -   Image in List Statistics Panel will change.
    -   Task-list panel will display only 10 task cards.

**Purpose**: Lists all normal priority tasks
**Action**:
Execute `list normal`
**Expected**:
    -   Display Area will show message: `Listed all pending normal priority tasks`
    -   Image in List Statistics Panel will change.
    -   Task-list panel will display only 30 task cards.

**Purpose**: Lists all high priority tasks
**Action**:
Execute `list high`
**Expected**:
    -   Display Area will show message: `Listed all pending low priority tasks`
    -   Image in List Statistics Panel will change.
    -   Task-list panel will display only 11 task cards.
<br>
<br>
<br>

### 3. **EDIT**

**Purpose**: Edit an existing task 
**Action**:
Execute `edit 1 check out 01 sep 2016 -l`
**Expected**:
Display Area will show message: `Task changed to:check out due 01 Sep`

<br>
<br>
<br>

### 4. **DELETE**

**Purpose**: Deletes the first task in the list 
**Action**:
Execute `list`
Execute `delete 1`
**Expected**:
Display Area will show message: `Delete Task: 1`

**Purpose**: Deletes the last task in the list 
**Action**:
Execute `list`
Execute `delete 49`
**Expected**:
Display Area will show message: `Delete Task: 49`

**Purpose**: Deletes the last task in the list 
**Action**:
Execute `list`
Execute `delete 49`
**Expected**:
Display Area will show message: `Delete Task: 49`

**Purpose**: Deletes the task in the middle of the list 
**Action**:
Execute `list`
Execute `delete 23`
**Expected**:
Display Area will show message: `Delete Task: 23`

<br>
<br>
<br>

### 5. **DONE**

**Purpose**: Mark a task as done
**Action**:
Execute `list`
Execute `done 5`
**Expected**:
Display Area will show message: `The following task is done: 5`

**Purpose**: Mark an invalid task as done
**Action**:
Execute `list`
Execute `done 60`
**Expected**:
Display Area will show message: `Task does not exist in task-tracker`

<br>
<br>
<br>

### 6. **SORT**

**Purpose**: Sort the list of tasks by date
**Action**:
Execute `list`
Execute `sort date`
**Expected**:
Display Area will show message: `Sorting by date`
Task card in Task-List Panel are arranged such that the closest deadlines are displayed at the top of the list.

**Purpose**: Sort the list of tasks lexicographically
**Action**:
Execute `list`
Execute `sort name`
**Expected**:
Display Area will show message: `Sorting by name`
Task card in Task-List Panel are arranged alphebatically.

<br>
<br>
<br>

### 7. **FIND**

**Purpose**: Find any tasks whose messages start with the letter "w"
**Action**:
Execute `list`
Execute `find w`
**Expected**:
Task-list panel will display only task cards whose messages starts with the letter 'w'.

<br>
<br>
<br>

### 8. **SEARCH**

**Purpose**: Search any tasks whose messages start with the word "buy"
**Action**:
Execute `search buy`
**Expected**:
Task-list panel will display only task cards whose messages starts with the word `buy`.

<br>
<br>
<br>
### 9. **CLEAR**

**Purpose**: Clears T-T of all tasks
**Action**:
Execute `list`
Execute `clear`
**Expected**:
- Display Area will show message: `Task-Tracker has been cleared!`
- Task-list panel will display 0 task cards.

<br>
<br>
<br>

### 10. **UNDO**

**Purpose**: Reverts previous command
**Action**:
Execute `undo`
**Expected**:
- Display Area will show message: `Reverted last command`
- Task cards in the Task List panel will be restored to its previous state. 

<br>
<br>
<br>

### 11. **STORAGE**

**Purpose**: Change the current storage file path.
**Action**:
Execute `storage src/test/data/sandbox/newfile.xml`
**Expected**:
- Display Area will show message: `Successfully changed storaged path`
- A new xml file named `newfile` will appear under the specified project directory.

<br>
<br>
<br>

### 11. **HELP**

**Purpose**: Open up the help window
**Action**:
Execute `help`
**Expected**:
- A help window should have opened with details of what available commands there are for the users to execute as well as what hot keys they can use to access T-T's features. 
- Display Area will show message: `Opened help window`

<br>
<br>
<br>

### 11. **EXIT**

**Purpose**: Exits the program
**Action**:
Execute `exit`
**Expected**:
- The program closes.

<br>
<br>
<br>

### Testing Hot-Keys

<br>

### 1. **Command History Toggle**

**Purpose**: Navigating up the command history to the oldest command input
**Action**:
Execute `list`
Execute `find`
Press `UP` ARROW KEY 
**Expected**:
- The command box should display the recent command input `find`.

**Purpose**: Navigating down command history to the most recent command input
**Action**:
Execute `list`
Execute `find`
Execute `list high`
Press `UP` ARROW KEY 3 times
Press `DOWN` ARROW KEY 
**Expected**:
- The command box should display the recent command input `find`.

<br>
<br>
<br>

### 2. **Color Toggling**

**Purpose**: Change color theme
**Action**:
Press `F1` 
**Expected**:
- The color of the entire UI should have changed.

**Purpose**: Revert color theme
**Action**:
Press `F2` 
**Expected**:
- The color of the entire UI should have returned to the initial color.

<br>
<br>
<br>

### 3. **Task-List Navigation**

**Purpose**: Scroll down the task list panel
**Action**:
Execute `list`
Press `PgDn` 
**Expected**:
- The scroll bar in the Task List panel will have moved down by 1 card
- If the scroll bar is already at the bottom of the list, it will jump back to the top of the list.

**Purpose**: Scroll up the task list panel
**Action**:
Execute `list`
Press `PgUp` 
**Expected**:
- The scroll bar in the Task List panel will have moved up by 1 card
- If the scroll bar is already at the top, it will jump down to the bottom of the list.

<br>
<br>
<br>

### 4. **Autocompletion**

**Purpose**: Autocompletes user command input
**Action**:
Type `d`
Press `Tab` 
**Expected**:
- The command input will have been autocompleted to `delete`

**Purpose**: Shows list of available commands
**Action**:
Clear the command box of any text
Press `Tab` 
**Expected**:
- The command input will show a list of commands, starting with `add`.

<br>
<br>
<br>

### 5. **Minimize Window**

**Purpose**: Minimizes the window
**Action**:
Press `Esc` 
**Expected**:
- The window should be minimized.

<br>
<br>
<br>
