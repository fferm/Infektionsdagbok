package se.fermitet.android.infektionsdagbok.activity;

import org.joda.time.DateTime;

import se.fermitet.android.infektionsdagbok.R;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;


public class ExportTest extends ActivityTestWithSolo<ExportActivity> {


	public ExportTest() {
		super(ExportActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}

	public void testInitials() throws Exception {
		assertTrue("Year", solo.waitForText("År:"));
		assertTrue("Year spinner", solo.waitForView(R.id.yearSpinner));

		assertTrue("Name", solo.waitForText("Namn:"));
		assertTrue("Personnummer", solo.waitForText("Personnummer:"));

		assertTrue("Export button", solo.waitForView(R.id.exportBTN));
	}

	public void testYearSpinnerWithNoData() throws Exception {
		int[] desiredYears = new int[] {DateTime.now().weekyear().get()};

		Spinner yearSpinner = (Spinner) solo.getView(R.id.yearSpinner);

		SpinnerAdapter adapter = yearSpinner.getAdapter();
		assertNotNull("adapter null", adapter);

		assertEquals("Same amount of years", desiredYears.length, adapter.getCount());

		for (int i = 0; i < adapter.getCount(); i++) {
			assertEquals("Same year on location " + i, desiredYears[i], ((Integer) adapter.getItem(i)).intValue());
		}
	}

	public void testGetName() throws Exception {
		String testName = "TESTNAME";
		EditText nameEdit = (EditText) solo.getView(R.id.nameEdit);
		solo.enterText(nameEdit, testName);

		assertEquals(testName, getActivity().getView().getName());
	}

	public void testGetSSN() throws Exception {
		String testSSN = "123456-7890";
		EditText ssnEdit = (EditText) solo.getView(R.id.ssnEdit);
		solo.enterText(ssnEdit, testSSN);

		assertEquals(testSSN, getActivity().getView().getSSN());
	}
}
