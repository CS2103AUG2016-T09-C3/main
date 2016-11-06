//@@author A0139422J
package main.ui;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import main.commons.events.model.TaskTrackerChangedEvent;
import main.commons.util.FxViewUtil;
import main.commons.core.LogsCenter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.logging.Logger;
import org.controlsfx.control.StatusBar;
import com.google.common.eventbus.Subscribe;

/**
 * A ui for the status bar that is displayed at the footer of the application.
 */
public class StatusBarFooter extends UiPart {
	private static final Logger logger = LogsCenter.getLogger(StatusBarFooter.class);
	private static StatusBarFooter statusBarFooter;
	private StatusBar syncStatus;
	private StatusBar saveLocationStatus;

	private GridPane mainPane;

	@FXML
	private AnchorPane saveLocStatusBarPane;

	@FXML
	private AnchorPane syncStatusBarPane;

	private AnchorPane placeHolder;

	private static final String FXML = "StatusBarFooter.fxml";

	private static String saveLocation;

	public static StatusBarFooter load(Stage stage, AnchorPane placeHolder, String saveLocation) {
		statusBarFooter = UiPartLoader.loadUiPart(stage, placeHolder, new StatusBarFooter());
		statusBarFooter.configure(saveLocation);
		return statusBarFooter;
	}

	public void configure(String saveLocation) {
		addMainPane();
		addSyncStatus();
		setSyncStatus(getCurrentLocalDateTime());
		addSaveLocation();
		setSaveLocation("./" + saveLocation);
		registerAsAnEventHandler(this);
	}

	private String getCurrentLocalDateTime() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
		LocalDateTime dateTime = LocalDateTime.now();
		String formattedDateTime = dateTime.format(formatter);
		return formattedDateTime;
	}

	private void addMainPane() {
		FxViewUtil.applyAnchorBoundaryParameters(mainPane, 0.0, 0.0, 0.0, 0.0);
		placeHolder.getChildren().add(mainPane);
	}

	private void setSaveLocation(String location) {
		this.saveLocation = location;
		String tidyPath = tidyNewSavePath(location);
		this.saveLocationStatus.setText(tidyPath);
	}

	private String tidyNewSavePath(String command) {
		command = command.replaceAll("storage", "");
		command = command.trim();
		return command;

	}

	public static void updateSaveLocation(String newSaveLocation) {
		statusBarFooter.setSaveLocation(newSaveLocation);
	}

	private void addSaveLocation() {
		this.saveLocationStatus = new StatusBar();
		FxViewUtil.applyAnchorBoundaryParameters(saveLocationStatus, 0.0, 0.0, 0.0, 0.0);
		saveLocStatusBarPane.getChildren().add(saveLocationStatus);
		saveLocStatusBarPane.setMinWidth(150);
	}

	private void setSyncStatus(String status) {
		this.syncStatus.setText(status);
	}

	private void addSyncStatus() {
		this.syncStatus = new StatusBar();
		FxViewUtil.applyAnchorBoundaryParameters(syncStatus, 0.0, 0.0, 0.0, 0.0);
		syncStatusBarPane.getChildren().add(syncStatus);
	}

	@Override
	public void setNode(Node node) {
		mainPane = (GridPane) node;
	}

	@Override
	public void setPlaceholder(AnchorPane placeholder) {
		this.placeHolder = placeholder;
	}

	@Override
	public String getFxmlPath() {
		return FXML;
	}

	@Subscribe
	public void handleTaskTrackerChangedEvent(TaskTrackerChangedEvent abce) {
		String lastUpdated = (new Date()).toString();
		logger.info(LogsCenter.getEventHandlingLogMessage(abce, "Setting last updated status to " + lastUpdated));
		setSyncStatus("Last Updated: " + lastUpdated);
	}

	public void changeStyle(String colorChange) {
		mainPane.setStyle(colorChange);
		saveLocStatusBarPane.setStyle(colorChange);
		syncStatusBarPane.setStyle(colorChange);
		placeHolder.setStyle(colorChange);
	}
}
