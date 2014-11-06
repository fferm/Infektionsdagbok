package se.fermitet.android.infektionsdagbok.views;

import se.fermitet.android.infektionsdagbok.R;
import se.fermitet.android.infektionsdagbok.model.SickDay;
import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.TextView;

public class SickDayMasterView extends InfektionsdagbokMasterView<SickDay, SickDayAdapter> {

	private TextView startHeader;
	private TextView endHeader;

	public SickDayMasterView(Context context, AttributeSet attrs) {
		super(context, attrs, "Sjukskrivningar");
	}

	@Override
	protected void attachWidgets() throws Exception {
		super.attachWidgets();
		ViewGroup headerRow = (ViewGroup) findViewById(R.id.header);
		startHeader = (TextView) headerRow.findViewById(R.id.startValueField);
		endHeader = (TextView) headerRow.findViewById(R.id.endValueField);
	}

	@Override
	protected void setupWidgets() throws Exception {
		super.setupWidgets();
		setupListViewHeader();
	}

	private void setupListViewHeader() throws Exception {
		startHeader.setText("Start");
		startHeader.setBackground(getResources().getDrawable(R.drawable.background_header));

		endHeader.setText("Slut");
		endHeader.setBackground(getResources().getDrawable(R.drawable.background_header));
	}

}
