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
		numDaysListHeader.setText("Dgr");

		listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				try {
					listView.setItemChecked(position, true);
					listView.setSelection(position);
					((TreatmentAdapter) listView.getAdapter()).setSelectedItem(position);
//					Treatment treatment = (Treatment) parent.getItemAtPosition(position);
//					TreatmentMasterView.this.treatmentSingleEditView.selectTreatment(new Treatment(treatment));
				} catch (Exception e) {
					handleException(e);
				}
			}
		});
	}


	public void setAdapter(TreatmentAdapter adapter) {
		listView.setAdapter(adapter);
	}

/*	public TreatmentDetailView getSingleEditView() {
		return this.treatmentSingleEditView;
	}*/
}
