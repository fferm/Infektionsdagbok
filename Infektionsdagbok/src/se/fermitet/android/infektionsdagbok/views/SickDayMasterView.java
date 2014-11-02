package se.fermitet.android.infektionsdagbok.views;

import se.fermitet.android.infektionsdagbok.model.SickDay;
import android.content.Context;
import android.util.AttributeSet;

public class SickDayMasterView extends InfektionsdagbokMasterView<SickDay, SickDayAdapter> {

	public SickDayMasterView(Context context, AttributeSet attrs) {
		super(context, attrs, "Sjukskrivningar");
	}

}
