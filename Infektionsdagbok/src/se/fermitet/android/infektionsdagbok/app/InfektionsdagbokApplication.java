package se.fermitet.android.infektionsdagbok.app;

import se.fermitet.android.infektionsdagbok.model.ModelManager;
import se.fermitet.android.infektionsdagbok.storage.Storage;
import android.app.Application;

public class InfektionsdagbokApplication extends Application {
	private ModelManager modelManager = null;
	private Factory factory = null;

	public ModelManager getModelManager() {
		if (this.modelManager == null) {
			this.modelManager = new ModelManager(getFactory().createStorage());
		}
		return this.modelManager;
	}

	private Factory getFactory() {
		if (this.factory == null) {
			this.factory = new InfektionsdagbokFactory();
		}
		return this.factory;
	}

	private class InfektionsdagbokFactory implements Factory {
		@Override
		public Storage createStorage() {
			return new Storage(InfektionsdagbokApplication.this);
		}

	}
}
