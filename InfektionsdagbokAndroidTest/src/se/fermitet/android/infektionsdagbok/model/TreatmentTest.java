package se.fermitet.android.infektionsdagbok.model;

import java.text.DateFormat;
import java.util.UUID;

import junit.framework.TestCase;

import org.joda.time.DateTime;

public class TreatmentTest extends TestCase {

	public void testStartingValues() throws Exception {
		Treatment treatment = new Treatment();

		assertNull("Null InfectionType", treatment.getInfectionType());
		assertNull("Null Medicine", treatment.getMedicine());
		assertNull("Null startingDate", treatment.getStartingDate());
		assertNull("Null numDays", treatment.getNumDays());
	}

	public void testConstructorWithValues() throws Exception {
		String infectionType ="TESTINFECTIONTYPE";
		String medicine = "MEDICINE";
		DateTime startingDate = DateTime.now();
		Integer numDays = 5;

		Treatment treatment = new Treatment(startingDate, numDays, infectionType, medicine);

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

		Treatment original = new Treatment(startingDate, numDays, infectionType, medicine);
		Treatment copy = new Treatment(original);

		assertEquals("Equal", original, copy);
		assertFalse("Not same", original == copy);
	}

	public void testGettersAndSetter() throws Exception {
		Treatment treatment = new Treatment();

		UUID testUUID = UUID.randomUUID();
		treatment.setUUID(testUUID);
		assertEquals("UUID", testUUID, treatment.getUUID());

		String testInfectionType = "TESTINFECTIONTYPE";
		treatment.setInfectionType(testInfectionType);
		assertEquals("Infection type", testInfectionType, treatment.getInfectionType());

		String testMedicine = "TESTMEDICINE";
		treatment.setMedicine(testMedicine);
		assertEquals("Medicine", testMedicine, treatment.getMedicine());

		DateTime testStartingDate = DateTime.now();
		treatment.setStartingDate(testStartingDate);
		assertEquals("Starting Date", testStartingDate.withMillisOfDay(0), treatment.getStartingDate());

		Integer testNumDays = 1000;
		treatment.setNumDays(testNumDays);
		assertEquals("Num days", testNumDays, treatment.getNumDays());
	}

	public void testToString() throws Exception {
		DateTime start = DateTime.now();
		int numDays = 1;
		String medicine = "MEDICINE";
		String infectionType = "INFECTION_TYPE";

		Treatment normal = new Treatment(start, numDays, infectionType, medicine);
		Treatment nullInfection = new Treatment(start, numDays, null, medicine);
		Treatment nullMedicine = new Treatment(start, numDays, infectionType, null);
		Treatment nullStart = new Treatment(null, numDays, infectionType, medicine);

		DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);

		assertEquals("normal", "Treatment{uuid: " + normal.getUUID().toString() + ", start: " + df.format(start.toDate()) + ", numDays: " + numDays + ", medicine: " + medicine + ", infectionType: " + infectionType + "}", normal.toString());
		assertEquals("nullMedicine", "Treatment{uuid: " + nullMedicine.getUUID().toString() + ", start: " + df.format(start.toDate()) + ", numDays: " + numDays + ", medicine: null, infectionType: " + infectionType + "}", nullMedicine.toString());
		assertEquals("nullInfection", "Treatment{uuid: " + nullInfection.getUUID().toString() + ", start: " + df.format(start.toDate()) + ", numDays: " + numDays + ", medicine: " + medicine + ", infectionType: null}", nullInfection.toString());
		assertEquals("nullStart", "Treatment{uuid: " + nullStart.getUUID().toString() + ", start: null, numDays: " + numDays + ", medicine: " + medicine + ", infectionType: " + infectionType + "}", nullStart.toString());
	}

	public void testStartingDateString() throws Exception {
		DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);

		DateTime start = DateTime.now();

		Treatment normal = new Treatment(start, 0, null, null);
		Treatment nullStart = new Treatment(null, 0, null, null);

		assertEquals("Normal", df.format(start.toDate()), normal.getStartingDateString());
		assertNull("null", nullStart.getStartingDateString());
	}

	public void testValueObject() throws Exception {
		String origInfectionType = "INFTYPE";
		String origMedicine = "MEDICINE";
		DateTime origStartingDate = DateTime.now();
		int origNumDays = 5;

		Treatment original = new Treatment(origStartingDate, origNumDays, origInfectionType, origMedicine);
		Treatment equal = new Treatment(original);

		Treatment sameValuesDifferentUUID = new Treatment(origStartingDate, origNumDays, origInfectionType, origMedicine);

		Treatment nullInfectionType = new Treatment(origStartingDate, origNumDays, null, origMedicine);
		Treatment nullMedicine = new Treatment(origStartingDate, origNumDays, origInfectionType, null);
		Treatment nullStartingDate = new Treatment(null, origNumDays, origInfectionType, origMedicine);
		Treatment nullNumDays = new Treatment(origStartingDate, null, origInfectionType, origMedicine);

		Treatment diffInfectionType = new Treatment(origStartingDate, origNumDays, "DIFFERENT", origMedicine);
		Treatment diffMedicine = new Treatment(origStartingDate, origNumDays, origInfectionType, "DIFFERENT");
		Treatment diffStartingDate = new Treatment(new DateTime(2012,1,1,1,1), origNumDays, origInfectionType, origMedicine);
		Treatment diffNumDays = new Treatment(origStartingDate, 20, origInfectionType, origMedicine);


		Treatment diffStartingDateButSameDay = new Treatment(original);
		diffStartingDateButSameDay.setStartingDate(diffStartingDateButSameDay.getStartingDate().plusSeconds(1));

		assertTrue("Equal to equal", original.equals(equal));
		assertTrue("Equal to same starting date but same day", original.equals(diffStartingDateButSameDay));

		assertFalse("Not equal to sameValuesDifferentUUID", original.equals(sameValuesDifferentUUID));
		assertFalse("Not equal to nullInfectionType", original.equals(nullInfectionType));
		assertFalse("Not equal to nullMedicine", original.equals(nullMedicine));
		assertFalse("Not equal to nullStartingDate", original.equals(nullStartingDate));
		assertFalse("Not equal to nullNumDays", original.equals(nullNumDays));
		assertFalse("Not equal to diffInfectionType", original.equals(diffInfectionType));
		assertFalse("Not equal to diffMedicine", original.equals(diffMedicine));
		assertFalse("Not equal to diffStartingDate", original.equals(diffStartingDate));
		assertFalse("Not equal to diffNumDays", original.equals(diffNumDays));

		assertTrue("Hash code same for equal objects", original.hashCode() == diffStartingDateButSameDay.hashCode());

		assertFalse("Not equal to null", original.equals(null));
		assertFalse("Not equal to object of different class", original.equals("TEST"));
	}
}
