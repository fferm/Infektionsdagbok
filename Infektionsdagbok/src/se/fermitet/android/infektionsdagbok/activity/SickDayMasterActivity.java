package se.fermitet.android.infektionsdagbok.activity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.joda.time.LocalDate;

import se.fermitet.android.infektionsdagbok.R;
import se.fermitet.android.infektionsdagbok.model.SickDay;
import se.fermitet.android.infektionsdagbok.views.SickDayAdapter;
import se.fermitet.android.infektionsdagbok.views.SickDayMasterView;


public class SickDayMasterActivity extends MasterActivity<SickDayMasterView, SickDay, SickDayAdapter>{

	public SickDayMasterActivity() {
		super(R.layout.sick_day_master_view, SickDayDetailActivity.class);
	}

	@Override
	protected SickDayAdapter createListAdapter() throws Exception {
		Collection<SickDay> unsorted = getLocalApplication().getModelManager().getAllSickDays().values();
		return new SickDayAdapter(this, sortedListOfSickDays(unsorted));
	}

	private List<SickDay> sortedListOfSickDays(Collection<SickDay> unsorted) {
		List<SickDay> list = new ArrayList<SickDay>(unsorted);

		Collections.sort(list, new Comparator<SickDay>() {
			@Override
			public int compare(SickDay lhs, SickDay rhs) {
				LocalDate lhsDate = lhs.getStart();
				LocalDate rhsDate = rhs.getStart();

				if (lhsDate == null) return 1;
				if (rhsDate == null) return -1;

				if (lhsDate.isBefore(rhsDate)) return 1;
				else if (lhsDate.equals(rhsDate)) return 0;
				else return -1;
			}
		});

		return list;
	}


}
