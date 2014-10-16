package se.fermitet.android.infektionsdagbok.storage;

import java.io.File;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class EmailHandler {

	public EmailHandler() {
		super();
	}

	public void sendEmail(File file, Context context) {
		Intent intent = prepareIntent(file);
		context.startActivity(intent);
	}

	Intent prepareIntent(File file) {
		String subject = "Infektionsdagbok";
		String message = "HŠr kommer min senaste infektionsdagbok";

		Intent intent = createIntent(Intent.ACTION_SEND);

		intent.setType("application/vnd.ms-excel");

		intent.putExtra(Intent.EXTRA_SUBJECT, subject);

		intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
		intent.putExtra(android.content.Intent.EXTRA_TEXT, message);

		return intent;
	}

	protected Intent createIntent(String action) {
		return new Intent(action);
	}
}
