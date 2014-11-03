package se.fermitet.android.infektionsdagbok.views;

import org.joda.time.LocalDate;

import se.fermitet.android.infektionsdagbok.R;
import se.fermitet.android.infektionsdagbok.model.Treatment;
import se.fermitet.android.infektionsdagbok.widget.DateTextView;
import se.fermitet.android.infektionsdagbok.widget.DateTextView.OnModelChangedListener;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.TextView;

public class TreatmentDetailView extends InfektionsdagbokDetailView<Treatment> {

	private TextView startHeader;
	private DateTextView startTV;
	private EditText numDaysEdit;
	private EditText medicineEdit;
	private EditText infectionTypeEdit;

	public TreatmentDetailView(Context context, AttributeSet attrs) {
		super(context, attrs, "Behandling");
	}

	@Override
	protected Treatment createEmptyModel() {
		return new Treatment();
	}


	@Override
	protected void onFinishInflate() {
		try {
			attachWidgets();
			super.onFinishInflate();
			setupWidgets();
		} catch (Exception e) {
			handleException(e);
		}
	}

	private void attachWidgets() throws Exception {
		startHeader = (TextView) findViewById(R.id.startHeader);
		startTV = (DateTextView) findViewById(R.id.startTV);
		numDaysEdit = (EditText) findViewById(R.id.numDaysEdit);
		medicineEdit = (EditText) findViewById(R.id.medicineEdit);
		infectionTypeEdit = (EditText) findViewById(R.id.infectionTypeEdit);
	}

	private void setupWidgets() throws Exception {
		setupStartTV();
		setupMedicineEdit();
		setupInfectionTypeEdit();
		setupNumDaysEdit();
	}

	private void setupStartTV() {
		startTV.addAdditionalViewToOpenDialogWhenClicked(startHeader);
		startTV.setOnModelChangedListener(new OnModelChangedListener() {
			@Override
			public void onDateChangedTo(LocalDate newDate) throws Exception {
				TreatmentDetailView.this.getModel().setStartingDate(startTV.getModel());
			}
		});
	}

	private void setupMedicineEdit() throws Exception {
		medicineEdit.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

			@Override
			public void afterTextChanged(Editable s) {
				try {
					String newMedicine = medicineEdit.getText().toString();
					TreatmentDetailView.this.getModel().setMedicine(newMedicine);
				} catch (Exception e) {
					handleException(e);
				}
			}
		});
	}

	private void setupInfectionTypeEdit() throws Exception {
		infectionTypeEdit.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,	int after) {}

			@Override
			public void afterTextChanged(Editable s) {
				try {
					String newInfectionType = infectionTypeEdit.getText().toString();
					TreatmentDetailView.this.getModel().setInfectionType(newInfectionType);
				} catch (Exception e) {
					handleException(e);
				}
			}
		});
	}

	private void setupNumDaysEdit() throws Exception {
		numDaysEdit.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,	int after) {}

			@Override
			public void afterTextChanged(Editable s) {
				try {
					String newNumDaysText = numDaysEdit.getText().toString();
					if (newNumDaysText != null && newNumDaysText.length() > 0) {
						if (newNumDaysText.equals("null")) {
							TreatmentDetailView.this.getModel().setNumDays(null);
						} else {
							TreatmentDetailView.this.getModel().setNumDays(Integer.valueOf(newNumDaysText));
						}
					}
				} catch (Exception e) {
					handleException(e);
				}
			}
		});
	}


	@Override
	protected void syncUIWithModel() throws Exception {
		startTV.setModel(getModel().getStartingDate());

		if (getModel().getNumDays() == null) {
			numDaysEdit.setText(null);
		} else {
			numDaysEdit.setText(getModel().getNumDays().toString());
		}

		if (getModel().getMedicine() == null) {
			medicineEdit.setText(null);
		} else {
			medicineEdit.setText(getModel().getMedicine());
		}

		if (getModel().getInfectionType() == null) {
			infectionTypeEdit.setText(null);
		} else {
			infectionTypeEdit.setText(getModel().getInfectionType());
		}
	}
}
