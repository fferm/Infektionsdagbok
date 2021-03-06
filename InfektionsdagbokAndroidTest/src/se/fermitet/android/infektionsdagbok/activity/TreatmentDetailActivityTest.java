package se.fermitet.android.infektionsdagbok.activity;

import org.joda.time.LocalDate;

import se.fermitet.android.infektionsdagbok.R;
import se.fermitet.android.infektionsdagbok.model.Treatment;
import se.fermitet.android.infektionsdagbok.widget.DateTextView;
import android.widget.EditText;

public class TreatmentDetailActivityTest extends DetailActivityTest<Treatment, TreatmentDetailActivity> {

	public TreatmentDetailActivityTest() {
		super(TreatmentDetailActivity.class);
	}

	@Override
	protected String getExpectedHeaderText() {
		return "Behandling";
	}

	@Override
	protected void checkSubInitials() throws Exception {
		Thread.sleep(10000);
		checkHeaderTextView(R.id.startHeader, "Start:");
		assertNotNull("Start date text field", solo.getView(R.id.startTV));
		assertTrue("Start date field class", solo.getView(R.id.startTV) instanceof DateTextView);

		checkHeaderTextView(R.id.numDaysHeader, "Dagar:");
		assertNotNull("Num days edit text", solo.getView(R.id.numDaysEdit));

		checkHeaderTextView(R.id.medicineHeader, "Preparat:");
		assertNotNull("Medicine text field", solo.getView(R.id.medicineEdit));

		checkHeaderTextView(R.id.infectionTypeHeader, "Sjukdom:");
		assertNotNull("Infection type field", solo.getView(R.id.infectionTypeEdit));
	}

	@Override
	public void testChangingFieldsChangesModel() throws Exception {
		Treatment model = getActivity().view.getModel();

		final LocalDate newDate = LocalDate.now().minusMonths(10);
		String newMedicine = "NEW MEDICINE";
		String newInfectionType = "NEW INFECTION TYPE";
		Integer newNumDays = 1000;

		final DateTextView dtv = (DateTextView) solo.getView(R.id.startTV);
		getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				try {
					dtv.setModel(newDate);
				} catch (Exception e) {
					getActivity().view.handleException(e);
				}
			}
		});

		LocalDate dateFromTreatment = null;
		setStart();
		do {
			dateFromTreatment = model.getStartingDate();
			setElapsed();
		} while (! newDate.equals(dateFromTreatment) && notYetTimeout());
		assertEquals("Starting date", newDate, dateFromTreatment);


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

	public void testClickingHeadersOpensDateEditors() throws Exception {
		solo.clickOnView(solo.getView(R.id.startHeader));
		assertTrue("Wait for dialog to open on start", solo.waitForDialogToOpen());
	}

}
