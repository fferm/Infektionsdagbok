package se.fermitet.android.infektionsdagbok.storage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.joda.time.DateTime;

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

	public void testClearOutFiles() throws Exception {
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
}
