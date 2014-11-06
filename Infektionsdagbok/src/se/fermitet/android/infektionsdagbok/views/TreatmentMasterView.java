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
	protected void attachWidgets() throws Exception {
		super.attachWidgets();
		ViewGroup headerRow = (ViewGroup) findViewById(R.id.header);
		startListHeader = (TextView) headerRow.findViewById(R.id.dateValueField);
		numDaysListHeader = (TextView) headerRow.findViewById(R.id.numDaysValueField);
	}

	@Override
	protected void setupWidgets() throws Exception {
		super.setupWidgets();
		setupListViewHeader();
	}

	private void setupListViewHeader() throws Exception {
		startListHeader.setText("Start");
		startListHeader.setBackground(getResources().getDrawable(R.drawable.background_header));

		numDaysListHeader.setText("Dgr");
		numDaysListHeader.setBackground(getResources().getDrawable(R.drawable.background_header));
	}

}
