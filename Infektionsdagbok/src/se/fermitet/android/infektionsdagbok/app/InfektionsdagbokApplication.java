package se.fermitet.android.infektionsdagbok.app;

import se.fermitet.android.infektionsdagbok.model.ModelManager;
import se.fermitet.android.infektionsdagbok.storage.EmailHandler;
import se.fermitet.android.infektionsdagbok.storage.Storage;
import android.app.AlarmManager;
import android.app.Application;

public class InfektionsdagbokApplication extends Application {
	private ModelManager modelManager = null;
	private Storage storage = null;
	private Factory factory = null;
	private AlarmManager alarmManager = null;
	private EmailHandler emailHandler = null;

	public Storage getStorage() {
		if (this.storage == null) {
			this.storage = getFactory().createStorage();
		}
		return this.storage;
	}

	public ModelManager getModelManager() {
		if (this.modelManager == null) {
			this.modelManager = new ModelManager(getStorage());
		}
		return this.modelManager;
	}

	public Factory getFactory() {
		if (this.factory == null) {
			this.factory = new InfektionsdagbokFactory(this);
		}
		return this.factory;
	}

	public AlarmManager getAlarmManager() {
		if (this.alarmManager == null) {
			this.alarmManager = getFactory().getAlarmManager();
		}
		return this.alarmManager;
	}

	public EmailHandler getEmailHandler() {
		if (this.emailHandler == null) {
			this.emailHandler = getFactory().createEmailHandler();
		}
		return this.emailHandler;
	}

	// TODO: Does this correspond to some lifecycle method on Application
	public void clear() {
		this.modelManager = null;
		this.storage = null;
		this.factory = null;
		this.alarmManager = null;
	}

	public void setFactory(Factory factory) {
		this.factory = factory;
	}

}
