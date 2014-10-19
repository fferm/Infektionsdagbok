package se.fermitet.android.infektionsdagbok.model;

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
	
	public void testValueObject() throws Exception {
		String origInfectionType = "INFTYPE";
		String origMedicine = "MEDICINE";
		DateTime origStartingDate = DateTime.now();
		int origNumDays = 5;
		
		Treatment original = createTreatment(origInfectionType, origMedicine, origStartingDate, origNumDays);
		Treatment equal = createTreatment(origInfectionType, origMedicine, origStartingDate, origNumDays);
		
		Treatment nullInfectionType = createTreatment(null, origMedicine, origStartingDate, origNumDays);
		Treatment nullMedicine = createTreatment(origInfectionType, null, origStartingDate, origNumDays);
		Treatment nullStartingDate = createTreatment(origInfectionType, origMedicine, null, origNumDays);
		
		Treatment diffInfectionType = createTreatment("DIFFERENT", origMedicine, origStartingDate, origNumDays);
		Treatment diffMedicine = createTreatment(origInfectionType, "DIFFERENT", origStartingDate, origNumDays);
		Treatment diffStartingDate = createTreatment(origInfectionType, origMedicine, new DateTime(2012,1,1,1,1), origNumDays);
		Treatment diffNumDays = createTreatment(origInfectionType, origMedicine, origStartingDate, 20);
		Treatment diffStartingDateButSameDay = createTreatment(origInfectionType, origMedicine, DateTime.now().plusSeconds(1), origNumDays);

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
	
	private Treatment createTreatment(String infectionType, String medicine, DateTime startingDate, int numDays) {
		Treatment treatment = new Treatment();
		
		treatment.setInfectionType(infectionType);
		treatment.setMedicine(medicine);
		treatment.setStartingDate(startingDate);
		treatment.setNumDays(numDays);
		
		return treatment;
	}
}
