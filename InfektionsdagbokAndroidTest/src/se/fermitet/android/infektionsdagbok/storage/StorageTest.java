package se.fermitet.android.infektionsdagbok.storage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.joda.time.DateTime;

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
		Week week = new Week(new DateTime());

		assertNull(storage.getAnswersForWeek(week));
	}

	public void testSaveAndRetrieveGivesEqualResponse() throws Exception {
		Week week = new Week(new DateTime());
		WeekAnswers original = new WeekAnswers(week);

		storage.saveAnswers(original);

		WeekAnswers retrieved = storage.getAnswersForWeek(week);

		assertEquals(original, retrieved);
	}

	public void testClearForWeekAnswers() throws Exception {
		Week week = new Week(new DateTime());
		WeekAnswers original = new WeekAnswers(week);

		storage.saveAnswers(original);

		storage.clear();

		assertNull(storage.getAnswersForWeek(week));
	}

	public void testGetAllAnswers() throws Exception {
		WeekAnswers wa1 = new WeekAnswers(new Week(new DateTime(2013, 1, 1, 1, 1, 1)));
		WeekAnswers wa2 = new WeekAnswers(new Week(new DateTime(2013, 2, 1, 1, 1, 1)));
		WeekAnswers wa3 = new WeekAnswers(new Week(new DateTime(2014, 1, 1, 1, 1, 1)));
		WeekAnswers wa4 = new WeekAnswers(new Week(new DateTime(2013, 1, 8, 1, 1, 1)));

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
		Collection<Treatment> received = storage.getAllTreatments();

		assertNotNull("Not null before any work", received);
		assertEquals("Empty treatments before any work", 0, received.size());
	}

	public void testSaveAndRetrieveTreatments() throws Exception {
		Collection<Treatment> toSave = new ArrayList<Treatment>();

		Treatment t1 = new Treatment(DateTime.now(), 1, "INF1", "MED1");
		Treatment t2 = new Treatment(DateTime.now(), 2, "INF2", "MED2");
		Treatment t3 = new Treatment(DateTime.now(), 3, "INF3", "MED3");
		Treatment t4 = new Treatment(DateTime.now(), 4, "INF4", "MED4");

		toSave.add(t1);
		toSave.add(t2);
		toSave.add(t3);
		toSave.add(t4);

		storage.saveTreatments(toSave);

		Collection<Treatment> received = storage.getAllTreatments();

		assertEquals("Size after work", 4, received.size());
		assertTrue("Contains t1", received.contains(t1));
		assertTrue("Contains t2", received.contains(t2));
		assertTrue("Contains t3", received.contains(t3));
		assertTrue("Contains t4", received.contains(t4));
	}

	public void testClearForTreatment() throws Exception {
		Collection<Treatment> toSave = new ArrayList<Treatment>();

		Treatment t1 = new Treatment(DateTime.now(), 1, "INF1", "MED1");
		Treatment t2 = new Treatment(DateTime.now(), 2, "INF2", "MED2");
		Treatment t3 = new Treatment(DateTime.now(), 3, "INF3", "MED3");
		Treatment t4 = new Treatment(DateTime.now(), 4, "INF4", "MED4");

		toSave.add(t1);
		toSave.add(t2);
		toSave.add(t3);
		toSave.add(t4);

		storage.saveTreatments(toSave);

		storage.clear();

		Collection<Treatment> received = storage.getAllTreatments();

		assertEquals("Empty after clear", 0, received.size());
	}
}
