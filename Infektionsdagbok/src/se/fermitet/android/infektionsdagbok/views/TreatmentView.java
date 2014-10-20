package se.fermitet.android.infektionsdagbok.views;

import se.fermitet.android.infektionsdagbok.R;
import se.fermitet.android.infektionsdagbok.model.Treatment;
import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TreatmentView extends RelativeLayout {

	private ListView listView;
	private TextView dateHeader;
	private TextView numDaysHeader;

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
		dateHeader = (TextView) headerRow.findViewById(R.id.dateValueField);
		numDaysHeader = (TextView) headerRow.findViewById(R.id.numDaysValueField);
	}

	private void setupWidgets() {
		dateHeader.setText("Start");
		numDaysHeader.setText("Dgr");
	}


	public void setAdapter(ArrayAdapter<Treatment> adapter) {
		listView.setAdapter(adapter);
	}
}
