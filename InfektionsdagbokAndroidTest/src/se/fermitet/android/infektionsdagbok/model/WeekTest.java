package se.fermitet.android.infektionsdagbok.model;

import junit.framework.TestCase;

import org.joda.time.DateTime;

public class WeekTest extends TestCase {

	public void testTextualRepresentation() {
		DateTime dt = new DateTime();

		Week w = new Week(dt);

		assertEquals("" + dt.getYear() + "-" + dt.getWeekOfWeekyear(), w.toString());
	}


	public void testValueObject() throws Exception {
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

	public void testPreviousWeek() throws Exception {
		DateTime initialDt = new DateTime();
		DateTime previousDt = initialDt.minusWeeks(1);

		Week initialWeek = new Week(initialDt);
		Week previousWeekFromMethod = initialWeek.previous();
		Week previousWeekFromConstructor = new Week(previousDt);

		assertEquals(previousWeekFromConstructor, previousWeekFromMethod);
	}

	public void testNextWeek() throws Exception {
		DateTime initialDt = new DateTime();
		DateTime nextDt = initialDt.plusWeeks(1);

		Week initialWeek = new Week(initialDt);
		Week nextWeekFromMethod = initialWeek.next();
		Week nextWeekFromConstructor = new Week(nextDt);

		assertEquals(nextWeekFromConstructor, nextWeekFromMethod);

	}
}
