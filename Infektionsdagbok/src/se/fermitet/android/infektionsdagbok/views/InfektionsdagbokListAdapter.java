package se.fermitet.android.infektionsdagbok.views;

import java.util.List;

import se.fermitet.android.infektionsdagbok.R;
import se.fermitet.android.infektionsdagbok.model.ModelObjectBase;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public abstract class InfektionsdagbokListAdapter<T extends ModelObjectBase> extends ArrayAdapter<T> {

	private List<T> values;
	private Integer selectedPosition;
	private LayoutInflater inflater;
	private int textViewResourceId;

	public InfektionsdagbokListAdapter(Context context, int textViewResourceId, List<T> values) {
		super(context, textViewResourceId, values);

		this.values = values;
		this.textViewResourceId = textViewResourceId;

		this.inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView = inflater.inflate(this.textViewResourceId, parent, false);

		T item = values.get(position);

		setupRowView(rowView, item);

		highlightItemIfSelected(position, rowView);

		return rowView;
	}

	protected abstract void setupRowView(View rowView, T item);

	private void highlightItemIfSelected(int position, View view) {
		if (view instanceof ViewGroup) {
			highlightChildren(position, (ViewGroup) view);
		} else {
			if(selectedPosition != null && position == selectedPosition) {
				view.setBackground(getContext().getResources().getDrawable(R.drawable.background_selected_item));
			} else {
				view.setBackground(getContext().getResources().getDrawable(R.drawable.background_not_selected_item));
			}
		}
	}

	private void highlightChildren(int position, ViewGroup viewGroup) {
		for (int i = 0; i < viewGroup.getChildCount(); i++) {
			View child = viewGroup.getChildAt(i);

			highlightItemIfSelected(position, child);
		}
	}

	public void setSelectedPosition(Integer selectedPosition) {
		this.selectedPosition = selectedPosition;
	}

	public Integer getSelectedPosition() {
		return this.selectedPosition;
	}

	public T getSelectedItem() {
		Integer position = getSelectedPosition();
		if (position == null) {
			return null;
		} else {
			return getItem(position);
		}
	}


}
