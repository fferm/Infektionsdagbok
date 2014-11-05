package se.fermitet.android.infektionsdagbok.views;

import se.fermitet.android.infektionsdagbok.R;
import se.fermitet.android.infektionsdagbok.model.WeekAnswers;
import se.fermitet.android.infektionsdagbok.widget.OnSwipeTouchListener;
import android.content.Context;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;

public class QuestionnaireView extends InfektionsdagbokRelativeLayoutView {

	private TextView weekDisplay;
	private TextView mondayTV;
	private TextView sundayTV;
	private SparseArray<QuestionView> questions;

	private WeekAnswers model;
	private OnWeekChangeListener weekChangeListener;

	public QuestionnaireView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onFinishInflate() {
		try {
			attachWidgets();
			super.onFinishInflate();
			setupWidgets();
		} catch (Exception e) {
			handleException(e);
		}
	}

	public void setModel(WeekAnswers model) throws Exception {
		this.model = model;

		syncUIWithModel();
	}

	public void setOnWeekChangeListener(OnWeekChangeListener listener) throws Exception {
		this.weekChangeListener = listener;
	}

	private void attachWidgets() throws Exception {
		weekDisplay = (TextView) findViewById(R.id.weekDisplay);
		mondayTV = (TextView) findViewById(R.id.mondayTV);
		sundayTV = (TextView) findViewById(R.id.sundayTV);
	}


	private void setupWidgets() throws Exception {
		setupSwipes();
		setupWeekNavigationButtons();
		setupQuestions();
	}

	private void setupSwipes() {
		this.setOnTouchListener(new OnSwipeTouchListener(getContext()) {
			@Override
			public void onSwipeLeft() {
				QuestionnaireView.this.weekChangeListener.onWeekIncrement();
			}

			@Override
			public void onSwipeRight() {
				QuestionnaireView.this.weekChangeListener.onWeekDecrement();
			}
		});
	}

	private void setupWeekNavigationButtons() throws Exception {
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

	private void setupQuestions() throws Exception {
		questions = new SparseArray<QuestionView>();
		for (int i = 0; i < getChildCount(); i++) {
			View view = getChildAt(i);
			if (!(view instanceof QuestionView)) continue;
			questions.put(Integer.valueOf(view.getId()), (QuestionView) view);

			view.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					try {
						onQuestionClicked((QuestionView) v);
					} catch (Exception e) {
						handleException(e);
					}
				}
			});
		}
	}

	private void syncUIWithModel() throws Exception {
		weekDisplay.setText(model.week.toString());

		for (int i = 0; i < this.getChildCount(); i++) {
			View view = this.getChildAt(i);
			if (!(view instanceof QuestionView)) {
				continue;
			}
			QuestionView qv = (QuestionView) view;
			syncAnswer(qv);
		}

		mondayTV.setText(model.week.getMondayString());
		sundayTV.setText(model.week.getSundayString());
	}

	private void syncAnswer(QuestionView qv) throws Exception {
		qv.setChecked(model.getAnswer(qv.getId()));
	}

	public void onQuestionClicked(QuestionView qv) throws Exception {
		boolean checked = qv.isChecked();

		int id = qv.getId();
		model.setAnswer(id, checked);
	}

	public interface OnWeekChangeListener {
		public void onWeekIncrement();
		public void onWeekDecrement();
	}


}
