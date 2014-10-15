package se.fermitet.android.infektionsdagbok.activity;

import java.util.ArrayList;
import java.util.Collection;

import org.joda.time.DateTime;

import se.fermitet.android.infektionsdagbok.R;
import se.fermitet.android.infektionsdagbok.model.ModelManager;
import se.fermitet.android.infektionsdagbok.model.Week;
import se.fermitet.android.infektionsdagbok.model.WeekAnswers;
import se.fermitet.android.infektionsdagbok.storage.Storage;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;


public class ExportTest_WithOldTestData extends ActivityTestWithSolo<ExportActivity> {


	private int[] desiredYears;
	private ModelManager mm;


	public ExportTest_WithOldTestData() {
		super(ExportActivity.class);
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
			int yearToWrite = firstYear + i;
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
}
