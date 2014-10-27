package se.fermitet.android.infektionsdagbok.activity;

import static org.mockito.Mockito.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.joda.time.DateTime;
import org.joda.time.Duration;

import se.fermitet.android.infektionsdagbok.R;
import se.fermitet.android.infektionsdagbok.model.ModelManager;
import se.fermitet.android.infektionsdagbok.model.Week;
import se.fermitet.android.infektionsdagbok.model.WeekAnswers;
import se.fermitet.android.infektionsdagbok.storage.EmailHandler;
import se.fermitet.android.infektionsdagbok.storage.Storage;
import se.fermitet.android.infektionsdagbok.test.MockedEmailFactory;
import android.widget.EditText;
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
		final String expectedFileName = "Infektionsdagbok" + desiredYear + ".xls";

		setYearSpinnerToDesiredYear(desiredYear);
		removeOldFile(expectedFileName);
		solo.clickOnView(solo.getView(R.id.exportBTN));

		File foundFile = timeoutSearchForFileWithName(expectedFileName);
		
		DateTime lastModified = new DateTime(foundFile.lastModified());
		Duration duration = new Duration(lastModified, DateTime.now());

		int age = duration.toStandardSeconds().getSeconds();
		assertTrue("File must be max 5 seconds old.  Was " + age, age <= 5);

		// Email
		EmailHandler mockedEmailHandler = getActivity().getLocalApplication().getEmailHandler();
		verify(mockedEmailHandler, timeout((int) TIMEOUT)).sendEmail(foundFile, getActivity());
	}
	
	private void removeOldFile(String expectedFileName) {
		File oldFile = getFileFromStorage(expectedFileName);
		if (oldFile != null) {
			oldFile.delete();
		}
	}

	private File timeoutSearchForFileWithName(String expectedFileName) {
		File foundFile = null;
		
		setStart();
		do {
			foundFile = getFileFromStorage(expectedFileName);

			setElapsed();
		} while (foundFile == null && notYetTimeout());

		assertNotNull("Should find file with name " + expectedFileName, foundFile);

		return foundFile;
	}

	private File getFileFromStorage(String expectedFileName) {
		File foundFile = null;
		File externalFilesDir = getActivity().getExternalFilesDir(null);
		File[] files = externalFilesDir.listFiles();
		for (int i = 0; i < files.length; i++) {
			File file = files[i];
			if (file.getName().equals(expectedFileName)) {
				foundFile = file;
				break;
			}
		}
		return foundFile;
	}


	public void testGetSelectedYear() throws Exception {
		assertEquals("Before setting, should be current", DateTime.now().weekyear().get(), getActivity().getView().getSelectedYear());

		int changedYear = 2013;
		setYearSpinnerToDesiredYear(changedYear);

		assertEquals("After setting, should be changed", DateTime.now().weekyear().get(), getActivity().getView().getSelectedYear());

	}

	public void testExportHasNameAndSSN() throws Exception {
		String testName = "TESTNAME";
		String testSSN = "123456-7890";

		EditText nameEdit = (EditText) solo.getView(R.id.nameEdit);
		EditText ssnEdit = (EditText) solo.getView(R.id.ssnEdit);

		solo.enterText(nameEdit, testName);
		solo.enterText(ssnEdit, testSSN);

		solo.clickOnView(solo.getView(R.id.exportBTN));
		
		Workbook wb;
		
		setStart();
		do {
			wb = getActivity().wb;
			
			setElapsed();
		} while (wb == null && notYetTimeout());
		assertNotNull("Workbook null", wb);

		Sheet sheet = wb.getSheet("Infektionsdagbok");

		Cell nameCell = sheet.getRow(1).getCell(10);
		assertEquals("Name cell text", testName, nameCell.getStringCellValue());

		Cell ssnCell = sheet.getRow(2).getCell(10);
		assertEquals("SSN cell text", testSSN, ssnCell.getStringCellValue());
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
