package se.fermitet.android.infektionsdagbok.views;

import java.text.DateFormat;

import se.fermitet.android.infektionsdagbok.R;
import se.fermitet.android.infektionsdagbok.model.Treatment;
import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SingleTreatmentEditorView extends RelativeLayout {

	private TextView startEdit;
	private EditText numDaysEdit;
	private EditText medicineEdit;
	private EditText infectionTypeEdit;

	public SingleTreatmentEditorView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();

		Activity ctx = (Activity) getContext();
		LayoutInflater inflater = ctx.getLayoutInflater();
		inflater.inflate(R.layout.single_treatment_view, this);

		attachWidgets();
		setupWidgets();
	}

	private void attachWidgets() {
		startEdit = (TextView) findViewById(R.id.startTV);
		numDaysEdit = (EditText) findViewById(R.id.numDaysEdit);
		medicineEdit = (EditText) findViewById(R.id.medicineEdit);
		infectionTypeEdit = (EditText) findViewById(R.id.infectionTypeEdit);
	}

	private void setupWidgets() {
		// TODO Auto-generated method stub

	}

	public void selectTreatment(Treatment treatment) {
		DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);

		startEdit.setText(df.format(treatment.getStartingDate().toDate()));
		numDaysEdit.setText("" + treatment.getNumDays());
		medicineEdit.setText(treatment.getMedicine());
		infectionTypeEdit.setText(treatment.getInfectionType());
	}
}
