package se.fermitet.android.infektionsdagbok.views;

import se.fermitet.android.infektionsdagbok.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class TreatmentMasterView extends InfektionsdagbokRelativeLayoutView {

	private ListView listView;
	private TextView startListHeader;
	private TextView numDaysListHeader;

//	private TreatmentDetailView treatmentSingleEditView;

	public TreatmentMasterView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		attachWidgets();
		setupWidgets();
	}

	private void attachWidgets() {
		this.listView = (ListView) findViewById(R.id.treatmentListView);

		ViewGroup headerRow = (ViewGroup) findViewById(R.id.header);
		startListHeader = (TextView) headerRow.findViewById(R.id.dateValueField);
		numDaysListHeader = (TextView) headerRow.findViewById(R.id.numDaysValueField);

//		treatmentSingleEditView = (TreatmentDetailView) findViewById(R.id.treatmentEdit);
	}

	private void setupWidgets() {
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
		handleClicksForItemChecked(position);
		handleClicksForSelection(position);
//		Treatment treatment = (Treatment) parent.getItemAtPosition(position);
//		TreatmentMasterView.this.treatmentSingleEditView.selectTreatment(new Treatment(treatment));
	}
	
	private void handleClicksForItemChecked(int position) throws Exception {
		if (listView.getCheckedItemPosition() == position) {
			listView.setItemChecked(position, false);
		} else {
			listView.setItemChecked(position, true);
		}
	}
	
	private void handleClicksForSelection(int position) throws Exception {
		TreatmentAdapter adapter = (TreatmentAdapter) listView.getAdapter();
		
		if (adapter.getSelectedPosition() != null && ((int) adapter.getSelectedPosition()) == position) {
			adapter.setSelectedPosition(null);
		} else {
			adapter.setSelectedPosition(position);
		}
	}

	public void setAdapter(TreatmentAdapter adapter) {
		listView.setAdapter(adapter);
	}

/*	public TreatmentDetailView getSingleEditView() {
		return this.treatmentSingleEditView;
	}*/
}
