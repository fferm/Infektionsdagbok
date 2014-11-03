package se.fermitet.android.infektionsdagbok.activity;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.joda.time.LocalDate;

import se.fermitet.android.infektionsdagbok.R;
import se.fermitet.android.infektionsdagbok.model.ModelManager;
import se.fermitet.android.infektionsdagbok.model.Treatment;
import se.fermitet.android.infektionsdagbok.views.TreatmentAdapter;
import se.fermitet.android.infektionsdagbok.views.TreatmentMasterView;
import android.content.Intent;

public class TreatmentMasterActivity extends InfektionsdagbokMasterActivity<TreatmentMasterView, Treatment, TreatmentAdapter> {

	public TreatmentMasterActivity() {
		super(R.layout.treatment_master_view, TreatmentDetailActivity.class);
	}

	@Override
	protected TreatmentAdapter createListAdapter() throws Exception {
		Collection<Treatment> unsorted = getLocalApplication().getModelManager().getAllTreatments().values();
		return new TreatmentAdapter(this,  sortedListOfTreatments(unsorted));
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		try {
			syncListViewDataWithStored();
		} catch (Exception e) {
			view.handleException(e);
		}
	}


/*	private void savePressed(Treatment treatment) throws Exception {
		getLocalApplication().getModelManager().saveTreatment(treatment);
		syncListViewDataWithStored();
	}*/


	private List<Treatment> sortedListOfTreatments(Collection<Treatment> unsorted) {
		List<Treatment> list = new ArrayList<Treatment>(unsorted);

		Collections.sort(list, new Comparator<Treatment>() {
			@Override
			public int compare(Treatment lhs, Treatment rhs) {
				LocalDate lhsDate = lhs.getStartingDate();
				LocalDate rhsDate = rhs.getStartingDate();

				if (lhsDate == null) return 1;
				if (rhsDate == null) return -1;

				if (lhsDate.isBefore(rhsDate)) return 1;
				else if (lhsDate.equals(rhsDate)) return 0;
				else return -1;
			}
		});

		return list;
	}

	// TODO: Delete this
	private void fillWithTestData() throws Exception {
		ModelManager mm = getLocalApplication().getModelManager();
		if (mm.getAllTreatments().size() == 0) return;

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

		Treatment nullMedicine = new Treatment(LocalDate.now().minusDays(100), 100, "INFECTION", null);
		Treatment nullInfection = new Treatment(LocalDate.now().minusDays(101), 101, null, "MEDICINE");
		Treatment nullStartingDate = new Treatment(null, 102, "INFECT102", "MEDICINE102");

		testData.add(nullMedicine);
		testData.add(nullInfection);
		testData.add(nullStartingDate);

		mm.saveAll(testData);
	}

}

