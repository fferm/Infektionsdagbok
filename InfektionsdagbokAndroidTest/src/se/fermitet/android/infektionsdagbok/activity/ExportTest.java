package se.fermitet.android.infektionsdagbok.activity;

import java.io.File;
import java.text.DateFormat;

import org.joda.time.DateTime;
import org.joda.time.Duration;

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
		assertTrue("Export button", solo.waitForView(R.id.exportBTN));

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
		setDate = DateTime.now().minusDays(10);
		startDateBefore = view.getStartDate();
		endDateBefore = view.getEndDate();

		solo.clickOnView(solo.getView(R.id.endDateTV));
		setDatePickerDate(setDate);

		checkDayIsSameRegardlessOfTime("End date - value", setDate, view.getEndDate());
		checkDayIsSameRegardlessOfTime("Start date should not have changed", startDateBefore, view.getStartDate());
		assertTrue("End date - text", solo.waitForText(format.format(setDate.toDate())));
	}

	public void testStartDateAfterEndDateShouldGiveError() {
		ExportView view = getActivity().view;

		DateTime oneDayAfterEndDate = view.getEndDate().plusDays(1);

		solo.clickOnView(solo.getView(R.id.startDateTV));
		setDatePickerDate(oneDayAfterEndDate);

		solo.clickOnView(solo.getView(R.id.exportBTN));

		assertTrue(solo.searchText("Kan inte ha startdatum efter slutdatum"));
	}
	
	public void testExportCreatesFile() throws Exception {
		solo.clickOnView(solo.getView(R.id.exportBTN));
		Thread.sleep(1000); // Sleep to ensure time for the file to get written
		
		int currentYear = DateTime.now().weekyear().get();
		final String expectedFileName = "Infektionsdagbok" + currentYear + ".xls";
		
		File externalFilesDir = getActivity().getExternalFilesDir(null);
		File foundFile = null;
		File[] files = externalFilesDir.listFiles();
		for (int i = 0; i < files.length; i++) {
			File file = files[i];
			if (file.getName().equals(expectedFileName)) {
				foundFile = file;
				break;
			}
		}

		assertNotNull("Should find file with name " + expectedFileName, foundFile);

		DateTime lastModified = new DateTime(foundFile.lastModified());
		Duration duration = new Duration(lastModified, DateTime.now());
		
		int age = duration.toStandardSeconds().getSeconds();
		assertTrue("File must be max 5 seconds old.  Was " + age, age <= 5);
	}

	private void setDatePickerDate(DateTime setDate) {
		solo.setDatePicker(0, setDate.year().get(), setDate.monthOfYear().get() - 1, setDate.dayOfMonth().get());
		solo.clickOnText("StŠll in");
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
