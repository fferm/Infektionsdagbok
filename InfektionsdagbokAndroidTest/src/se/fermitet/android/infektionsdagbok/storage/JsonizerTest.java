package se.fermitet.android.infektionsdagbok.storage;

import junit.framework.TestCase;

import org.joda.time.DateTime;

import se.fermitet.android.infektionsdagbok.helper.NameFromIdHelper;
import se.fermitet.android.infektionsdagbok.model.Treatment;
import se.fermitet.android.infektionsdagbok.model.Week;
import se.fermitet.android.infektionsdagbok.model.WeekAnswers;

public class JsonizerTest extends TestCase {

	public void testToFromWeekAnswers() throws Exception {
		WeekAnswers wa = new WeekAnswers(new Week(DateTime.now()));
		
		for (int id : WeekAnswers.questionIds) {
			wa.setAnswer(id, ! wa.getAnswer(id));

			assertEquals("after changing " + NameFromIdHelper.getNameFromId(id), wa, Jsonizer.weekAnswersFromJSON(Jsonizer.weekAnswersToJSON(wa)));
		}
	}
	
	public void testToFromTreatment() throws Exception {
		String origInfectionType = "INFTYPE";
		String origMedicine = "MEDICINE";
		DateTime origStartingDate = DateTime.now();
		int origNumDays = 5;

		Treatment original = new Treatment(origInfectionType, origMedicine, origStartingDate, origNumDays);
		
		Treatment nullInfectionType = new Treatment(null, origMedicine, origStartingDate, origNumDays);
		Treatment nullMedicine = new Treatment(origInfectionType, null, origStartingDate, origNumDays);
		Treatment nullStartingDate = new Treatment(origInfectionType, origMedicine, null, origNumDays);
		
		assertEquals("Original", original, Jsonizer.treatmentFromJSON(Jsonizer.treatmentToJSON(original)));
		assertEquals("nullInfectionType", nullInfectionType, Jsonizer.treatmentFromJSON(Jsonizer.treatmentToJSON(nullInfectionType)));
		assertEquals("nullMedicine", nullMedicine, Jsonizer.treatmentFromJSON(Jsonizer.treatmentToJSON(nullMedicine)));
		assertEquals("nullStartingDate", nullStartingDate, Jsonizer.treatmentFromJSON(Jsonizer.treatmentToJSON(nullStartingDate)));
	}
}
