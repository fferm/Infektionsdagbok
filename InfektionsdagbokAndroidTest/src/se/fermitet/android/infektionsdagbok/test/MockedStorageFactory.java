package se.fermitet.android.infektionsdagbok.test;

import static org.mockito.Mockito.mock;
import se.fermitet.android.infektionsdagbok.app.Factory;
import se.fermitet.android.infektionsdagbok.storage.Storage;
import android.os.Parcel;
import android.os.Parcelable;

public class MockedStorageFactory implements Factory, Parcelable {

	@Override
	public Storage createStorage() {
		return mock(Storage.class);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {}

	public static final Parcelable.Creator<MockedStorageFactory> CREATOR = new Parcelable.Creator<MockedStorageFactory>() {
		@Override
		public MockedStorageFactory createFromParcel(Parcel source) {
			return new MockedStorageFactory();
		}

		@Override
		public MockedStorageFactory[] newArray(int size) {
			return new MockedStorageFactory[size];
		}
	};
}