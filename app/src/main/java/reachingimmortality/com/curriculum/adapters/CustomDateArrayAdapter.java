package reachingimmortality.com.curriculum.adapters;

import android.app.Activity;
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
public class CustomDateArrayAdapter extends ArrayAdapter{

    Context context;
    int layoutResourceId;
    String dateValue[] = null;

    public CustomDateArrayAdapter(Context context, int layoutResourceId, String[] data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.dateValue = data;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ItemHolder holder = null;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new ItemHolder();
            holder.tvSpinItem = (TextView)row.findViewById(R.id.spinnerDropdownItem);

            row.setTag(holder);
        }
        else
        {
            holder = (ItemHolder)row.getTag();
        }

        String data = dateValue[position];
        holder.tvSpinItem.setText(data);

        return row;
    }

    static class ItemHolder
    {
        TextView tvSpinItem;
    }
}