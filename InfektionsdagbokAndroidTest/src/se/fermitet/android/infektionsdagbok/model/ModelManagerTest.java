package se.fermitet.android.infektionsdagbok.model;

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

	public void testReset() throws Exception {
		WeekAnswers initial = modelManager.getInitialWeekAnswers();

		// Change some data
		initial.setAnswer(R.id.fever, !initial.getAnswer(R.id.fever));
		initial.setAnswer(R.id.malaise, !initial.getAnswer(R.id.malaise));

		modelManager.reset();

		WeekAnswers afterReset = modelManager.getInitialWeekAnswers();

		assertFalse(initial.equals(afterReset));
	}

}