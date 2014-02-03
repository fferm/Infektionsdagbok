package se.fermitet.android.infektionsdagbok.model;

import junit.framework.TestCase;

import org.joda.time.DateTime;

import se.fermitet.android.infektionsdagbok.R;

public class ModelManagerTest extends TestCase {

	public void testSingleton() throws Exception {
		ModelManager firstTry = ModelManager.instance();
		ModelManager secondTry = ModelManager.instance();

		assertSame(firstTry, secondTry);
	}

	public void testInitialWeekAnswers() throws Exception {
		WeekAnswers initial = ModelManager.instance().getInitialWeekAnswers();

		assertEquals("Week in initial WeekAnswers should be current week", new Week(new DateTime()), initial.week);
	}

	public void testPreviousWeekWeekAnswers() throws Exception {
		WeekAnswers initial = ModelManager.instance().getInitialWeekAnswers();
		WeekAnswers previous = ModelManager.instance().getPreviousWeekAnswers(initial);

		Week previousWeek = initial.week.previous();

		assertEquals(previousWeek, previous.week);
	}

	public void testNextWeekWeekAnswers() throws Exception {
		WeekAnswers initial = ModelManager.instance().getInitialWeekAnswers();
		WeekAnswers next = ModelManager.instance().getNextWeekAnswers(initial);

		Week nextWeek = initial.week.next();

		assertEquals(nextWeek, next.week);
	}

	public void testWeekAnswersAreStored() throws Exception {
		WeekAnswers initial = ModelManager.instance().getInitialWeekAnswers();

		// Change some data
		initial.setAnswer(R.id.generallyWell, !initial.getAnswer(R.id.generallyWell));
		initial.setAnswer(R.id.malaise, !initial.getAnswer(R.id.malaise));

		WeekAnswers previous = ModelManager.instance().getPreviousWeekAnswers(initial);
		WeekAnswers backAgain = ModelManager.instance().getNextWeekAnswers(previous);

		assertEquals("equals after going back and forward", initial, backAgain);

		WeekAnswers forward = ModelManager.instance().getNextWeekAnswers(backAgain);
		backAgain = ModelManager.instance().getPreviousWeekAnswers(forward);

		assertEquals("equals after going forward and back", initial, backAgain);
	}

	public void testReset() throws Exception {
		WeekAnswers initial = ModelManager.instance().getInitialWeekAnswers();

		// Change some data
		initial.setAnswer(R.id.generallyWell, !initial.getAnswer(R.id.generallyWell));
		initial.setAnswer(R.id.malaise, !initial.getAnswer(R.id.malaise));

		ModelManager.instance().reset();

		WeekAnswers afterReset = ModelManager.instance().getInitialWeekAnswers();

		assertFalse(initial.equals(afterReset));

	}

}