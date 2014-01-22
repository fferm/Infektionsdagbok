package se.fermitet.android.infektionsdagbok.views;

import java.util.ArrayList;
import java.util.Iterator;

import org.joda.time.DateTime;

import se.fermitet.android.infektionsdagbok.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class QuestionnaireView extends RelativeLayout {

	public QuestionnaireView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
        setupViews();
	}

	private void setupViews() {
		setupGenerallyWell();
		diableAllButGenerallyWell();
		setupWeekDisplay();
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

	private void setupWeekDisplay() {
		TextView tv = (TextView) findViewById(R.id.weekDisplay);
		DateTime now = new DateTime();

		tv.setText("" + now.getYear() + ":" + now.getWeekOfWeekyear());
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

		ViewGroup topLayout = (ViewGroup) findViewById(R.id.questionnaireView);
		for (int i = 0; i < topLayout.getChildCount(); i++) {
			View v = topLayout.getChildAt(i);
			if (v instanceof QuestionView) {
				arrayList.add((QuestionView) v);
			}
		}

		return arrayList.iterator();
	}


}
