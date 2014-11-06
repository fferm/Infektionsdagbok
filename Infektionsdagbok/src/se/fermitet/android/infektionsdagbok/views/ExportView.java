package se.fermitet.android.infektionsdagbok.views;

import java.util.List;

import se.fermitet.android.infektionsdagbok.R;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class ExportView extends InfektionsdagbokRelativeLayoutView {

	private Button exportBTN;
	private Spinner yearSpinner;
	private EditText nameEdit;
	private EditText ssnEdit;

	private OnExportCommandListener listener = null;
	private List<Integer> yearsToShow;
	private int selectedYear;
	private String name;
	private String ssn;

	public ExportView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void setYearsToShow(List<Integer> yearsToShow) throws Exception {
		this.yearsToShow = yearsToShow;
		setupSpinner();
	}

	@Override
	protected void attachWidgets() throws Exception {
		super.attachWidgets();
		exportBTN = (Button) findViewById(R.id.exportBTN);
		yearSpinner = (Spinner) findViewById(R.id.yearSpinner);
		nameEdit = (EditText) findViewById(R.id.nameEdit);
		ssnEdit = (EditText) findViewById(R.id.ssnEdit);
	}

	@Override
	protected void setupWidgets() throws Exception {
		super.setupWidgets();
		exportBTN.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					handleExportButtonClick();
				} catch (Exception e) {
					handleException(e);
				}
			}
		});

		yearSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				try {
					int selectedYear = (Integer) yearSpinner.getSelectedItem();
					ExportView.this.setSelectedYear(selectedYear);
				} catch (Exception e) {
					handleException(e);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {}
		});

		nameEdit.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

			@Override
			public void afterTextChanged(Editable s) {
				try {
					ExportView.this.setName(s.toString());
				} catch (Exception e) {
					handleException(e);
				}
			}
		});

		ssnEdit.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

			@Override
			public void afterTextChanged(Editable s) {
				try {
					ExportView.this.setSSN(s.toString());
				} catch (Exception e) {
					handleException(e);
				}
			}
		});
	}

	private void setupSpinner() {
		ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(getContext(), R.layout.spinner_item, this.yearsToShow);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		yearSpinner.setAdapter(adapter);
	}

	public void setOnExportCommandListener(OnExportCommandListener listener) throws Exception {
		this.listener = listener;
	}

	private void handleExportButtonClick() throws Exception {
		if (listener != null) {
			listener.onExportCommand(getSelectedYear(), getName(), getSSN());
		}
	}

	public interface OnExportCommandListener {
		public void onExportCommand(int year, String name, String ssn);
	}

	public int getSelectedYear() throws Exception {
		return selectedYear;
	}

	private void setSelectedYear(int selectedYear) throws Exception {
		this.selectedYear = selectedYear;
	}

	public String getName() throws Exception {
		return this.name;
	}

	private void setName(String name) throws Exception {
		this.name = name;
	}

	public String getSSN() throws Exception{
		return this.ssn;
	}

	private void setSSN(String ssn) throws Exception{
		this.ssn = ssn;
	}
}
