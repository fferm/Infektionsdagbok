package se.fermitet.android.infektionsdagbok.widget;

import java.text.DateFormat;

import org.joda.time.LocalDate;

import se.fermitet.android.infektionsdagbok.app.Factory;
import se.fermitet.android.infektionsdagbok.app.InfektionsdagbokApplication;
import se.fermitet.android.infektionsdagbok.views.InfektionsdagbokView;
import se.fermitet.android.infektionsdagbok.views.InfektionsdagbokViewHandler;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

public class DateTextView extends TextView implements InfektionsdagbokView, OnDateSetListener {

	private DatePickerDialog dp;
	private LocalDate model;
	private InfektionsdagbokViewHandler handler;
	private OnModelChangedListener onModelChangedListener;

	private static final DateFormat df;

	static {
		df = DateFormat.getDateInstance(DateFormat.SHORT);
	}

	public DateTextView(Context ctx) {
		super(ctx);

		handleFactory();
	}

	public DateTextView(Context ctx, AttributeSet attrs) {
		super(ctx, attrs);

		handleFactory();
	}

	public DateTextView(Context ctx, AttributeSet attrs, int defStyleAttr) {
		super(ctx, attrs, defStyleAttr);

		handleFactory();
	}

	private void handleFactory() {
		Factory factory = InfektionsdagbokApplication.getApplicationInstance().getFactory();
		handler = factory.createInfektionsdagbokViewHandler(getContext());
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		try {
			setupMainView();
		} catch (Exception e) {
			handleException(e);
		}
	}

	public LocalDate getModel() throws Exception {
		return this.model;
	}


	private void setupMainView() throws Exception {
		setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					dateClicked();
				} catch (Exception e) {
					handleException(e);
				}
			}
		});
	}

	public void addAdditionalViewToOpenDialogWhenClicked(View v) {
		v.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					dateClicked();
				} catch (Exception e) {
					handleException(e);
				}
			}
		});
	}

	public void removeAdditionalViewToOpenDialogWhenClicked(View v) {
		v.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Do nothing
			}
		});
	}


	private void dateClicked() throws Exception {
		if (model == null) model = LocalDate.now();

		dp = new DatePickerDialog(getContext(), this, model.getYear(), model.getMonthOfYear() - 1, model.getDayOfMonth());

		dp.show();
	}

	public DatePickerDialog getDatePickerDialog() throws Exception {
		return dp;
	}

	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
		try {
			LocalDate newDate = LocalDate.now().withYear(year).withMonthOfYear(monthOfYear + 1).withDayOfMonth(dayOfMonth);
			model = newDate;

			syncUIWithModel();

			if (onModelChangedListener != null)	onModelChangedListener.onDateChangedTo(newDate);
		} catch (Exception e) {
			handleException(e);
		}
	}

	private void syncUIWithModel() throws Exception {
		if (model == null) {
			setText(null);
		} else {
			setText(df.format(model.toDate()));
		}
	}

	@Override
	public void handleException(Exception e) {
		handler.handleExceptionFromView(e);
	}

	public void setOnModelChangedListener(OnModelChangedListener listener) {
		this.onModelChangedListener = listener;
	}

	public void removeOnModelChangedListener() {
		this.onModelChangedListener = null;
	}

	public interface OnModelChangedListener {
		public void onDateChangedTo(LocalDate newDate) throws Exception;
	}
}
