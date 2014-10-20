package se.fermitet.android.infektionsdagbok.views;

import se.fermitet.android.infektionsdagbok.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;

public class TreatmentView extends RelativeLayout {

	private ListView listView;
	private View header;

	public TreatmentView(Context context, AttributeSet attrs) {
		super(context, attrs);

//		header = View.inflate(getContext(), R.layout.treatment_item, this);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		attachWidgets();
		setupWidgets();
	}

	private void attachWidgets() {
		this.listView = (ListView) findViewById(R.id.treatmentListView);

	}

	private void setupWidgets() {
		this.listView.addHeaderView(View.inflate(getContext(), R.layout.treatment_item, listView));
	}
}
