package main.storage;


import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import main.commons.events.model.TaskTrackerChangedEvent;
import main.commons.events.storage.DataSavingExceptionEvent;
import main.model.TaskTracker;
import main.model.ReadOnlyTaskTracker;
import main.model.UserPrefs;
import main.testutil.TypicalTestPersons;
import main.testutil.EventsCollector;

import java.io.IOException;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class StorageManagerTest {

    private StorageManager storageManager;

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();


    @Before
    public void setup() {
        storageManager = new StorageManager(getTempFilePath("ab"), getTempFilePath("prefs"));
    }


    private String getTempFilePath(String fileName) {
        return testFolder.getRoot().getPath() + fileName;
    }


    /*
     * Note: This is an integration test that verifies the StorageManager is properly wired to the
     * {@link JsonUserPrefsStorage} class.
     * More extensive testing of UserPref saving/reading is done in {@link JsonUserPrefsStorageTest} class.
     */

    @Test
    public void prefsReadSave() throws Exception {
        UserPrefs original = new UserPrefs();
        original.setGuiSettings(300, 600, 4, 6);
        storageManager.saveUserPrefs(original);
        UserPrefs retrieved = storageManager.readUserPrefs().get();
        assertEquals(original, retrieved);
    }

    @Test
    public void addressBookReadSave() throws Exception {
        TaskTracker original = new TypicalTestPersons().getTypicalTaskTracker();
        storageManager.saveTaskTracker(original);
        ReadOnlyTaskTracker retrieved = storageManager.readTaskTracker().get();
        assertEquals(original, new TaskTracker(retrieved));
        //More extensive testing of TaskTracker saving/reading is done in XmlTaskTrackerStorageTest
    }

    @Test
    public void getTaskTrackerFilePath(){
        assertNotNull(storageManager.getTaskTrackerFilePath());
    }

    @Test
    public void handleTaskTrackerChangedEvent_exceptionThrown_eventRaised() throws IOException {
        //Create a StorageManager while injecting a stub that throws an exception when the save method is called
        Storage storage = new StorageManager(new XmlTaskTrackerStorageExceptionThrowingStub("dummy"), new JsonUserPrefsStorage("dummy"));
        EventsCollector eventCollector = new EventsCollector();
        storage.handleTaskTrackerChangedEvent(new TaskTrackerChangedEvent(new TaskTracker()));
        assertTrue(eventCollector.get(0) instanceof DataSavingExceptionEvent);
    }


    /**
     * A Stub class to throw an exception when the save method is called
     */
    class XmlTaskTrackerStorageExceptionThrowingStub extends XmlTaskTrackerStorage{

        public XmlTaskTrackerStorageExceptionThrowingStub(String filePath) {
            super(filePath);
        }

        @Override
        public void saveTaskTracker(ReadOnlyTaskTracker addressBook, String filePath) throws IOException {
            throw new IOException("dummy exception");
        }
    }


}
