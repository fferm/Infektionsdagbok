package se.fermitet.android.infektionsdagbok.views;

import java.util.List;

import se.fermitet.android.infektionsdagbok.R;
import se.fermitet.android.infektionsdagbok.model.Treatment;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

public class TreatmentAdapter extends InfektionsdagbokListAdapter<Treatment> {


	public TreatmentAdapter(Context context, List<Treatment> values) {
		super(context, R.layout.treatment_item, values);
	}

	@Override
	protected void setupRowView(View rowView, Treatment item) {
		setDateText(rowView, item);
		setNumDaysText(rowView, item);
	}

	private void setNumDaysText(View rowView, Treatment item) {
		TextView numDaysView = (TextView) rowView.findViewById(R.id.numDaysValueField);
		if (item.getNumDays() == null) {
			numDaysView.setText("");
		} else {
			numDaysView.setText("" + item.getNumDays());
		}
	}

	private void setDateText(View rowView, Treatment item) {
		TextView dateView = (TextView) rowView.findViewById(R.id.dateValueField);
		if (item.getStartingDate() == null) {
			dateView.setText("");
		} else {
			dateView.setText(item.getStartingDateString());
		}
	}





}
