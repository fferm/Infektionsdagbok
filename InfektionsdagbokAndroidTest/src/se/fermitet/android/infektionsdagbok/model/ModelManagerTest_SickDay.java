package se.fermitet.android.infektionsdagbok.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;

import org.joda.time.LocalDate;

import se.fermitet.android.infektionsdagbok.storage.Storage;
import android.test.AndroidTestCase;

public class ModelManagerTest_SickDay extends AndroidTestCase {
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
		Map<UUID, SickDay> initial = modelManager.getAllSickDays();
		assertEquals("initial size", 0, initial.size());

		SickDay obj1 = new SickDay(LocalDate.now().minusDays(10), LocalDate.now());
		modelManager.save(obj1);

		Map<UUID, SickDay> afterFirstSave = modelManager.getAllSickDays();

		assertEquals("First save size", 1, afterFirstSave.size());
		assertTrue("Contains t1", afterFirstSave.values().contains(obj1));

		obj1.setStart(LocalDate.now().minusDays(5));
		modelManager.save(obj1);

		Map<UUID, SickDay> afterSecondSave = modelManager.getAllSickDays();

		assertEquals("Second save size", 1, afterSecondSave.size());
		assertTrue("Contains obj1", afterSecondSave.values().contains(obj1));
		SickDay fromFile = afterSecondSave.get(obj1.getUUID());
		assertEquals("Changed value", obj1.getStart(), fromFile.getStart());
	}

	public void testSaveAll() throws Exception {
		SickDay obj1 = new SickDay(LocalDate.now().minusDays(1), LocalDate.now().plusDays(1));
		SickDay obj2 = new SickDay(LocalDate.now().minusDays(2), LocalDate.now().plusDays(2));
		SickDay obj3 = new SickDay(LocalDate.now().minusDays(3), LocalDate.now().plusDays(3));

		Collection<SickDay> toSave = new ArrayList<SickDay>();
		toSave.add(obj1);
		toSave.add(obj2);
		toSave.add(obj3);

		modelManager.saveAll(toSave);

		Collection<SickDay> fromFile = modelManager.getAllSickDays().values();

		assertEquals("size", toSave.size(), fromFile.size());
		assertTrue("Contains all", fromFile.containsAll(toSave));
	}

	public void testDelete() throws Exception {
		SickDay obj1 = new SickDay(LocalDate.now().minusDays(1), LocalDate.now().plusDays(1));
		SickDay obj2 = new SickDay(LocalDate.now().minusDays(2), LocalDate.now().plusDays(2));
		SickDay obj3 = new SickDay(LocalDate.now().minusDays(3), LocalDate.now().plusDays(3));

		Collection<SickDay> toSave = new ArrayList<SickDay>();
		toSave.add(obj1);
		toSave.add(obj2);
		toSave.add(obj3);

		modelManager.saveAll(toSave);

		modelManager.delete(obj1);

		Collection<SickDay> fromFile = modelManager.getAllSickDays().values();

		assertEquals("size", toSave.size() - 1, fromFile.size());
		assertFalse("Contains", fromFile.contains(obj1));
	}

	public void testGetAllForYear() throws Exception {
		int year = 2014;
		SickDay incl1 = new SickDay(new LocalDate(2014,1,1), new LocalDate(2014,2,1));	// Fully in year
		SickDay incl2 = new SickDay(new LocalDate(2014,2,1), new LocalDate(2014,3,1));	// Fully in year
		SickDay incl3 = new SickDay(new LocalDate(2013,12,1), new LocalDate(2014,2,1));	// Starting previous year, but going into current
		SickDay incl4 = new SickDay(new LocalDate(2014,12,1), new LocalDate(2015,2,1));	// Starting current year, but going into next
		SickDay incl5 = new SickDay(new LocalDate(2013,1,1), new LocalDate(2015,2,1));	// Stretches over current year
		SickDay incl6 = new SickDay(new LocalDate(2014,1,1), null);						// Starting in current year with null end
		SickDay incl7 = new SickDay(null, new LocalDate(2014,2,1));						// Null start with end in current
		SickDay incl8 = new SickDay(null, new LocalDate(2015,1,1));						// Null start with end in future year
		SickDay incl9 = new SickDay(new LocalDate(2013,1,1), null);						// Starting in previous, null end

		SickDay excl1 = new SickDay(new LocalDate(2013,1,1), new LocalDate(2013,2,2));
		SickDay excl2 = new SickDay(new LocalDate(2015,1,1), new LocalDate(2015,2,2));
		SickDay excl3 = new SickDay(new LocalDate(2015,1,1), null);
		SickDay excl4 = new SickDay(null, new LocalDate(2013,2,2));
		SickDay excl5 = new SickDay(null, null);

		Collection<SickDay> toSave = new ArrayList<SickDay>();
		toSave.add(incl1);
		toSave.add(incl2);
		toSave.add(incl3);
		toSave.add(incl4);
		toSave.add(incl5);
		toSave.add(incl6);
		toSave.add(incl7);
		toSave.add(incl8);
		toSave.add(incl9);
		toSave.add(excl1);
		toSave.add(excl2);
		toSave.add(excl3);
		toSave.add(excl4);
		toSave.add(excl5);

		Collection<SickDay> expected = new ArrayList<SickDay>();
		expected.add(incl1);
		expected.add(incl2);
		expected.add(incl3);
		expected.add(incl4);
		expected.add(incl5);
		expected.add(incl6);
		expected.add(incl7);
		expected.add(incl8);
		expected.add(incl9);

		modelManager.saveAll(toSave);

		Collection<SickDay> fromFile = modelManager.getAllSickDaysForYear(year);

		assertEquals("size", expected.size(), fromFile.size());
		assertTrue("contains all", fromFile.containsAll(expected));
	}



}
