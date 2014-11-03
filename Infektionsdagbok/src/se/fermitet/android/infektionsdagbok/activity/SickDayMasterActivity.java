package se.fermitet.android.infektionsdagbok.activity;

import java.util.ArrayList;
import java.util.Collection;

import se.fermitet.android.infektionsdagbok.R;
import se.fermitet.android.infektionsdagbok.model.SickDay;
import se.fermitet.android.infektionsdagbok.views.SickDayAdapter;
import se.fermitet.android.infektionsdagbok.views.SickDayMasterView;


public class SickDayMasterActivity extends MasterActivity<SickDayMasterView, SickDay, SickDayAdapter>{

	public SickDayMasterActivity() {
		super(R.layout.sick_day_master_view);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected SickDayAdapter createListAdapter() throws Exception {
		Collection<SickDay> unsorted = getLocalApplication().getModelManager().getAllSickDays().values();
		return new SickDayAdapter(this, new ArrayList<SickDay>(unsorted));
	}

}
