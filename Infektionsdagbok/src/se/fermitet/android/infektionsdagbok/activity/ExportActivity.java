package se.fermitet.android.infektionsdagbok.activity;

import se.fermitet.android.infektionsdagbok.R;
import se.fermitet.android.infektionsdagbok.views.ExportView;
import android.os.Bundle;
import android.view.View;

public class ExportActivity extends InfektionsdagbokActivity {

	private ExportView view = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        view = (ExportView) View.inflate(this, R.layout.export_view, null);
        setContentView(view);

	}
}
