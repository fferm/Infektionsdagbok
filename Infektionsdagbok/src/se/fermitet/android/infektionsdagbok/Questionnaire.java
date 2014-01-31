package se.fermitet.android.infektionsdagbok;

import se.fermitet.android.infektionsdagbok.model.ModelManager;
import se.fermitet.android.infektionsdagbok.model.WeekAnswers;
import se.fermitet.android.infektionsdagbok.views.QuestionnaireView;
import se.fermitet.android.infektionsdagbok.views.QuestionnaireView.OnWeekChangeListener;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

public class Questionnaire extends Activity implements OnWeekChangeListener {

	private QuestionnaireView view;

	WeekAnswers model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        view = (QuestionnaireView) View.inflate(this, R.layout.questionnaire_view, null);
        view.setOnWeekChangeListener(this);

        setNewWeek(ModelManager.instance().getInitialWeekAnswers());

        setContentView(view);
    }


	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.quesionnaire, menu);
        return true;
    }


	@Override
	public void onWeekIncrement() {
		setNewWeek(ModelManager.instance().getNextWeekAnswers(this.model));
	}


	@Override
	public void onWeekDecrement() {
		setNewWeek(ModelManager.instance().getPreviousWeekAnswers(this.model));
	}

	private void setNewWeek(WeekAnswers newWeekAnswers) {
		model = newWeekAnswers;
        view.setModel(newWeekAnswers);
	}


}
