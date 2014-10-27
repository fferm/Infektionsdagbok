package se.fermitet.android.infektionsdagbok.model;

import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Collection;

import junit.framework.TestCase;

import org.joda.time.DateTime;

import se.fermitet.android.infektionsdagbok.storage.Storage;

public class ModelManagerTestMockedStorage extends TestCase {
	private ModelManager modelManager;
	private Storage mockedStorage;

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		mockedStorage = mock(Storage.class);
		modelManager = new ModelManager(mockedStorage);
	}

	public void testSaveTreatments() throws Exception {
		Collection<Treatment> toSave = new ArrayList<Treatment>();
		toSave.add(new Treatment(DateTime.now(), 3, "TEST", "TEST"));

		modelManager.saveTreatments(toSave);

		verify(mockedStorage).saveTreatments(toSave);
	}

	public void testGetAllTreatments() throws Exception {
		modelManager.getAllTreatments();

		verify(mockedStorage).getAllTreatments();
	}

}
