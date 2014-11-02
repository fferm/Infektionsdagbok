package se.fermitet.android.infektionsdagbok.activity;

import java.util.ArrayList;
import java.util.Collection;

import org.joda.time.LocalDate;

import se.fermitet.android.infektionsdagbok.R;
import se.fermitet.android.infektionsdagbok.model.Treatment;
import se.fermitet.android.infektionsdagbok.views.TreatmentAdapter;
import android.app.DatePickerDialog;
import android.text.Editable;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.TextView;

public class TreatmentMasterActivityTest extends MasterActivityTest<
Treatment, TreatmentMasterActivity, TreatmentAdapter> {

	private Treatment nullMedicine;
	private Treatment nullInfection;
	private Treatment nullStartingDate;
	private Treatment nullNumDays;

	public TreatmentMasterActivityTest() {
		super(TreatmentMasterActivity.class, TreatmentDetailActivity.class, Treatment.class);
	}

	@Override
	protected void checkSubInitials() throws Exception {
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
	}

	@Override
	protected String getHeaderText() throws Exception {
		return "Behandlingar";
	}

	private void searchForTreatmentInListAndCheckDisplayedValues(Treatment treatment) {
		ListAdapter adapter = getListAdapter();

		int index = indexOfItemInAdapter(treatment);
		assertFalse("Didn't find treatment: " + treatment, index == -1);

		View treatmentView = adapter.getView(index, null, null);
		TextView startTv = (TextView) treatmentView.findViewById(R.id.dateValueField);
		TextView numDaysTV = (TextView) treatmentView.findViewById(R.id.numDaysValueField);

		LocalDate startingDate = treatment.getStartingDate();
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

	}


	public void testTreatmentsOrderedByStartDateDescending() throws Exception {
		ListAdapter adapter = getListAdapter();

		LocalDate previousStartingDate = null;
		for (int i = 0; i < adapter.getCount(); i++) {
			Treatment treatment = (Treatment) adapter.getItem(i);

			LocalDate currentStartingDate = treatment.getStartingDate();

			boolean condition = (previousStartingDate == null) || (currentStartingDate == null) || previousStartingDate.isAfter(currentStartingDate);

			assertTrue("Wrong order on treatments starting with the one with startingDate =  " + treatment.getStartingDate(), condition);

			previousStartingDate = currentStartingDate;
		}
	}




	public void testClickingOnListViewFillsEditors() throws Exception {
		ListAdapter adapter = getListAdapter();

		// Regular
		clickOnItemInListClickEditAndCheckEditorContentsThenGoBack((Treatment) adapter.getItem(0));

		// Nulls
		clickOnItemInListClickEditAndCheckEditorContentsThenGoBack(nullStartingDate);
		clickOnItemInListClickEditAndCheckEditorContentsThenGoBack(nullMedicine);
		clickOnItemInListClickEditAndCheckEditorContentsThenGoBack(nullInfection);
		clickOnItemInListClickEditAndCheckEditorContentsThenGoBack(nullNumDays);
	}

	private void clickOnItemInListClickEditAndCheckEditorContentsThenGoBack(Treatment treatment) throws Exception {
		assertTrue("Adapter must contain treatment: " + treatment, adapterContains(treatment));
		int i = indexOfItemInAdapter(treatment);

		solo.clickInList(i + 1);
		Thread.sleep(100);
		solo.clickOnView(solo.getView(R.id.editBTN));
		timeoutGetCurrentActivity(TreatmentDetailActivity.class);

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

		// Cancel to go back
		solo.clickOnView(solo.getView(R.id.cancelBTN));
		timeoutGetCurrentActivity(TreatmentMasterActivity.class);
	}

	@Override
	protected void saveTestData() throws Exception {
		ArrayList<Treatment> testData = new ArrayList<Treatment>();

		for (int i = 1; i <= 5; i++) {
			LocalDate date;
			if (i % 4 == 0) date = LocalDate.now().minusDays(i);
			else if (i % 4 == 1) date = LocalDate.now().minusWeeks(i);
			else if (i % 4 == 2) date = LocalDate.now().minusMonths(i);
			else date = LocalDate.now().minusYears(i);

			testData.add(
					new Treatment(
							date,
							i,
							"INF" + i,
							"MEDICINE_NAME" + i));
		}

		nullMedicine = new Treatment(LocalDate.now().minusDays(100), 100, "INFECTION", null);
		nullInfection = new Treatment(LocalDate.now().minusDays(101), 101, null, "MEDICINE");
		nullStartingDate = new Treatment(null, 102, "INFECT102", "MEDICINE102");
		nullNumDays = new Treatment(LocalDate.now().minusDays(103), null, "INFECT103", "MEDICINE103");

		testData.add(nullMedicine);
		testData.add(nullInfection);
		testData.add(nullStartingDate);
		testData.add(nullNumDays);

		mm.saveAll(testData);
	}

	@Override
	protected void checkDetailEditorsEmpty() {
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
	}

	@Override
	protected Treatment getTestItem() {
		return new Treatment(LocalDate.now().minusMonths(2), 1000, "Fšrkylning", "Doxyferm");
	}

	@Override
	protected void editUIBasedOnItem(Treatment itemWithNewValues) throws Exception {
		TreatmentDetailActivity detailActivity = (TreatmentDetailActivity) solo.getCurrentActivity();

		// Start editing
		TextView startTV = (TextView) solo.getView(R.id.startTV);
		solo.clickOnView(startTV);
		solo.waitForDialogToOpen();
		DatePickerDialog dialog = detailActivity.view.getDatePickerDialog();
		DatePicker picker = dialog.getDatePicker();

		LocalDate start = itemWithNewValues.getStartingDate();
		solo.setDatePicker(picker, start.getYear(), start.getMonthOfYear() - 1, start.getDayOfMonth());
		solo.clickOnButton("StŠll in");

		EditText numDaysEdit = (EditText) solo.getView(R.id.numDaysEdit);
		solo.clearEditText(numDaysEdit);
		solo.enterText(numDaysEdit, itemWithNewValues.getNumDays().toString());

		EditText medicineEdit = (EditText) solo.getView(R.id.medicineEdit);
		solo.clearEditText(medicineEdit);
		solo.enterText(medicineEdit, itemWithNewValues.getMedicine());

		EditText infectionTypeEdit = (EditText) solo.getView(R.id.infectionTypeEdit);
		solo.clearEditText(infectionTypeEdit);
		solo.enterText(infectionTypeEdit, itemWithNewValues.getInfectionType());
	}
}
