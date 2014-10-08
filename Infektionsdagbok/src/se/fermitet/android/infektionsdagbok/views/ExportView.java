package se.fermitet.android.infektionsdagbok.views;

import java.text.DateFormat;

import org.joda.time.DateTime;

import se.fermitet.android.infektionsdagbok.R;
import android.app.DatePickerDialog;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ExportView extends RelativeLayout implements View.OnClickListener {

	private TextView startDateTV;
	private TextView endDateTV;
	private DateTime startDate;
	private DateTime endDate;

	public ExportView(Context context, AttributeSet attrs) {
		super(context, attrs);

		startDate = DateTime.now().withDayOfYear(1);
		endDate = DateTime.now();
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		attachWidgets();
		setupWidgets();
	}

	private void attachWidgets() {
		startDateTV = (TextView) findViewById(R.id.startDateTV);
		endDateTV = (TextView) findViewById(R.id.endDateTV);
	}

	private void setupWidgets() {
		startDateTV.setOnClickListener(this);
		endDateTV.setOnClickListener(this);
		syncDateTexts();
	}


	public DateTime getStartDate() {
		return startDate;
	}

	public DateTime getEndDate() {
		return endDate;
	}

	@Override
	public void onClick(View v) {
		if (v == startDateTV || v == endDateTV) {
			handleDateClicks(v);
		}
	}

	private void handleDateClicks(View v) {
		DateTime initial = null;
		DatePickerDialog.OnDateSetListener listener = null;

		if (v == startDateTV) {
			initial = getStartDate();
			listener = new DatePickerDialog.OnDateSetListener() {

				@Override
				public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
					System.out.println("!!!! " + year + "-" + monthOfYear + "-" + dayOfMonth);
					ExportView.this.startDate = new DateTime(year, monthOfYear, dayOfMonth, 0, 0);
					syncDateTexts();
				}
			};
		} else if (v == endDateTV) {
			initial = getEndDate();
			listener = new DatePickerDialog.OnDateSetListener() {

				@Override
				public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
					ExportView.this.endDate = new DateTime(year, monthOfYear, dayOfMonth, 0, 0);
					syncDateTexts();
				}
			};
		}
		System.out.println("!!!! initial: " + initial);

		DatePickerDialog picker = new DatePickerDialog(getContext(), listener, initial.year().get(), initial.monthOfYear().get() - 1, initial.dayOfMonth().get());
		picker.show();
	}

	private void syncDateTexts() {
		DateFormat format = DateFormat.getDateInstance(DateFormat.SHORT);

		startDateTV.setText(format.format(startDate.toDate()));
		endDateTV.setText(format.format(endDate.toDate()));
	}


}
