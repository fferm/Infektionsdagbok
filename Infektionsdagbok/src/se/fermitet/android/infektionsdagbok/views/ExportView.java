package se.fermitet.android.infektionsdagbok.views;

import java.util.List;

import se.fermitet.android.infektionsdagbok.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;

public class ExportView extends RelativeLayout implements View.OnClickListener, OnItemSelectedListener {

	private Button exportBTN;
	private Spinner yearSpinner;

	private OnExportCommandListener listener = null;
	private List<Integer> yearsToShow;
	private int selectedYear;

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
		yearSpinner.setOnItemSelectedListener(this);
	}

	private void setupSpinner() {
		ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(getContext(), R.layout.spinner_item, this.yearsToShow);
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

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		if (parent == yearSpinner) {
			int selectedYear = (Integer) yearSpinner.getSelectedItem();
			this.setSelectedYear(selectedYear);
		}
	}


	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// Do nothing
	}

	private void handleExportButtonClick() {
		if (listener != null) {
			listener.onExportCommand(getSelectedYear());
		}
	}

	public interface OnExportCommandListener {
		public void onExportCommand(int year);
	}

	public int getSelectedYear() {
		return selectedYear;
	}

	private void setSelectedYear(int selectedYear) {
		this.selectedYear = selectedYear;
	}


}
