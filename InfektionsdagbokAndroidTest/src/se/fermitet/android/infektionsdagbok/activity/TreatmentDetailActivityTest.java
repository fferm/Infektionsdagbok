package se.fermitet.android.infektionsdagbok.activity;

import se.fermitet.android.infektionsdagbok.R;
import android.widget.TextView;

public class TreatmentDetailActivityTest extends ActivityTestWithSolo<TreatmentDetailActivity> {

	public TreatmentDetailActivityTest() {
		super(TreatmentDetailActivity.class);
	}

	public void testInitials() throws Exception {
		assertTrue("Header text", solo.waitForText("Behandling"));

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

}
