package se.fermitet.android.infektionsdagbok.views;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.joda.time.DateTime;

import se.fermitet.android.infektionsdagbok.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class QuestionnaireView extends RelativeLayout {

	private QuestionView generallyWell;
	private TextView weekDisplay;
	private List<QuestionView> questionList;

	public QuestionnaireView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		attachReferencesToViews();
        setupViews();
	}

	private void attachReferencesToViews() {
		generallyWell = (QuestionView) findViewById(R.id.generallyWell);
		weekDisplay = (TextView) findViewById(R.id.weekDisplay);

		questionList = new ArrayList<QuestionView>();
		ViewGroup topLayout = (ViewGroup) findViewById(R.id.questionnaireView);
		for (int i = 0; i < topLayout.getChildCount(); i++) {
			View v = topLayout.getChildAt(i);
			if (v instanceof QuestionView) {
				questionList.add((QuestionView) v);
			}
		}
	}

	private void setupViews() {
		setupGenerallyWell();
		disableAllButGenerallyWell();
		setupWeekDisplay();
	}

	private void setupGenerallyWell() {
		generallyWell.setChecked(true);
		generallyWell.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				generallyWellClicked((QuestionView) v);
			}
		});
	}

	private void disableAllButGenerallyWell() {
		for (Iterator<QuestionView> iter = questionList.iterator(); iter.hasNext(); ) {
			QuestionView question = iter.next();
			question.setEnabled(question == this.generallyWell);
		}
	}

	private void setupWeekDisplay() {
		DateTime now = new DateTime();
		weekDisplay.setText("" + now.getYear() + ":" + now.getWeekOfWeekyear());
	}

	private void generallyWellClicked(QuestionView generallyWell) {
		if (! generallyWell.isChecked()) {
			enableAllQuestions();
		} else {
			disableAllButGenerallyWell();
		}
	}

	private void enableAllQuestions() {
		for (Iterator<QuestionView> iter = questionList.iterator(); iter.hasNext(); ) {
			QuestionView question = iter.next();
			question.setEnabled(true);
		}
	}


}
