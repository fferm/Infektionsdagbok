package se.fermitet.android.infektionsdagbok.views;

import org.joda.time.DateTime;

import se.fermitet.android.infektionsdagbok.R;
import se.fermitet.android.infektionsdagbok.model.Treatment;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class TreatmentDetailView extends InfektionsdagbokRelativeLayoutView implements OnDateSetListener {

	private TextView startTV;
	private EditText numDaysEdit;
	private EditText medicineEdit;
	private EditText infectionTypeEdit;
	private ImageButton saveBTN;
/*	private ImageButton newBTN;
	private ImageButton deleteBTN;*/
	private DatePickerDialog dp;

	private Treatment model;

	private OnButtonPressedListener onButtonPressedListener;

	public TreatmentDetailView(Context context, AttributeSet attrs) {
		super(context, attrs);
		model = new Treatment();
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		try {
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
/*		newBTN = (ImageButton) findViewById(R.id.newBTN);
		deleteBTN = (ImageButton) findViewById(R.id.deleteBTN);*/
	}

	private void setupWidgets() throws Exception {
		setupStartTV();
		setupMedicineEdit();
		setupInfectionTypeEdit();
		setupNumDaysEdit();
		setupSaveBTN();
/*		setupNewBTN();
		setupDeleteBTN();*/
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

/*	private void setupNewBTN() throws Exception {
		newBTN.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					TreatmentDetailView.this.selectTreatment(new Treatment());
				} catch (Exception e) {
					handleException(e);
				}
			}
		});
	}

	private void setupDeleteBTN() throws Exception{
		deleteBTN.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					TreatmentDetailView.this.onButtonsPressedListener.onDeletePressed(getModel());
					reset();
				} catch (Exception e) {
					handleException(e);
				}
			}
		});
	}*/


	public void selectTreatment(Treatment treatment) throws Exception {
		this.model = treatment;

		syncUIWithModel();
	}

	private void reset() throws Exception {
		this.model = new Treatment();
		syncUIWithModel();
	}

	private void syncUIWithModel() throws Exception {
		if (model.getStartingDate() == null) {
			startTV.setText(null);
		} else {
			startTV.setText(model.getStartingDateString());
		}

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

	private void dateClicked() throws Exception {
		DateTime startDate = model.getStartingDate();
		if (startDate == null) startDate = DateTime.now();

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

	public interface OnButtonPressedListener {
		public void onSavePressed(Treatment treatment) throws Exception;
	}

	public void setOnButtonPressedListener(OnButtonPressedListener listener) {
		this.onButtonPressedListener = listener;
	}

}
