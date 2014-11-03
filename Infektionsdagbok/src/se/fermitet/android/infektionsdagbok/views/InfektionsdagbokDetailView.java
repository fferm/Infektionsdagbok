package se.fermitet.android.infektionsdagbok.views;

import se.fermitet.android.infektionsdagbok.R;
import se.fermitet.android.infektionsdagbok.model.ModelObjectBase;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;

public abstract class InfektionsdagbokDetailView<ITEM extends ModelObjectBase> extends InfektionsdagbokRelativeLayoutView {
	private ImageButton saveBTN;
	private ImageButton cancelBTN;
	private ImageButton deleteBTN;

	private ITEM model;
	private OnDetailButtonsPressedListener<ITEM> onDetailButtonsPressedListener;

	protected abstract ITEM createEmptyModel();
	protected abstract void syncUIWithModel() throws Exception;

	public InfektionsdagbokDetailView(Context context, AttributeSet attrs, String headerText) {
		super(context, attrs, headerText);
		model = createEmptyModel();
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
		saveBTN = (ImageButton) findViewById(R.id.saveBTN);
		cancelBTN = (ImageButton) findViewById(R.id.cancelBTN);
		deleteBTN = (ImageButton) findViewById(R.id.deleteBTN);
	}

	private void setupWidgets() throws Exception {
		setupSaveBTN();
		setupCancelBTN();
		setupDeleteBTN();
	}

	private void setupSaveBTN() throws Exception {
		saveBTN.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					InfektionsdagbokDetailView.this.onDetailButtonsPressedListener.onSavePressed(getModel());
				} catch (Exception e) {
					handleException(e);
				}
			}
		});
	}

	private void setupCancelBTN() throws Exception {
		cancelBTN.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					InfektionsdagbokDetailView.this.onDetailButtonsPressedListener.onCancelPressed();
				} catch (Exception e) {
					handleException(e);
				}
			}
		});
	}


	private void setupDeleteBTN() throws Exception{
		deleteBTN.setEnabled(false);
		deleteBTN.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					InfektionsdagbokDetailView.this.onDetailButtonsPressedListener.onDeletePressed(getModel());
				} catch (Exception e) {
					handleException(e);
				}
			}
		});
	}

	public void setModel(ITEM item) throws Exception {
		this.model = item;
		deleteBTN.setEnabled(true);

		syncUIWithModel();
	}


	public ITEM getModel() throws Exception {
		return this.model;
	}

	public interface OnDetailButtonsPressedListener<ITEM extends ModelObjectBase> {
		public void onSavePressed(ITEM item) throws Exception;
		public void onCancelPressed() throws Exception;
		public void onDeletePressed(ITEM item) throws Exception;
	}

	public void setOnDetailButtonsPressedListener(OnDetailButtonsPressedListener<ITEM> listener) {
		this.onDetailButtonsPressedListener = listener;
	}





}
