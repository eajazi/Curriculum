package reachingimmortality.com.curriculum.adapters;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import reachingimmortality.com.curriculum.R;
import reachingimmortality.com.curriculum.database_library.MyContentProvider;


/**
 * Created by Ajki on 19.6.2015..
 */
public class CursorExperienceAdapter extends CursorRecyclerAdapter<CursorExperienceAdapter.ViewHolder> {


    public CursorExperienceAdapter(Cursor c) {
        super(c);
    }

    @Override
    public void onBindViewHolder(CursorExperienceAdapter.ViewHolder holder, Cursor cursor) {
        holder.tvEmployer.setText(cursor.getString(cursor.getColumnIndex(MyContentProvider.WORK_EXPERIENCE_EMPLOYER)));
        holder.tvPosition.setText(cursor.getString(cursor.getColumnIndex(MyContentProvider.WORK_EXPERIENCE_POSITION)));
        holder.tvStartDate.setText(cursor.getString(cursor.getColumnIndex(MyContentProvider.WORK_EXPERIENCE_START_DATE)));
        holder.tvEndDate.setText(cursor.getString(cursor.getColumnIndex(MyContentProvider.WORK_EXPERIENCE_END_DATE)));

    }

    @Override
    public CursorExperienceAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_card_item_experience, parent, false);
        ViewHolder vh = new ViewHolder(itemView);
        return vh;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvEmployer, tvStartDate,tvEndDate, tvPosition;

        public ViewHolder(View view) {
            super(view);
            tvEmployer = (TextView) view.findViewById(R.id.irExpEmployer);
            tvPosition = (TextView) view.findViewById(R.id.irExpPosition);
            tvStartDate = (TextView) view.findViewById(R.id.irExpStartDate);
            tvEndDate = (TextView) view.findViewById(R.id.irExpEndDate);
        }
    }
}
