package se.fermitet.android.infektionsdagbok.views;

import se.fermitet.android.infektionsdagbok.R;
import se.fermitet.android.infektionsdagbok.app.Factory;
import se.fermitet.android.infektionsdagbok.app.InfektionsdagbokApplication;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

public abstract class InfektionsdagbokRelativeLayoutView extends RelativeLayout implements InfektionsdagbokView {
	private InfektionsdagbokViewHandler handler;
	private String headerText;

	public InfektionsdagbokRelativeLayoutView(Context context, AttributeSet attrs) {
		super(context, attrs);

		Factory factory = InfektionsdagbokApplication.getApplicationInstance().getFactory();
		handler = factory.createInfektionsdagbokViewHandler(getContext());

		handler.setViewDefaults(this);
	}

	public InfektionsdagbokRelativeLayoutView(Context context, AttributeSet attrs, String headerText) {
		this(context, attrs);

		this.headerText = headerText;
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

	protected void attachWidgets() throws Exception {
	}

	protected void setupWidgets() throws Exception {
		if (headerText != null) {
			TextView titleTV = (TextView) findViewById(R.id.title);
			if (titleTV != null) titleTV.setText(headerText);
		}
	}


	@Override
	public void handleException(Exception e) {
		handler.handleExceptionFromView(e);
	}
}

