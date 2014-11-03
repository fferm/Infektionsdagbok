package se.fermitet.android.infektionsdagbok.widget;
import static org.mockito.Mockito.*;

import java.text.DateFormat;

import org.joda.time.LocalDate;

import se.fermitet.android.infektionsdagbok.R;
import se.fermitet.android.infektionsdagbok.activity.ActivityTestWithSolo;
import se.fermitet.android.infektionsdagbok.widget.DateTextView.OnModelChangedListener;
import android.app.DatePickerDialog;
import android.widget.DatePicker;
import android.widget.TextView;


public class DateTextViewTest extends ActivityTestWithSolo<DateTextViewActivity>{

	public DateTextViewTest() {
		super(DateTextViewActivity.class);
	}

	public void testInitials() throws Exception {
		TextView header = getActivity().getHeader();
		assertNotNull("header", header);

		DateTextView dtv = getActivity().getDateTextView();
		assertNotNull("Date text view", dtv);
	}

	public void testClickDateFieldOpensDatePickerAndChangingPickerChangesFieldAndModel() throws Exception {
		LocalDate firstExpected = LocalDate.now();
		LocalDate firstSet = new LocalDate(2012, 1, 1);
		LocalDate secondSet = new LocalDate(2013, 10, 2);

		testDatePicker("Null start", firstExpected, firstSet, R.id.startTV);
		testDatePicker("Value start", firstSet, secondSet, R.id.startTV);
	}

	private void testDatePicker(String messagePrefix, LocalDate expectedWhenOpeningPicker, LocalDate setTo, int textViewFieldID) throws Exception {
		TextView textView = getActivity().getDateTextView();
		solo.clickOnView(textView);

		assertTrue(messagePrefix + ": " + "Open date dialog", solo.waitForDialogToOpen());

		DatePickerDialog dialog = getActivity().getDateTextView().getDatePickerDialog();
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

		LocalDate newDateFromView = getActivity().getDateTextView().getModel();
		assertEquals(messagePrefix + ": " + "view model date value (year)", setTo.year(), newDateFromView.year());
		assertEquals(messagePrefix + ": " + "view model date value (month)", setTo.monthOfYear(), newDateFromView.monthOfYear());
		assertEquals(messagePrefix + ": " + "view model date value (day)", setTo.dayOfMonth(), newDateFromView.dayOfMonth());
	}

	public void testClickingHeaderAlsoOpensDialog() throws Exception {
		TextView header = getActivity().getHeader();
		solo.clickOnView(header);

		assertTrue("Open dialog", solo.waitForDialogToOpen());
	}

	public void testChangingDateSendsOutNotification() throws Exception {
		OnModelChangedListener listener = mock(OnModelChangedListener.class);
		getActivity().getDateTextView().setOnModelChangedListener(listener);

		DateTextView dtv = getActivity().getDateTextView();

		solo.clickOnView(dtv);
		solo.clickOnText("Ställ in");

		verify(listener, timeout(1000)).onDateChangedTo(LocalDate.now());
	}
}


