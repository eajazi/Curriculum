package reachingimmortality.com.curriculum.adapters.items;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import reachingimmortality.com.curriculum.R;
import reachingimmortality.com.curriculum.adapters.CursorRecyclerAdapter;
import reachingimmortality.com.curriculum.database_library.MyContentProvider;

/**
 * Created by ReachingIm on 10.10.2015..
 */
public class CursorEducationItemAdapter extends CursorRecyclerAdapter<CursorEducationItemAdapter.ViewHolder> {


    public CursorEducationItemAdapter(Cursor c) {
        super(c);
    }

    @Override
    public void onBindViewHolder(CursorEducationItemAdapter.ViewHolder holder, Cursor cursor) {
        holder.tvQualification.setText(cursor.getString(cursor.getColumnIndex(MyContentProvider.EDUCATION_QUALIFICATION_NAME)));
        holder.tvProvider.setText(cursor.getString(cursor.getColumnIndex(MyContentProvider.EDUCATION_PROVIDER)));
        holder.tvStartDate.setText(cursor.getString(cursor.getColumnIndex(MyContentProvider.EDUCATION_START_DATE)));
        holder.tvEndDate.setText(cursor.getString(cursor.getColumnIndex(MyContentProvider.EDUCATION_END_DATE)));

    }

    @Override
    public CursorEducationItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_item_exp_edu, parent, false);
        ViewHolder vh = new ViewHolder(itemView);
        return vh;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvQualification, tvStartDate,tvEndDate, tvProvider;

        public ViewHolder(View view) {
            super(view);
            tvQualification = (TextView) view.findViewById(R.id.mainFragTitle);
            tvProvider = (TextView) view.findViewById(R.id.mainFragSubtitle);
            tvStartDate = (TextView) view.findViewById(R.id.mainFragStartDate);
            tvEndDate = (TextView) view.findViewById(R.id.mainFragEndDate);
        }
    }
}

