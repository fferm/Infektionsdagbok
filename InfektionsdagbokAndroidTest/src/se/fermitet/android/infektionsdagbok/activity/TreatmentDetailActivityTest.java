package se.fermitet.android.infektionsdagbok.activity;

import java.text.DateFormat;

import org.joda.time.DateTime;

import se.fermitet.android.infektionsdagbok.R;
import se.fermitet.android.infektionsdagbok.model.Treatment;
import android.app.DatePickerDialog;
import android.widget.DatePicker;
import android.widget.TextView;

public class TreatmentDetailActivityTest extends ActivityTestWithSolo<TreatmentDetailActivity> {

	public TreatmentDetailActivityTest() {
		super(TreatmentDetailActivity.class);
	}

	public void testInitials() throws Exception {
		assertTrue("Header text", solo.waitForText("Behandling"));

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

	public void testClickDateFieldOpensDatePickerAndChangingPickerChangesField() throws Exception {
		Treatment treatment = getActivity().view.getModel();
		assertNull("start date should be null when starting this test to make the test work", treatment.getStartingDate());

		DateTime firstExpected = DateTime.now().withMillisOfDay(0);
		DateTime firstSet = new DateTime(2012, 1, 1, 1, 1);
		DateTime secondSet = new DateTime(2013, 10, 2, 1, 1);

		testDatePicker("Null start", firstExpected, firstSet);
		testDatePicker("Value start", firstSet, secondSet);
	}

	private void testDatePicker(String messagePrefix, DateTime expectedWhenOpeningPicker, DateTime setTo) throws Exception {
		TextView startTV = (TextView) solo.getView(R.id.startTV);
		solo.clickOnView(startTV);

		assertTrue(messagePrefix + ": " + "Open date dialog", solo.waitForDialogToOpen());

		DatePickerDialog dialog = getActivity().view.getDatePickerDialog();
		assertNotNull(messagePrefix + ": " + "not null dialog", dialog);

		DatePicker picker = dialog.getDatePicker();

		assertEquals(messagePrefix + ": " + "year", expectedWhenOpeningPicker.getYear(), picker.getYear());
		assertEquals(messagePrefix + ": " + "month", expectedWhenOpeningPicker.getMonthOfYear(), picker.getMonth() + 1);  // picker is 0 based in month
		assertEquals(messagePrefix + ": " + "day", expectedWhenOpeningPicker.getDayOfMonth(), picker.getDayOfMonth());

		solo.setDatePicker(picker, setTo.getYear(), setTo.getMonthOfYear() - 1, setTo.getDayOfMonth());
		solo.clickOnButton("St�ll in");

		String startTVText = null;
		String expected = DateFormat.getDateInstance(DateFormat.SHORT).format(setTo.toDate());
		setStart();
		do {
			startTVText = startTV.getText().toString();

			setElapsed();
		} while (!expected.equals(startTVText) && notYetTimeout());

		assertEquals(messagePrefix + ": " + "Start date field text", expected, startTV.getText());

		DateTime newDateFromView = getActivity().view.getModel().getStartingDate();
		assertEquals(messagePrefix + ": " + "view model date value (year)", setTo.year(), newDateFromView.year());
		assertEquals(messagePrefix + ": " + "view model date value (month)", setTo.monthOfYear(), newDateFromView.monthOfYear());
		assertEquals(messagePrefix + ": " + "view model date value (day)", setTo.dayOfMonth(), newDateFromView.dayOfMonth());

	}



}
