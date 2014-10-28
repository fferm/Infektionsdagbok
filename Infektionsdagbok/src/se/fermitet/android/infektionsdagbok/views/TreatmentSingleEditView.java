package se.fermitet.android.infektionsdagbok.views;

import org.joda.time.DateTime;

import se.fermitet.android.infektionsdagbok.R;
import se.fermitet.android.infektionsdagbok.model.Treatment;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class TreatmentSingleEditView extends InfektionsdagbokRelativeLayoutView implements OnDateSetListener {

	private TextView startTV;
	private EditText numDaysEdit;
	private EditText medicineEdit;
	private EditText infectionTypeEdit;
	private ImageButton saveBTN;
	private DatePickerDialog dp;

	private Treatment model;

	private OnSavePressedListener onSavePressedListener;

	public TreatmentSingleEditView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onFinishInflate() {
		try {
			super.onFinishInflate();

			Activity ctx = (Activity) getContext();
			LayoutInflater inflater = ctx.getLayoutInflater();
			inflater.inflate(R.layout.treatment_single_edit_view, this);

			attachWidgets();
			setupWidgets();
		} catch (Exception e) {
			handleException(e);
		}
	}

	private void attachWidgets() throws Exception {
		startTV = (TextView) findViewById(R.id.startTV);
		numDaysEdit = (EditText) findViewById(R.id.numDaysEdit);
		medicineEdit = (EditText) findViewById(R.id.medicineEdit);
		infectionTypeEdit = (EditText) findViewById(R.id.infectionTypeEdit);
		saveBTN = (ImageButton) findViewById(R.id.saveBTN);
	}

	private void setupWidgets() throws Exception {
		setupStartTV();
		setupMedicineEdit();
		setupInfectionTypeEdit();
		setupNumDaysEdit();
		setupSaveBTN();
	}

	private void setupStartTV() throws Exception {
		startTV.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					dateClicked();
				} catch (Exception e) {
					handleException(e);
				}
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
					TreatmentSingleEditView.this.model.setMedicine(newMedicine);
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
					TreatmentSingleEditView.this.model.setInfectionType(newInfectionType);
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
						TreatmentSingleEditView.this.model.setNumDays(Integer.valueOf(newNumDaysText));
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
					TreatmentSingleEditView.this.onSavePressedListener.onSavePressed(getModel());
				} catch (Exception e) {
					handleException(e);
				}
			}
		});
	}


	public void selectTreatment(Treatment treatment) throws Exception {
		this.model = treatment;

		syncUIWithModel();
	}

	private void syncUIWithModel() throws Exception {
		if (model.getStartingDate() == null) {
			startTV.setText(null);
		} else {
			startTV.setText(model.getStartingDateString());
		}

		numDaysEdit.setText("" + model.getNumDays());

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

	private void dateClicked() throws Exception {
		DateTime startDate = model.getStartingDate();
		dp = new DatePickerDialog(getContext(), this, startDate.getYear(), startDate.getMonthOfYear() - 1, startDate.getDayOfMonth());
		dp.show();
	}

	public DatePickerDialog getDatePickerDialog() throws Exception {
		return dp;
	}

	public Treatment getModel() throws Exception {
		return this.model;
	}

	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
		try {
			DateTime newDate = DateTime.now().withYear(year).withMonthOfYear(monthOfYear + 1).withDayOfMonth(dayOfMonth);
			model.setStartingDate(newDate);

			syncUIWithModel();
		} catch (Exception e) {
			handleException(e);
		}
	}

	public interface OnSavePressedListener {
		public void onSavePressed(Treatment treatment) throws Exception;
	}

	public void setOnSavePressedListener(OnSavePressedListener listener) {
		this.onSavePressedListener = listener;
	}

}
