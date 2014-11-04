package se.fermitet.android.infektionsdagbok.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;

import org.joda.time.LocalDate;

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

	public void testSaveSingleForInsertAndUpdate() throws Exception {
		Map<UUID, Treatment> initial = modelManager.getAllTreatments();
		assertEquals("initial size", 0, initial.size());

		Treatment t1 = new Treatment(LocalDate.now(), 1, "INF1", "MED1");
		modelManager.save(t1);

		Map<UUID, Treatment> afterFirstSave = modelManager.getAllTreatments();

		assertEquals("First save size", 1, afterFirstSave.size());
		assertTrue("Contains t1", afterFirstSave.values().contains(t1));

		t1.setNumDays(2);
		modelManager.save(t1);

		Map<UUID, Treatment> afterSecondSave = modelManager.getAllTreatments();

		assertEquals("Second save size", 1, afterSecondSave.size());
		assertTrue("Contains t1", afterSecondSave.values().contains(t1));
		Treatment fromFile = afterSecondSave.get(t1.getUUID());
		assertEquals("Changed value", t1.getNumDays(), fromFile.getNumDays());
	}

	public void testSaveAll() throws Exception {
		Treatment t1 = new Treatment(LocalDate.now().plusDays(1), 1, "INF1", "MED1");
		Treatment t2 = new Treatment(LocalDate.now().plusDays(2), 2, "INF2", "MED2");
		Treatment t3 = new Treatment(LocalDate.now().plusDays(3), 3, "INF3", "MED3");

		Collection<Treatment> toSave = new ArrayList<Treatment>();
		toSave.add(t1);
		toSave.add(t2);
		toSave.add(t3);

		modelManager.saveAll(toSave);

		Collection<Treatment> fromFile = modelManager.getAllTreatments().values();

		assertEquals("size", toSave.size(), fromFile.size());
		assertTrue("Contains all", fromFile.containsAll(toSave));
	}

	public void testDelete() throws Exception {
		Treatment t1 = new Treatment(LocalDate.now().plusDays(1), 1, "INF1", "MED1");
		Treatment t2 = new Treatment(LocalDate.now().plusDays(2), 2, "INF2", "MED2");
		Treatment t3 = new Treatment(LocalDate.now().plusDays(3), 3, "INF3", "MED3");

		Collection<Treatment> toSave = new ArrayList<Treatment>();
		toSave.add(t1);
		toSave.add(t2);
		toSave.add(t3);

		modelManager.saveAll(toSave);

		modelManager.delete(t1);

		Collection<Treatment> fromFile = modelManager.getAllTreatments().values();

		assertEquals("size", toSave.size() - 1, fromFile.size());
		assertFalse("Contains", fromFile.contains(t1));
	}

	public void testGetAllForYear() throws Exception {
		int year = 2014;
		int prev = year - 1;
		int next = year + 1;

		Treatment incl1 = new Treatment(new LocalDate(year,1,1), 1, "INF1", "MED1");	// Fully in year
		Treatment incl2 = new Treatment(new LocalDate(year,2,1), 1, "INF2", "MED2");	// Fully in year
		Treatment incl3 = new Treatment(new LocalDate(prev,12,30), 10, "INF3", "MED3");	// Starting previous year, but going into current
		Treatment incl4 = new Treatment(new LocalDate(year,12,30), 10, "INF4", "MED4");	// Starting current year, but going into next
		Treatment incl5 = new Treatment(new LocalDate(prev,12,29), 500, "INF5", "MED5");// Stretches over current year
		Treatment incl6 = new Treatment(new LocalDate(year, 3, 1), null, "INF6", "MED6");	// Starting in current year with null days
		Treatment excl1 = new Treatment(new LocalDate(prev,12,30), 1, "INF7", "MED7");
		Treatment excl2 = new Treatment(new LocalDate(next,1,1), 1, "INF8", "MED8");
		Treatment excl3 = new Treatment(new LocalDate(next,1,1), null, "INF8", "MED8");
		Treatment excl4 = new Treatment(new LocalDate(prev,12,31), null, "INF8", "MED8");
		Treatment excl5 = new Treatment(null, 1, "INF8", "MED8");
		Treatment excl6 = new Treatment(null, null, "INF9", "MED9");

		Collection<Treatment> toSave = new ArrayList<Treatment>();
		toSave.add(incl1);
		toSave.add(incl2);
		toSave.add(incl3);
		toSave.add(incl4);
		toSave.add(incl5);
		toSave.add(incl6);
		toSave.add(excl1);
		toSave.add(excl2);
		toSave.add(excl3);
		toSave.add(excl4);
		toSave.add(excl5);
		toSave.add(excl6);

		Collection<Treatment> expected = new ArrayList<Treatment>();
		expected.add(incl1);
		expected.add(incl2);
		expected.add(incl3);
		expected.add(incl4);
		expected.add(incl5);
		expected.add(incl6);

		modelManager.saveAll(toSave);

		Collection<Treatment> fromFile = modelManager.getAllTreatmentsForYear(year);

		assertEquals("size", expected.size(), fromFile.size());
		assertTrue("contains all", fromFile.containsAll(expected));
	}

}
