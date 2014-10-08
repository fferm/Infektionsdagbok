package se.fermitet.android.infektionsdagbok.views;

import java.text.DateFormat;

import org.joda.time.DateTime;

import se.fermitet.android.infektionsdagbok.R;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ExportView extends RelativeLayout {

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
		DateFormat format = DateFormat.getDateInstance(DateFormat.SHORT);

		startDateTV.setText(format.format(startDate.toDate()));
		endDateTV.setText(format.format(endDate.toDate()));
	}

	public DateTime getStartDate() {
		return startDate;
	}

	public DateTime getEndDate() {
		return endDate;
	}

}
