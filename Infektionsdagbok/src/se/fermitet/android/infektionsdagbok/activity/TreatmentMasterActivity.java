package se.fermitet.android.infektionsdagbok.activity;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.joda.time.DateTime;

import se.fermitet.android.infektionsdagbok.R;
import se.fermitet.android.infektionsdagbok.model.ModelManager;
import se.fermitet.android.infektionsdagbok.model.Treatment;
import se.fermitet.android.infektionsdagbok.views.TreatmentAdapter;
import se.fermitet.android.infektionsdagbok.views.TreatmentMasterView;
import se.fermitet.android.infektionsdagbok.views.TreatmentMasterView.OnButtonsPressedListener;
import android.content.Intent;
import android.os.Bundle;

public class TreatmentMasterActivity extends InfektionsdagbokActivity<TreatmentMasterView> {

	protected static final int REQUEST_CODE_NEW = 0;

	public TreatmentMasterActivity() {
		super(R.layout.treatment_master_view);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
//			fillWithTestData();

/*			view.getSingleEditView().setOnButtonsPressedListener(new OnButtonsPressedListener() {
				@Override
				public void onSavePressed(Treatment treatment) throws Exception {
					TreatmentMasterActivity.this.savePressed(treatment);
				}
				@Override
				public void onDeletePressed(Treatment treatment) throws Exception {
					TreatmentMasterActivity.this.deletePressed(treatment);
				}
			});*/

			view.setOnButtonsPressedListener(new OnButtonsPressedListener() {
				@Override
				public void onNewPressed() throws Exception {
					Intent newIntent = new Intent(TreatmentMasterActivity.this, TreatmentDetailActivity.class);
					startActivityForResult(newIntent, REQUEST_CODE_NEW);
				}
			});

			syncListViewDataWithStored();
		} catch (Exception e) {
			view.handleException(e);
		}
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

	public void syncListViewDataWithStored() throws Exception {
		Collection<Treatment> allTreatments = getLocalApplication().getModelManager().getAllTreatments().values();

		TreatmentAdapter adapter = new TreatmentAdapter(this, sortedListOfTreatments(allTreatments));

		view.setAdapter(adapter);
	}

/*	private void savePressed(Treatment treatment) throws Exception {
		getLocalApplication().getModelManager().saveTreatment(treatment);
		syncListViewDataWithStored();
	}

	private void deletePressed(Treatment treatment) throws Exception {
		getLocalApplication().getModelManager().delete(treatment);
		syncListViewDataWithStored();
	}*/

	private List<Treatment> sortedListOfTreatments(Collection<Treatment> unsorted) {
		List<Treatment> list = new ArrayList<Treatment>(unsorted);

		Collections.sort(list, new Comparator<Treatment>() {
			@Override
			public int compare(Treatment lhs, Treatment rhs) {
				DateTime lhsDate = lhs.getStartingDate();
				DateTime rhsDate = rhs.getStartingDate();

				if (lhsDate == null) return 1;
				if (rhsDate == null) return -1;

				lhsDate = lhsDate.withMillisOfDay(0);
				rhsDate = rhsDate.withMillisOfDay(0);

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
			DateTime date;
			if (i % 4 == 0) date = DateTime.now().minusDays(i);
			else if (i % 4 == 1) date = DateTime.now().minusWeeks(i);
			else if (i % 4 == 2) date = DateTime.now().minusMonths(i);
			else date = DateTime.now().minusYears(i);

			testData.add(
					new Treatment(
							date,
							i,
							"INF" + i,
							"MEDICINE_NAME" + i));
		}

		Treatment nullMedicine = new Treatment(DateTime.now().minusDays(100), 100, "INFECTION", null);
		Treatment nullInfection = new Treatment(DateTime.now().minusDays(101), 101, null, "MEDICINE");
		Treatment nullStartingDate = new Treatment(null, 102, "INFECT102", "MEDICINE102");

		testData.add(nullMedicine);
		testData.add(nullInfection);
		testData.add(nullStartingDate);

		mm.saveTreatments(testData);
	}
}

