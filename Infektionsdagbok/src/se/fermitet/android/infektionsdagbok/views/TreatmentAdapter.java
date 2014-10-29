package se.fermitet.android.infektionsdagbok.views;

import java.util.List;

import se.fermitet.android.infektionsdagbok.R;
import se.fermitet.android.infektionsdagbok.model.Treatment;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class TreatmentAdapter extends ArrayAdapter<Treatment> {

	private List<Treatment> values;
	private int selectedItem;

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

		if (treatment.getNumDays() == null) {
			numDaysView.setText("");
		} else {
			numDaysView.setText("" + treatment.getNumDays());
		}

		highlightItem(position, rowView);

		return rowView;
	}

	private void highlightItem(int position, View result) {

		TextView dateView = (TextView) result.findViewById(R.id.dateValueField);
		TextView numDaysView = (TextView) result.findViewById(R.id.numDaysValueField);

		if(position == selectedItem) {
			// you can define your own color of selected item here
			dateView.setBackgroundColor(Color.BLUE);
		} else {
			// you can define your own default selector here
			dateView.setBackgroundColor(Color.WHITE);
		}
	}

	public void setSelectedItem(int selectedItem) {
		this.selectedItem = selectedItem;
	}


}
