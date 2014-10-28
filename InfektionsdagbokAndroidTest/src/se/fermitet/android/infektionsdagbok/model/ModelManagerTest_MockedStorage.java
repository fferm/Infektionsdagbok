package se.fermitet.android.infektionsdagbok.model;

import static org.mockito.Mockito.*;
import junit.framework.TestCase;
import se.fermitet.android.infektionsdagbok.storage.Storage;

public class ModelManagerTest_MockedStorage extends TestCase {
	private ModelManager modelManager;
	private Storage mockedStorage;

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		mockedStorage = mock(Storage.class);
		modelManager = new ModelManager(mockedStorage);
	}

	public void testGetAllTreatments() throws Exception {
		modelManager.getAllTreatments();

		verify(mockedStorage).getAllTreatments();
	}

}
