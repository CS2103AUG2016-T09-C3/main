package main.storage;


import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;
import main.commons.exceptions.DataConversionException;
import main.commons.util.FileUtil;
import main.model.TaskTracker;
import main.model.ReadOnlyTaskTracker;
//import main.model.person.Person;
import main.testutil.TypicalTestPersons;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class XmlTaskTrackerStorageTest {
    private static String TEST_DATA_FOLDER = FileUtil.getPath("./src/test/data/XmlTaskTrackerStorageTest/");

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

    @Test
    public void readTaskTracker_nullFilePath_assertionFailure() throws Exception {
        thrown.expect(AssertionError.class);
        readTaskTracker(null);
    }

    private java.util.Optional<ReadOnlyTaskTracker> readTaskTracker(String filePath) throws Exception {
        return new XmlTaskTrackerStorage(filePath).readTaskTracker(addToTestDataPathIfNotNull(filePath));
    }

    private String addToTestDataPathIfNotNull(String prefsFileInTestDataFolder) {
        return prefsFileInTestDataFolder != null
                ? TEST_DATA_FOLDER + prefsFileInTestDataFolder
                : null;
    }

    @Test
    public void read_missingFile_emptyResult() throws Exception {
        assertFalse(readTaskTracker("NonExistentFile.xml").isPresent());
    }

    @Test
    public void read_notXmlFormat_exceptionThrown() throws Exception {

        thrown.expect(DataConversionException.class);
        readTaskTracker("NotXmlFormatTaskTracker.xml");

        /* IMPORTANT: Any code below an exception-throwing line (like the one above) will be ignored.
         * That means you should not have more than one exception test in one method
         */
    }

    @Test
    public void readAndSaveTaskTracker_allInOrder_success() throws Exception {
        String filePath = testFolder.getRoot().getPath() + "TempTaskTracker.xml";
        TypicalTestPersons td = new TypicalTestPersons();
        TaskTracker original = td.getTypicalTaskTracker();
        XmlTaskTrackerStorage xmlTaskTrackerStorage = new XmlTaskTrackerStorage(filePath);

        //Save in new file and read back
        xmlTaskTrackerStorage.saveTaskTracker(original, filePath);
        ReadOnlyTaskTracker readBack = xmlTaskTrackerStorage.readTaskTracker(filePath).get();
        assertEquals(original, new TaskTracker(readBack));

        //Modify data, overwrite exiting file, and read back
        original.addPerson(new Person(TypicalTestPersons.hoon));
        original.removePerson(new Person(TypicalTestPersons.alice));
        xmlTaskTrackerStorage.saveTaskTracker(original, filePath);
        readBack = xmlTaskTrackerStorage.readTaskTracker(filePath).get();
        assertEquals(original, new TaskTracker(readBack));

        //Save and read without specifying file path
        original.addPerson(new Person(TypicalTestPersons.ida));
        xmlTaskTrackerStorage.saveTaskTracker(original); //file path not specified
        readBack = xmlTaskTrackerStorage.readTaskTracker().get(); //file path not specified
        assertEquals(original, new TaskTracker(readBack));

    }

    @Test
    public void saveTaskTracker_nullTaskTracker_assertionFailure() throws IOException {
        thrown.expect(AssertionError.class);
        saveTaskTracker(null, "SomeFile.xml");
    }

    private void saveTaskTracker(ReadOnlyTaskTracker addressBook, String filePath) throws IOException {
        new XmlTaskTrackerStorage(filePath).saveTaskTracker(addressBook, addToTestDataPathIfNotNull(filePath));
    }

    @Test
    public void saveTaskTracker_nullFilePath_assertionFailure() throws IOException {
        thrown.expect(AssertionError.class);
        saveTaskTracker(new TaskTracker(), null);
    }


}
