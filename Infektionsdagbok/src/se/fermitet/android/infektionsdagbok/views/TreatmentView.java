package se.fermitet.android.infektionsdagbok.views;

import java.util.Collection;

import se.fermitet.android.infektionsdagbok.R;
import se.fermitet.android.infektionsdagbok.model.Treatment;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

public class TreatmentView extends RelativeLayout {

	private ListView listView;

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
	}


	// TODO: Maybe the activity should manage the adapter?
	public void setTreatmentsToShow(Collection<Treatment> allTreatments) {
			ArrayAdapter<Treatment> adapter = new ArrayAdapter<Treatment>(getContext(), android.R.layout.simple_list_item_1);
			adapter.addAll(allTreatments);

			listView.setAdapter(adapter);
	}
}
