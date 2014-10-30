package se.fermitet.android.infektionsdagbok.activity;

import se.fermitet.android.infektionsdagbok.R;
import se.fermitet.android.infektionsdagbok.model.Treatment;
import se.fermitet.android.infektionsdagbok.views.TreatmentAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

public abstract class TreatmentMasterActivityTest extends ActivityTestWithSolo<TreatmentMasterActivity>{

	public TreatmentMasterActivityTest() {
		super(TreatmentMasterActivity.class);
	}


	protected TreatmentAdapter getListAdapter() {
		TreatmentMasterActivity activity = getActivity();
		ListView listView = (ListView) activity.view.findViewById(R.id.treatmentListView);
		return (TreatmentAdapter) listView.getAdapter();
	}

	protected boolean adapterContains(Treatment treatment) {
		ListAdapter adapter = getListAdapter();
		for(int i = 0; i < adapter.getCount(); i++) {
			Treatment inAdapter = (Treatment) adapter.getItem(i);

			if(inAdapter.equals(treatment)) return true;
		}
		return false;
	}



}
