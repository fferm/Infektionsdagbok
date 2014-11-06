package se.fermitet.android.infektionsdagbok.activity;

import se.fermitet.android.infektionsdagbok.R;
import se.fermitet.android.infektionsdagbok.app.Factory;
import se.fermitet.android.infektionsdagbok.app.InfektionsdagbokApplication;
import se.fermitet.android.infektionsdagbok.demo.DemoDataProvider;
import se.fermitet.android.infektionsdagbok.views.InfektionsdagbokView;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class InfektionsdagbokActivity<V extends View & InfektionsdagbokView> extends Activity {

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
        	handleInformationFromIntent();

			view = (V) View.inflate(this, this.viewLayoutId, null);
	        setContentView(view);

		} catch (Exception e) {
			view.handleException(e);
		}

	}

	private void handleInformationFromIntent() throws Exception {
		Intent intent = getIntent();
		if (intent == null) return;

        Bundle extras = intent.getExtras();
        if (extras == null) return;

        setApplicationFactory(extras);
	}

	private void setApplicationFactory(Bundle extras) throws Exception{
		String factoryClassName = extras.getString(FACTORY_KEY);
        if (factoryClassName != null) {
        	Class<?> clz = Class.forName(factoryClassName);
        	Factory myFactory = (Factory) clz.getConstructor(Context.class).newInstance(this);

        	InfektionsdagbokApplication app = getLocalApplication();
        	app.setFactory(myFactory);
        }
	}

	protected InfektionsdagbokApplication getLocalApplication() {
		return (InfektionsdagbokApplication) getApplication();
	}

	V getView() {
		return view;
	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
 	   	getMenuInflater().inflate(R.menu.actionbar_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case R.id.actionQuestionnaire:
			if (! (this instanceof Questionnaire)) startActivity(new Intent(this, Questionnaire.class));
			return true;
		case R.id.actionExport:
			if (! (this instanceof ExportActivity)) startActivity(new Intent(this, ExportActivity.class));
			return true;
		case R.id.actionTreatment:
			if (! (this instanceof TreatmentMasterActivity)) startActivity(new Intent(this, TreatmentMasterActivity.class));
			return true;
		case R.id.actionSickDay:
			if (! (this instanceof SickDayMasterActivity)) startActivity(new Intent(this, SickDayMasterActivity.class));
			return true;
		case R.id.actionSetupDemo:
			setupDemoData();
			return true;
		case R.id.actionClear:
			clearData();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

    // TODO: Delete.  Also delete menu option
	private void setupDemoData() {
		DemoDataProvider ddp = new DemoDataProvider(getLocalApplication().getModelManager());
		try {
			ddp.saveDemoData();
		} catch (Exception e) {
			view.handleException(e);
		}
	}

    // TODO: Delete.  Also delete menu option
	private void clearData() {
		try {
			getLocalApplication().getModelManager().reset();
		} catch (Exception e) {
			view.handleException(e);
		}
	}



}
