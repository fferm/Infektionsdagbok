package se.fermitet.android.infektionsdagbok;

import se.fermitet.android.infektionsdagbok.views.QuestionView;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

public class Quesionnaire extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quesionnaire);
        setupViews();
    }


	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.quesionnaire, menu);
        return true;
    }

	private void setupViews() {
		QuestionView generallyWell = (QuestionView) findViewById(R.id.generallyWell);
		generallyWell.setChecked(true);
		generallyWell.setEnabled(true);
		generallyWell.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				generallyWellClicked((QuestionView) v);
			}
		});
	}

	private void generallyWellClicked(QuestionView generallyWell) {
		if (! generallyWell.isChecked()) {
			enableAllQuestions();
		} else {
			disableAllSicknessQuestions();
		}
	}

	private void enableAllQuestions() {
		ViewGroup parentOfQuestions = (ViewGroup) findViewById(R.id.parentOfQuestions);

		for (int i = 0; i < parentOfQuestions.getChildCount(); i++) {
			QuestionView question = (QuestionView) parentOfQuestions.getChildAt(i);
			question.setEnabled(true);
		}
	}

	private void disableAllSicknessQuestions() {
		ViewGroup parentOfQuestions = (ViewGroup) findViewById(R.id.parentOfQuestions);

		for (int i = 0; i < parentOfQuestions.getChildCount(); i++) {
			QuestionView question = (QuestionView) parentOfQuestions.getChildAt(i);
			if (question.getId() != R.id.generallyWell) {
				question.setEnabled(false);
			}
		}
	}


}
