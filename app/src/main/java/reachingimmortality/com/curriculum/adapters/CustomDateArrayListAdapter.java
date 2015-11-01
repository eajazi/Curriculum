package reachingimmortality.com.curriculum.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import reachingimmortality.com.curriculum.R;

/**
 * Created by ReachingIm on 11.10.2015..
 */
public class CustomDateArrayListAdapter extends ArrayAdapter<String> {

    private ViewHolder viewHolder;
    int layoutResourceId;

    private static class ViewHolder {
        private TextView itemView;
    }

    public CustomDateArrayListAdapter(Context context, int layoutResourceId, ArrayList<String> items) {
        super(context, layoutResourceId, items);
        this.layoutResourceId = layoutResourceId;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(this.getContext())
                    .inflate(layoutResourceId, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.itemView = (TextView) convertView.findViewById(R.id.spinnerDropdownItem);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        String item = getItem(position);
        if (item!= null) {
            viewHolder.itemView.setText(item);
        }

        return convertView;
    }
}
