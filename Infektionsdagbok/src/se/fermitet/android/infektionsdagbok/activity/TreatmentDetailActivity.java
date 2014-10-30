package se.fermitet.android.infektionsdagbok.activity;

import se.fermitet.android.infektionsdagbok.R;
import se.fermitet.android.infektionsdagbok.model.Treatment;
import se.fermitet.android.infektionsdagbok.views.TreatmentDetailView;
import se.fermitet.android.infektionsdagbok.views.TreatmentDetailView.OnButtonPressedListener;
import android.os.Bundle;

public class TreatmentDetailActivity extends InfektionsdagbokActivity<TreatmentDetailView>{

	public TreatmentDetailActivity() {
		super(R.layout.treatment_detail_view);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onStart() {
		super.onStart();

		view.setOnButtonPressedListener(new OnButtonPressedListener() {
			@Override
			public void onSavePressed(Treatment treatment) throws Exception {
				try {
					getLocalApplication().getModelManager().saveTreatment(treatment);
					finish();
				} catch (Exception e) {
					view.handleException(e);
				}
			}

			@Override
			public void onCancelPressed() throws Exception {
				try {
					finish();
				} catch (Exception e) {
					view.handleException(e);
				}
			}
		});
	}

	@Override
	protected void onStop() {
		super.onStop();

		view.setOnButtonPressedListener(null);
	}


}
