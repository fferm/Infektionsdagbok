package se.fermitet.android.infektionsdagbok.views;

import se.fermitet.android.infektionsdagbok.app.Factory;
import se.fermitet.android.infektionsdagbok.app.InfektionsdagbokApplication;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public interface InfektionsdagbokView {

	public void handleException(Exception e);
}

class InfektionsdagbokRelativeLayoutView extends RelativeLayout implements InfektionsdagbokView {
	public InfektionsdagbokRelativeLayoutView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public void handleException(Exception e) {
		Factory factory = InfektionsdagbokApplication.getApplicationInstance().getFactory();
		InfektionsdagbokViewHandler handler = factory.createInfektionsdagbokViewHandler(getContext());
		
		handler.handleExceptionFromView(e);
	}
}

class InfektionsdabokLinearLayoutView extends LinearLayout implements InfektionsdagbokView {

	public InfektionsdabokLinearLayoutView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public void handleException(Exception e) {
		Factory factory = InfektionsdagbokApplication.getApplicationInstance().getFactory();
		InfektionsdagbokViewHandler handler = factory.createInfektionsdagbokViewHandler(getContext());
		
		handler.handleExceptionFromView(e);
	}
}

