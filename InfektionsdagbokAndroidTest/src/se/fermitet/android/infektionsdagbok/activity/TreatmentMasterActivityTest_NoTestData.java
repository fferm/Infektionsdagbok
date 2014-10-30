package se.fermitet.android.infektionsdagbok.activity;

import java.util.Map;
import java.util.UUID;

import org.joda.time.DateTime;

import se.fermitet.android.infektionsdagbok.R;
import se.fermitet.android.infektionsdagbok.model.Treatment;
import se.fermitet.android.infektionsdagbok.views.TreatmentAdapter;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;


public class TreatmentMasterActivityTest_NoTestData extends ActivityTestWithSolo<TreatmentMasterActivity>{

	public TreatmentMasterActivityTest_NoTestData() {
		super(TreatmentMasterActivity.class);
	}

	public void testEnterNewTreatmentFromEmpty() throws Exception {
		DateTime startDate = DateTime.now().withMillisOfDay(0);
		Integer numDays = 100;
		String infectionType = "INFTYPE";
		String medicine = "MEDICINE";

		int sizeBefore = getActivity().getLocalApplication().getModelManager().getAllTreatments().size();

		// Click new
		solo.clickOnView(solo.getView(R.id.newBTN));
		TreatmentDetailActivity detailActivity = (TreatmentDetailActivity) timeoutGetCurrentActivity(TreatmentDetailActivity.class);

		// Enter data
		TextView startTV = (TextView) solo.getView(R.id.startTV);
		solo.clickOnView(startTV);
		solo.waitForDialogToOpen();

		DatePickerDialog dialog = detailActivity.view.getDatePickerDialog();
		DatePicker picker = dialog.getDatePicker();

		solo.setDatePicker(picker, startDate.getYear(), startDate.getMonthOfYear() - 1, startDate.getDayOfMonth());
		solo.clickOnButton("St�ll in");

		solo.clearEditText((EditText)solo.getView(R.id.numDaysEdit));
		solo.enterText((EditText) solo.getView(R.id.numDaysEdit), "" + numDays);
		solo.enterText((EditText) solo.getView(R.id.medicineEdit), medicine);
		solo.enterText((EditText) solo.getView(R.id.infectionTypeEdit), infectionType);

		// Click save
		solo.clickOnView(solo.getView(R.id.saveBTN));
		timeoutGetCurrentActivity(TreatmentMasterActivity.class);

		// Check saved data
		Map<UUID, Treatment> allFromFile = null;
		setStart();
		do {
			allFromFile = getActivity().getLocalApplication().getModelManager().getAllTreatments();

			setElapsed();
		} while (allFromFile.size() != sizeBefore + 1 && notYetTimeout());
		assertEquals("Size after save", sizeBefore + 1, allFromFile.size());

		Treatment fromFile = allFromFile.values().iterator().next();

		assertEquals("Start", startDate, fromFile.getStartingDate());
		assertEquals("Num days", numDays, fromFile.getNumDays());
		assertEquals("Medicine", medicine, fromFile.getMedicine());
		assertEquals("InfectionType", infectionType, fromFile.getInfectionType());

		// Check in adapter
		TreatmentAdapter adapter = getListAdapter();

		int count;
		setStart();
		do {
			count = adapter.getCount();

			setElapsed();
		} while (count != 1 && notYetTimeout());
		assertEquals("adapter size",  1, count);

		Treatment inAdapter = adapter.getItem(0);

		assertEquals("Start", startDate, inAdapter.getStartingDate());
		assertEquals("Num days", numDays, inAdapter.getNumDays());
		assertEquals("Medicine", medicine, inAdapter.getMedicine());
		assertEquals("InfectionType", infectionType, inAdapter.getInfectionType());
		assertEquals("UUID", fromFile.getUUID(), inAdapter.getUUID());
	}

	private Activity timeoutGetCurrentActivity(Class<?> expectedActivityClass) throws Exception {
		Activity currentActivity = null;
		setStart();
		do {
			currentActivity = solo.getCurrentActivity();
			setElapsed();
		} while (!currentActivity.getClass().equals(expectedActivityClass) && notYetTimeout());
		assertEquals("Wrong activity class", expectedActivityClass, currentActivity.getClass());

		return currentActivity;
	}

	private TreatmentAdapter getListAdapter() throws Exception {
		TreatmentMasterActivity activity = getActivity();
		ListView listView = (ListView) activity.view.findViewById(R.id.treatmentListView);
		ListAdapter adapter = listView.getAdapter();
		return (TreatmentAdapter) adapter;
	}


}