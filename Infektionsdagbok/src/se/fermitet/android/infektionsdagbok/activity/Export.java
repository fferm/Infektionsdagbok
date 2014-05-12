package se.fermitet.android.infektionsdagbok.activity;

import se.fermitet.android.infektionsdagbok.R;
import se.fermitet.android.infektionsdagbok.views.ExportView;
import android.app.Activity;
import android.os.Bundle;

public class Export extends Activity {
	private ExportView view;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
//        view = (ExportView) View.inflate(this, R.layout.questionnaire_view, null);

        setContentView(R.layout.export_view);
	}
}
