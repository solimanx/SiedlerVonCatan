package view;

import java.awt.AWTException;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionListener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Minimizer {
	private Stage stage;
	private boolean firstTime;
	private TrayIcon trayIcon;
	private static Logger logger = LogManager.getLogger(Minimizer.class.getSimpleName());

	public Minimizer(final Stage stage, boolean firstTime, TrayIcon trayIcon) {
		this.stage = stage;
		this.firstTime = firstTime;
		this.trayIcon = trayIcon;
	}
	public void setTrayIcon(TrayIcon trayIcon){
		this.trayIcon = trayIcon;
	}

	public void createTrayIcon() {
		if (SystemTray.isSupported()) {
			SystemTray tray = SystemTray.getSystemTray();

			stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				@Override
				public void handle(WindowEvent t) {
					hide();
				}
			});
			final ActionListener closeListener = new ActionListener() {
				@Override
				public void actionPerformed(java.awt.event.ActionEvent e) {
					System.exit(0);
				}
			};

			ActionListener showListener = new ActionListener() {
				@Override
				public void actionPerformed(java.awt.event.ActionEvent e) {
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							stage.show();
						}
					});
				}
			};

			PopupMenu popup = new PopupMenu();
			trayIcon.setPopupMenu(popup);
			
			MenuItem showItem = new MenuItem("Show");
			showItem.addActionListener(showListener);
			popup.add(showItem);

			MenuItem closeItem = new MenuItem("Close");
			closeItem.addActionListener(closeListener);
			popup.add(closeItem);

			trayIcon.addActionListener(showListener);
			try {
				tray.add(trayIcon);
			} catch (AWTException e) {
				logger.error(e);
			}
		}
	}

	public void showProgramIsMinimizedMsg() {
		if (firstTime) {
			trayIcon.displayMessage("Some message.", "Some other message.", TrayIcon.MessageType.INFO);
			firstTime = false;
		}
	}

	private void hide() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				if (SystemTray.isSupported()) {
					stage.hide();
					showProgramIsMinimizedMsg();
				} else {
					System.exit(0);
				}
			}
		});
	}

}
