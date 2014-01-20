package se.fermitet.android.infektionsdagbok;

import java.util.ArrayList;
import java.util.Iterator;

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
		setupGenerallyWell();
		diableAllButGenerallyWell();
	}


	private void setupGenerallyWell() {
		QuestionView generallyWell = (QuestionView) findViewById(R.id.generallyWell);
		generallyWell.setChecked(true);
		generallyWell.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				generallyWellClicked((QuestionView) v);
			}
		});
	}


	private void diableAllButGenerallyWell() {
		for (Iterator<QuestionView> iter = questionIterator(); iter.hasNext(); ) {
			QuestionView question = iter.next();
			question.setEnabled(question.getId() == R.id.generallyWell);
		}
	}

	private void generallyWellClicked(QuestionView generallyWell) {
		if (! generallyWell.isChecked()) {
			enableAllQuestions();
		} else {
			disableAllSicknessQuestions();
		}
	}

	private void enableAllQuestions() {
		for (Iterator<QuestionView> iter = questionIterator(); iter.hasNext(); ) {
			QuestionView question = iter.next();
			question.setEnabled(true);
		}
	}

	private void disableAllSicknessQuestions() {
		for (Iterator<QuestionView> iter = questionIterator(); iter.hasNext(); ) {
			QuestionView question = iter.next();
			if (question.getId() != R.id.generallyWell) {
				question.setEnabled(false);
			}
		}
	}

	private Iterator<QuestionView> questionIterator() {
		ArrayList<QuestionView> arrayList = new ArrayList<QuestionView>();

		ViewGroup parentOfQuestions = (ViewGroup) findViewById(R.id.parentOfQuestions);
		for (int i = 0; i < parentOfQuestions.getChildCount(); i++) {
			arrayList.add((QuestionView) parentOfQuestions.getChildAt(i));
		}

		return arrayList.iterator();
	}


}
