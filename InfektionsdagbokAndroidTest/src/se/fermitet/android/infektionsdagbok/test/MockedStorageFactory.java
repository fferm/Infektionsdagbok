package se.fermitet.android.infektionsdagbok.test;

import static org.mockito.Mockito.mock;
import se.fermitet.android.infektionsdagbok.storage.Storage;
import android.app.AlarmManager;
import android.content.Context;

public class MockedStorageFactory extends MockedVibratorFactory {

	public MockedStorageFactory(Context context) {
		super(context);
	}

	@Override
	public Storage createStorage() {
		return mock(Storage.class);
	}

	@Override
	public AlarmManager getAlarmManager() {
		return mock(AlarmManager.class);
	}


}