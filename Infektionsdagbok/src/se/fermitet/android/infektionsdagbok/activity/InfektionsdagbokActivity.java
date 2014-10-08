package se.fermitet.android.infektionsdagbok.activity;

import se.fermitet.android.infektionsdagbok.app.Factory;
import se.fermitet.android.infektionsdagbok.app.InfektionsdagbokApplication;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class InfektionsdagbokActivity<V extends View> extends Activity {

	public static final String FACTORY_KEY = "FACTORY_OBJECT";
	
	protected V view;
	private int viewLayoutId;

	public InfektionsdagbokActivity(int viewLayoutId) {
		super();
		
		this.viewLayoutId = viewLayoutId;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
        setApplicationFactory();

        view = (V) View.inflate(this, this.viewLayoutId, null);
        setContentView(view);
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

	protected void notifyUserOfException(Exception e) {
		notifyUserWithMessage(e.getMessage());
	}
	
	protected void notifyUserWithMessage(String msg) {
		Toast toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
		toast.show();
	}

}
