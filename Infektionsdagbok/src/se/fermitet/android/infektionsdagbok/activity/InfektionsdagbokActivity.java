package se.fermitet.android.infektionsdagbok.activity;

import se.fermitet.android.infektionsdagbok.app.Factory;
import se.fermitet.android.infektionsdagbok.app.InfektionsdagbokApplication;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class InfektionsdagbokActivity extends Activity {

	public static final String FACTORY_KEY = "FACTORY_OBJECT";

	public InfektionsdagbokActivity() {
		super();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
        setApplicationFactory();

	}
	
	private void setApplicationFactory() {
		Intent intent = getIntent();
		if (intent == null) return;

        Bundle extras = intent.getExtras();
        if (extras == null) return;

        Object obj = extras.get(FACTORY_KEY);
        if (obj != null && obj instanceof Factory) {
        	InfektionsdagbokApplication app = (InfektionsdagbokApplication) getApplication();
        	app.setFactory((Factory) obj);
        }
	}

}
