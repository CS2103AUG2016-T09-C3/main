package main.commons.util;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import main.model.TaskTracker;
import main.storage.XmlSerializableTaskTracker;
import main.testutil.TaskTrackerBuilder;
import main.testutil.TestUtil;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.FileNotFoundException;

import static org.junit.Assert.assertEquals;

public class XmlUtilTest {

    private static final String TEST_DATA_FOLDER = FileUtil.getPath("src/test/data/XmlUtilTest/");
    private static final File EMPTY_FILE = new File(TEST_DATA_FOLDER + "empty.xml");
    private static final File MISSING_FILE = new File(TEST_DATA_FOLDER + "missing.xml");
    private static final File VALID_FILE = new File(TEST_DATA_FOLDER + "validTaskTracker.xml");
    private static final File TEMP_FILE = new File(TestUtil.getFilePathInSandboxFolder("tempTaskTracker.xml"));

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void getDataFromFile_nullFile_AssertionError() throws Exception {
        thrown.expect(AssertionError.class);
        XmlUtil.getDataFromFile(null, TaskTracker.class);
    }

    @Test
    public void getDataFromFile_nullClass_AssertionError() throws Exception {
        thrown.expect(AssertionError.class);
        XmlUtil.getDataFromFile(VALID_FILE, null);
    }

    @Test
    public void getDataFromFile_missingFile_FileNotFoundException() throws Exception {
        thrown.expect(FileNotFoundException.class);
        XmlUtil.getDataFromFile(MISSING_FILE, TaskTracker.class);
    }

    @Test
    public void getDataFromFile_emptyFile_DataFormatMismatchException() throws Exception {
        thrown.expect(JAXBException.class);
        XmlUtil.getDataFromFile(EMPTY_FILE, TaskTracker.class);
    }

    @Test
    public void getDataFromFile_validFile_validResult() throws Exception {
        XmlSerializableTaskTracker dataFromFile = XmlUtil.getDataFromFile(VALID_FILE, XmlSerializableTaskTracker.class);
        assertEquals(9, dataFromFile.getPersonList().size());
        assertEquals(0, dataFromFile.getTagList().size());
    }

    @Test
    public void saveDataToFile_nullFile_AssertionError() throws Exception {
        thrown.expect(AssertionError.class);
        XmlUtil.saveDataToFile(null, new TaskTracker());
    }

    @Test
    public void saveDataToFile_nullClass_AssertionError() throws Exception {
        thrown.expect(AssertionError.class);
        XmlUtil.saveDataToFile(VALID_FILE, null);
    }

    @Test
    public void saveDataToFile_missingFile_FileNotFoundException() throws Exception {
        thrown.expect(FileNotFoundException.class);
        XmlUtil.saveDataToFile(MISSING_FILE, new TaskTracker());
    }

    @Test
    public void saveDataToFile_validFile_dataSaved() throws Exception {
        TEMP_FILE.createNewFile();
        XmlSerializableTaskTracker dataToWrite = new XmlSerializableTaskTracker(new TaskTracker());
        XmlUtil.saveDataToFile(TEMP_FILE, dataToWrite);
        XmlSerializableTaskTracker dataFromFile = XmlUtil.getDataFromFile(TEMP_FILE, XmlSerializableTaskTracker.class);
        assertEquals((new TaskTracker(dataToWrite)).toString(),(new TaskTracker(dataFromFile)).toString());
        //TODO: use equality instead of string comparisons

        TaskTrackerBuilder builder = new TaskTrackerBuilder(new TaskTracker());
        dataToWrite = new XmlSerializableTaskTracker(builder.withPerson(TestUtil.generateSamplePersonData().get(0)).withTag("Friends").build());

        XmlUtil.saveDataToFile(TEMP_FILE, dataToWrite);
        dataFromFile = XmlUtil.getDataFromFile(TEMP_FILE, XmlSerializableTaskTracker.class);
        assertEquals((new TaskTracker(dataToWrite)).toString(),(new TaskTracker(dataFromFile)).toString());
    }
}
