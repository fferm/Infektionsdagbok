package se.fermitet.android.infektionsdagbok.views;

import se.fermitet.android.infektionsdagbok.R;
import se.fermitet.android.infektionsdagbok.model.Treatment;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public class TreatmentMasterView extends InfektionsdagbokRelativeLayoutView {

	private ListView listView;
	private TextView startListHeader;
	private TextView numDaysListHeader;
	private ImageButton editBTN;
	private ImageButton deleteBTN;
	private ImageButton newBTN;
	private OnButtonsPressedListener onButtonsPressedListener;

	public TreatmentMasterView(Context context, AttributeSet attrs) {
		super(context, attrs);
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
		this.listView = (ListView) findViewById(R.id.itemListView);

		ViewGroup headerRow = (ViewGroup) findViewById(R.id.header);
		startListHeader = (TextView) headerRow.findViewById(R.id.dateValueField);
		numDaysListHeader = (TextView) headerRow.findViewById(R.id.numDaysValueField);
		editBTN = (ImageButton) findViewById(R.id.editBTN);
		deleteBTN = (ImageButton) findViewById(R.id.deleteBTN);
		newBTN = (ImageButton) findViewById(R.id.newBTN);
	}

	private void setupWidgets() throws Exception {
		setupEditBTN();
		setupDeleteBTN();
		setupNewBTN();
		setupListView();
	}

	protected void setupEditBTN() {
		editBTN.setEnabled(false);
		editBTN.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					TreatmentAdapter adapter = (TreatmentAdapter) listView.getAdapter();
					Treatment selected = adapter.getSelectedItem();
					TreatmentMasterView.this.onButtonsPressedListener.onEditPressed(selected);
				} catch (Exception e) {
					handleException(e);
				}
			}
		});
	}

	protected void setupDeleteBTN() {
		deleteBTN.setEnabled(false);
		deleteBTN.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					TreatmentAdapter adapter = (TreatmentAdapter) listView.getAdapter();
					Treatment selected = adapter.getSelectedItem();
					TreatmentMasterView.this.onButtonsPressedListener.onDeletePressed(selected);
				} catch (Exception e) {
					handleException(e);
				}
			}
		});
	}

	protected void setupNewBTN() {
		newBTN.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					TreatmentMasterView.this.onButtonsPressedListener.onNewPressed();
				} catch (Exception e) {
					handleException(e);
				}
			}
		});
	}

	protected void setupListView() {
		startListHeader.setText("Start");
		startListHeader.setBackground(getResources().getDrawable(R.drawable.background_header));

		numDaysListHeader.setText("Dgr");
		numDaysListHeader.setBackground(getResources().getDrawable(R.drawable.background_header));

		listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				try {
					clickOnPosition(position);
				} catch (Exception e) {
					handleException(e);
				}
			}
		});
	}


	private void clickOnPosition(int position) throws Exception {
		if (!wasSelectedBefore(position)) {
			select(position);
			handleButtonsEnablement(true);
		} else {
			deselect(position);
			handleButtonsEnablement(false);
		}
		//		Treatment treatment = (Treatment) parent.getItemAtPosition(position);
		//		TreatmentMasterView.this.treatmentSingleEditView.selectTreatment(new Treatment(treatment));
	}

	private boolean wasSelectedBefore(int position) throws Exception {
		TreatmentAdapter adapter = (TreatmentAdapter) listView.getAdapter();
		return (adapter.getSelectedPosition() != null && (adapter.getSelectedPosition()) == position);
	}

	private void select(int position) throws Exception{
		TreatmentAdapter adapter = (TreatmentAdapter) listView.getAdapter();

		listView.setItemChecked(position, true);
		adapter.setSelectedPosition(position);
	}

	private void deselect(int position) throws Exception {
		TreatmentAdapter adapter = (TreatmentAdapter) listView.getAdapter();

		listView.setItemChecked(position, false);
		adapter.setSelectedPosition(null);
	}

	private void handleButtonsEnablement(boolean isEnabled) {
		editBTN.setEnabled(isEnabled);
		deleteBTN.setEnabled(isEnabled);
	}

	public void setAdapter(TreatmentAdapter adapter) {
		listView.setAdapter(adapter);
	}

	public void setOnButtonsPressedListener(OnButtonsPressedListener listener) {
		this.onButtonsPressedListener = listener;
	}

	public interface OnButtonsPressedListener {
		public void onNewPressed() throws Exception;
		public void onDeletePressed(Treatment treatment) throws Exception;
		public void onEditPressed(Treatment treatment) throws Exception;
	}
}
