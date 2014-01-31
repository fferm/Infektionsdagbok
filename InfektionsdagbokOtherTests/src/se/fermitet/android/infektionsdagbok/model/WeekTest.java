package se.fermitet.android.infektionsdagbok.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.joda.time.DateTime;
import org.junit.Test;

public class WeekTest {

	@Test
	public void textualRepresentation() {
		DateTime dt = new DateTime();

		Week w = new Week(dt);

		assertEquals("" + dt.getYear() + "-" + dt.getWeekOfWeekyear(), w.toString());
	}


	@Test
	public void valueObject() throws Exception {
		DateTime mondayMorning = new DateTime(2014, 1, 20, 0, 0, 1);
		DateTime sundayEvening = new DateTime(2014, 1, 26, 23, 59, 59);
		DateTime nextMonday = new DateTime(2014, 1, 27, 0, 0, 1);

		Week mon1 = new Week(mondayMorning);
		Week mon2 = new Week(mondayMorning);
		Week sun = new Week(sundayEvening);
		Week nxt = new Week(nextMonday);

		assertTrue("Equal to itself", mon1.equals(mon1));
		assertTrue("Equal to other week of same time", mon1.equals(mon2));
		assertTrue("Equal to other week of time in same week", mon1.equals(sun));
		assertFalse("Not equal to next week", mon1.equals(nxt));
		assertFalse("Not equal to object of other class", mon1.equals("STRING"));
		assertFalse("Not equal to null", mon1.equals(null));

		assertTrue("hashCode of other week of same time", mon1.hashCode() == mon2.hashCode());
		assertTrue("hashCode of other week of time in same week", mon1.hashCode() == sun.hashCode());
	}

	@Test
	public void previousWeek() throws Exception {
		DateTime initialDt = new DateTime();
		DateTime previousDt = initialDt.minusWeeks(1);

		Week initialWeek = new Week(initialDt);
		Week previousWeekFromMethod = initialWeek.previous();
		Week previousWeekFromConstructor = new Week(previousDt);

		assertEquals(previousWeekFromConstructor, previousWeekFromMethod);
	}

	@Test
	public void nextWeek() throws Exception {
		DateTime initialDt = new DateTime();
		DateTime nextDt = initialDt.plusWeeks(1);

		Week initialWeek = new Week(initialDt);
		Week nextWeekFromMethod = initialWeek.next();
		Week nextWeekFromConstructor = new Week(nextDt);

		assertEquals(nextWeekFromConstructor, nextWeekFromMethod);

	}
}
