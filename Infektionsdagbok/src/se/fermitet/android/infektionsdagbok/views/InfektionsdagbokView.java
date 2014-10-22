package se.fermitet.android.infektionsdagbok.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

public interface InfektionsdagbokView {

	public void handleException(Exception e);
}

class InfektionsdagbokRelativeLayoutView extends RelativeLayout implements InfektionsdagbokView {

	private InfektionsdagbokViewImpl impl;

	public InfektionsdagbokRelativeLayoutView(Context context, AttributeSet attrs) {
		super(context, attrs);
		impl = new InfektionsdagbokViewImpl(context);
	}

	@Override
	public void handleException(Exception e) {
		impl.handleException(e);
	}
}

class InfektionsdabokLinearLayoutView extends LinearLayout implements InfektionsdagbokView {

	private InfektionsdagbokViewImpl impl;

	public InfektionsdabokLinearLayoutView(Context context, AttributeSet attrs) {
		super(context, attrs);
		impl = new InfektionsdagbokViewImpl(context);
	}

	@Override
	public void handleException(Exception e) {
		impl.handleException(e);
	}
}

class InfektionsdagbokViewImpl {
	private Context context;

	public InfektionsdagbokViewImpl(Context context) {
		this.context = context;
	}
	public void handleException(Exception e) {
		e.printStackTrace();
		notifyUserWithMessage(e.getMessage() + "\n" + e.getClass().getName());
	}
	
	protected void notifyUserWithMessage(String msg) {
		Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
		toast.show();
	}
}
