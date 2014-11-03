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
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class TreatmentDetailView extends InfektionsdagbokDetailView {

	private TextView startHeader;
	private DateTextView startTV;
	private EditText numDaysEdit;
	private EditText medicineEdit;
	private EditText infectionTypeEdit;
	private ImageButton saveBTN;
	private ImageButton cancelBTN;
	private ImageButton deleteBTN;

	private Treatment model;

	private OnButtonPressedListener onButtonPressedListener;

	public TreatmentDetailView(Context context, AttributeSet attrs) {
		super(context, attrs, "Behandling");
		model = new Treatment();
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
		saveBTN = (ImageButton) findViewById(R.id.saveBTN);
		cancelBTN = (ImageButton) findViewById(R.id.cancelBTN);
		deleteBTN = (ImageButton) findViewById(R.id.deleteBTN);
	}

	private void setupWidgets() throws Exception {
		setupStartTV();
		setupMedicineEdit();
		setupInfectionTypeEdit();
		setupNumDaysEdit();
		setupSaveBTN();
		setupCancelBTN();
		setupDeleteBTN();
	}

	private void setupStartTV() {
		startTV.addAdditionalViewToOpenDialogWhenClicked(startHeader);
		startTV.setOnModelChangedListener(new OnModelChangedListener() {
			@Override
			public void onDateChangedTo(LocalDate newDate) throws Exception {
				TreatmentDetailView.this.model.setStartingDate(startTV.getModel());
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
					TreatmentDetailView.this.model.setMedicine(newMedicine);
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
					TreatmentDetailView.this.model.setInfectionType(newInfectionType);
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
							TreatmentDetailView.this.model.setNumDays(null);
						} else {
							TreatmentDetailView.this.model.setNumDays(Integer.valueOf(newNumDaysText));
						}
					}
				} catch (Exception e) {
					handleException(e);
				}
			}
		});
	}

	private void setupSaveBTN() throws Exception {
		saveBTN.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					TreatmentDetailView.this.onButtonPressedListener.onSavePressed(getModel());
				} catch (Exception e) {
					handleException(e);
				}
			}
		});
	}

	private void setupCancelBTN() throws Exception {
		cancelBTN.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					TreatmentDetailView.this.onButtonPressedListener.onCancelPressed();
				} catch (Exception e) {
					handleException(e);
				}
			}
		});
	}


	private void setupDeleteBTN() throws Exception{
		deleteBTN.setEnabled(false);
		deleteBTN.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					TreatmentDetailView.this.onButtonPressedListener.onDeletePressed(getModel());
				} catch (Exception e) {
					handleException(e);
				}
			}
		});
	}


	public void selectTreatment(Treatment treatment) throws Exception {
		this.model = treatment;
		deleteBTN.setEnabled(true);

		syncUIWithModel();
	}

	private void syncUIWithModel() throws Exception {
		startTV.setModel(model.getStartingDate());

		if (model.getNumDays() == null) {
			numDaysEdit.setText(null);
		} else {
			numDaysEdit.setText(model.getNumDays().toString());
		}

		if (model.getMedicine() == null) {
			medicineEdit.setText(null);
		} else {
			medicineEdit.setText(model.getMedicine());
		}

		if (model.getInfectionType() == null) {
			infectionTypeEdit.setText(null);
		} else {
			infectionTypeEdit.setText(model.getInfectionType());
		}
	}

	public Treatment getModel() throws Exception {
		return this.model;
	}

	public interface OnButtonPressedListener {
		public void onSavePressed(Treatment treatment) throws Exception;
		public void onCancelPressed() throws Exception;
		public void onDeletePressed(Treatment treatment) throws Exception;
	}

	public void setOnButtonPressedListener(OnButtonPressedListener listener) {
		this.onButtonPressedListener = listener;
	}

}
