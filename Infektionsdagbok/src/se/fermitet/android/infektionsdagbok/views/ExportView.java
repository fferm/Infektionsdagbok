package se.fermitet.android.infektionsdagbok.views;

import java.util.List;

import org.joda.time.DateTime;

import se.fermitet.android.infektionsdagbok.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;

public class ExportView extends RelativeLayout implements View.OnClickListener {

	private Button exportBTN;
	private Spinner yearSpinner;

	private OnExportCommandListener listener = null;
	private List<Integer> yearsToShow;

	public ExportView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		attachWidgets();
		setupWidgets();
	}

	public void setYearsToShow(List<Integer> yearsToShow) {
		this.yearsToShow = yearsToShow;
		setupSpinner();
	}

	private void attachWidgets() {
		exportBTN = (Button) findViewById(R.id.exportBTN);
		yearSpinner = (Spinner) findViewById(R.id.yearSpinner);
	}

	private void setupWidgets() {
		exportBTN.setOnClickListener(this);
	}

	private void setupSpinner() {
		ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(getContext(), android.R.layout.simple_spinner_item, this.yearsToShow);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		yearSpinner.setAdapter(adapter);
	}

	public void setOnExportCommandListener(OnExportCommandListener listener) {
		this.listener = listener;
	}


	@Override
	public void onClick(View v) {
		if (v == exportBTN) {
			handleExportButtonClick();
		}
	}

	private void handleExportButtonClick() {
/*		if (getStartDate().isAfter(getEndDate())) {
			String msg = "Kan inte ha startdatum efter slutdatum";
			Toast toast = Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT);
			toast.show();
			return;
		}

		if (listener != null) {
			listener.onExportCommand(getStartDate(), getEndDate());
		}*/
	}

	public interface OnExportCommandListener {
		public void onExportCommand(DateTime startDate, DateTime endDate);
	}
}
