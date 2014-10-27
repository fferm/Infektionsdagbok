package se.fermitet.android.infektionsdagbok.app;

import se.fermitet.android.infektionsdagbok.storage.EmailHandler;
import se.fermitet.android.infektionsdagbok.storage.Storage;
import se.fermitet.android.infektionsdagbok.views.InfektionsdagbokViewHandler;
import android.app.AlarmManager;
import android.content.Context;

public interface Factory {

	public Storage createStorage();
	public AlarmManager getAlarmManager();
	public EmailHandler createEmailHandler();
	
	public InfektionsdagbokViewHandler createInfektionsdagbokViewHandler(Context context);
}
