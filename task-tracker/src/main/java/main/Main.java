//@@author A0139422J
package main;

import java.io.IOException;
import javafx.application.Application;
import javafx.stage.Stage;
import main.commons.events.ui.ExitAppRequestEvent;
import main.commons.exceptions.DataConversionException;
import main.commons.util.ConfigUtil;
import main.commons.util.StringUtil;
import main.commons.core.Config;
import main.commons.core.EventsCenter;
import main.commons.core.LogsCenter;
import main.commons.core.Version;
import main.logic.Logic;
import main.storage.Storage;
import main.storage.StorageManager;
import main.ui.Ui;
import main.ui.UiManager;
import main.logic.LogicManager;
import com.google.common.eventbus.Subscribe;
import javafx.application.Platform;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;
import main.model.TaskTracker;
import main.model.Model;
import main.model.ModelManager;
import main.model.ReadOnlyTaskTracker;
import main.model.UserPrefs;

/**
 * The main entry point to the application.
 */
public class Main extends Application {
	private static final Logger logger = LogsCenter.getLogger(Main.class);

	public static final Version VERSION = new Version(1, 0, 0, true);

	protected Ui ui;
	protected Logic logic;
	protected Storage storage;
	protected Model model;
	protected Config config;
	protected UserPrefs userPrefs;
	protected int colourPointer;

	public Main() {
	}

	@Override
	public void init() throws Exception {
		logger.info("=============================[ Initializing Task-Tracker ]===========================");
		super.init();

		config = initConfig(getApplicationParameter("config"));
		storage = new StorageManager(config.getTaskTrackerFilePath(), config.getUserPrefsFilePath());

		userPrefs = initPrefs(config);

		initLogging(config);

		colourPointer = userPrefs.getColourPointer();

		model = initModelManager(storage, userPrefs);

		logic = new LogicManager(model);

		ui = new UiManager(logic, config, userPrefs);

		initEventsCenter();
	}

	private String getApplicationParameter(String parameterName) {
		Map<String, String> applicationParameters = getParameters().getNamed();
		return applicationParameters.get(parameterName);
	}

	private Model initModelManager(Storage storage, UserPrefs userPrefs) {
		Optional<ReadOnlyTaskTracker> taskTrackerOptional;
		ReadOnlyTaskTracker initialData;
		try {
			taskTrackerOptional = storage.readTaskTracker();
			if (!taskTrackerOptional.isPresent()) {
				logger.info("Data file not found. Will be starting with an empty TaskTracker");
			}
			initialData = taskTrackerOptional.orElse(new TaskTracker());
			logger.info(initialData.getTaskList().size() + "");
		} catch (DataConversionException e) {
			logger.warning("Data file not in the correct format. Will be starting with an empty TaskTracker");
			initialData = new TaskTracker();
		} catch (IOException e) {
			logger.warning("Problem while reading from the file. . Will be starting with an empty TaskTracker");
			initialData = new TaskTracker();
		}

		return new ModelManager(new TaskTracker(initialData), userPrefs);
	}

	private void initLogging(Config config) {
		LogsCenter.init(config);
	}

	protected Config initConfig(String configFilePath) {
		Config initializedConfig;
		String configFilePathUsed;

		configFilePathUsed = Config.DEFAULT_CONFIG_FILE;

		if (configFilePath != null) {
			logger.info("Custom Config file specified " + configFilePath);
			configFilePathUsed = configFilePath;
		}

		logger.info("Using config file : " + configFilePathUsed);

		try {
			Optional<Config> configOptional = ConfigUtil.readConfig(configFilePathUsed);
			initializedConfig = configOptional.orElse(new Config());
		} catch (DataConversionException e) {
			logger.warning("Config file at " + configFilePathUsed + " is not in the correct format. "
					+ "Using default config properties");
			initializedConfig = new Config();
		}

		// Update config file in case it was missing to begin with or there are
		// new/unused fields
		try {
			ConfigUtil.saveConfig(initializedConfig, configFilePathUsed);
		} catch (IOException e) {
			logger.warning("Failed to save config file : " + StringUtil.getDetails(e));
		}
		return initializedConfig;
	}

	protected UserPrefs initPrefs(Config config) {
		assert config != null;

		String prefsFilePath = config.getUserPrefsFilePath();
		logger.info("Using prefs file : " + prefsFilePath);

		UserPrefs initializedPrefs;
		try {
			Optional<UserPrefs> prefsOptional = storage.readUserPrefs();
			initializedPrefs = prefsOptional.orElse(new UserPrefs());
		} catch (DataConversionException e) {
			logger.warning("UserPrefs file at " + prefsFilePath + " is not in the correct format. "
					+ "Using default user prefs");
			initializedPrefs = new UserPrefs();
		} catch (IOException e) {
			logger.warning("Problem while reading from the file. . Will be starting with an empty TaskTracker");
			initializedPrefs = new UserPrefs();
		}

		// Update prefs file in case it was missing to begin with or there are
		// new/unused fields
		try {
			storage.saveUserPrefs(initializedPrefs);
		} catch (IOException e) {
			logger.warning("Failed to save config file : " + StringUtil.getDetails(e));
		}

		return initializedPrefs;
	}

	private void initEventsCenter() {
		EventsCenter.getInstance().registerHandler(this);
	}

	@Override
	public void start(Stage primaryStage) {
		logger.info("Starting Task-Tracker " + Main.VERSION);
		ui.start(primaryStage);
	}

	@Override
	public void stop() {
		logger.info("============================ [ Stopping Task-Tracker ] =============================");
		ui.stop();

		try {
			storage.saveUserPrefs(userPrefs);
		} catch (IOException e) {
			logger.severe("Failed to save preferences " + StringUtil.getDetails(e));
		}
		Platform.exit();
		System.exit(0);
	}

	@Subscribe
	public void handleExitAppRequestEvent(ExitAppRequestEvent event) {
		logger.info(LogsCenter.getEventHandlingLogMessage(event));
		this.stop();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
