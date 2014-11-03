package se.fermitet.android.infektionsdagbok.activity;

import se.fermitet.android.infektionsdagbok.R;
import se.fermitet.android.infektionsdagbok.model.SickDay;
import se.fermitet.android.infektionsdagbok.views.SickDayDetailView;


public class SickDayDetailActivity extends InfektionsdagbokDetailActivity<SickDayDetailView, SickDay>{

	public SickDayDetailActivity() {
		super(R.layout.sick_day_detail_view);
	}

}
