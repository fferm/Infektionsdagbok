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

public class QuestionView extends LinearLayout {

	private static final String TEXT_ATTRIBUTE_NAME = "text";
	private static final String ANDROID_NAMESPACE = "http://schemas.android.com/apk/res/android";

	private final String questionText;

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
	}

	public boolean isChecked() {
		return getAnswerSelector().isChecked();
	}

	@Override
	public void setEnabled(boolean enabled) {
		getAnswerSelector().setEnabled(enabled);
	}

	private void setupQuestionItems() {
		TextView questionTV = (TextView) findViewById(R.id.questionText);
		questionTV.setText(this.questionText);

		getAnswerSelector().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				QuestionView.this.performClick();
			}
		});
	}

	private CompoundButton getAnswerSelector() {
		return (CompoundButton) findViewById(R.id.answerSelector);
	}
}
