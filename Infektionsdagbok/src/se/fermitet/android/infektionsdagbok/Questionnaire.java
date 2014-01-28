package se.fermitet.android.infektionsdagbok;

import se.fermitet.android.infektionsdagbok.model.WeekAnswers;
import se.fermitet.android.infektionsdagbok.views.QuestionnaireView;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

public class Questionnaire extends Activity {

	private QuestionnaireView view;

	WeekAnswers model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        view = (QuestionnaireView) View.inflate(this, R.layout.questionnaire_view, null);

        model = new WeekAnswers();
        view.setModel(model);

        setContentView(view);
    }


	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.quesionnaire, menu);
        return true;
    }

}
