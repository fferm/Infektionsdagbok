package se.fermitet.android.infektionsdagbok.activity;

import java.util.ArrayList;
import java.util.Collection;

import org.joda.time.LocalDate;

import se.fermitet.android.infektionsdagbok.R;
import se.fermitet.android.infektionsdagbok.model.Treatment;
import se.fermitet.android.infektionsdagbok.views.TreatmentAdapter;
import se.fermitet.android.infektionsdagbok.widget.DateTextView;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class TreatmentMasterActivityTest extends MasterActivityTest<Treatment, TreatmentMasterActivity, TreatmentAdapter> {

	private Treatment nullMedicine;
	private Treatment nullInfection;
	private Treatment nullStartingDate;
	private Treatment nullNumDays;

	public TreatmentMasterActivityTest() {
		super(TreatmentMasterActivity.class, TreatmentDetailActivity.class, Treatment.class);
	}

	@Override
	protected void checkSubInitials() throws Exception {
		assertTrue("Date list header", solo.waitForText("Start"));
		assertTrue("numDays list header", solo.waitForText("Dgr"));
	}

	@Override
	protected String getExpectedHeaderText() throws Exception {
		return "Behandlingar";
	}



	@Override
	protected void saveTestData() throws Exception {
		ArrayList<Treatment> testData = new ArrayList<Treatment>();

		for (int i = 1; i <= 5; i++) {
			LocalDate date;
			if (i % 4 == 0) date = LocalDate.now().minusDays(i);
			else if (i % 4 == 1) date = LocalDate.now().minusWeeks(i);
			else if (i % 4 == 2) date = LocalDate.now().minusMonths(i);
			else date = LocalDate.now().minusYears(i);

			testData.add(
					new Treatment(
							date,
							i,
							"INF" + i,
							"MEDICINE_NAME" + i));
		}

		nullMedicine = new Treatment(LocalDate.now().minusDays(100), 100, "INFECTION", null);
		nullInfection = new Treatment(LocalDate.now().minusDays(101), 101, null, "MEDICINE");
		nullStartingDate = new Treatment(null, 102, "INFECT102", "MEDICINE102");
		nullNumDays = new Treatment(LocalDate.now().minusDays(103), null, "INFECT103", "MEDICINE103");

		testData.add(nullMedicine);
		testData.add(nullInfection);
		testData.add(nullStartingDate);
		testData.add(nullNumDays);

		mm.saveAll(testData);
	}

	@Override
	protected void checkDetailEditorsEmpty() {
		// Check empty
		TextView startTV = (TextView) solo.getView(R.id.startTV);
		CharSequence startText = startTV.getText();
		assertTrue("Start date empty", startText == null || startText.length() == 0);

		EditText numDaysEdit = (EditText) solo.getView(R.id.numDaysEdit);
		Editable numDaysText = numDaysEdit.getText();
		assertTrue("NumDays empty", numDaysText == null || numDaysText.length() == 0);

		EditText infectionTypeEdit = (EditText) solo.getView(R.id.infectionTypeEdit);
		Editable infectionTypeText = infectionTypeEdit.getText();
		assertTrue("infection type empty", infectionTypeText == null || infectionTypeText.length() == 0);

		EditText medicineEdit = (EditText) solo.getView(R.id.medicineEdit);
		Editable medicineText = medicineEdit.getText();
		assertTrue("medicine empty", medicineText == null || medicineText.length() == 0);
	}

	@Override
	protected void checkDetailEditorsContents(Treatment item) throws Exception {
		TextView startTv = (TextView) solo.getView(R.id.startTV);
		checkTextViewForStart(item, startTv);

		TextView numDaysTv = (TextView) solo.getView(R.id.numDaysEdit);
		checkTextViewForNumDays(item, numDaysTv);

		TextView medicineTv = (TextView) solo.getView(R.id.medicineEdit);
		checkTextViewForMedicine(item, medicineTv);

		TextView infectionTypeTv = (TextView) solo.getView(R.id.infectionTypeEdit);
		checkTextViewForInfectionType(item, infectionTypeTv);
	}


	@Override
	protected Treatment getTestItem() {
		return new Treatment(new LocalDate(2000, 1, 1), 1000, "Fšrkylning", "Doxyferm");
	}

	@Override
	protected void editUIBasedOnItem(Treatment itemWithNewValues) throws Exception {
		// Start editing
		DateTextView startTV = (DateTextView) solo.getView(R.id.startTV);
		startTV.setModel(itemWithNewValues.getStartingDate());

		EditText numDaysEdit = (EditText) solo.getView(R.id.numDaysEdit);
		solo.clearEditText(numDaysEdit);
		solo.enterText(numDaysEdit, itemWithNewValues.getNumDays().toString());

		EditText medicineEdit = (EditText) solo.getView(R.id.medicineEdit);
		solo.clearEditText(medicineEdit);
		solo.enterText(medicineEdit, itemWithNewValues.getMedicine());

		EditText infectionTypeEdit = (EditText) solo.getView(R.id.infectionTypeEdit);
		solo.clearEditText(infectionTypeEdit);
		solo.enterText(infectionTypeEdit, itemWithNewValues.getInfectionType());
	}

	@Override
	protected Collection<Treatment> getSpecialItemsToCheck() {
		Collection<Treatment> ret = new ArrayList<Treatment>();
		ret.add(nullInfection);
		ret.add(nullMedicine);
		ret.add(nullNumDays);
		ret.add(nullStartingDate);

		return ret;
	}

	@Override
	protected void checkListSubViewForItemData(View listSubView, Treatment item) {
		TextView startTv = (TextView) listSubView.findViewById(R.id.dateValueField);
		TextView numDaysTV = (TextView) listSubView.findViewById(R.id.numDaysValueField);

		checkTextViewForStart(item, startTv);
		checkTextViewForNumDays(item, numDaysTV);
	}

	private void checkTextViewForStart(Treatment item, TextView startTv) {
		checkDateTextView(item.getStartingDate(), startTv);
	}

	private void checkTextViewForNumDays(Treatment item, TextView numDaysTV) {
		checkIntegerTextView(item.getNumDays(), numDaysTV);
	}

	private void checkTextViewForInfectionType(Treatment treatment,	TextView infectionTypeTv) {
		checkStringTextView(treatment.getInfectionType(), infectionTypeTv);
	}

	private void checkTextViewForMedicine(Treatment treatment, TextView medicineTv) {
		checkStringTextView(treatment.getMedicine(), medicineTv);
	}



	@Override
	protected void checkListOrder(Treatment previous, Treatment current) {
		LocalDate previousStart = previous.getStartingDate();
		LocalDate currentStart = current.getStartingDate();

		boolean condition = (previousStart == null) || (currentStart == null) || previousStart.isAfter(currentStart);
		assertTrue("Wrong order on treatments starting with the one with startingDate =  " + current.getStartingDate(), condition);
	}
}
