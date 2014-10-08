package se.fermitet.android.infektionsdagbok.activity;

import se.fermitet.android.infektionsdagbok.R;
import se.fermitet.android.infektionsdagbok.views.ExportView;
import android.os.Bundle;

public class ExportActivity extends InfektionsdagbokActivity<ExportView> {

	public ExportActivity() {
		super(R.layout.export_view);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
}
