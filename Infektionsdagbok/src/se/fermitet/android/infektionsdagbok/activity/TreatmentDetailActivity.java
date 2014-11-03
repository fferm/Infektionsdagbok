package se.fermitet.android.infektionsdagbok.activity;

import se.fermitet.android.infektionsdagbok.R;
import se.fermitet.android.infektionsdagbok.model.Treatment;
import se.fermitet.android.infektionsdagbok.views.InfektionsdagbokDetailView.OnDetailButtonsPressedListener;
import se.fermitet.android.infektionsdagbok.views.TreatmentDetailView;
import android.content.Intent;
import android.os.Bundle;

public class TreatmentDetailActivity extends InfektionsdagbokActivity<TreatmentDetailView>{

	public TreatmentDetailActivity() {
		super(R.layout.treatment_detail_view);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);

			Intent intent = getIntent();
			Treatment toEdit = (Treatment) intent.getSerializableExtra(InfektionsdagbokMasterActivity.EXTRA_NAME_ITEM_TO_EDIT);
			if (toEdit != null) {
				view.setModel(toEdit);
			}
		} catch (Exception e) {
			view.handleException(e);
		}
	}

	@Override
	protected void onStart() {
		super.onStart();

		view.setOnDetailButtonsPressedListener(new OnDetailButtonsPressedListener<Treatment>() {

			@Override
			public void onSavePressed(Treatment item) throws Exception {
				getLocalApplication().getModelManager().save(item);
				finish();
			}

			@Override
			public void onCancelPressed() throws Exception {
				finish();
			}

			@Override
			public void onDeletePressed(Treatment item) throws Exception {
				getLocalApplication().getModelManager().delete(item);
				finish();
			}
		});
	}

	@Override
	protected void onStop() {
		super.onStop();

		view.setOnDetailButtonsPressedListener(null);
	}


}
