package se.fermitet.android.infektionsdagbok.activity;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collection;

import org.joda.time.DateTime;

import se.fermitet.android.infektionsdagbok.model.Treatment;

public class TreatmentTest extends ActivityTestWithSolo<TreatmentActivity> {

	private Treatment t1;
	private Treatment t2;
	private Treatment t3;
	private Treatment t4;

	public TreatmentTest() {
		super(TreatmentActivity.class);
	}

	@Override
	protected void onBeforeActivityCreation() throws Exception {
		super.onBeforeActivityCreation();

		Collection<Treatment> testDataTreatments = new ArrayList<Treatment>();

		t1 = new Treatment("INF1", "MED1", DateTime.now().minusDays(1), 1);
		t2 = new Treatment("INF2", "MED2", DateTime.now().minusWeeks(2), 2);
		t3 = new Treatment("INF3", "MED3", DateTime.now().minusMonths(3), 3);
		t4 = new Treatment("INF4", "MED4", DateTime.now().minusYears(4), 4);

		testDataTreatments.add(t1);
		testDataTreatments.add(t2);
		testDataTreatments.add(t3);
		testDataTreatments.add(t4);

		getActivity().getLocalApplication().getModelManager().saveTreatments(testDataTreatments);
	}

	public void testInitials() throws Exception {
		assertTrue("Header text", solo.waitForText("Behandlingar"));

		assertTrue("Date header", solo.waitForText("Start"));
		assertTrue("numDays header", solo.waitForText("Dagar"));

		DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);

		assertTrue("t1 date", solo.waitForText(df.format(t1.getStartingDate().toDate())));
		assertTrue("t2 date", solo.waitForText(df.format(t2.getStartingDate().toDate())));
		assertTrue("t3 date", solo.waitForText(df.format(t3.getStartingDate().toDate())));
		assertTrue("t4 date", solo.waitForText(df.format(t4.getStartingDate().toDate())));

		assertTrue("t1 numDays", solo.waitForText("" + t1.getNumDays()));
		assertTrue("t2 numDays", solo.waitForText("" + t2.getNumDays()));
		assertTrue("t3 numDays", solo.waitForText("" + t3.getNumDays()));
		assertTrue("t4 numDays", solo.waitForText("" + t4.getNumDays()));
	}
}
