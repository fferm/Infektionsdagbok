package se.fermitet.android.infektionsdagbok.activity;

import se.fermitet.android.infektionsdagbok.model.ModelObjectBase;
import se.fermitet.android.infektionsdagbok.views.InfektionsdagbokListAdapter;
import se.fermitet.android.infektionsdagbok.views.InfektionsdagbokMasterView;
import android.os.Bundle;

@SuppressWarnings("rawtypes")
public abstract class MasterActivity<
	VIEW extends InfektionsdagbokMasterView,
	ITEM extends ModelObjectBase,
	ADAPTER extends InfektionsdagbokListAdapter<ITEM>> extends InfektionsdagbokActivity<VIEW> {

	public MasterActivity(int viewLayoutId) {
		super(viewLayoutId);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
//			fillWithTestData();

			syncListViewDataWithStored();
		} catch (Exception e) {
			view.handleException(e);
		}
	}

	protected abstract ADAPTER createListAdapter() throws Exception;

	@SuppressWarnings("unchecked")
	public void syncListViewDataWithStored() throws Exception {
		ADAPTER adapter = createListAdapter();

		view.setAdapter(adapter);
	}



}
