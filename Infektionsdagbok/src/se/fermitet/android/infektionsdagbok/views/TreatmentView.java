package se.fermitet.android.infektionsdagbok.views;

import se.fermitet.android.infektionsdagbok.R;
import se.fermitet.android.infektionsdagbok.model.Treatment;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TreatmentView extends RelativeLayout {

	private ListView listView;
	private TextView startListHeader;
	private TextView numDaysListHeader;

	private SingleTreatmentEditorView singleTreatmentEditor;

	public TreatmentView(Context context, AttributeSet attrs) {
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

		singleTreatmentEditor = (SingleTreatmentEditorView) findViewById(R.id.treatmentEdit);
	}

	private void setupWidgets() {
		startListHeader.setText("Start");
		numDaysListHeader.setText("Dgr");

		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Treatment treatment = (Treatment) parent.getItemAtPosition(position);
				TreatmentView.this.singleTreatmentEditor.selectTreatment(treatment);
			}
		});
	}


	public void setAdapter(ArrayAdapter<Treatment> adapter) {
		listView.setAdapter(adapter);
	}
}
