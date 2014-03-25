package se.fermitet.android.infektionsdagbok.app;

import se.fermitet.android.infektionsdagbok.model.ModelManager;
import se.fermitet.android.infektionsdagbok.storage.Storage;
import android.app.AlarmManager;
import android.app.Application;
import android.content.Context;

public class InfektionsdagbokApplication extends Application {
	private ModelManager modelManager = null;
	private Storage storage = null;
	private Factory factory = null;
	private AlarmManager alarmManager = null;

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
			this.factory = new InfektionsdagbokFactory();
		}
		return this.factory;
	}

	public AlarmManager getAlarmManager() {
		if (this.alarmManager == null) {
			this.alarmManager = getFactory().getAlarmManager();
		}
		return this.alarmManager;
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

	private class InfektionsdagbokFactory implements Factory {
		@Override
		public Storage createStorage() {
			return new Storage(InfektionsdagbokApplication.this);
		}

		@Override
		public AlarmManager getAlarmManager() {
			return (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		}
	}


}
