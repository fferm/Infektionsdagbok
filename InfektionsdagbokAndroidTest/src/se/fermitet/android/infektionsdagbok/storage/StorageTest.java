package se.fermitet.android.infektionsdagbok.storage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.joda.time.LocalDate;

import se.fermitet.android.infektionsdagbok.model.SickDay;
import se.fermitet.android.infektionsdagbok.model.Treatment;
import se.fermitet.android.infektionsdagbok.model.Week;
import se.fermitet.android.infektionsdagbok.model.WeekAnswers;
import android.test.AndroidTestCase;

public class StorageTest extends AndroidTestCase {

	private Storage storage;

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		this.storage = new Storage(this.getContext());
	}

	@Override
	protected void tearDown() throws Exception {
		this.storage.clear();

		super.tearDown();
	}

	public void testNoExistingAnswerShouldGiveNullAnswer() throws Exception {
		Week week = new Week(new LocalDate());

		assertNull(storage.getAnswersForWeek(week));
	}

	public void testSaveAndRetrieveGivesEqualResponse() throws Exception {
		Week week = new Week(new LocalDate());
		WeekAnswers original = new WeekAnswers(week);

		storage.saveAnswers(original);

		WeekAnswers retrieved = storage.getAnswersForWeek(week);

		assertEquals(original, retrieved);
	}

	public void testClearForWeekAnswers() throws Exception {
		Week week = new Week(new LocalDate());
		WeekAnswers original = new WeekAnswers(week);

		storage.saveAnswers(original);

		storage.clear();

		assertNull(storage.getAnswersForWeek(week));
	}

	public void testGetAllAnswers() throws Exception {
		WeekAnswers wa1 = new WeekAnswers(new Week(new LocalDate(2013, 1, 1)));
		WeekAnswers wa2 = new WeekAnswers(new Week(new LocalDate(2013, 2, 1)));
		WeekAnswers wa3 = new WeekAnswers(new Week(new LocalDate(2014, 1, 1)));
		WeekAnswers wa4 = new WeekAnswers(new Week(new LocalDate(2013, 1, 8)));

		List<WeekAnswers> saved = new ArrayList<WeekAnswers>();
		saved.add(wa4);
		saved.add(wa3);
		saved.add(wa2);
		saved.add(wa1);

		storage.saveAnswers(wa1);
		storage.saveAnswers(wa2);
		storage.saveAnswers(wa3);
		storage.saveAnswers(wa4);

		Collection<WeekAnswers> retrieved = storage.getAllAnswers();

		assertNotNull("Not null", retrieved);
		assertEquals("Size", saved.size(), retrieved.size());
		assertTrue("Contains all", retrieved.containsAll(saved));
	}

	public void testEmptyTreatments() throws Exception {
		Map<UUID, Treatment> received = storage.getAllTreatments();

		assertNotNull("Not null before any work", received);
		assertEquals("Empty treatments before any work", 0, received.size());
	}

	public void testSaveAndRetrieveTreatments() throws Exception {
		Collection<Treatment> toSave = createTreatmentTestData();

		storage.saveTreatments(toSave);

		Map<UUID, Treatment> received = storage.getAllTreatments();

		assertEquals("Size after work", toSave.size(), received.size());
		for (Treatment t :  toSave) {
			assertTrue("Contains " + t, received.values().contains(t));
		}

		for (UUID uuid : received.keySet()) {
			Treatment treatment = received.get(uuid);
			assertEquals("Key for treatment", uuid, treatment.getUUID());
		}
	}

	public void testClearForTreatment() throws Exception {
		Collection<Treatment> toSave = createTreatmentTestData();

		storage.saveTreatments(toSave);

		storage.clear();

		Map<UUID, Treatment> received = storage.getAllTreatments();

		assertEquals("Empty after clear", 0, received.size());
	}

	private Collection<Treatment> createTreatmentTestData() {
		Collection<Treatment> toSave = new ArrayList<Treatment>();

		toSave.add(new Treatment(LocalDate.now(), 1, "INF1", "MED1"));
		toSave.add(new Treatment(null, 2, "INF2", "MED2"));
		toSave.add(new Treatment(LocalDate.now(), null, "INF3", "MED3"));
		toSave.add(new Treatment(LocalDate.now(), 4, null, "MED4"));
		toSave.add(new Treatment(LocalDate.now(), 5, "INF5", null));
		return toSave;
	}

	public void testSaveAndRetrieveSickDays() throws Exception {
		Collection<SickDay> toSave = createSickDaysTestData();
		storage.saveSickDays(toSave);

		Map<UUID, SickDay> received = storage.getAllSickDays();

		assertEquals("Size after work", toSave.size(), received.size());
		for (SickDay obj :  toSave) {
			assertTrue("Contains " + obj, received.values().contains(obj));
		}

		for (UUID uuid : received.keySet()) {
			SickDay obj = received.get(uuid);
			assertEquals("Key", uuid, obj.getUUID());
		}
	}

	public void testClearForSickDays() throws Exception {
		Collection<SickDay> toSave = createSickDaysTestData();
		storage.saveSickDays(toSave);

		storage.clear();

		Map<UUID, SickDay> received = storage.getAllSickDays();

		assertEquals("Empty after clear", 0, received.size());
	}

	private Collection<SickDay> createSickDaysTestData() {
		Collection<SickDay> toSave = new ArrayList<SickDay>();

		toSave.add(new SickDay(LocalDate.now().minusDays(1),	LocalDate.now().plusDays(1)));
		toSave.add(new SickDay(null, 							LocalDate.now().plusDays(2)));
		toSave.add(new SickDay(LocalDate.now().minusDays(1), 	null));
		return toSave;
	}

}
