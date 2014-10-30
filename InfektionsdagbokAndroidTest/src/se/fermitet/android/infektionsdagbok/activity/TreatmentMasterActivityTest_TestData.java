package se.fermitet.android.infektionsdagbok.activity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;

import org.joda.time.DateTime;

import se.fermitet.android.infektionsdagbok.R;
import se.fermitet.android.infektionsdagbok.model.ModelManager;
import se.fermitet.android.infektionsdagbok.model.Treatment;
import se.fermitet.android.infektionsdagbok.storage.Storage;
import se.fermitet.android.infektionsdagbok.views.TreatmentAdapter;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.TextView;

public class TreatmentMasterActivityTest_TestData extends TreatmentMasterActivityTest {

	private ModelManager mm;
	private Treatment nullMedicine;
	private Treatment nullInfection;
	private Treatment nullStartingDate;
	private Treatment nullNumDays;

	public TreatmentMasterActivityTest_TestData() {
		super();
	}

	@Override
	protected void onBeforeActivityCreation() throws Exception {
		super.onBeforeActivityCreation();

		mm = new ModelManager(new Storage(getInstrumentation().getTargetContext()));

		saveTestData();
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
		searchForTreatmentInListAndCheckDisplayedValues(nullNumDays);

		ImageButton editBTN = (ImageButton) solo.getView(R.id.editBTN);
		assertNotNull("Edit button", editBTN);
		assertFalse("Edit button enabled", editBTN.isEnabled());

		ImageButton deleteBTN = (ImageButton) solo.getView(R.id.deleteBTN);
		assertNotNull("Delete button", deleteBTN);
		assertFalse("Delete button enabled", deleteBTN.isEnabled());

		ImageButton newBTN = (ImageButton) solo.getView(R.id.newBTN);
		assertNotNull("New button", newBTN);
		assertTrue("New button enabled", newBTN.isEnabled());

/*
		assertNotNull("New button", solo.getView(R.id.newBTN));
		assertNotNull("Delete button", solo.getView(R.id.deleteBTN));*/
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

				Integer numDays = treatment.getNumDays();
				CharSequence shownText = numDaysTV.getText();
				if (numDays == null) {
					assertTrue("Should show numDays as null", shownText == null || shownText.length() == 0);
				} else {
					assertTrue("Should show treatment numDays " + treatment.getNumDays(), numDaysTV.getText().equals("" + treatment.getNumDays()));
				}

				break;
			}
		}
		assertTrue("Didn't find treatment in list: " + treatment.toString(), foundTreatment);
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

	public void testClickingOnTreatmentHighlightsItAndAffectsButtonsEnabledState() throws Exception {
		ImageButton editBTN = (ImageButton) solo.getView(R.id.editBTN);
		ImageButton deleteBTN = (ImageButton) solo.getView(R.id.deleteBTN);
		ImageButton newBTN = (ImageButton) solo.getView(R.id.newBTN);

		timeoutCheckAdapterSelected(null);
		timeoutCheckButtonEnabled("edit button enabled", editBTN, false);
		timeoutCheckButtonEnabled("deleteButton enabled", deleteBTN, false);
		timeoutCheckButtonEnabled("new button enabled", newBTN, true);

		solo.clickInList(3);
		timeoutCheckAdapterSelected(3);
		timeoutCheckButtonEnabled("edit button enabled", editBTN, true);
		timeoutCheckButtonEnabled("deleteButton enabled", deleteBTN, true);
		timeoutCheckButtonEnabled("new button enabled", newBTN, true);

		solo.clickInList(2);
		timeoutCheckAdapterSelected(2);
		timeoutCheckButtonEnabled("edit button enabled", editBTN, true);
		timeoutCheckButtonEnabled("deleteButton enabled", deleteBTN, true);
		timeoutCheckButtonEnabled("new button enabled", newBTN, true);

		solo.clickInList(2);
		timeoutCheckAdapterSelected(null);
		timeoutCheckButtonEnabled("edit button enabled", editBTN, false);
		timeoutCheckButtonEnabled("deleteButton enabled", deleteBTN, false);
		timeoutCheckButtonEnabled("new button enabled", newBTN, true);
	}

	private void timeoutCheckAdapterSelected(Integer positionOrNull) throws Exception {
		TreatmentAdapter adapter = getListAdapter();

		Integer selectedPosition = null;
		boolean condition;
		setStart();
		do {
			selectedPosition = adapter.getSelectedPosition();

			if (positionOrNull == null) {
				condition = selectedPosition == null;
			} else {
				if (selectedPosition != null) condition = ((positionOrNull - 1) == (selectedPosition));
				else condition = false;
			}
			setElapsed();
		} while (!condition && notYetTimeout());

		if (positionOrNull == null) {
			assertNull("Should have no selection", selectedPosition);
		} else {
			assertEquals("Selection position",  positionOrNull - 1, (int) selectedPosition);
		}
	}

	private void timeoutCheckButtonEnabled(String message, ImageButton button, boolean shouldBeEnabled) throws Exception {
		setStart();
		do {
			setElapsed();
		} while (button.isEnabled() != shouldBeEnabled && notYetTimeout());
		assertTrue(message, button.isEnabled() == shouldBeEnabled);
	}

	public void testClickingOnNewOpensEmptyTreatmentDetailActivityPlusBackNavigation() throws Exception {
		solo.clickOnView(solo.getView(R.id.newBTN));

		// Check that new activity is started
		boolean condition;
		setStart();
		do {
			condition = TreatmentDetailActivity.class.equals(solo.getCurrentActivity().getClass());
			setElapsed();
		} while (!condition && notYetTimeout());
		assertEquals("Should start new activity", TreatmentDetailActivity.class, solo.getCurrentActivity().getClass());

		// Check empty
		TextView startTV = (TextView) solo.getView(R.id.startTV);
		CharSequence startText = startTV.getText();
		assertTrue("Start date empty", startText == null || startText.length() == 0);

		EditText numDaysEdit = (EditText) solo.getView(R.id.numDaysEdit);
		Editable numDaysText = numDaysEdit.getText();
		assertTrue("NumDays empty", numDaysText == null || numDaysText.length() == 0);

		EditText infectionTypeEdit = (EditText) solo.getView(R.id.infectionTypeEdit);
		Editable infectionTypeText = infectionTypeEdit.getText();
		assertTrue("infection type empty", infectionTypeText == null || infectionTypeText.length() == 0);

		EditText medicineEdit = (EditText) solo.getView(R.id.medicineEdit);
		Editable medicineText = medicineEdit.getText();
		assertTrue("medicine empty", medicineText == null || medicineText.length() == 0);

		// Check back navigation
		solo.clickOnActionBarHomeButton();

		Class<?> activityClass = null;
		setStart();
		do {
			activityClass = solo.getCurrentActivity().getClass();

			setElapsed();
		} while (!TreatmentMasterActivity.class.equals(activityClass) && notYetTimeout());

		assertEquals("Should be back to TreatmentMaster after back click", TreatmentMasterActivity.class,  solo.getCurrentActivity().getClass());
	}

	public void testDelete_fromMaster() throws Exception {
		solo.clickInList(1);
		timeoutCheckAdapterSelected(1);

		TreatmentAdapter adapter = getListAdapter();

		Treatment toDelete = adapter.getItem(0);

		solo.clickOnView(solo.getView(R.id.deleteBTN));

		// Check file
		boolean contains;
		setStart();
		do {
			Map<UUID, Treatment> treatmentsFromFile = getActivity().getLocalApplication().getModelManager().getAllTreatments();

			contains = treatmentsFromFile.containsKey(toDelete.getUUID());

			setElapsed();
		} while(contains && notYetTimeout());
		assertFalse("Saved data contains deleted treatment", contains);

		// Check adapter
		setStart();
		do {
			setElapsed();
		} while(adapterContains(toDelete) && notYetTimeout());
		assertFalse("Adapter contains deleted treatment", adapterContains(toDelete));
	}

	public void testDelete_fromDetail() throws Exception {
		// TODO
		fail("TODO");
	}



/*	public void testClickingOnListViewFillsEditors() throws Exception {
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


		String numDaysTextFromUI = ((TextView) solo.getView(R.id.numDaysEdit)).getText().toString();
		if (treatment.getNumDays() == null) {
			assertTrue("NumDays text for treatment " + treatment + "  was: " + numDaysTextFromUI, (numDaysTextFromUI == null) || (numDaysTextFromUI.length() == 0));
		} else {
			assertEquals("NumDays text for treatment " + treatment, treatment.getNumDays().toString(), numDaysTextFromUI);
		}


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

	public void testChangingOtherFieldsThanStartingDateChangesModel() throws Exception {
		Treatment previous = getActivity().view.getSingleEditView().getModel();

		solo.clickInList(1);

		String newMedicine = "NEW MEDICINE";
		String newInfectionType = "NEW INFECTION TYPE";
		Integer newNumDays = 1000;

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


	private void assertBothNullOrBothEqual(String message, Object expected, Object actual) {
		if (expected == null) assertNull(message, actual);
		else assertEquals(message, expected, actual);
	}

	private void assertBothNullOrBothEqual(String message, String expected, String actual) {
		if (expected == null || expected.length() == 0) assertTrue(message, actual == null || actual.length() == 0);
		else assertEquals(message, expected, actual);
	}*/

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
		nullNumDays = new Treatment(DateTime.now().minusDays(103), null, "INFECT103", "MEDICINE103");

		testData.add(nullMedicine);
		testData.add(nullInfection);
		testData.add(nullStartingDate);
		testData.add(nullNumDays);

		mm.saveTreatments(testData);
	}

}
