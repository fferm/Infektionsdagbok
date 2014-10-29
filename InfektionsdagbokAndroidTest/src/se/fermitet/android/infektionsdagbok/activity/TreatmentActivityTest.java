package se.fermitet.android.infektionsdagbok.activity;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;

import org.joda.time.DateTime;

import se.fermitet.android.infektionsdagbok.R;
import se.fermitet.android.infektionsdagbok.model.ModelManager;
import se.fermitet.android.infektionsdagbok.model.Treatment;
import se.fermitet.android.infektionsdagbok.storage.Storage;
import android.app.DatePickerDialog;
import android.view.View;
import android.widget.DatePicker;
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

		mm = new ModelManager(new Storage(getInstrumentation().getTargetContext()));

		saveTestData();
	}

	private void saveTestData() throws Exception {
		ArrayList<Treatment> testData = new ArrayList<Treatment>();

		for (int i = 1; i <= 5; i++) {
			DateTime date;
			if (i % 4 == 0) date = DateTime.now().minusDays(i);
			else if (i % 4 == 1) date = DateTime.now().minusWeeks(i);
			else if (i % 4 == 2) date = DateTime.now().minusMonths(i);
			else date = DateTime.now().minusYears(i);

			testData.add(
					new Treatment(
							date,
							i,
							"INF" + i,
							"MEDICINE_NAME" + i));
		}

		nullMedicine = new Treatment(DateTime.now().minusDays(100), 100, "INFECTION", null);
		nullInfection = new Treatment(DateTime.now().minusDays(101), 101, null, "MEDICINE");
		nullStartingDate = new Treatment(null, 102, "INFECT102", "MEDICINE102");

		testData.add(nullMedicine);
		testData.add(nullInfection);
		testData.add(nullStartingDate);

		mm.saveTreatments(testData);
	}

	public void testInitials() throws Exception {
		assertTrue("Header text", solo.waitForText("Behandlingar"));

		assertTrue("Date list header", solo.waitForText("Start"));
		assertTrue("numDays list header", solo.waitForText("Dgr"));

		Collection<Treatment> testData = mm.getAllTreatments().values();

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

		assertNotNull("Save button", solo.getView(R.id.saveBTN));
		assertNotNull("New button", solo.getView(R.id.newBTN));
		assertNotNull("Delete button", solo.getView(R.id.deleteBTN));
	}

	private void searchForTreatmentInListAndCheckDisplayedValues(Treatment treatment) {
		ListAdapter adapter = getListAdapter();

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
					assertTrue("Should show treatment date " + startingDate, startTv.getText().equals(treatment.getStartingDateString()));
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
		ListAdapter adapter = getListAdapter();

		DateTime previousStartingDate = null;
		for (int i = 0; i < adapter.getCount(); i++) {
			Treatment treatment = (Treatment) adapter.getItem(i);

			DateTime currentStartingDate = treatment.getStartingDate();

			boolean condition = (previousStartingDate == null) || (currentStartingDate == null) || previousStartingDate.isAfter(currentStartingDate);

			assertTrue("Wrong order on treatments starting with the one with startingDate =  " + treatment.getStartingDate(), condition);

			previousStartingDate = currentStartingDate;
		}
	}

	public void testClickingOnListViewFillsEditors() throws Exception {
		ListAdapter adapter = getListAdapter();

		// Regular
		clickOnItemInListAndCheckEditorContents((Treatment) adapter.getItem(0));

		// Nulls
		clickOnItemInListAndCheckEditorContents(nullStartingDate);
		clickOnItemInListAndCheckEditorContents(nullMedicine);
		clickOnItemInListAndCheckEditorContents(nullInfection);
	}

	private void clickOnItemInListAndCheckEditorContents(Treatment treatment) throws Exception {
		ListAdapter adapter = getListAdapter();

		boolean found = false;
		int i = 0;
		for ( ; i < adapter.getCount(); i++) {
			Treatment fromList = (Treatment) adapter.getItem(i);
			if (fromList.equals(treatment)) {
				found = true;
				break;
			}
		}
		assertTrue("Didn't find treatment " + treatment, found);

		solo.waitForText("" + treatment.getNumDays(), 1, 500, true);
		solo.clickOnText("" + treatment.getNumDays());
//		solo.clickInList(i + 1);
		Thread.sleep(100);

		CharSequence dateTextFromUI = ((TextView) solo.getView(R.id.startTV)).getText();
		if (treatment.getStartingDate() == null) {
			assertTrue("Date text for treatment " + treatment + "  was: " + dateTextFromUI, (dateTextFromUI == null) || (dateTextFromUI.length() == 0));
		} else {
			assertEquals("Date text for treatment " + treatment, treatment.getStartingDateString(), dateTextFromUI);
		}


		assertEquals("Num Days text for treatment " + treatment, "" + treatment.getNumDays(), ((EditText) solo.getView(R.id.numDaysEdit)).getText().toString());


		String medicineTextFromUI = ((TextView) solo.getView(R.id.medicineEdit)).getText().toString();
		if (treatment.getMedicine() == null) {
			assertTrue("Medicine text for treatment " + treatment + "  was: " + medicineTextFromUI, (medicineTextFromUI == null) || (medicineTextFromUI.length() == 0));
		} else {
			assertEquals("Medicine text for treatment " + treatment, treatment.getMedicine(), medicineTextFromUI);
		}


		String infectionTypeTextFromUI = ((TextView) solo.getView(R.id.infectionTypeEdit)).getText().toString();
		if (treatment.getInfectionType() == null) {
			assertTrue("Infection type text for treatment " + treatment + "  was: " + infectionTypeTextFromUI, (infectionTypeTextFromUI == null) || (infectionTypeTextFromUI.length() == 0));
		} else {
			assertEquals("Infection type text for treatment " + treatment, treatment.getInfectionType(), infectionTypeTextFromUI);
		}

	}

	public void testClickDateFieldOpensDatePickerAndChangingPickerChangesField() throws Exception {
		Treatment previous = getActivity().view.getSingleEditView().getModel();
		solo.clickInList(1);

		Treatment treatment = timeoutGetSingleEditViewModelChangesFrom(previous);

		TextView startTV = (TextView) solo.getView(R.id.startTV);
		solo.clickOnView(startTV);

		assertTrue("Open date dialog", solo.waitForDialogToOpen());

		DatePickerDialog dialog = getActivity().view.getSingleEditView().getDatePickerDialog();
		assertNotNull("not null dialog", dialog);

		DatePicker picker = dialog.getDatePicker();

		assertEquals("year", treatment.getStartingDate().getYear(), picker.getYear());
		assertEquals("month", treatment.getStartingDate().getMonthOfYear(), picker.getMonth() + 1);  // picker is 0 based in month
		assertEquals("day", treatment.getStartingDate().getDayOfMonth(), picker.getDayOfMonth());

		DateTime newDate = new DateTime(2012, 1, 1, 1, 1);

		solo.setDatePicker(picker, newDate.getYear(), newDate.getMonthOfYear() - 1, newDate.getDayOfMonth());
		solo.clickOnButton("StŠll in");

		String startTVText = null;
		String expected = DateFormat.getDateInstance(DateFormat.SHORT).format(newDate.toDate());
		setStart();
		do {
			startTVText = startTV.getText().toString();

			setElapsed();
		} while (!expected.equals(startTVText) && notYetTimeout());

		assertEquals("Start date field text", expected, startTV.getText());

		DateTime newDateFromView = getActivity().view.getSingleEditView().getModel().getStartingDate();
		assertEquals("view model date value (year)", newDate.year(), newDateFromView.year());
		assertEquals("view model date value (month)", newDate.monthOfYear(), newDateFromView.monthOfYear());
		assertEquals("view model date value (day)", newDate.dayOfMonth(), newDateFromView.dayOfMonth());

	}

	public void testChangingOtherFieldsThanStartingDateChangesModel() throws Exception {
		Treatment previous = getActivity().view.getSingleEditView().getModel();

		solo.clickInList(1);

		String newMedicine = "NEW MEDICINE";
		String newInfectionType = "NEW INFECTION TYPE";
		int newNumDays = 1000;

		Treatment treatment = timeoutGetSingleEditViewModelChangesFrom(previous);

		EditText medicineEdit = (EditText) solo.getView(R.id.medicineEdit);
		solo.clearEditText(medicineEdit);
		solo.enterText(medicineEdit, newMedicine);
		assertEquals("Medicine", newMedicine, treatment.getMedicine());

		EditText infectionTypeEdit = (EditText) solo.getView(R.id.infectionTypeEdit);
		solo.clearEditText(infectionTypeEdit);
		solo.enterText(infectionTypeEdit, newInfectionType);
		assertEquals("Infection type", newInfectionType, treatment.getInfectionType());

		EditText numDaysEdit = (EditText) solo.getView(R.id.numDaysEdit);
		solo.clearEditText(numDaysEdit);
		solo.enterText(numDaysEdit, "" + newNumDays);
		assertEquals("Num days", newNumDays, treatment.getNumDays());
	}

	protected Treatment timeoutGetSingleEditViewModelChangesFrom(Treatment previous) throws Exception {
		Treatment treatment = null;

		setStart();
		do {
			treatment = getActivity().view.getSingleEditView().getModel();

			setElapsed();
		} while (treatment.equals(previous) && notYetTimeout());
		assertTrue("Timeout", notYetTimeout());

		return treatment;
	}

	public void testChangingFieldsInSingleEditViewAffectsOtherModelsOnlyWhenSavePressed() throws Exception {
		Treatment previous = getActivity().view.getSingleEditView().getModel();

		String newMedicine = "NEW MEDICINE";
		solo.clickInList(1);

		timeoutGetSingleEditViewModelChangesFrom(previous); // Wait for the model to be set, i e the effect of the click to happen

		Treatment treatmentFromList = (Treatment) getListAdapter().getItem(0);
		UUID uuid = treatmentFromList.getUUID();

		String oldMedicineInList = treatmentFromList.getMedicine();
		EditText medicineEdit = (EditText) solo.getView(R.id.medicineEdit);
		solo.clearEditText(medicineEdit);
		solo.enterText(medicineEdit, newMedicine);

		assertEquals("No change in list model", oldMedicineInList, treatmentFromList.getMedicine());

		solo.clickOnView(solo.getView(R.id.saveBTN));

		String medicineOnFile = null;
		ModelManager modelManager = getActivity().getLocalApplication().getModelManager();
		setStart();
		do {
			Map<UUID, Treatment> allTreatmentsFromFile = modelManager.getAllTreatments();
			Treatment savedTreatment = allTreatmentsFromFile.get(uuid);

			if (savedTreatment != null) {
				medicineOnFile = savedTreatment.getMedicine();
			}

			setElapsed();
		} while ((medicineOnFile == null || !medicineOnFile.equals(newMedicine)) && notYetTimeout());

		assertEquals("Medicine changed on file", newMedicine, medicineOnFile);

		Treatment newTreatmentFromList = null;
		setStart();
		do {
			newTreatmentFromList = (Treatment) getListAdapter().getItem(0);
			setElapsed();
		} while (!newMedicine.equals(newTreatmentFromList.getMedicine()) && notYetTimeout());
		assertEquals("List model after save", newMedicine, newTreatmentFromList.getMedicine());
	}

	public void testClickingNewButtonClearsSingleEditView() throws Exception {
		Treatment previous = getActivity().view.getSingleEditView().getModel();
		solo.clickInList(1);

		Treatment afterClickInList = timeoutGetSingleEditViewModelChangesFrom(previous);

		solo.clickOnView(solo.getView(R.id.newBTN));

		Treatment afterClickOnNew = timeoutGetSingleEditViewModelChangesFrom(afterClickInList);

		Treatment compare = new Treatment();

		assertBothNullOrBothEqual("Starting date", compare.getStartingDate(), afterClickOnNew.getStartingDate());
		assertBothNullOrBothEqual("num days", compare.getNumDays(), afterClickOnNew.getNumDays());
		assertBothNullOrBothEqual("medicine", compare.getMedicine(), afterClickOnNew.getMedicine());
		assertBothNullOrBothEqual("infection type", compare.getInfectionType(), afterClickOnNew.getInfectionType());
	}

	private void assertBothNullOrBothEqual(String message, Object expected, Object actual) {
		if (expected == null) assertNull(message, actual);
		else assertEquals(message, expected, actual);
	}

	private void assertBothNullOrBothEqual(String message, String expected, String actual) {
		if (expected == null || expected.length() == 0) assertTrue(message, actual == null || actual.length() == 0);
		else assertEquals(message, expected, actual);
	}

	private ListAdapter getListAdapter() {
		TreatmentActivity activity = getActivity();
		ListView listView = (ListView) activity.view.findViewById(R.id.treatmentListView);
		ListAdapter adapter = listView.getAdapter();
		return adapter;
	}
}
