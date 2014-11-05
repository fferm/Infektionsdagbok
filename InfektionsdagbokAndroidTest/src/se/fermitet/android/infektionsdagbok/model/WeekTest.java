package se.fermitet.android.infektionsdagbok.model;

import junit.framework.TestCase;

import org.joda.time.LocalDate;

public class WeekTest extends TestCase {

	public void testTextualRepresentation() {
		LocalDate dt = new LocalDate();

		Week w = new Week(dt);

		assertEquals("" + dt.getYear() + "-" + dt.getWeekOfWeekyear(), w.toString());
	}

	public void testConstructorFromToString() {
		LocalDate dt = new LocalDate(2012,2,4); // Not std time
		Week original = new Week(dt);

		Week retrieved = new Week(original.toString());

		assertEquals(original, retrieved);
	}

	public void testConstructorWithYearAndWeeknum() throws Exception {
		int year = 2013;
		int weeknum = 4;

		Week original = new Week(new LocalDate().withYear(year).withWeekOfWeekyear(weeknum));
		Week retrieved = new Week(year, weeknum);

		assertEquals(original, retrieved);
	}

	public void testYearEndSpecials() throws Exception {
		assertEquals("late in year with number on next", new Week("2013-01"), new Week(new LocalDate(2012 ,12, 31)));
		assertEquals("early in year with number on previous", new Week("2011-52"), new Week(new LocalDate(2012, 1, 1)));
		assertEquals("week 53 - previous year", new Week("2009-53"), new Week(new LocalDate(2009, 12, 28)));
		assertEquals("week 53 - next year", new Week("2009-53"), new Week(new LocalDate(2010, 1, 1)));
	}

	public void testAccessors() throws Exception {
		Week wk = new Week("2014-02");

		assertEquals("Year", 2014, wk.year());
		assertEquals("Weeknum", 2, wk.weeknum());

		assertEquals("Start date", "2014-01-06", wk.getMondayString());
		assertEquals("End date", "2014-01-12", wk.getSundayString());
	}

	public void testWeeksInAYear() throws Exception {
		assertEquals("52 week year", 52, Week.weeksInTheYear(2014));
		assertEquals("53 week year", 53, Week.weeksInTheYear(2009));
	}

	public void testValueObject() throws Exception {
		LocalDate monday = new LocalDate(2014, 1, 20);
		LocalDate sunday = new LocalDate(2014, 1, 26);
		LocalDate nextMonday = new LocalDate(2014, 1, 27);

		Week mon1 = new Week(monday);
		Week mon2 = new Week(monday);
		Week sun = new Week(sunday);
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
		LocalDate initialDt = new LocalDate();
		LocalDate previousDt = initialDt.minusWeeks(1);

		Week initialWeek = new Week(initialDt);
		Week previousWeekFromMethod = initialWeek.previous();
		Week previousWeekFromConstructor = new Week(previousDt);

		assertEquals(previousWeekFromConstructor, previousWeekFromMethod);
	}

	public void testNextWeek() throws Exception {
		LocalDate initialDt = new LocalDate();
		LocalDate nextDt = initialDt.plusWeeks(1);

		Week initialWeek = new Week(initialDt);
		Week nextWeekFromMethod = initialWeek.next();
		Week nextWeekFromConstructor = new Week(nextDt);

		assertEquals(nextWeekFromConstructor, nextWeekFromMethod);
	}

	public void testIsBeforeAndAfter() throws Exception {
		Week initial = new Week(new LocalDate(2014, 1, 1));
		Week sameWeek1 = new Week(new LocalDate(2014, 1, 2));
		Week sameWeek2 = new Week(new LocalDate(2013, 12, 31));
		Week weekAfter = new Week(new LocalDate(2014, 1, 8));
		Week weekBefore = new Week(new LocalDate(2013, 12, 25));

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
