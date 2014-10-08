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
