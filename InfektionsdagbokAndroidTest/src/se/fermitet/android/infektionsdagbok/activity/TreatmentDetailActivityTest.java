package se.fermitet.android.infektionsdagbok.activity;

import se.fermitet.android.infektionsdagbok.R;
import se.fermitet.android.infektionsdagbok.model.Treatment;
import android.widget.EditText;
import android.widget.TextView;

public class TreatmentDetailActivityTest extends DetailActivityTest<Treatment, TreatmentDetailActivity> {

	public TreatmentDetailActivityTest() {
		super(TreatmentDetailActivity.class);
	}

	@Override
	protected String getExpectedHeaderText() {
		return "Behandling";
	}

	@Override
	protected void checkSubInitials() {
		checkHeaderTextView(R.id.startHeader, "Start:");
		assertNotNull("Start date text field", solo.getView(R.id.startTV));

		checkHeaderTextView(R.id.numDaysHeader, "Dagar:");
		assertNotNull("Num days edit text", solo.getView(R.id.numDaysEdit));

		checkHeaderTextView(R.id.medicineHeader, "Preparat:");
		assertNotNull("Medicine text field", solo.getView(R.id.medicineEdit));

		checkHeaderTextView(R.id.infectionTypeHeader, "Sjukdom:");
		assertNotNull("Infection type field", solo.getView(R.id.infectionTypeEdit));
	}

	private void checkHeaderTextView(int id, String text) {
		TextView headerView = (TextView) solo.getView(id);
		assertNotNull(text + " header null", headerView);
		assertEquals(text + " header text", text, headerView.getText());
	}


	public void testChangingOtherFieldsThanStartingDateChangesModel() throws Exception {
		fail("needs to be updated with changing also date field");
		Treatment model = getActivity().view.getModel();

		String newMedicine = "NEW MEDICINE";
		String newInfectionType = "NEW INFECTION TYPE";
		Integer newNumDays = 1000;

		EditText medicineEdit = (EditText) solo.getView(R.id.medicineEdit);
		solo.clearEditText(medicineEdit);
		solo.enterText(medicineEdit, newMedicine);
		assertEquals("Medicine", newMedicine, model.getMedicine());

		EditText infectionTypeEdit = (EditText) solo.getView(R.id.infectionTypeEdit);
		solo.clearEditText(infectionTypeEdit);
		solo.enterText(infectionTypeEdit, newInfectionType);
		assertEquals("Infection type", newInfectionType, model.getInfectionType());

		EditText numDaysEdit = (EditText) solo.getView(R.id.numDaysEdit);
		solo.clearEditText(numDaysEdit);
		solo.enterText(numDaysEdit, newNumDays.toString());
		assertEquals("Num days", newNumDays, model.getNumDays());
	}
}
