package se.fermitet.android.infektionsdagbok.views;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import se.fermitet.android.infektionsdagbok.R;
import se.fermitet.android.infektionsdagbok.model.WeekAnswers;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class QuestionnaireView extends RelativeLayout {

	private TextView weekDisplay;

	private QuestionView generallyWell;
	private QuestionView malaise;
	private QuestionView fever;
	private QuestionView earAche;
	private QuestionView soreThroat;
	private QuestionView runnyNose;
	private QuestionView stommacAche;
	private QuestionView dryCough;
	private QuestionView wetCough;
	private QuestionView morningCough;

	private List<QuestionView> questions;

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

		generallyWell = (QuestionView) findViewById(R.id.generallyWell);
		malaise = (QuestionView) findViewById(R.id.malaise);
		fever = (QuestionView) findViewById(R.id.fever);
		earAche = (QuestionView) findViewById(R.id.earAche);
		soreThroat = (QuestionView) findViewById(R.id.soreThroat);
		runnyNose = (QuestionView) findViewById(R.id.runnyNose);
		stommacAche = (QuestionView) findViewById(R.id.stommacAche);
		dryCough = (QuestionView) findViewById(R.id.dryCough);
		wetCough = (QuestionView) findViewById(R.id.wetCough);
		morningCough = (QuestionView) findViewById(R.id.morningCough);

		questions = new ArrayList<QuestionView>();
		questions.add(generallyWell);
		questions.add(malaise);
		questions.add(fever);
		questions.add(earAche);
		questions.add(soreThroat);
		questions.add(runnyNose);
		questions.add(stommacAche);
		questions.add(dryCough);
		questions.add(wetCough);
		questions.add(morningCough);

		for (QuestionView qv : questions) {
			qv.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					questionClicked((QuestionView) v);
				}
			});
		}
	}

	private void bindUIToModel() {
		weekDisplay.setText(model.week.toString());

		bindAnswer(R.id.generallyWell);
		bindAnswer(R.id.malaise);
		bindAnswer(R.id.fever);
		bindAnswer(R.id.earAche);
		bindAnswer(R.id.soreThroat);
		bindAnswer(R.id.runnyNose);
		bindAnswer(R.id.stommacAche);
		bindAnswer(R.id.dryCough);
		bindAnswer(R.id.wetCough);
		bindAnswer(R.id.morningCough);

		// TODO: bör loopas genom collection av QuestionView istället. Då behöver dock den vara accessbar via id
	}

	private void bindAnswer(int id) {
		QuestionView view = (QuestionView) findViewById(id);
		view.setChecked(model.getAnswer(id));
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
		if (! generallyWell.isChecked()) {
			enableAllQuestions();
		} else {
			disableAllButGenerallyWell();
		}
	}

	private void disableAllButGenerallyWell() {
		for (Iterator<QuestionView> iter = questions.iterator(); iter.hasNext(); ) {
			QuestionView question = iter.next();
			question.setEnabled(question == this.generallyWell);
		}
	}

	private void enableAllQuestions() {
		for (Iterator<QuestionView> iter = questions.iterator(); iter.hasNext(); ) {
			QuestionView question = iter.next();
			question.setEnabled(true);
		}
	}


}
