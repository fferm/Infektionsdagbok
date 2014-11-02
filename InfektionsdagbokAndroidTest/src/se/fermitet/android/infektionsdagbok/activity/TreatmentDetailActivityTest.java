package se.fermitet.android.infektionsdagbok.activity;

import java.text.DateFormat;

import org.joda.time.LocalDate;

import se.fermitet.android.infektionsdagbok.R;
import se.fermitet.android.infektionsdagbok.model.Treatment;
import android.app.DatePickerDialog;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

public class TreatmentDetailActivityTest extends DetailActivityTest<Treatment, TreatmentDetailActivity> {

	public TreatmentDetailActivityTest() {
		super(TreatmentDetailActivity.class);
	}

	@Override
	protected String getHeaderText() {
		return "Behandling";
	}
	
	@Override
	protected void checkSubInitials() {
		checkHeaderTextView(R.id.startHeader, "Start:");
		assertNotNull("Start date text field", solo.getView(R.id.startTV));

		checkHeaderTextView(R.id.numDaysHeader, "Dagar:");
		assertNotNull("Num days edit text", solo.getView(R.id.numDaysEdit));

		checkHeaderTextView(R.id.medicineHeader, "Preparat:");
		assertNotNull("Medicine text field", solo.getView(R.id.medicineEdit));

		checkHeaderTextView(R.id.infectionTypeHeader, "Sjukdom:");
		assertNotNull("Infection type field", solo.getView(R.id.infectionTypeEdit));
	}

	private void checkHeaderTextView(int id, String text) {
		TextView headerView = (TextView) solo.getView(id);
		assertNotNull(text + " header null", headerView);
		assertEquals(text + " header text", text, headerView.getText());
	}

	public void testClickDateFieldOpensDatePickerAndChangingPickerChangesFieldAndModel() throws Exception {
		Treatment treatment = getActivity().view.getModel();
		assertNull("start date should be null when starting this test to make the test work", treatment.getStartingDate());

		LocalDate firstExpected = LocalDate.now();
		LocalDate firstSet = new LocalDate(2012, 1, 1);
		LocalDate secondSet = new LocalDate(2013, 10, 2);

		testDatePicker("Null start", firstExpected, firstSet, R.id.startTV);
		testDatePicker("Value start", firstSet, secondSet, R.id.startTV);
	}

	// TODO: Should be moved to testing of the widget itself
	private void testDatePicker(String messagePrefix, LocalDate expectedWhenOpeningPicker, LocalDate setTo, int textViewFieldID) throws Exception {
		TextView textView = (TextView) solo.getView(textViewFieldID);
		solo.clickOnView(textView);

		assertTrue(messagePrefix + ": " + "Open date dialog", solo.waitForDialogToOpen());

		DatePickerDialog dialog = getActivity().view.getDatePickerDialog();
		assertNotNull(messagePrefix + ": " + "not null dialog", dialog);

		DatePicker picker = dialog.getDatePicker();

		assertEquals(messagePrefix + ": " + "year", expectedWhenOpeningPicker.getYear(), picker.getYear());
		assertEquals(messagePrefix + ": " + "month", expectedWhenOpeningPicker.getMonthOfYear(), picker.getMonth() + 1);  // picker is 0 based in month
		assertEquals(messagePrefix + ": " + "day", expectedWhenOpeningPicker.getDayOfMonth(), picker.getDayOfMonth());

		solo.setDatePicker(picker, setTo.getYear(), setTo.getMonthOfYear() - 1, setTo.getDayOfMonth());
		solo.clickOnButton("Ställ in");

		String text = null;
		String expected = DateFormat.getDateInstance(DateFormat.SHORT).format(setTo.toDate());
		setStart();
		do {
			text = textView.getText().toString();

			setElapsed();
		} while (!expected.equals(text) && notYetTimeout());

		assertEquals(messagePrefix + ": " + "field text", expected, textView.getText());

		LocalDate newDateFromView = getActivity().view.getModel().getStartingDate();
		assertEquals(messagePrefix + ": " + "view model date value (year)", setTo.year(), newDateFromView.year());
		assertEquals(messagePrefix + ": " + "view model date value (month)", setTo.monthOfYear(), newDateFromView.monthOfYear());
		assertEquals(messagePrefix + ": " + "view model date value (day)", setTo.dayOfMonth(), newDateFromView.dayOfMonth());
	}

	public void testChangingOtherFieldsThanStartingDateChangesModel() throws Exception {
		Treatment model = getActivity().view.getModel();

		String newMedicine = "NEW MEDICINE";
		String newInfectionType = "NEW INFECTION TYPE";
		Integer newNumDays = 1000;

		EditText medicineEdit = (EditText) solo.getView(R.id.medicineEdit);
		solo.clearEditText(medicineEdit);
		solo.enterText(medicineEdit, newMedicine);
		assertEquals("Medicine", newMedicine, model.getMedicine());

		EditText infectionTypeEdit = (EditText) solo.getView(R.id.infectionTypeEdit);
		solo.clearEditText(infectionTypeEdit);
		solo.enterText(infectionTypeEdit, newInfectionType);
		assertEquals("Infection type", newInfectionType, model.getInfectionType());

		EditText numDaysEdit = (EditText) solo.getView(R.id.numDaysEdit);
		solo.clearEditText(numDaysEdit);
		solo.enterText(numDaysEdit, newNumDays.toString());
		assertEquals("Num days", newNumDays, model.getNumDays());
	}
}
