package se.fermitet.android.infektionsdagbok.activity;

import se.fermitet.android.infektionsdagbok.app.Factory;
import se.fermitet.android.infektionsdagbok.app.InfektionsdagbokApplication;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class InfektionsdagbokActivity<V extends View> extends Activity {

	public static final String FACTORY_KEY = "se.fermitet.infektionsdagbok.FACTORY_OBJECT";

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

        try {
			setApplicationFactory();

			view = (V) View.inflate(this, this.viewLayoutId, null);
	        setContentView(view);

		} catch (Exception e) {
			e.printStackTrace();
			notifyUserOfException(e);
		}

	}

	private void setApplicationFactory() throws Exception {
		Intent intent = getIntent();
		if (intent == null) return;

        Bundle extras = intent.getExtras();
        if (extras == null) return;

/*        Object obj = extras.get(FACTORY_KEY);
        if (obj != null && obj instanceof Factory) {
        	InfektionsdagbokApplication app = (InfektionsdagbokApplication) getApplication();
        	app.setFactory((Factory) obj);
        }*/
        String factoryClassName = extras.getString(FACTORY_KEY);
        if (factoryClassName != null) {
        	Class<?> clz = Class.forName(factoryClassName);
        	Factory myFactory = (Factory) clz.getConstructor(Context.class).newInstance(this);

        	InfektionsdagbokApplication app = getLocalApplication();
        	app.setFactory(myFactory);
        }
	}

	protected void notifyUserOfException(Exception e) {
		notifyUserWithMessage(e.getMessage());
	}

	protected void notifyUserWithMessage(String msg) {
		Toast toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
		toast.show();
	}

	protected InfektionsdagbokApplication getLocalApplication() {
		return (InfektionsdagbokApplication) getApplication();
	}

}
