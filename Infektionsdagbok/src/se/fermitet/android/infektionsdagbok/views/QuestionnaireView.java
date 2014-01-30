package se.fermitet.android.infektionsdagbok.views;

import se.fermitet.android.infektionsdagbok.R;
import se.fermitet.android.infektionsdagbok.model.WeekAnswers;
import android.content.Context;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class QuestionnaireView extends RelativeLayout {

	private TextView weekDisplay;

	private SparseArray<QuestionView> questions;

	private WeekAnswers model;

	public QuestionnaireView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		setupWidgets();
	}

	public void setModel(WeekAnswers model) {
		this.model = model;

		bindUIToModel();
		handleEnabledQuestions();
	}

	private void setupWidgets() {
		weekDisplay = (TextView) findViewById(R.id.weekDisplay);

		questions = new SparseArray<QuestionView>();
		for (int i = 0; i < getChildCount(); i++) {
			View view = getChildAt(i);
			if (!(view instanceof QuestionView)) continue;
			questions.put(Integer.valueOf(view.getId()), (QuestionView) view);

			view.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					questionClicked((QuestionView) v);
				}
			});
		}
	}

	private void bindUIToModel() {
		weekDisplay.setText(model.week.toString());

		for (int i = 0; i < this.getChildCount(); i++) {
			View view = this.getChildAt(i);
			if (!(view instanceof QuestionView)) {
				continue;
			}
			QuestionView qv = (QuestionView) view;
			bindAnswer(qv);
		}
	}

	private void bindAnswer(QuestionView qv) {
		qv.setChecked(model.getAnswer(qv.getId()));
	}

	private void questionClicked(QuestionView qv) {
		boolean checked = qv.isChecked();

		int id = qv.getId();
		model.setAnswer(id, checked);

		if (id == R.id.generallyWell) {
			handleEnabledQuestions();
		}
	}

	private void handleEnabledQuestions() {
		if (! questions.get(R.id.generallyWell).isChecked()) {
			enableAllQuestions();
		} else {
			disableAllButGenerallyWell();
		}
	}

	private void disableAllButGenerallyWell() {
		QuestionView generallyWell = questions.get(R.id.generallyWell);
		for (int i = 0; i < questions.size(); i++) {
			QuestionView question = questions.valueAt(i);
			question.setEnabled(question == generallyWell);
		}
	}

	private void enableAllQuestions() {
		for (int i = 0; i < questions.size(); i++) {
			QuestionView question = questions.valueAt(i);
			question.setEnabled(true);
		}
	}


}
