package se.fermitet.android.infektionsdagbok.activity;

import se.fermitet.android.infektionsdagbok.R;
import se.fermitet.android.infektionsdagbok.model.Treatment;
import se.fermitet.android.infektionsdagbok.views.TreatmentDetailView;
import se.fermitet.android.infektionsdagbok.views.TreatmentDetailView.OnButtonPressedListener;
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
			Treatment toEdit = (Treatment) intent.getSerializableExtra(TreatmentMasterActivity.EXTRA_NAME_TREATMENT);
			if (toEdit != null) {
				view.selectTreatment(toEdit);
			}
		} catch (Exception e) {
			view.handleException(e);
		}
	}

	@Override
	protected void onStart() {
		super.onStart();

		view.setOnButtonPressedListener(new OnButtonPressedListener() {
			@Override
			public void onSavePressed(Treatment treatment) throws Exception {
				getLocalApplication().getModelManager().save(treatment);
				finish();
			}

			@Override
			public void onCancelPressed() throws Exception {
				finish();
			}

			@Override
			public void onDeletePressed(Treatment treatment) throws Exception {
				getLocalApplication().getModelManager().delete(treatment);
				finish();
			}
		});
	}

	@Override
	protected void onStop() {
		super.onStop();

		view.setOnButtonPressedListener(null);
	}


}
