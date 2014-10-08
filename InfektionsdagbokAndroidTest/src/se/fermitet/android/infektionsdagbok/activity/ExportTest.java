package se.fermitet.android.infektionsdagbok.activity;

import java.text.DateFormat;

import org.joda.time.DateTime;

import se.fermitet.android.infektionsdagbok.R;
import se.fermitet.android.infektionsdagbok.views.ExportView;


public class ExportTest extends ActivityTestWithSolo<ExportActivity> {

	private DateFormat format;

	public ExportTest() {
		super(ExportActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		format = DateFormat.getDateInstance(DateFormat.SHORT);
	}

	public void testInitials() throws Exception {
		assertTrue("First Week", solo.waitForText("Start"));
		assertTrue("Last week", solo.waitForText("Slut"));
		assertTrue("Export button", solo.waitForView(R.id.exportButton));

		assertTrue("Start date - first day of current year", solo.waitForText(format.format(DateTime.now().withDayOfYear(1).toDate())));
		assertTrue("End date - today", solo.waitForText(format.format(DateTime.now().toDate())));
	}

	public void testInitialDates() throws Exception {
		ExportView view = getActivity().view;

		checkDayIsSameRegardlessOfTime("Start date should be first day of this year", DateTime.now().withDayOfYear(1), view.getStartDate());
		checkDayIsSameRegardlessOfTime("End day should be today", DateTime.now(), view.getEndDate());
	}
	
	public void testSettingDatePickerShouldChangeDate() throws Exception {
		ExportView view = getActivity().view;
		
		// Change startDate
		DateTime setDate = DateTime.now().withYear(2012).withMonthOfYear(1).withDayOfMonth(1);
		DateTime startDateBefore = view.getStartDate();
		DateTime endDateBefore = view.getEndDate();
		
		solo.clickOnView(solo.getView(R.id.startDateTV));
		
		setDatePickerDate(setDate);
		
		checkDayIsSameRegardlessOfTime("Start date - value", setDate, view.getStartDate());
		checkDayIsSameRegardlessOfTime("End date should not have changed", endDateBefore, view.getEndDate());
		assertTrue("Start date - text", solo.waitForText(format.format(setDate.toDate())));
		
		// Change endDate
		solo.clickOnView(solo.getView(R.id.endDateTV));
		
		setDate = DateTime.now().minusDays(10);
		startDateBefore = view.getStartDate();
		endDateBefore = view.getEndDate();
		
		setDatePickerDate(setDate);
		
		checkDayIsSameRegardlessOfTime("End date - value", setDate, view.getEndDate());
		checkDayIsSameRegardlessOfTime("Start date should not have changed", startDateBefore, view.getStartDate());
		assertTrue("End date - text", solo.waitForText(format.format(setDate.toDate())));
	}
	
	private void setDatePickerDate(DateTime setDate) {
		solo.setDatePicker(0, setDate.year().get(), setDate.monthOfYear().get() - 1, setDate.dayOfMonth().get());
		solo.clickOnText("Ställ in");
		
	}
	


	private void checkDayIsSameRegardlessOfTime(String errorMessage, DateTime expected, DateTime actual) {
		assertNotNull("expected was null", expected);
		assertNotNull("actual was null", actual);

		assertTrue(
				errorMessage + "Expected: " + format.format(expected.toDate()) + "  Was: " + format.format(actual.toDate()),
				expected.year().equals(actual.year()) &&
				expected.monthOfYear().equals(actual.monthOfYear()) &&
				expected.dayOfMonth().equals(actual.dayOfMonth()));
	}

}
