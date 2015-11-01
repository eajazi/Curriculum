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
public class CursorExperienceItemAdapter extends CursorRecyclerAdapter<CursorExperienceItemAdapter.ViewHolder> {


    public CursorExperienceItemAdapter(Cursor c) {
        super(c);
    }

    @Override
    public void onBindViewHolder(CursorExperienceItemAdapter.ViewHolder holder, Cursor cursor) {
        holder.tvEmployer.setText(cursor.getString(cursor.getColumnIndex(MyContentProvider.WORK_EXPERIENCE_EMPLOYER)));
        holder.tvPosition.setText(cursor.getString(cursor.getColumnIndex(MyContentProvider.WORK_EXPERIENCE_POSITION)));
        holder.tvStartDate.setText(cursor.getString(cursor.getColumnIndex(MyContentProvider.WORK_EXPERIENCE_START_DATE)));
        holder.tvEndDate.setText(cursor.getString(cursor.getColumnIndex(MyContentProvider.WORK_EXPERIENCE_END_DATE)));

    }

    @Override
    public CursorExperienceItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_item_exp_edu, parent, false);
        ViewHolder vh = new ViewHolder(itemView);
        return vh;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvEmployer, tvStartDate,tvEndDate, tvPosition;

        public ViewHolder(View view) {
            super(view);
            tvEmployer = (TextView) view.findViewById(R.id.mainFragTitle);
            tvPosition = (TextView) view.findViewById(R.id.mainFragSubtitle);
            tvStartDate = (TextView) view.findViewById(R.id.mainFragStartDate);
            tvEndDate = (TextView) view.findViewById(R.id.mainFragEndDate);
        }
    }
}

