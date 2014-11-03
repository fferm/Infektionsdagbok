package se.fermitet.android.infektionsdagbok.activity;

import se.fermitet.android.infektionsdagbok.model.ModelObjectBase;
import se.fermitet.android.infektionsdagbok.views.InfektionsdagbokDetailView;
import se.fermitet.android.infektionsdagbok.views.InfektionsdagbokDetailView.OnDetailButtonsPressedListener;
import android.content.Intent;
import android.os.Bundle;

@SuppressWarnings("rawtypes")
public class InfektionsdagbokDetailActivity<VIEW extends InfektionsdagbokDetailView, ITEM extends ModelObjectBase> extends InfektionsdagbokActivity<VIEW> {

	public InfektionsdagbokDetailActivity(int viewLayoutId) {
		super(viewLayoutId);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);

			Intent intent = getIntent();
			ITEM toEdit = (ITEM) intent.getSerializableExtra(InfektionsdagbokMasterActivity.EXTRA_NAME_ITEM_TO_EDIT);
			if (toEdit != null) {
				view.setModel(toEdit);
			}
		} catch (Exception e) {
			view.handleException(e);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onStart() {
		super.onStart();

		view.setOnDetailButtonsPressedListener(new OnDetailButtonsPressedListener<ITEM>() {

			@Override
			public void onSavePressed(ITEM item) throws Exception {
				getLocalApplication().getModelManager().save(item);
				finish();
			}

			@Override
			public void onCancelPressed() throws Exception {
				finish();
			}

			@Override
			public void onDeletePressed(ITEM item) throws Exception {
				getLocalApplication().getModelManager().delete(item);
				finish();
			}
		});
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onStop() {
		super.onStop();

		view.setOnDetailButtonsPressedListener(null);
	}


}
