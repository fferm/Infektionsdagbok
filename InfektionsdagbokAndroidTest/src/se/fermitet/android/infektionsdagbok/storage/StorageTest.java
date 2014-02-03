package se.fermitet.android.infektionsdagbok.storage;

import org.joda.time.DateTime;

import se.fermitet.android.infektionsdagbok.model.Week;
import se.fermitet.android.infektionsdagbok.model.WeekAnswers;
import android.test.AndroidTestCase;

public class StorageTest extends AndroidTestCase {

	public void testSingleton() throws Exception {
		Storage s1 = Storage.instance();
		Storage s2 = Storage.instance();

		assertNotNull("s1 not null", s1);
		assertNotNull("s2 not null", s2);
		assertSame("same objects from 2 instance() calls", s1, s2);
	}

	public void testNoExistingAnswerShouldGiveNullAnswer() throws Exception {
		Week week = new Week(new DateTime());

		assertNull(Storage.instance().getAnswersForWeek(week));
	}

	public void testSaveAndRetrieveGivesEqualResponse() throws Exception {
		Week week = new Week(new DateTime());

		WeekAnswers original = new WeekAnswers(week);

		Storage.instance().saveAnswers(original);

		WeekAnswers retrieved = Storage.instance().getAnswersForWeek(week);

		assertEquals(original, retrieved);

	}
}
