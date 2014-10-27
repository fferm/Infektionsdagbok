package se.fermitet.android.infektionsdagbok.views;

import android.content.Context;
import android.widget.Toast;

public class InfektionsdagbokViewHandler  {
	private Context context;

	public InfektionsdagbokViewHandler(Context context) {
		this.context = context;
	}
	public void handleExceptionFromView(Exception e) {
		e.printStackTrace();
		notifyUserWithMessage(e.getMessage() + "\n" + e.getClass().getName());
	}
	
	protected void notifyUserWithMessage(String msg) {
		Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
		toast.show();
	}
}
