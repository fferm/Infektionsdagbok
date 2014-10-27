package se.fermitet.android.infektionsdagbok.model;

import java.text.DateFormat;

import junit.framework.TestCase;

import org.joda.time.DateTime;

public class TreatmentTest extends TestCase {

	public void testStartingValues() throws Exception {
		Treatment treatment = new Treatment();

		assertNull("Null InfectionType", treatment.getInfectionType());
		assertNull("Null Medicine", treatment.getMedicine());
		assertNull("Null startingDate", treatment.getStartingDate());
		assertEquals("0 numDays to start with", 0, treatment.getNumDays());
	}

	public void testConstructorWithValues() throws Exception {
		String infectionType ="TESTINFECTIONTYPE";
		String medicine = "MEDICINE";
		DateTime startingDate = DateTime.now();
		int numDays = 5;

		Treatment treatment = new Treatment(infectionType, medicine, startingDate, numDays);

		assertEquals("Infection type", infectionType, treatment.getInfectionType());
		assertEquals("Medicine", medicine, treatment.getMedicine());
		assertEquals("Starting date", startingDate.withMillisOfDay(0), treatment.getStartingDate());
		assertEquals("num days", numDays, treatment.getNumDays());
	}

	public void testCopyConstructor() throws Exception {
		String infectionType ="TESTINFECTIONTYPE";
		String medicine = "MEDICINE";
		DateTime startingDate = DateTime.now();
		int numDays = 5;

		Treatment original = new Treatment(infectionType, medicine, startingDate, numDays);
		Treatment copy = new Treatment(original);

		assertEquals("Equal", original, copy);
		assertFalse("Not same", original == copy);
	}

	public void testGettersAndSetter() throws Exception {
		Treatment treatment = new Treatment();

		String testInfectionType = "TESTINFECTIONTYPE";
		treatment.setInfectionType(testInfectionType);
		assertEquals("Infection type", testInfectionType, treatment.getInfectionType());

		String testMedicine = "TESTMEDICINE";
		treatment.setMedicine(testMedicine);
		assertEquals("Medicine", testMedicine, treatment.getMedicine());

		DateTime testStartingDate = DateTime.now();
		treatment.setStartingDate(testStartingDate);
		assertEquals("Starting Date", testStartingDate.withMillisOfDay(0), treatment.getStartingDate());

		int testNumDays = 1000;
		treatment.setNumDays(testNumDays);
		assertEquals("Num days", testNumDays, treatment.getNumDays());
	}

	public void testToString() throws Exception {
		DateTime start = DateTime.now();
		int numDays = 1;
		String medicine = "MEDICINE";
		String infectionType = "INFECTION_TYPE";

		Treatment normal = new Treatment(infectionType, medicine, start, numDays);
		Treatment nullInfection = new Treatment(null, medicine, start, numDays);
		Treatment nullMedicine = new Treatment(infectionType, null, start, numDays);
		Treatment nullStart = new Treatment(infectionType, medicine, null, numDays);

		DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);

		assertEquals("normal", "Treatment{start: " + df.format(start.toDate()) + ", numDays: " + numDays + ", medicine: " + medicine + ", infectionType: " + infectionType + "}", normal.toString());
		assertEquals("nullMedicine", "Treatment{start: " + df.format(start.toDate()) + ", numDays: " + numDays + ", medicine: null, infectionType: " + infectionType + "}", nullMedicine.toString());
		assertEquals("nullInfection", "Treatment{start: " + df.format(start.toDate()) + ", numDays: " + numDays + ", medicine: " + medicine + ", infectionType: null}", nullInfection.toString());
		assertEquals("nullStart", "Treatment{start: null, numDays: " + numDays + ", medicine: " + medicine + ", infectionType: " + infectionType + "}", nullStart.toString());
	}

	public void testStartingDateString() throws Exception {
		DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);

		DateTime start = DateTime.now();

		Treatment normal = new Treatment(null, null, start, 0);
		Treatment nullStart = new Treatment(null, null, null, 0);

		assertEquals("Normal", df.format(start.toDate()), normal.getStartingDateString());
		assertNull("null", nullStart.getStartingDateString());
	}

	public void testValueObject() throws Exception {
		String origInfectionType = "INFTYPE";
		String origMedicine = "MEDICINE";
		DateTime origStartingDate = DateTime.now();
		int origNumDays = 5;

		Treatment original = new Treatment(origInfectionType, origMedicine, origStartingDate, origNumDays);
		Treatment equal = new Treatment(origInfectionType, origMedicine, origStartingDate, origNumDays);

		Treatment nullInfectionType = new Treatment(null, origMedicine, origStartingDate, origNumDays);
		Treatment nullMedicine = new Treatment(origInfectionType, null, origStartingDate, origNumDays);
		Treatment nullStartingDate = new Treatment(origInfectionType, origMedicine, null, origNumDays);

		Treatment diffInfectionType = new Treatment("DIFFERENT", origMedicine, origStartingDate, origNumDays);
		Treatment diffMedicine = new Treatment(origInfectionType, "DIFFERENT", origStartingDate, origNumDays);
		Treatment diffStartingDate = new Treatment(origInfectionType, origMedicine, new DateTime(2012,1,1,1,1), origNumDays);
		Treatment diffNumDays = new Treatment(origInfectionType, origMedicine, origStartingDate, 20);
		Treatment diffStartingDateButSameDay = new Treatment(origInfectionType, origMedicine, DateTime.now().plusSeconds(1), origNumDays);

		assertTrue("Equal to equal", original.equals(equal));
		assertTrue("Equal to same starting date but same day", original.equals(diffStartingDateButSameDay));

		assertFalse("Not equal to nullInfectionType", original.equals(nullInfectionType));
		assertFalse("Not equal to nullMedicine", original.equals(nullMedicine));
		assertFalse("Not equal to nullStartingDate", original.equals(nullStartingDate));
		assertFalse("Not equal to diffInfectionType", original.equals(diffInfectionType));
		assertFalse("Not equal to diffMedicine", original.equals(diffMedicine));
		assertFalse("Not equal to diffStartingDate", original.equals(diffStartingDate));
		assertFalse("Not equal to diffNumDays", original.equals(diffNumDays));

		assertTrue("Hash code same for equal objects", original.hashCode() == diffStartingDateButSameDay.hashCode());

		assertFalse("Not equal to null", original.equals(null));
		assertFalse("Not equal to object of different class", original.equals("TEST"));
	}
}
