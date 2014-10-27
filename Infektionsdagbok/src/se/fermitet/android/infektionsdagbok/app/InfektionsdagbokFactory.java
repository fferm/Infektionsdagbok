package se.fermitet.android.infektionsdagbok.app;

import se.fermitet.android.infektionsdagbok.storage.EmailHandler;
import se.fermitet.android.infektionsdagbok.storage.Storage;
import se.fermitet.android.infektionsdagbok.views.InfektionsdagbokViewHandler;
import android.app.AlarmManager;
import android.content.Context;

public class InfektionsdagbokFactory implements Factory {
	private Context context;

	/**
	 * All subclasses that should be instantiated via the mocking framework should implement a constructor
	 * with one single context parameter
	 * @param context
	 */
	public InfektionsdagbokFactory(Context context) {
		super();
		this.context = context;
	}
	@Override
	public Storage createStorage() {
		return new Storage(context);
	}

	@Override
	public AlarmManager getAlarmManager() {
		return (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
	}
	@Override
	public EmailHandler createEmailHandler() {
		return new EmailHandler();
	}
	@Override
	public InfektionsdagbokViewHandler createInfektionsdagbokViewHandler(Context context) {
		return new InfektionsdagbokViewHandler(context);
	}
}


