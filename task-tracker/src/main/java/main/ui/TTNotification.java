package main.ui;

import javafx.scene.paint.Paint;
import main.commons.util.AppUtil;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

public class TTNotification {
	private TrayNotification ttbot;
	private String title = "How can I help you today?";
	private String message = "";
	
	public TTNotification(){
		ttbot = new TrayNotification();
		ttbot.setTitle(title);
		ttbot.setMessage(message);
		ttbot.setNotificationType(NotificationType.INFORMATION);
		ttbot.setAnimationType(AnimationType.POPUP);
		ttbot.setImage(AppUtil.getImage("/images/T-T.gif"));
		ttbot.setRectangleFill(Paint.valueOf("#ff4d4d"));
	}
	
	public TrayNotification getTTbot(){
		return ttbot;
	}
	
	public void setTitle(String title){
		this.title = title;
		ttbot.setTitle(title);
	}
	
	public void setMessage(String message){
		this.message = message;
		ttbot.setMessage(message);
	}
	
}
