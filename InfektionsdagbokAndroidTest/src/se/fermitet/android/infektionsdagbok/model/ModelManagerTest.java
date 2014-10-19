package se.fermitet.android.infektionsdagbok.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;

import se.fermitet.android.infektionsdagbok.R;
import se.fermitet.android.infektionsdagbok.storage.Storage;
import android.test.AndroidTestCase;

public class ModelManagerTest extends AndroidTestCase {

	private ModelManager modelManager;

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		// TODO: maybe this should be done through some factory instead
		modelManager = new ModelManager(new Storage(getContext()));
	}

	@Override
	protected void tearDown() throws Exception {
		if (this.modelManager != null) {
			this.modelManager.reset();
		}
		super.tearDown();
	}

	public void testInitialWeekAnswers() throws Exception {
		WeekAnswers initial = modelManager.getInitialWeekAnswers();

		assertEquals("Week in initial WeekAnswers should be current week", new Week(new DateTime()), initial.week);
	}

	public void testPreviousWeekWeekAnswers() throws Exception {
		WeekAnswers initial = modelManager.getInitialWeekAnswers();
		WeekAnswers previous = modelManager.getPreviousWeekAnswers(initial);

		Week previousWeek = initial.week.previous();

		assertEquals(previousWeek, previous.week);
	}

	public void testNextWeekWeekAnswers() throws Exception {
		WeekAnswers initial = modelManager.getInitialWeekAnswers();
		WeekAnswers next = modelManager.getNextWeekAnswers(initial);

		Week nextWeek = initial.week.next();

		assertEquals(nextWeek, next.week);
	}

	public void testWeekAnswersAreStored() throws Exception {
		WeekAnswers initial = modelManager.getInitialWeekAnswers();

		// Change some data
		initial.setAnswer(R.id.fever, !initial.getAnswer(R.id.fever));
		initial.setAnswer(R.id.malaise, !initial.getAnswer(R.id.malaise));

		WeekAnswers previous = modelManager.getPreviousWeekAnswers(initial);
		WeekAnswers backAgain = modelManager.getNextWeekAnswers(previous);

		assertEquals("equals after going back and forward", initial, backAgain);

		WeekAnswers forward = modelManager.getNextWeekAnswers(backAgain);
		backAgain = modelManager.getPreviousWeekAnswers(forward);

		assertEquals("equals after going forward and back", initial, backAgain);
	}

	public void testResetWeekAnswers() throws Exception {
		WeekAnswers initial = modelManager.getInitialWeekAnswers();

		// Change some data
		initial.setAnswer(R.id.fever, !initial.getAnswer(R.id.fever));
		initial.setAnswer(R.id.malaise, !initial.getAnswer(R.id.malaise));

		modelManager.reset();

		WeekAnswers afterReset = modelManager.getInitialWeekAnswers();

		assertFalse(initial.equals(afterReset));
	}

	public void testGetAllWeekAnswersForYear() throws Exception {
		int year = 2014;

		Collection<WeekAnswers> toSave = prepareTestDataIndexedByWeek(year).values();

		// Save
		modelManager.saveWeekAnswers(toSave);

		// Retrieve
		Map<Week, WeekAnswers> retrieved = modelManager.getAllWeekAnswersInYear(year);

		for (Week wk : retrieved.keySet()) {
			WeekAnswers wa = retrieved.get(wk);
			assertNotNull("WeekAnswers retrieved with a week as key should have non-null value", wa);
			assertEquals("Week of WeekAnswers retrieved with a Week as key should be equal to the key", wk, wa.week);
		}

		Collection<WeekAnswers> values = retrieved.values();

		// Check result
		assertEquals("Size of answer", toSave.size(), values.size());
		assertTrue("Contains all", values.containsAll(toSave));
	}
	
	public void testGetEarliestWeekAnswers() throws Exception {
		WeekAnswers wa1 = new WeekAnswers(new Week(new DateTime(2013, 1, 1, 1, 1, 1)));
		WeekAnswers wa2 = new WeekAnswers(new Week(new DateTime(2013, 2, 1, 1, 1, 1)));
		WeekAnswers wa3 = new WeekAnswers(new Week(new DateTime(2014, 1, 1, 1, 1, 1)));
		WeekAnswers wa4 = new WeekAnswers(new Week(new DateTime(2013, 1, 8, 1, 1, 1)));
		
		List<WeekAnswers> weekAnswers = new ArrayList<WeekAnswers>();
		weekAnswers.add(wa4);
		weekAnswers.add(wa3);
		weekAnswers.add(wa2);
		weekAnswers.add(wa1);
		
		modelManager.saveWeekAnswers(weekAnswers);
		
		WeekAnswers retrieved = modelManager.getEarliestWeekAnswers();
		
		assertEquals(wa1, retrieved);
	}
	
	public void testSaveAndGetTreatments() throws Exception {
		Collection<Treatment> received = modelManager.getAllTreatments();
		
		assertNotNull("Not null before any work", received);
		assertEquals("Empty treatments before any work", 0, received.size());
		
		Treatment t1 = new Treatment("INF1", "MED1", DateTime.now(), 1);
		Treatment t2 = new Treatment("INF2", "MED2", DateTime.now(), 2);
		Treatment t3 = new Treatment("INF3", "MED3", DateTime.now(), 3);
		Treatment t4 = new Treatment("INF4", "MED4", DateTime.now(), 4);
		
		modelManager.saveTreatment(t1);
		modelManager.saveTreatment(t2);
		modelManager.saveTreatment(t3);
		modelManager.saveTreatment(t4);
		
		received = modelManager.getAllTreatments();
		
		assertEquals("Size after work", 4, received.size());
		assertTrue("Contains t1", received.contains(t1));
		assertTrue("Contains t2", received.contains(t2));
		assertTrue("Contains t3", received.contains(t3));
		assertTrue("Contains t4", received.contains(t4));
	}
	
	public void testResetTreatment() throws Exception {
		Treatment t1 = new Treatment("INF1", "MED1", DateTime.now(), 1);
		Treatment t2 = new Treatment("INF2", "MED2", DateTime.now(), 2);
		Treatment t3 = new Treatment("INF3", "MED3", DateTime.now(), 3);
		Treatment t4 = new Treatment("INF4", "MED4", DateTime.now(), 4);
		
		modelManager.saveTreatment(t1);
		modelManager.saveTreatment(t2);
		modelManager.saveTreatment(t3);
		modelManager.saveTreatment(t4);
		
		modelManager.reset();
		
		Collection<Treatment> received = modelManager.getAllTreatments();
		
		assertEquals("Empty after reset", 0, received.size());
	}

	public static Map<Week, WeekAnswers> prepareTestDataIndexedByWeek(int year) {
		Map<Week, WeekAnswers> toSave = new HashMap<Week, WeekAnswers>();

		// Add one WeekAnswers with each one of the questions marked as true
		Week week = new Week(year + "-01");
		for (int i : WeekAnswers.questionIds) {
			WeekAnswers answ = new WeekAnswers(week);
			answ.setAnswer(i, true);

			toSave.put(week, answ);

			week = week.next();
		}

		// Add one WeekAnswers with no questions marked as true
		toSave.put(week, new WeekAnswers(week));

		return toSave;
	}
}

