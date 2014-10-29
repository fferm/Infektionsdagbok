package se.fermitet.android.infektionsdagbok.activity;
import java.util.Map;
import java.util.UUID;

import org.joda.time.DateTime;

import se.fermitet.android.infektionsdagbok.R;
import se.fermitet.android.infektionsdagbok.model.Treatment;
import android.app.DatePickerDialog;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;


public class TreatmentActivityTest_NoTestData extends ActivityTestWithSolo<TreatmentActivity>{

	public TreatmentActivityTest_NoTestData() {
		super(TreatmentActivity.class);
	}

	public void testEnterNewTreatmentFromEmpty() throws Exception {
		DateTime startDate = DateTime.now().withMillisOfDay(0);
		int numDays = 100;
		String infectionType = "INFTYPE";
		String medicine = "MEDICINE";

		// Enter data
		TextView startTV = (TextView) solo.getView(R.id.startTV);
		solo.clickOnView(startTV);
		solo.waitForDialogToOpen();

		DatePickerDialog dialog = getActivity().view.getSingleEditView().getDatePickerDialog();
		DatePicker picker = dialog.getDatePicker();

		solo.setDatePicker(picker, startDate.getYear(), startDate.getMonthOfYear() - 1, startDate.getDayOfMonth());
		solo.clickOnButton("Ställ in");

		solo.clearEditText((EditText)solo.getView(R.id.numDaysEdit));
		solo.enterText((EditText) solo.getView(R.id.numDaysEdit), "" + numDays);
		solo.enterText((EditText) solo.getView(R.id.medicineEdit), medicine);
		solo.enterText((EditText) solo.getView(R.id.infectionTypeEdit), infectionType);

		// Click save
		solo.clickOnView(solo.getView(R.id.saveBTN));

		// Check saved data
		Map<UUID, Treatment> allFromFile = null;
		setStart();
		do {
			allFromFile = getActivity().getLocalApplication().getModelManager().getAllTreatments();

			setElapsed();
		} while (allFromFile.size() != 1 && notYetTimeout());
		System.out.println("!!! elapsed: " + elapsed);
		assertEquals("Size after save", 1, allFromFile.size());

		Treatment fromFile = allFromFile.values().iterator().next();

		assertEquals("Start", startDate, fromFile.getStartingDate());
		assertEquals("Num days", numDays, fromFile.getNumDays());
		assertEquals("Medicine", medicine, fromFile.getMedicine());
		assertEquals("InfectionType", infectionType, fromFile.getInfectionType());

		// Check in adapter
		ListAdapter adapter = getListAdapter();

		int count;
		setStart();
		do {
			count = adapter.getCount();

			setElapsed();
		} while (count != 1 && notYetTimeout());
		assertEquals("adapter size",  1, count);

		Treatment inAdapter = (Treatment) adapter.getItem(0);

		assertEquals("Start", startDate, inAdapter.getStartingDate());
		assertEquals("Num days", numDays, inAdapter.getNumDays());
		assertEquals("Medicine", medicine, inAdapter.getMedicine());
		assertEquals("InfectionType", infectionType, inAdapter.getInfectionType());
		assertEquals("UUID", fromFile.getUUID(), inAdapter.getUUID());
	}

	private ListAdapter getListAdapter() {
		TreatmentActivity activity = getActivity();
		ListView listView = (ListView) activity.view.findViewById(R.id.treatmentListView);
		ListAdapter adapter = listView.getAdapter();
		return adapter;
	}


}
