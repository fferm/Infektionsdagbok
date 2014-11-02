package se.fermitet.android.infektionsdagbok.views;

import se.fermitet.android.infektionsdagbok.R;
import se.fermitet.android.infektionsdagbok.model.Treatment;
import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.TextView;

public class TreatmentMasterView extends InfektionsdagbokMasterView<Treatment, TreatmentAdapter> {

	private TextView startListHeader;
	private TextView numDaysListHeader;

	public TreatmentMasterView(Context context, AttributeSet attrs) {
		super(context, attrs, "Behandlingar");
	}

	@Override
	protected void onFinishInflate() {
		try {
			attachWidgets();
			super.onFinishInflate();
			setupWidgets();
		} catch (Exception e) {
			handleException(e);
		}
	}

	private void attachWidgets() throws Exception {
		ViewGroup headerRow = (ViewGroup) findViewById(R.id.header);
		startListHeader = (TextView) headerRow.findViewById(R.id.dateValueField);
		numDaysListHeader = (TextView) headerRow.findViewById(R.id.numDaysValueField);
	}

	private void setupWidgets() throws Exception {
		setupListViewHeader();
	}

	private void setupListViewHeader() throws Exception {
		startListHeader.setText("Start");
		startListHeader.setBackground(getResources().getDrawable(R.drawable.background_header));

		numDaysListHeader.setText("Dgr");
		numDaysListHeader.setBackground(getResources().getDrawable(R.drawable.background_header));
	}

}
