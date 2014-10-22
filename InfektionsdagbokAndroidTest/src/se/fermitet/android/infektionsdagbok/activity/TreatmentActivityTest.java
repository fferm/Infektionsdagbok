package se.fermitet.android.infektionsdagbok.activity;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collection;

import org.joda.time.DateTime;

import se.fermitet.android.infektionsdagbok.R;
import se.fermitet.android.infektionsdagbok.model.ModelManager;
import se.fermitet.android.infektionsdagbok.model.Treatment;
import se.fermitet.android.infektionsdagbok.storage.Storage;
import android.view.View;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class TreatmentActivityTest extends ActivityTestWithSolo<TreatmentActivity> {

	private ModelManager mm;
	private Treatment nullMedicine;
	private Treatment nullInfection;
	private Treatment nullStartingDate;

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

		nullMedicine = new Treatment("INFECTION", null, DateTime.now().minusDays(100), 100);
		nullInfection = new Treatment(null, "MEDICINE", DateTime.now().minusDays(101), 101);
		nullStartingDate = new Treatment("INFECT102", "MEDICINE102", null, 102);

		testData.add(nullMedicine);
		testData.add(nullInfection);
		testData.add(nullStartingDate);

		mm = new ModelManager(new Storage(getInstrumentation().getTargetContext()));
		mm.saveTreatments(testData);
	}

	public void testInitials() throws Exception {
		assertTrue("Header text", solo.waitForText("Behandlingar"));

		assertTrue("Date list header", solo.waitForText("Start"));
		assertTrue("numDays list header", solo.waitForText("Dgr"));

		Collection<Treatment> testData = mm.getAllTreatments();

		for (Treatment treatment : testData) {
			searchForTreatmentInListAndCheckDisplayedValues(treatment);
		}
		searchForTreatmentInListAndCheckDisplayedValues(nullStartingDate);
		searchForTreatmentInListAndCheckDisplayedValues(nullInfection);
		searchForTreatmentInListAndCheckDisplayedValues(nullMedicine);

		checkHeaderTextView(R.id.startHeader, "Start:");
		assertNotNull("Start date text field", solo.getView(R.id.startTV));

		checkHeaderTextView(R.id.numDaysHeader, "Dagar:");
		assertNotNull("Num days edit text", solo.getView(R.id.numDaysEdit));

		checkHeaderTextView(R.id.medicineHeader, "Preparat:");
		assertNotNull("Medicine text field", solo.getView(R.id.medicineEdit));

		checkHeaderTextView(R.id.infectionTypeHeader, "Sjukdom:");
		assertNotNull("Infection type field", solo.getView(R.id.infectionTypeEdit));
	}

	protected void searchForTreatmentInListAndCheckDisplayedValues(Treatment treatment) {
		DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);

		TreatmentActivity activity = getActivity();
		ListView listView = (ListView) activity.view.findViewById(R.id.treatmentListView);
		ListAdapter adapter = listView.getAdapter();

		boolean foundTreatment = false;
		for (int i = 0; i < adapter.getCount(); i++) {
			Treatment fromView = (Treatment) adapter.getItem(i);
			if (treatment.equals(fromView)) {
				foundTreatment = true;

				View treatmentView = adapter.getView(i, null, null);
				TextView startTv = (TextView) treatmentView.findViewById(R.id.dateValueField);
				TextView numDaysTV = (TextView) treatmentView.findViewById(R.id.numDaysValueField);

				DateTime startingDate = treatment.getStartingDate();
				if (startingDate == null) {
					assertTrue("Should show null date", (startTv.getText() == null) || (startTv.getText().length() == 0));
				} else {
					assertTrue("Should show treatment date " + startingDate, startTv.getText().equals(df.format(startingDate.toDate())));
				}
				assertTrue("Should show treatment numDays " + treatment.getNumDays(), numDaysTV.getText().equals("" + treatment.getNumDays()));

				break;
			}
		}
		assertTrue("Didn't find treatment in list: " + treatment.toString(), foundTreatment);
	}

	private void checkHeaderTextView(int id, String text) {
		TextView headerView = (TextView) solo.getView(id);
		assertNotNull(text + " header null", headerView);
		assertEquals(text + " header text", text, headerView.getText());
	}

	public void testTreatmentsOrderedByStartDateDescending() throws Exception {
		TreatmentActivity activity = getActivity();
		ListView listView = (ListView) activity.view.findViewById(R.id.treatmentListView);
		ListAdapter adapter = listView.getAdapter();

		DateTime previousStartingDate = null;
		for (int i = 0; i < adapter.getCount(); i++) {
			Treatment treatment = (Treatment) adapter.getItem(i);

			DateTime currentStartingDate = treatment.getStartingDate();

			boolean condition = (previousStartingDate == null) || previousStartingDate.isAfter(currentStartingDate);

			assertTrue("Wrong order on treatments starting with the one with startingDate =  " + treatment.getStartingDate(), condition);

			previousStartingDate = currentStartingDate;
		}
	}

	public void testClickingOnListViewFillsEditors() throws Exception {
		TreatmentActivity activity = getActivity();
		ListView listView = (ListView) activity.view.findViewById(R.id.treatmentListView);
		ListAdapter adapter = listView.getAdapter();

		int treatmentIdx = 0;
		solo.clickInList(treatmentIdx);

		Treatment t1 = (Treatment) adapter.getItem(treatmentIdx);

		DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);

		assertEquals("Date text", df.format(t1.getStartingDate().toDate()), ((TextView) solo.getView(R.id.startTV)).getText());
		assertEquals("Num Days text", "" + t1.getNumDays(), ((EditText) solo.getView(R.id.numDaysEdit)).getText().toString());
		assertEquals("Medicine text", t1.getMedicine(), ((TextView) solo.getView(R.id.medicineEdit)).getText().toString());
		assertEquals("Infection type text", t1.getInfectionType(), ((TextView) solo.getView(R.id.infectionTypeEdit)).getText().toString());
	}

	public void testClickingOnListViewFillsEditors_handleNulls() throws Exception {
		// TODO

	}
}
