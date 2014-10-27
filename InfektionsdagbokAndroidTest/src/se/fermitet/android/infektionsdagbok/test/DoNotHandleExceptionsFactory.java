package se.fermitet.android.infektionsdagbok.test;

import junit.framework.Assert;
import se.fermitet.android.infektionsdagbok.app.InfektionsdagbokFactory;
import se.fermitet.android.infektionsdagbok.views.InfektionsdagbokViewHandler;
import android.content.Context;

public class DoNotHandleExceptionsFactory extends InfektionsdagbokFactory {

	public DoNotHandleExceptionsFactory(Context context) {
		super(context);
	}
	
	@Override
	public InfektionsdagbokViewHandler createInfektionsdagbokViewHandler(Context context) {
		return new DoNotHandleExceptionsViewHandler(context);
	}

}

class DoNotHandleExceptionsViewHandler extends InfektionsdagbokViewHandler {

	public DoNotHandleExceptionsViewHandler(Context context) {
		super(context);
	}

	@Override
	public void handleExceptionFromView(Exception e)  {
		e.printStackTrace();
		Assert.fail(e.getMessage());
	}
}
