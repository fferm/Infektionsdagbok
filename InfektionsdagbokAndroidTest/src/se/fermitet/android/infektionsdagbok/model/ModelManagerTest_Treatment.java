package se.fermitet.android.infektionsdagbok.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;

import org.joda.time.DateTime;

import se.fermitet.android.infektionsdagbok.storage.Storage;
import android.test.AndroidTestCase;

public class ModelManagerTest_Treatment extends AndroidTestCase {

	private ModelManager modelManager;

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		modelManager = new ModelManager(new Storage(getContext()));
	}

	@Override
	protected void tearDown() throws Exception {
		if (this.modelManager != null) {
			this.modelManager.reset();
		}
		super.tearDown();
	}

	public void testSaveTreatmentForInsertAndUpdate() throws Exception {
		Map<UUID, Treatment> initial = modelManager.getAllTreatments();
		assertEquals("initial size", 0, initial.size());
		
		Treatment t1 = new Treatment(DateTime.now(), 1, "INF1", "MED1");
		modelManager.saveTreatment(t1);
		
		Map<UUID, Treatment> afterFirstSave = modelManager.getAllTreatments();
		
		assertEquals("First save size", 1, afterFirstSave.size());
		assertTrue("Contains t1", afterFirstSave.values().contains(t1));
		
		t1.setNumDays(2);
		modelManager.saveTreatment(t1);
		
		Map<UUID, Treatment> afterSecondSave = modelManager.getAllTreatments();

		assertEquals("Second save size", 1, afterSecondSave.size());
		assertTrue("Contains t1", afterSecondSave.values().contains(t1));
		Treatment fromFile = afterSecondSave.get(t1.getUUID());
		assertEquals("Changed value", t1.getNumDays(), fromFile.getNumDays());
	}
	
	public void testSaveTreatments() throws Exception {
		Treatment t1 = new Treatment(DateTime.now().plusSeconds(1), 1, "INF1", "MED1");
		Treatment t2 = new Treatment(DateTime.now().plusSeconds(2), 2, "INF2", "MED2");
		Treatment t3 = new Treatment(DateTime.now().plusSeconds(3), 3, "INF3", "MED3");

		Collection<Treatment> toSave = new ArrayList<Treatment>();
		toSave.add(t1);
		toSave.add(t2);
		toSave.add(t3);
		
		modelManager.saveTreatments(toSave);
		
		Collection<Treatment> fromFile = modelManager.getAllTreatments().values();
		
		assertEquals("size", toSave.size(), fromFile.size());
		assertTrue("Contains all", fromFile.containsAll(toSave));
		
		
	}

}
