package se.fermitet.android.infektionsdagbok;

import se.fermitet.android.infektionsdagbok.model.ModelManager;
import se.fermitet.android.infektionsdagbok.model.WeekAnswers;
import se.fermitet.android.infektionsdagbok.views.QuestionnaireView;
import se.fermitet.android.infektionsdagbok.views.QuestionnaireView.OnWeekChangeListener;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

public class Questionnaire extends Activity implements OnWeekChangeListener {

	private QuestionnaireView view;
	private ModelManager modelManager;

	WeekAnswers model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        modelManager = new ModelManager(getApplicationContext());

        view = (QuestionnaireView) View.inflate(this, R.layout.questionnaire_view, null);
        view.setOnWeekChangeListener(this);

        setNewWeek(modelManager.getInitialWeekAnswers());

        setContentView(view);
    }


	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.quesionnaire, menu);
        return true;
    }

	ModelManager getModelManager() {
		return modelManager;
	}

	void setModelManager(ModelManager mgr) {
		this.modelManager = mgr;
	}



	@Override
	public void onWeekIncrement() {
		try {
			setNewWeek(modelManager.getNextWeekAnswers(this.model));
		} catch (Exception e) {
			e.printStackTrace();
			notifyUserOfException(e);
		}
	}


	@Override
	public void onWeekDecrement() {
		try {
			setNewWeek(modelManager.getPreviousWeekAnswers(this.model));
		} catch (Exception e) {
			e.printStackTrace();
			notifyUserOfException(e);
		}
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
