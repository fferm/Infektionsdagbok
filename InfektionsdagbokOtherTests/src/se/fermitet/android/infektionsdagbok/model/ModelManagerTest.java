package se.fermitet.android.infektionsdagbok.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertSame;

import org.joda.time.DateTime;
import org.junit.Test;

import se.fermitet.android.infektionsdagbok.R;

public class ModelManagerTest {

	@Test
	public void singleton() throws Exception {
		ModelManager firstTry = ModelManager.instance();
		ModelManager secondTry = ModelManager.instance();

		assertSame(firstTry, secondTry);
	}

	@Test
	public void testInitialWeekAnswers() throws Exception {
		WeekAnswers initial = ModelManager.instance().getInitialWeekAnswers();

		assertEquals("Week in initial WeekAnswers should be current week", new Week(new DateTime()), initial.week);
	}

	@Test
	public void testPreviousWeekWeekAnswers() throws Exception {
		WeekAnswers initial = ModelManager.instance().getInitialWeekAnswers();
		WeekAnswers previous = ModelManager.instance().getPreviousWeekAnswers(initial);

		Week previousWeek = initial.week.previous();

		assertEquals(previousWeek, previous.week);
	}

	@Test
	public void testNextWeekWeekAnswers() throws Exception {
		WeekAnswers initial = ModelManager.instance().getInitialWeekAnswers();
		WeekAnswers next = ModelManager.instance().getNextWeekAnswers(initial);

		Week nextWeek = initial.week.next();

		assertEquals(nextWeek, next.week);
	}

	@Test
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

	@Test
	public void reset() throws Exception {
		WeekAnswers initial = ModelManager.instance().getInitialWeekAnswers();

		// Change some data
		initial.setAnswer(R.id.generallyWell, !initial.getAnswer(R.id.generallyWell));
		initial.setAnswer(R.id.malaise, !initial.getAnswer(R.id.malaise));

		ModelManager.instance().reset();

		WeekAnswers afterReset = ModelManager.instance().getInitialWeekAnswers();

		assertNotEquals(initial, afterReset);

	}

}
