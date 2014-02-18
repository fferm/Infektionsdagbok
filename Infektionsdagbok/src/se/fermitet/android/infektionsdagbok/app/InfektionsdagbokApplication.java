package se.fermitet.android.infektionsdagbok.app;

import se.fermitet.android.infektionsdagbok.model.ModelManager;
import se.fermitet.android.infektionsdagbok.storage.Storage;
import android.app.Application;

public class InfektionsdagbokApplication extends Application {
	private ModelManager modelManager = null;
	private Storage storage = null;
	private Factory factory = null;

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

	public void setFactory(Factory factory) {
		this.factory = factory;
	}

	private class InfektionsdagbokFactory implements Factory {
		@Override
		public Storage createStorage() {
			return new Storage(InfektionsdagbokApplication.this);
		}

	}


}
