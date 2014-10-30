package se.fermitet.android.infektionsdagbok.model;

import junit.framework.TestCase;

import org.joda.time.DateTime;

public class SickDayTest extends TestCase {

	public void testStartingValues() throws Exception {
		SickDay sickDay = new SickDay();

		assertNotNull("Not null uuid", sickDay.getUUID());
		assertNull("Null starting day", sickDay.getStart());
		assertNull("Null end day", sickDay.getEnd());
	}

	public void testConstructorWithValues() throws Exception {
		DateTime start = DateTime.now().minusDays(2);
		DateTime end = DateTime.now();

		SickDay sickDay = new SickDay(start, end);

		assertEquals("Start", start.withMillisOfDay(0), sickDay.getStart());
		assertEquals("End", end.withMillisOfDay(0), sickDay.getEnd());
	}

	public void testCopyConstructor() throws Exception {
		DateTime start = DateTime.now().minusDays(2);
		DateTime end = DateTime.now();

		SickDay original = new SickDay(start, end);
		SickDay copy = new SickDay(original);

		assertEquals("Equal", original, copy);
		assertFalse("Not same", original == copy);
	}

	public void testGettersAndSetter() throws Exception {
		// TODO
		fail("TODO");
	}

	public void testToString() throws Exception {
		// TODO
		fail("TODO");
	}

	public void testStartingDateString() throws Exception {
		// TODO
		fail("TODO");
	}

	public void testValueObject() throws Exception {
		DateTime origStart = DateTime.now().minusDays(2);
		DateTime origEnd = DateTime.now();

		SickDay original = new SickDay(origStart, origEnd);
		SickDay equal = new SickDay(original);

		SickDay sameValuesDifferentUUID = new SickDay(origStart, origEnd);

		SickDay nullStart = new SickDay(null, origEnd);
		SickDay nullEnd = new SickDay(origStart, null);

		SickDay diffStart = new SickDay(DateTime.now().minusMonths(10), origEnd);
		SickDay diffEnd = new SickDay(origStart, DateTime.now().plusDays(10));

		SickDay diffStartButSameDay = new SickDay(original);
		diffStartButSameDay.setStart(diffStartButSameDay.getStart().plusSeconds(1));

		SickDay diffEndButSameDay = new SickDay(original);
		diffEndButSameDay.setEnd(diffEndButSameDay.getEnd().plusSeconds(1));


		assertTrue("Equal to equal", original.equals(equal));
		assertTrue("Equal to same start but same day", original.equals(diffStartButSameDay));
		assertTrue("Equal to same end but same day", original.equals(diffEndButSameDay));

		assertFalse("Not equal to sameValuesDifferentUUID", original.equals(sameValuesDifferentUUID));
		assertFalse("Not equal to nullStart", original.equals(nullStart));
		assertFalse("Not equal to nullEnd", original.equals(nullEnd));
		assertFalse("Not equal to diffStart", original.equals(diffStart));
		assertFalse("Not equal to diffEnd", original.equals(diffEnd));

		assertTrue("Hash code same for equal objects", original.hashCode() == equal.hashCode());

		assertFalse("Not equal to null", original.equals(null));
		assertFalse("Not equal to object of different class", original.equals("TEST"));
	}






}
