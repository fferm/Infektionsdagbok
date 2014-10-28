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
import se.fermitet.android.infektionsdagbok.views.TreatmentSingleEditView.OnSavePressedListener;
import se.fermitet.android.infektionsdagbok.views.TreatmentView;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class TreatmentActivity extends InfektionsdagbokActivity<TreatmentView> {

	public TreatmentActivity() {
		super(R.layout.treatment_view);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
//			fillWithTestData();

			view.getSingleEditView().setOnSavePressedListener(new OnSavePressedListener() {
				@Override
				public void onSavePressed(Treatment treatment) throws Exception {
					TreatmentActivity.this.savePressed(treatment);
				}
			});
			
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
	
	private void savePressed(Treatment treatment) throws Exception {
		getLocalApplication().getModelManager().saveTreatment(treatment);
		syncListViewDataWithStored();
	}

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

class TreatmentAdapter extends ArrayAdapter<Treatment> {

	private List<Treatment> values;

	public TreatmentAdapter(Context context, List<Treatment> values) {
		super(context, R.layout.treatment_item, values);
		this.values = values;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View rowView = inflater.inflate(R.layout.treatment_item, parent, false);

		TextView dateView = (TextView) rowView.findViewById(R.id.dateValueField);
		TextView numDaysView = (TextView) rowView.findViewById(R.id.numDaysValueField);

		Treatment treatment = values.get(position);
		if (treatment.getStartingDate() == null) {
			dateView.setText("");
		} else {
			dateView.setText(treatment.getStartingDateString());
		}
		numDaysView.setText("" + treatment.getNumDays());

		return rowView;
	}
}
