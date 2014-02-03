package se.fermitet.android.infektionsdagbok.views;

import se.fermitet.android.infektionsdagbok.R;
import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class QuestionView extends LinearLayout implements View.OnClickListener {

	private static final String TEXT_ATTRIBUTE_NAME = "text";
	private static final String ANDROID_NAMESPACE = "http://schemas.android.com/apk/res/android";

	private String questionText;

	public QuestionView(Context context, AttributeSet attrs) {
		super(context, attrs);

		questionText = attrs.getAttributeValue(ANDROID_NAMESPACE, TEXT_ATTRIBUTE_NAME);
	}


	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();

		Activity ctx = (Activity) getContext();
		LayoutInflater inflater = ctx.getLayoutInflater();
		inflater.inflate(R.layout.question, this);

		setupQuestionItems();
	}

	public void setChecked(boolean checked) {
		getAnswerSelector().setChecked(checked);
		setBackgroundBasedOnCheckedStatus();
	}

	public boolean isChecked() {
		return getAnswerSelector().isChecked();
	}

	@Override
	public void setEnabled(boolean enabled) {
		getAnswerSelector().setEnabled(enabled);
	}

	@Override
	public boolean isEnabled() {
		return getAnswerSelector().isEnabled();
	}

	private void setupQuestionItems() {
		TextView questionTV = (TextView) findViewById(R.id.questionText);
		questionTV.setText(this.questionText);
		questionTV.setOnClickListener(this);

		getAnswerSelector().setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v == getAnswerSelector()) {
			this.performClick();
			setBackgroundBasedOnCheckedStatus();
		} else {
			if (this.isEnabled()) {
				getAnswerSelector().performClick();
			}
		}
	}

	private void setBackgroundBasedOnCheckedStatus() {
		boolean checked = this.isChecked();

		if (checked) {
			this.setBackgroundColor(getContext().getResources().getColor(R.color.selectedQuestionBackground));
		} else {
			this.setBackground(null);
		}
	}

	private CompoundButton getAnswerSelector() {
		return (CompoundButton) findViewById(R.id.answerSelector);
	}
}
