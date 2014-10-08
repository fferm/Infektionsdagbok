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
	private OnWeekChangeListener weekChangeListener;

	public QuestionnaireView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		attachWidgets();
		setupWidgets();
	}

	public void setModel(WeekAnswers model) {
		this.model = model;

		bindUIToModel();
	}

	public void setOnWeekChangeListener(OnWeekChangeListener listener) {
		this.weekChangeListener = listener;
	}

	private void attachWidgets() {
		weekDisplay = (TextView) findViewById(R.id.weekDisplay);
	}


	private void setupWidgets() {
		setupWeekNavigationButtons();
		setupQuestions();
	}

	private void setupWeekNavigationButtons() {
		findViewById(R.id.previousWeek).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				QuestionnaireView.this.weekChangeListener.onWeekDecrement();
			}
		});

		findViewById(R.id.nextWeek).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				QuestionnaireView.this.weekChangeListener.onWeekIncrement();
			}
		});
	}

	private void setupQuestions() {
		questions = new SparseArray<QuestionView>();
		for (int i = 0; i < getChildCount(); i++) {
			View view = getChildAt(i);
			if (!(view instanceof QuestionView)) continue;
			questions.put(Integer.valueOf(view.getId()), (QuestionView) view);

			view.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					onQuestionClicked((QuestionView) v);
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

	public void onQuestionClicked(QuestionView qv) {
		boolean checked = qv.isChecked();

		int id = qv.getId();
		model.setAnswer(id, checked);
	}

	public interface OnWeekChangeListener {
		public void onWeekIncrement();
		public void onWeekDecrement();
	}


}
