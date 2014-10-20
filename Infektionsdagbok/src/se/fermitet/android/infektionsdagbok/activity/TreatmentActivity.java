package se.fermitet.android.infektionsdagbok.activity;

import java.util.ArrayList;
import java.util.Collection;

import org.joda.time.DateTime;

import se.fermitet.android.infektionsdagbok.R;
import se.fermitet.android.infektionsdagbok.model.Treatment;
import se.fermitet.android.infektionsdagbok.views.TreatmentView;
import android.os.Bundle;

public class TreatmentActivity extends InfektionsdagbokActivity<TreatmentView> {

	public TreatmentActivity() {
		super(R.layout.treatment_view);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			fillWithTestData();

			view.setTreatmentsToShow(getLocalApplication().getModelManager().getAllTreatments());
		} catch (Exception e) {
			handleException(e);
		}
	}

	// TODO: Delete this
	private void fillWithTestData() throws Exception {
		Collection<Treatment> testDataTreatments = new ArrayList<Treatment>();

		Treatment t1 = new Treatment("INF1", "MED1", DateTime.now().minusDays(1), 1);
		Treatment t2 = new Treatment("INF2", "MED2", DateTime.now().minusWeeks(2), 2);
		Treatment t3 = new Treatment("INF3", "MED3", DateTime.now().minusMonths(3), 3);
		Treatment t4 = new Treatment("INF4", "MED4", DateTime.now().minusYears(4), 4);

		testDataTreatments.add(t1);
		testDataTreatments.add(t2);
		testDataTreatments.add(t3);
		testDataTreatments.add(t4);

		getLocalApplication().getModelManager().saveTreatments(testDataTreatments);
	}
}
