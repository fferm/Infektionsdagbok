package se.fermitet.android.infektionsdagbok.views;

import java.util.List;

import se.fermitet.android.infektionsdagbok.R;
import se.fermitet.android.infektionsdagbok.model.SickDay;
import android.content.Context;
import android.view.View;

public class SickDayAdapter extends InfektionsdagbokListAdapter<SickDay> {

	public SickDayAdapter(Context context, List<SickDay> values) {
		super(context, R.layout.sick_day_item, values);
	}

	@Override
	protected void setupRowView(View rowView, SickDay item) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

}
