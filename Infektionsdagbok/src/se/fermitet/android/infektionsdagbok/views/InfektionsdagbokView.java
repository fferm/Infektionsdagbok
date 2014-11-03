package se.fermitet.android.infektionsdagbok.views;

import se.fermitet.android.infektionsdagbok.app.Factory;
import se.fermitet.android.infektionsdagbok.app.InfektionsdagbokApplication;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public interface InfektionsdagbokView {

	public void handleException(Exception e);
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

