package se.fermitet.android.infektionsdagbok.views;

import org.joda.time.DateTime;

import se.fermitet.android.infektionsdagbok.R;
import se.fermitet.android.infektionsdagbok.model.Treatment;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

public class TreatmentSingleEditView extends InfektionsdagbokRelativeLayoutView implements OnDateSetListener {

	private TextView startTV;
	private EditText numDaysEdit;
	private EditText medicineEdit;
	private EditText infectionTypeEdit;
	private DatePickerDialog dp;

	private Treatment model;

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
	}

	private void setupWidgets() throws Exception {
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

}
