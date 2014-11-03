package se.fermitet.android.infektionsdagbok.widget;

import se.fermitet.android.infektionsdagbok.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class DateTextViewActivity extends Activity {

	private View contentView;
	private TextView header;
	private DateTextView dtv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		contentView =  View.inflate(this, R.layout.date_text_view, null);
        setContentView(contentView);

        attachWidgets();
        setupWidgets();
	}

	private void attachWidgets() {
		header = (TextView) findViewById(R.id.header);
		dtv = (DateTextView) findViewById(R.id.dateTextView);
	}

	private void setupWidgets() {
		dtv.addAdditionalViewToOpenDialogWhenClicked(header);
	}

	public TextView getHeader() {
		return this.header;
	}

	public DateTextView getDateTextView() {
		return dtv;
	}
}
