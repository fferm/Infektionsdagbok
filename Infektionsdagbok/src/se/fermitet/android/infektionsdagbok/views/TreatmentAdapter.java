package se.fermitet.android.infektionsdagbok.views;

import java.util.List;

import se.fermitet.android.infektionsdagbok.R;
import se.fermitet.android.infektionsdagbok.model.Treatment;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class TreatmentAdapter extends ArrayAdapter<Treatment> {

	private List<Treatment> values;
	private Integer selectedPosition;
	private LayoutInflater inflater;

	public TreatmentAdapter(Context context, List<Treatment> values) {
		super(context, R.layout.treatment_item, values);
		this.values = values;

		this.inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView = inflater.inflate(R.layout.treatment_item, parent, false);

		Treatment treatment = values.get(position);

		setDateText(rowView, treatment);
		setNumDaysText(rowView, treatment);

		highlightItemIfSelected(position, rowView);

		return rowView;
	}

	protected void setNumDaysText(View rowView, Treatment treatment) {
		TextView numDaysView = (TextView) rowView.findViewById(R.id.numDaysValueField);
		if (treatment.getNumDays() == null) {
			numDaysView.setText("");
		} else {
			numDaysView.setText("" + treatment.getNumDays());
		}
	}

	protected void setDateText(View rowView, Treatment treatment) {
		TextView dateView = (TextView) rowView.findViewById(R.id.dateValueField);
		if (treatment.getStartingDate() == null) {
			dateView.setText("");
		} else {
			dateView.setText(treatment.getStartingDateString());
		}
	}

	private void highlightItemIfSelected(int position, View view) {
		if (view instanceof ViewGroup) {
			highlightChildren(position, (ViewGroup) view);
		} else {
			if(selectedPosition != null && position == selectedPosition) {
				view.setBackground(getContext().getResources().getDrawable(R.drawable.background_selected_item));
			} else {
				view.setBackground(getContext().getResources().getDrawable(R.drawable.background_not_selected_item));
			}
		}
	}

	private void highlightChildren(int position, ViewGroup viewGroup) {
		for (int i = 0; i < viewGroup.getChildCount(); i++) {
			View child = viewGroup.getChildAt(i);
			
			highlightItemIfSelected(position, child);
		}
	}

	public void setSelectedPosition(Integer selectedPosition) {
		this.selectedPosition = selectedPosition;
	}
	
	public Integer getSelectedPosition() {
		return this.selectedPosition;
	}


}
