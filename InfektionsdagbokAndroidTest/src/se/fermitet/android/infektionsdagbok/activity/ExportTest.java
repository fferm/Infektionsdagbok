package se.fermitet.android.infektionsdagbok.activity;

import static org.mockito.Mockito.verify;

import java.io.File;

import org.joda.time.DateTime;
import org.joda.time.Duration;

import se.fermitet.android.infektionsdagbok.R;
import se.fermitet.android.infektionsdagbok.storage.EmailHandler;
import se.fermitet.android.infektionsdagbok.test.MockedEmailFactory;


public class ExportTest extends ActivityTestWithSolo<ExportActivity> {


	public ExportTest() {
		super(ExportActivity.class, MockedEmailFactory.class);
	}

	@Override
	protected void setUp() throws Exception {
		System.out.println("!!!! before super.setup()");
		super.setUp();
	}

	public void testInitials() throws Exception {
		assertTrue("Year", solo.waitForText("År"));
		assertTrue("Year spinner", solo.waitForView(R.id.yearSpinner));
		assertTrue("Export button", solo.waitForView(R.id.exportBTN));
	}
	
	
	public void testYearSpinnerWithNoData() throws Exception {
		// TODO
	}

	public void testExportCreatesFileAndCallsEmailHandler() throws Exception {
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


		// Email
		EmailHandler mockedEmailHandler = getActivity().getLocalApplication().getEmailHandler();
		verify(mockedEmailHandler).sendEmail(foundFile, getActivity());
	}
}
