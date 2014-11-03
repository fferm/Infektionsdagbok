package se.fermitet.android.infektionsdagbok.activity;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.joda.time.LocalDate;

import se.fermitet.android.infektionsdagbok.R;
import se.fermitet.android.infektionsdagbok.model.Treatment;
import se.fermitet.android.infektionsdagbok.views.TreatmentAdapter;
import se.fermitet.android.infektionsdagbok.views.TreatmentMasterView;

public class TreatmentMasterActivity extends InfektionsdagbokMasterActivity<TreatmentMasterView, Treatment, TreatmentAdapter> {

	public TreatmentMasterActivity() {
		super(R.layout.treatment_master_view, TreatmentDetailActivity.class);
	}

	@Override
	protected TreatmentAdapter createListAdapter() throws Exception {
		Collection<Treatment> unsorted = getLocalApplication().getModelManager().getAllTreatments().values();
		return new TreatmentAdapter(this,  sortedListOfTreatments(unsorted));
	}

	private List<Treatment> sortedListOfTreatments(Collection<Treatment> unsorted) {
		List<Treatment> list = new ArrayList<Treatment>(unsorted);

		Collections.sort(list, new Comparator<Treatment>() {
			@Override
			public int compare(Treatment lhs, Treatment rhs) {
				LocalDate lhsDate = lhs.getStartingDate();
				LocalDate rhsDate = rhs.getStartingDate();

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

