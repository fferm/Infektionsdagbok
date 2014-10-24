package se.fermitet.android.infektionsdagbok.views;

import se.fermitet.android.infektionsdagbok.R;
import se.fermitet.android.infektionsdagbok.model.Treatment;
import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.TextView;

public class TreatmentSingleEditView extends InfektionsdagbokRelativeLayoutView {

	private TextView startEdit;
	private EditText numDaysEdit;
	private EditText medicineEdit;
	private EditText infectionTypeEdit;

	public TreatmentSingleEditView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onFinishInflate() {
		try {
			super.onFinishInflate();

			Activity ctx = (Activity) getContext();
			LayoutInflater inflater = ctx.getLayoutInflater();
			inflater.inflate(R.layout.single_treatment_view, this);

			attachWidgets();
			setupWidgets();
		} catch (Exception e) {
			handleException(e);
		}
	}

	private void attachWidgets() throws Exception {
		startEdit = (TextView) findViewById(R.id.startTV);
		numDaysEdit = (EditText) findViewById(R.id.numDaysEdit);
		medicineEdit = (EditText) findViewById(R.id.medicineEdit);
		infectionTypeEdit = (EditText) findViewById(R.id.infectionTypeEdit);
	}

	private void setupWidgets() throws Exception {
		// TODO Auto-generated method stub

	}

	public void selectTreatment(Treatment treatment) throws Exception {
		if (treatment.getStartingDate() == null) {
			startEdit.setText(null);
		} else {
			startEdit.setText(treatment.getStartingDateString());
		}
		
		numDaysEdit.setText("" + treatment.getNumDays());
		
		if (treatment.getMedicine() == null) {
			medicineEdit.setText(null);
		} else {
			medicineEdit.setText(treatment.getMedicine());
		}
		
		if (treatment.getInfectionType() == null) {
			infectionTypeEdit.setText(null);
		} else {
			infectionTypeEdit.setText(treatment.getInfectionType());
		}
	}
}
