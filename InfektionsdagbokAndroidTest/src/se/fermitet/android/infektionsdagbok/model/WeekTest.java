package se.fermitet.android.infektionsdagbok.model;

import junit.framework.TestCase;

import org.joda.time.DateTime;

public class WeekTest extends TestCase {

	public void testTextualRepresentation() {
		DateTime dt = new DateTime();

		Week w = new Week(dt);

		assertEquals("" + dt.getYear() + "-" + dt.getWeekOfWeekyear(), w.toString());
	}

	public void testConstructorFromToString() {
		DateTime dt = new DateTime(2012,2,4,12,3,1); // Not std time
		Week original = new Week(dt);

		Week retrieved = new Week(original.toString());

		assertEquals(original, retrieved);
	}

	public void testConstructorWithYearAndWeeknum() throws Exception {
		int year = 2013;
		int weeknum = 4;

		Week original = new Week(new DateTime().withYear(year).withWeekOfWeekyear(weeknum));
		Week retrieved = new Week(year, weeknum);

		assertEquals(original, retrieved);
	}

	public void testYearEndSpecials() throws Exception {
		assertEquals("late in year with number on next", new Week("2013-01"), new Week(new DateTime(2012 ,12, 31, 1, 1)));
		assertEquals("early in year with number on previous", new Week("2011-52"), new Week(new DateTime(2012, 1, 1, 1, 1)));
		assertEquals("week 53 - previous year", new Week("2009-53"), new Week(new DateTime(2009, 12, 28, 1, 1)));
		assertEquals("week 53 - next year", new Week("2009-53"), new Week(new DateTime(2010, 1, 1, 1, 1)));
	}

	public void testAccessors() throws Exception {
		Week wk = new Week("2014-02");

		assertEquals("Year", 2014, wk.year());
		assertEquals("Weeknum", 2, wk.weeknum());
	}

	public void testWeeksInAYear() throws Exception {
		assertEquals("52 week year", 52, Week.weeksInTheYear(2014));
		assertEquals("53 week year", 53, Week.weeksInTheYear(2009));
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

	public void testIsBeforeAndAfter() throws Exception {
		Week initial = new Week(new DateTime(2014, 1, 1, 1, 1));
		Week sameWeek1 = new Week(new DateTime(2014, 1, 2, 1, 1));
		Week sameWeek2 = new Week(new DateTime(2013, 12, 31, 1, 1));
		Week weekAfter = new Week(new DateTime(2014, 1, 8, 1, 1));
		Week weekBefore = new Week(new DateTime(2013, 12, 25, 1, 1));

		assertTrue("initial before next week", initial.isBefore(weekAfter));
		assertFalse("initial after next week", initial.isAfter(weekAfter));

		assertFalse("initial before prev week", initial.isBefore(weekBefore));
		assertTrue("initial after prev week", initial.isAfter(weekBefore));

		assertFalse("initial before same week 1", initial.isBefore(sameWeek1));
		assertFalse("initial after same week 1", initial.isAfter(sameWeek1));

		assertFalse("initial before same week 2", initial.isBefore(sameWeek2));
		assertFalse("initial after same week 2", initial.isAfter(sameWeek2));
}
}
