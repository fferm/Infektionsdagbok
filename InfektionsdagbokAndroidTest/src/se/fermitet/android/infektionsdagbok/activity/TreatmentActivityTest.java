package se.fermitet.android.infektionsdagbok.activity;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collection;

import org.joda.time.DateTime;

import se.fermitet.android.infektionsdagbok.model.ModelManager;
import se.fermitet.android.infektionsdagbok.model.Treatment;
import se.fermitet.android.infektionsdagbok.storage.Storage;

public class TreatmentActivityTest extends ActivityTestWithSolo<TreatmentActivity> {

	private ModelManager mm;

	public TreatmentActivityTest() {
		super(TreatmentActivity.class);
	}

	@Override
	protected void onBeforeActivityCreation() throws Exception {
		super.onBeforeActivityCreation();

		ArrayList<Treatment> testData = new ArrayList<Treatment>();

		for (int i = 1; i <= 20; i++) {
			DateTime date;
			if (i % 4 == 0) date = DateTime.now().minusDays(i);
			else if (i % 4 == 1) date = DateTime.now().minusWeeks(i);
			else if (i % 4 == 2) date = DateTime.now().minusMonths(i);
			else date = DateTime.now().minusYears(i);
			
			testData.add(
					new Treatment(
							"INF" + i,
							"MEDICINE_NAME" + i,
							date,
							i));
		}

		mm = new ModelManager(new Storage(getInstrumentation().getTargetContext()));
		mm.saveTreatments(testData);
	}
	
	public void testInitials() throws Exception {
		assertTrue("Header text", solo.waitForText("Behandlingar"));

		assertTrue("Date header", solo.waitForText("Start"));
		assertTrue("numDays header", solo.waitForText("Dgr"));

		DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);

		Collection<Treatment> testData = mm.getAllTreatments();
		
		// Just look for 2, that should be enough (performance...)
		int i = 0;
		for (Treatment treatment : testData) {
			if (i++ > 2) break;
			
			assertTrue("Should show treatment date " + treatment.getStartingDate(), solo.waitForText(df.format(treatment.getStartingDate().toDate())));
			assertTrue("Should show treatment numDays " + treatment.getNumDays(), solo.waitForText("" + treatment.getNumDays()));
		}
	}
}
