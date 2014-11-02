package se.fermitet.android.infektionsdagbok.views;

import se.fermitet.android.infektionsdagbok.R;
import se.fermitet.android.infektionsdagbok.app.Factory;
import se.fermitet.android.infektionsdagbok.app.InfektionsdagbokApplication;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public interface InfektionsdagbokView {

	public void handleException(Exception e);
}

class InfektionsdagbokRelativeLayoutView extends RelativeLayout implements InfektionsdagbokView {
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
	public void handleException(Exception e) {
		handler.handleExceptionFromView(e);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();

		if (headerText != null) {
			TextView titleTV = (TextView) findViewById(R.id.title);
			if (titleTV != null) titleTV.setText(headerText);
		}
	}

}

class InfektionsdabokLinearLayoutView extends LinearLayout implements InfektionsdagbokView {

	private InfektionsdagbokViewHandler handler;

	public InfektionsdabokLinearLayoutView(Context context, AttributeSet attrs) {
		super(context, attrs);

		Factory factory = InfektionsdagbokApplication.getApplicationInstance().getFactory();
		handler = factory.createInfektionsdagbokViewHandler(getContext());
}

	@Override
	public void handleException(Exception e) {
		handler.handleExceptionFromView(e);
	}
}

