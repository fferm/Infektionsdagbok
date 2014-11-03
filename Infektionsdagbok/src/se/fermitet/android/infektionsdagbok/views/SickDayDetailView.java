package se.fermitet.android.infektionsdagbok.views;

import org.joda.time.LocalDate;

import se.fermitet.android.infektionsdagbok.R;
import se.fermitet.android.infektionsdagbok.model.SickDay;
import se.fermitet.android.infektionsdagbok.widget.DateTextView;
import se.fermitet.android.infektionsdagbok.widget.DateTextView.OnModelChangedListener;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

public class SickDayDetailView extends InfektionsdagbokDetailView<SickDay> {

	private DateTextView startTV;
	private DateTextView endTV;
	private TextView startHeader;
	private TextView endHeader;

	public SickDayDetailView(Context context, AttributeSet attrs) {
		super(context, attrs, "Sjukskrivning");
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

	private void attachWidgets() throws Exception {
		startHeader = (TextView) findViewById(R.id.startHeader);
		startTV = (DateTextView) findViewById(R.id.startTV);
		endHeader = (TextView) findViewById(R.id.endHeader);
		endTV = (DateTextView) findViewById(R.id.endTV);
	}

	private void setupWidgets() throws Exception {
		setupStartTV();
		setupEndTV();
	}

	private void setupStartTV() {
		startTV.addAdditionalViewToOpenDialogWhenClicked(startHeader);
		startTV.setOnModelChangedListener(new OnModelChangedListener() {
			@Override
			public void onDateChangedTo(LocalDate newDate) throws Exception {
				SickDayDetailView.this.getModel().setStart(startTV.getModel());
			}
		});
	}

	private void setupEndTV() {
		endTV.addAdditionalViewToOpenDialogWhenClicked(endHeader);
		endTV.setOnModelChangedListener(new OnModelChangedListener() {
			@Override
			public void onDateChangedTo(LocalDate newDate) throws Exception {
				SickDayDetailView.this.getModel().setEnd(endTV.getModel());
			}
		});
	}



	@Override
	protected SickDay createEmptyModel() {
		return new SickDay();
	}

	@Override
	protected void syncUIWithModel() throws Exception {
		startTV.setModel(getModel().getStart());
		endTV.setModel(getModel().getEnd());
	}

}
