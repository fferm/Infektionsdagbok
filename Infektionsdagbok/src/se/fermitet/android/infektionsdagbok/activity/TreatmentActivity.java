package se.fermitet.android.infektionsdagbok.activity;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.joda.time.DateTime;

import se.fermitet.android.infektionsdagbok.R;
import se.fermitet.android.infektionsdagbok.model.Treatment;
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
			fillWithTestData();

			syncListViewDataWithStored();
		} catch (Exception e) {
			handleException(e);
		}
	}

	private void syncListViewDataWithStored() throws Exception {
		Collection<Treatment> allTreatments = getLocalApplication().getModelManager().getAllTreatments();

		TreatmentAdapter adapter = new TreatmentAdapter(this, new ArrayList<Treatment>(allTreatments));

		view.setAdapter(adapter);
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
		dateView.setText(DateFormat.getDateInstance(DateFormat.SHORT).format(treatment.getStartingDate().toDate()));
		numDaysView.setText("" + treatment.getNumDays());

		return rowView;
	}
}
