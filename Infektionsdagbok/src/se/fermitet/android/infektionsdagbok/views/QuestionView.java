package se.fermitet.android.infektionsdagbok.views;

import se.fermitet.android.infektionsdagbok.R;
import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

public class QuestionView extends InfektionsdabokLinearLayoutView implements View.OnClickListener {

	private static final String TEXT_ATTRIBUTE_NAME = "text";
	private static final String ANDROID_NAMESPACE = "http://schemas.android.com/apk/res/android";

	private String questionText;

	public QuestionView(Context context, AttributeSet attrs) {
		super(context, attrs);

		questionText = attrs.getAttributeValue(ANDROID_NAMESPACE, TEXT_ATTRIBUTE_NAME);
	}


	@Override
	protected void onFinishInflate() {
		try {
			super.onFinishInflate();

			Activity ctx = (Activity) getContext();
			LayoutInflater inflater = ctx.getLayoutInflater();
			inflater.inflate(R.layout.question, this);

			setupQuestionItems();
		} catch (Exception e) {
			handleException(e);
		}
	}

	public void setChecked(boolean checked) throws Exception {
		getAnswerSelector().setChecked(checked);
		setBackgroundBasedOnCheckedStatus();
	}

	public boolean isChecked() throws Exception {
		return getAnswerSelector().isChecked();
	}

	private void setupQuestionItems() throws Exception {
		TextView questionTV = (TextView) findViewById(R.id.questionText);
		questionTV.setText(this.questionText);
		questionTV.setOnClickListener(this);

		getAnswerSelector().setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		try {
			if (v == getAnswerSelector()) {
				this.performClick();
				setBackgroundBasedOnCheckedStatus();
			} else {
				getAnswerSelector().performClick();
			}
		} catch (Exception e) {
			handleException(e);
		}
	}

	private void setBackgroundBasedOnCheckedStatus() throws Exception {
		boolean checked = this.isChecked();

		if (checked) {
			this.setBackgroundColor(getContext().getResources().getColor(R.color.selectedQuestionBackground));
		} else {
			this.setBackground(null);
		}
	}

	private CompoundButton getAnswerSelector() throws Exception {
		return (CompoundButton) findViewById(R.id.answerSelector);
	}

}
