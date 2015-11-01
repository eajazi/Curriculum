package reachingimmortality.com.curriculum.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import reachingimmortality.com.curriculum.R;
import reachingimmortality.com.curriculum.database_library.MyContentProvider;

/**
 * Created by ReachingIm on 10.10.2015..
 */
public class MySpinnerAdapter extends CursorAdapter {
    private Context context;

    public MySpinnerAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, 0);
        this.context = context;

    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.spinner_item_row_dropdown, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView tvType = (TextView) view.findViewById(R.id.spinnerDropdownItem);

        tvType.setText(cursor.getString(cursor.getColumnIndex(MyContentProvider.APPLICATION_TYPE_NAME)));
    }

    @Override
    public View getDropDownView(int position, View view, ViewGroup parent) {
        if (view == null || !view.getTag().toString().equals("DROPDOWN")) {
            LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = li.inflate(R.layout.spinner_item_row_dropdown, parent, false);
            view.setTag("DROPDOWN");
        }

        //TextView textView = (TextView) view.findViewById(android.R.id.text1);
        //textView.setText(getTitle(position));

        return view;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null || !view.getTag().toString().equals("NON_DROPDOWN")) {
            LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = li.inflate(R.layout.
                    spinner_item_row_prompt, parent, false);
            view.setTag("NON_DROPDOWN");
        }
        //TextView textView = (TextView) view.findViewById(android.R.id.text1);
        //textView.setText(getTitle(position));
        return view;
    }
}
