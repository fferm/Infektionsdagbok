package se.fermitet.android.infektionsdagbok.views;

import se.fermitet.android.infektionsdagbok.R;
import se.fermitet.android.infektionsdagbok.model.ModelObjectBase;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;

public abstract class InfektionsdagbokMasterView<ITEM extends ModelObjectBase, ADAPTER extends InfektionsdagbokListAdapter<ITEM>> extends InfektionsdagbokRelativeLayoutView {

	private ImageButton editBTN;
	private ImageButton deleteBTN;
	private ImageButton newBTN;
	private ListView listView;
	private OnMasterButtonsPressedListener<ITEM> onMasterButtonsPressedListener;

	public InfektionsdagbokMasterView(Context context, AttributeSet attrs, String headerText) {
		super(context, attrs, headerText);
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
		listView = (ListView) findViewById(R.id.itemListView);

		editBTN = (ImageButton) findViewById(R.id.editBTN);
		deleteBTN = (ImageButton) findViewById(R.id.deleteBTN);
		newBTN = (ImageButton) findViewById(R.id.newBTN);
	}

	private void setupWidgets() throws Exception {
		setupEditBTN();
		setupDeleteBTN();
		setupNewBTN();
		setupListView();
	}

	private void setupListView() throws Exception {
		listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				try {
					clickOnPosition(position);
				} catch (Exception e) {
					handleException(e);
				}
			}
		});
	}

	private void setupEditBTN() throws Exception {
		editBTN.setEnabled(false);
		editBTN.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					@SuppressWarnings("unchecked")
					ADAPTER adapter = (ADAPTER) listView.getAdapter();
					ITEM selected = adapter.getSelectedItem();
					InfektionsdagbokMasterView.this.onMasterButtonsPressedListener.onEditPressed(selected);
				} catch (Exception e) {
					handleException(e);
				}
			}
		});
	}

	private void setupDeleteBTN() throws Exception {
		deleteBTN.setEnabled(false);
		deleteBTN.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					@SuppressWarnings("unchecked")
					ADAPTER adapter = (ADAPTER) listView.getAdapter();
					ITEM selected = adapter.getSelectedItem();
					InfektionsdagbokMasterView.this.onMasterButtonsPressedListener.onDeletePressed(selected);
				} catch (Exception e) {
					handleException(e);
				}
			}
		});
	}

	private void setupNewBTN() throws Exception{
		newBTN.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					InfektionsdagbokMasterView.this.onMasterButtonsPressedListener.onNewPressed();
				} catch (Exception e) {
					handleException(e);
				}
			}
		});
	}

	private void clickOnPosition(int position) throws Exception {
		if (!wasSelectedBefore(position)) {
			select(position);
			handleButtonsEnablement(true);
		} else {
			deselect(position);
			handleButtonsEnablement(false);
		}
		//		Treatment treatment = (Treatment) parent.getItemAtPosition(position);
		//		TreatmentMasterView.this.treatmentSingleEditView.selectTreatment(new Treatment(treatment));
	}

	private boolean wasSelectedBefore(int position) throws Exception {
		@SuppressWarnings("unchecked")
		ADAPTER adapter = (ADAPTER) listView.getAdapter();
		return (adapter.getSelectedPosition() != null && (adapter.getSelectedPosition()) == position);
	}

	private void select(int position) throws Exception{
		@SuppressWarnings("unchecked")
		ADAPTER adapter = (ADAPTER) listView.getAdapter();

		listView.setItemChecked(position, true);
		adapter.setSelectedPosition(position);
	}

	private void deselect(int position) throws Exception {
		@SuppressWarnings("unchecked")
		ADAPTER adapter = (ADAPTER) listView.getAdapter();

		listView.setItemChecked(position, false);
		adapter.setSelectedPosition(null);
	}

	private void handleButtonsEnablement(boolean isEnabled) {
		editBTN.setEnabled(isEnabled);
		deleteBTN.setEnabled(isEnabled);
	}

	public void setAdapter(ADAPTER adapter) {
		listView.setAdapter(adapter);
	}




	public void setOnMasterButtonsPressedListener(OnMasterButtonsPressedListener<ITEM> listener) {
		this.onMasterButtonsPressedListener = listener;
	}

	public interface OnMasterButtonsPressedListener<ITEM extends ModelObjectBase> {
		public void onNewPressed() throws Exception;
		public void onDeletePressed(ITEM item) throws Exception;
		public void onEditPressed(ITEM item) throws Exception;
	}


}
