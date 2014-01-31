package se.fermitet.android.infektionsdagbok.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import org.joda.time.DateTime;
import org.junit.Test;

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

}
