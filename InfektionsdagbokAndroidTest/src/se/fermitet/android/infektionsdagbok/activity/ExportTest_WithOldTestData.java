package se.fermitet.android.infektionsdagbok.activity;

import static org.mockito.Mockito.verify;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import org.joda.time.DateTime;
import org.joda.time.Duration;

import se.fermitet.android.infektionsdagbok.R;
import se.fermitet.android.infektionsdagbok.model.ModelManager;
import se.fermitet.android.infektionsdagbok.model.Week;
import se.fermitet.android.infektionsdagbok.model.WeekAnswers;
import se.fermitet.android.infektionsdagbok.storage.EmailHandler;
import se.fermitet.android.infektionsdagbok.storage.Storage;
import se.fermitet.android.infektionsdagbok.test.MockedEmailFactory;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;


public class ExportTest_WithOldTestData extends ActivityTestWithSolo<ExportActivity> {


	private int[] desiredYears;
	private ModelManager mm;


	public ExportTest_WithOldTestData() {
		super(ExportActivity.class, MockedEmailFactory.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}

	@Override
	protected void onBeforeActivityCreation() throws Exception {
		super.onBeforeActivityCreation();
		addOldTestData();
	}

	private void addOldTestData() throws Exception {
		int firstYear = 2012;
		int currentYear = DateTime.now().year().get();
		this.desiredYears = new int[currentYear - firstYear + 1];
		for (int i = 0; i < desiredYears.length; i++) {
			int yearToWrite = currentYear - i;
			desiredYears[i] = yearToWrite;
		}

		WeekAnswers waFirst = new WeekAnswers(new Week(new DateTime(firstYear, 2, 1, 1, 1))); // February to avoid 2011-52

		Collection<WeekAnswers> collection = new ArrayList<WeekAnswers>();
		collection.add(waFirst);

		mm = new ModelManager(new Storage(getInstrumentation().getTargetContext()));
		mm.saveWeekAnswers(collection);
	}


	public void testYearSpinnerContentsWithEarlierYears() throws Exception {
		Spinner yearSpinner = (Spinner) solo.getView(R.id.yearSpinner);

		SpinnerAdapter adapter = yearSpinner.getAdapter();
		assertNotNull("adapter null", adapter);

		assertEquals("Same amount of years", desiredYears.length, adapter.getCount());

		for (int i = 0; i < adapter.getCount(); i++) {
			assertEquals("Same year on location " + i, desiredYears[i], ((Integer) adapter.getItem(i)).intValue());
		}
	}

	public void testSpinnerShouldShowCurrentYearWhenStarted() throws Exception {
		Spinner yearSpinner = (Spinner) solo.getView(R.id.yearSpinner);

		assertEquals(DateTime.now().weekyear().get(), yearSpinner.getSelectedItem());
	}

	public void testExportCreatesFileAndCallsEmailHandler() throws Exception {
		int desiredYear = 2013;

		setYearSpinnerToDesiredYear(desiredYear);

		solo.clickOnView(solo.getView(R.id.exportBTN));
		Thread.sleep(1000); // Sleep to ensure time for the file to get written

		final String expectedFileName = "Infektionsdagbok" + desiredYear + ".xls";

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


		// Email
		EmailHandler mockedEmailHandler = getActivity().getLocalApplication().getEmailHandler();
		verify(mockedEmailHandler).sendEmail(foundFile, getActivity());
	}

	public void testGetSelectedYear() throws Exception {
		assertEquals("Before setting, should be current", DateTime.now().weekyear().get(), getActivity().getView().getSelectedYear());

		int changedYear = 2013;
		setYearSpinnerToDesiredYear(changedYear);

		assertEquals("After setting, should be changed", DateTime.now().weekyear().get(), getActivity().getView().getSelectedYear());

	}

	private void setYearSpinnerToDesiredYear(int desiredYear) throws Exception {
		Spinner yearSpinner = (Spinner) solo.getView(R.id.yearSpinner);

		SpinnerAdapter adapter = yearSpinner.getAdapter();
		int idxOfDesiredYear = -1;
		for (int i = 0; i < adapter.getCount(); i++) {
			int yearAtIdxI = (Integer) adapter.getItem(i);
			if (yearAtIdxI == desiredYear) {
				idxOfDesiredYear = i;
				break;
			}
		}

		solo.pressSpinnerItem(0, idxOfDesiredYear);
	}

}
