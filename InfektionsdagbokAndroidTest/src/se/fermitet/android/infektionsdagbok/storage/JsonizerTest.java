package se.fermitet.android.infektionsdagbok.storage;

import junit.framework.TestCase;

import org.joda.time.LocalDate;

import se.fermitet.android.infektionsdagbok.helper.NameFromIdHelper;
import se.fermitet.android.infektionsdagbok.model.SickDay;
import se.fermitet.android.infektionsdagbok.model.Treatment;
import se.fermitet.android.infektionsdagbok.model.Week;
import se.fermitet.android.infektionsdagbok.model.WeekAnswers;

public class JsonizerTest extends TestCase {

	public void testToFromWeekAnswers() throws Exception {
		WeekAnswers wa = new WeekAnswers(new Week(LocalDate.now()));

		for (int id : WeekAnswers.questionIds) {
			wa.setAnswer(id, ! wa.getAnswer(id));

			assertEquals("after changing " + NameFromIdHelper.getNameFromId(id), wa, Jsonizer.weekAnswersFromJSON(Jsonizer.weekAnswersToJSON(wa)));
		}
	}

	public void testToFromTreatment() throws Exception {
		String origInfectionType = "INFTYPE";
		String origMedicine = "MEDICINE";
		LocalDate origStartingDate = LocalDate.now();
		int origNumDays = 5;

		Treatment original = new Treatment(origStartingDate, origNumDays, origInfectionType, origMedicine);

		Treatment nullInfectionType = new Treatment(origStartingDate, origNumDays, null, origMedicine);
		Treatment nullMedicine = new Treatment(origStartingDate, origNumDays, origInfectionType, null);
		Treatment nullStartingDate = new Treatment(null, origNumDays, origInfectionType, origMedicine);
		Treatment nullNumDays = new Treatment(origStartingDate, null, origInfectionType, origMedicine);

		assertEquals("Original", original, Jsonizer.treatmentFromJSON(Jsonizer.modelObjectToJSON(original)));
		assertEquals("nullInfectionType", nullInfectionType, Jsonizer.treatmentFromJSON(Jsonizer.modelObjectToJSON(nullInfectionType)));
		assertEquals("nullMedicine", nullMedicine, Jsonizer.treatmentFromJSON(Jsonizer.modelObjectToJSON(nullMedicine)));
		assertEquals("nullStartingDate", nullStartingDate, Jsonizer.treatmentFromJSON(Jsonizer.modelObjectToJSON(nullStartingDate)));
		assertEquals("nullNumDays", nullNumDays, Jsonizer.treatmentFromJSON(Jsonizer.modelObjectToJSON(nullNumDays)));
	}

	public void testToFromSickDay() throws Exception {
		LocalDate start = LocalDate.now().minusWeeks(2);
		LocalDate end = LocalDate.now();

		SickDay original = new SickDay(start, end);
		SickDay nullStart = new SickDay(null, end);
		SickDay nullEnd = new SickDay(start, null);

		assertEquals("original", original, Jsonizer.sickDayFromJSON(Jsonizer.modelObjectToJSON(original)));
		assertEquals("nullStart", nullStart, Jsonizer.sickDayFromJSON(Jsonizer.modelObjectToJSON(nullStart)));
		assertEquals("nullEnd", nullEnd, Jsonizer.sickDayFromJSON(Jsonizer.modelObjectToJSON(nullEnd)));

	}
}
