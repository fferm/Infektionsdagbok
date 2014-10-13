package se.fermitet.android.infektionsdagbok.app;

import se.fermitet.android.infektionsdagbok.storage.EmailHandler;
import se.fermitet.android.infektionsdagbok.storage.Storage;
import android.app.AlarmManager;

public interface Factory {

	public Storage createStorage();
	public AlarmManager getAlarmManager();
	public EmailHandler createEmailHandler();
}
