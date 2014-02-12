package se.fermitet.android.infektionsdagbok;

import se.fermitet.android.infektionsdagbok.app.InfektionsdagbokApplication;
import se.fermitet.android.infektionsdagbok.model.WeekAnswers;
import se.fermitet.android.infektionsdagbok.views.QuestionnaireView;
import se.fermitet.android.infektionsdagbok.views.QuestionnaireView.OnWeekChangeListener;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

public class Questionnaire extends Activity implements OnWeekChangeListener {

	private QuestionnaireView view;

	WeekAnswers model;

	public Questionnaire() {
		super();
	}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        view = (QuestionnaireView) View.inflate(this, R.layout.questionnaire_view, null);
        view.setOnWeekChangeListener(this);

        setNewWeek(getLocalApplication().getModelManager().getInitialWeekAnswers());

        setContentView(view);
    }


	private void printBundle() {
		Intent intent = getIntent();
		System.out.println("!!!! intent class: " + intent.getClass().getName());

		Bundle b = intent.getExtras();

		for (String key : b.keySet()) {
			String value = b.getString(key);

			System.out.println("!!!! key: " + key + "    value : " + value);
		}
	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.quesionnaire, menu);
        return true;
    }

/*	ModelManager getModelManager() {
		return modelManager;
	}

	void setModelManager(ModelManager mgr) {
		this.modelManager = mgr;
	}*/



	@Override
	public void onWeekIncrement() {
		try {
			setNewWeek(getLocalApplication().getModelManager().getNextWeekAnswers(this.model));
		} catch (Exception e) {
			e.printStackTrace();
			notifyUserOfException(e);
		}
	}


	@Override
	public void onWeekDecrement() {
		try {
			setNewWeek(getLocalApplication().getModelManager().getPreviousWeekAnswers(this.model));
		} catch (Exception e) {
			e.printStackTrace();
			notifyUserOfException(e);
		}
	}

	private InfektionsdagbokApplication getLocalApplication() {
		return (InfektionsdagbokApplication) getApplication();
	}



	private void notifyUserOfException(Exception e) {
		Toast toast = Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT);
		toast.show();
	}


	private void setNewWeek(WeekAnswers newWeekAnswers) {
		model = newWeekAnswers;
        view.setModel(newWeekAnswers);
	}






}
