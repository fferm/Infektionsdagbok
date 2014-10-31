package se.fermitet.android.infektionsdagbok.model;

import java.util.UUID;

import junit.framework.TestCase;

import org.joda.time.LocalDate;

public class SickDayTest extends TestCase {

	public void testStartingValues() throws Exception {
		SickDay sickDay = new SickDay();

		assertNotNull("Not null uuid", sickDay.getUUID());
		assertNull("Null starting day", sickDay.getStart());
		assertNull("Null end day", sickDay.getEnd());
	}

	public void testConstructorWithValues() throws Exception {
		LocalDate start = LocalDate.now().minusDays(2);
		LocalDate end = LocalDate.now();

		SickDay sickDay = new SickDay(start, end);

		assertEquals("Start", start, sickDay.getStart());
		assertEquals("End", end, sickDay.getEnd());
	}

	public void testCopyConstructor() throws Exception {
		LocalDate start = LocalDate.now().minusDays(2);
		LocalDate end = LocalDate.now();

		SickDay original = new SickDay(start, end);
		SickDay copy = new SickDay(original);

		assertEquals("Equal", original, copy);
		assertFalse("Not same", original == copy);
	}

	public void testGettersAndSetter() throws Exception {
		SickDay sickDay = new SickDay();

		UUID testUUID = UUID.randomUUID();
		sickDay.setUUID(testUUID);
		assertEquals("UUID", testUUID, sickDay.getUUID());

		LocalDate testStart = LocalDate.now().minusDays(10);
		sickDay.setStart(testStart);
		assertEquals("Start", testStart, sickDay.getStart());

		LocalDate testEnd = LocalDate.now().plusDays(10);
		sickDay.setEnd(testEnd);
		assertEquals("End", testEnd, sickDay.getEnd());
	}

	public void testToString() throws Exception {
		fail("TODO");
	}

	public void testStartingDateString() throws Exception {
		// TODO
		fail("TODO");
	}

	public void testValueObject() throws Exception {
		LocalDate origStart = LocalDate.now().minusDays(2);
		LocalDate origEnd = LocalDate.now();

		SickDay original = new SickDay(origStart, origEnd);
		SickDay equal = new SickDay(original);

		SickDay sameValuesDifferentUUID = new SickDay(origStart, origEnd);

		SickDay nullStart = new SickDay(null, origEnd);
		SickDay nullEnd = new SickDay(origStart, null);

		SickDay diffStart = new SickDay(LocalDate.now().minusMonths(10), origEnd);
		SickDay diffEnd = new SickDay(origStart, LocalDate.now().plusDays(10));

		assertTrue("Equal to equal", original.equals(equal));

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
