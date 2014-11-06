package se.fermitet.android.infektionsdagbok.app;

import se.fermitet.android.infektionsdagbok.model.ModelManager;
import se.fermitet.android.infektionsdagbok.storage.EmailHandler;
import se.fermitet.android.infektionsdagbok.storage.Storage;
import android.app.AlarmManager;
import android.app.Application;
import android.os.Vibrator;

public class InfektionsdagbokApplication extends Application {
	private ModelManager modelManager = null;
	private Storage storage = null;
	private Factory factory = null;
	private AlarmManager alarmManager = null;
	private EmailHandler emailHandler = null;
	private Vibrator vibrator;

	private static InfektionsdagbokApplication instance = null;
	public InfektionsdagbokApplication() {
		super();
		instance = this;
	}

	public static InfektionsdagbokApplication getApplicationInstance() {
		return instance;
	}

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

	public Vibrator getVibrator() {
		if (this.vibrator == null) {
			this.vibrator = getFactory().getVibrator();
		}
		return this.vibrator;
	}

	public void clear() {
		this.modelManager = null;
		this.storage = null;
		this.factory = null;
		this.alarmManager = null;
		this.vibrator = null;
		this.emailHandler = null;
	}

	public void setFactory(Factory factory) {
		this.factory = factory;
	}

}
