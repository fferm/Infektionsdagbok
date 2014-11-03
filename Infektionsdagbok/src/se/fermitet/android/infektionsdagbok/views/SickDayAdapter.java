package se.fermitet.android.infektionsdagbok.views;

import java.util.List;

import se.fermitet.android.infektionsdagbok.R;
import se.fermitet.android.infektionsdagbok.model.SickDay;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

public class SickDayAdapter extends InfektionsdagbokListAdapter<SickDay> {

	public SickDayAdapter(Context context, List<SickDay> values) {
		super(context, R.layout.sick_day_item, values);
	}

	@Override
	protected void setupRowView(View rowView, SickDay item) {
		setValue(rowView, R.id.startValueField, item.getStartString());
		setValue(rowView, R.id.endValueField, item.getEndString());
	}

	private void setValue(View rowView, int fieldID, String txt) {
		TextView tv = (TextView) rowView.findViewById(fieldID);
		if (txt == null) {
			tv.setText("");
		} else {
			tv.setText(txt);
		}
	}


}

