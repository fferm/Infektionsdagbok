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


}
