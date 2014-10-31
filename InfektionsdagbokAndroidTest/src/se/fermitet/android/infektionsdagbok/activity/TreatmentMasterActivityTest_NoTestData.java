package se.fermitet.android.infektionsdagbok.activity;

import java.util.Map;
import java.util.UUID;

import org.joda.time.LocalDate;

import se.fermitet.android.infektionsdagbok.R;
import se.fermitet.android.infektionsdagbok.model.Treatment;
import se.fermitet.android.infektionsdagbok.views.TreatmentAdapter;
import android.app.DatePickerDialog;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;


public class TreatmentMasterActivityTest_NoTestData extends TreatmentMasterActivityTest{

	public TreatmentMasterActivityTest_NoTestData() {
		super();
	}

	public void testEnterNewTreatmentFromEmpty() throws Exception {
		LocalDate startDate = LocalDate.now();
		Integer numDays = 100;
		String infectionType = "INFTYPE";
		String medicine = "MEDICINE";

		int sizeBefore = getActivity().getLocalApplication().getModelManager().getAllTreatments().size();

		Treatment treatmentWithData = new Treatment(startDate, numDays, infectionType, medicine);
		clickNewEnterDataPressButtonAndWaitToGoBack(treatmentWithData, R.id.saveBTN);

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

	public void testNewWithCancel() throws Exception {
		LocalDate startDate = LocalDate.now();
		Integer numDays = 100;
		String infectionType = "INFTYPE";
		String medicine = "MEDICINE";

		int sizeBefore = getActivity().getLocalApplication().getModelManager().getAllTreatments().size();

		Treatment treatmentWithData = new Treatment(startDate, numDays, infectionType, medicine);
		clickNewEnterDataPressButtonAndWaitToGoBack(treatmentWithData, R.id.cancelBTN);

		// Check saved data
		Map<UUID, Treatment> allFromFile = null;
		setStart();
		do {
			allFromFile = getActivity().getLocalApplication().getModelManager().getAllTreatments();

			setElapsed();
		} while (allFromFile.size() != sizeBefore  && notYetTimeout());
		assertEquals("Size after save", sizeBefore, allFromFile.size());
	}



	protected void clickNewEnterDataPressButtonAndWaitToGoBack(Treatment treatmentWithData, int finishButtonId) throws Exception {
		// Click new
		solo.clickOnView(solo.getView(R.id.newBTN));
		TreatmentDetailActivity detailActivity = (TreatmentDetailActivity) timeoutGetCurrentActivity(TreatmentDetailActivity.class);

		// Enter data
		TextView startTV = (TextView) solo.getView(R.id.startTV);
		solo.clickOnView(startTV);
		solo.waitForDialogToOpen();

		DatePickerDialog dialog = detailActivity.view.getDatePickerDialog();
		DatePicker picker = dialog.getDatePicker();

		LocalDate startDate = treatmentWithData.getStartingDate();
		solo.setDatePicker(picker, startDate.getYear(), startDate.getMonthOfYear() - 1, startDate.getDayOfMonth());
		solo.clickOnButton("Ställ in");

		solo.clearEditText((EditText)solo.getView(R.id.numDaysEdit));
		solo.enterText((EditText) solo.getView(R.id.numDaysEdit), treatmentWithData.getNumDays().toString());
		solo.enterText((EditText) solo.getView(R.id.medicineEdit), treatmentWithData.getMedicine());
		solo.enterText((EditText) solo.getView(R.id.infectionTypeEdit), treatmentWithData.getInfectionType());

		// Click save
		solo.clickOnView(solo.getView(finishButtonId));
		timeoutGetCurrentActivity(TreatmentMasterActivity.class);
	}
}
