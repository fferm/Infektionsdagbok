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

public class ModelManagerTest_WeekAnswers extends AndroidTestCase {

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
	
	public void testSaveTreatmentForUpdate() throws Exception {
		
	}
	
	public void testSaveTreatmentForInsert() throws Exception {
		// TODO
	}
}

