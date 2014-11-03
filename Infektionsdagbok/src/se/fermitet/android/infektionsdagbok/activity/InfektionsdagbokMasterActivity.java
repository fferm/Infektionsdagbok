package se.fermitet.android.infektionsdagbok.activity;

import se.fermitet.android.infektionsdagbok.model.ModelObjectBase;
import se.fermitet.android.infektionsdagbok.views.InfektionsdagbokListAdapter;
import se.fermitet.android.infektionsdagbok.views.InfektionsdagbokMasterView;
import se.fermitet.android.infektionsdagbok.views.InfektionsdagbokMasterView.OnMasterButtonsPressedListener;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

@SuppressWarnings("rawtypes")
public abstract class InfektionsdagbokMasterActivity<
	VIEW extends InfektionsdagbokMasterView,
	ITEM extends ModelObjectBase,
	ADAPTER extends InfektionsdagbokListAdapter<ITEM>> extends InfektionsdagbokActivity<VIEW> {

	public static final int REQUEST_CODE_NEW = 0;
	public static final int REQUEST_CODE_EDIT = 1;
	public static final String EXTRA_NAME_ITEM_TO_EDIT = "ITEM";
	private Class<? extends Activity> detailActivityClass;

	protected abstract ADAPTER createListAdapter() throws Exception;

	public InfektionsdagbokMasterActivity(int viewLayoutId, Class<? extends Activity> detailActivityClass) {
		super(viewLayoutId);

		this.detailActivityClass = detailActivityClass;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			syncViewWithStoredData();
		} catch (Exception e) {
			view.handleException(e);
		}
	}


	@SuppressWarnings("unchecked")
	@Override
	protected void onStart() {
		super.onStart();

		view.setOnMasterButtonsPressedListener(new OnMasterButtonsPressedListener<ITEM>() {
			@Override
			public void onNewPressed() throws Exception {
				Intent newIntent = new Intent(InfektionsdagbokMasterActivity.this, detailActivityClass);
				startActivityForResult(newIntent, REQUEST_CODE_NEW);
			}

			@Override
			public void onDeletePressed(ITEM item) throws Exception {
				getLocalApplication().getModelManager().delete(item);
				syncViewWithStoredData();
			}

			@Override
			public void onEditPressed(ITEM item) throws Exception {
				Intent newIntent = new Intent(InfektionsdagbokMasterActivity.this, detailActivityClass);
				newIntent.putExtra(EXTRA_NAME_ITEM_TO_EDIT, item);
				startActivityForResult(newIntent, REQUEST_CODE_EDIT);
			}
		});
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onStop() {
		super.onStop();

		view.setOnMasterButtonsPressedListener(null);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		try {
			syncViewWithStoredData();
		} catch (Exception e) {
			view.handleException(e);
		}
	}


	@SuppressWarnings("unchecked")
	public void syncViewWithStoredData() throws Exception {
		ADAPTER adapter = createListAdapter();
		view.setAdapter(adapter);

		if (adapter.getSelectedItem() == null) view.handleButtonsEnablement(false);
		else view.handleButtonsEnablement(true);
	}



}
