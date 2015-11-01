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
 * Created by Reaching Immortality on 1.7.2015..
 */
public class CursorEducationAdapter extends CursorRecyclerAdapter<CursorEducationAdapter.Holder> {
    public CursorEducationAdapter(Cursor c) {
        super(c);
    }

    @Override
    public void onBindViewHolder(Holder holder, Cursor cursor) {
        holder.tvQualification.setText(cursor.getString(cursor.getColumnIndex(MyContentProvider.EDUCATION_QUALIFICATION_NAME)));
        holder.tvProvider.setText(cursor.getString(cursor.getColumnIndex(MyContentProvider.EDUCATION_PROVIDER)));
        holder.tvStartDate.setText(cursor.getString(cursor.getColumnIndex(MyContentProvider.EDUCATION_START_DATE)));
        holder.tvEndDate.setText(cursor.getString(cursor.getColumnIndex(MyContentProvider.EDUCATION_END_DATE)));
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_item_education, parent, false);
        return new Holder(itemView);
    }


    public class Holder extends RecyclerView.ViewHolder{
        public TextView tvQualification, tvStartDate,tvEndDate, tvProvider;

        public Holder(View view) {
            super(view);
            tvQualification = (TextView) view.findViewById(R.id.irEduQualification);
            tvProvider = (TextView) view.findViewById(R.id.irEduProvider);
            tvStartDate = (TextView) view.findViewById(R.id.irEduStartDate);
            tvEndDate = (TextView) view.findViewById(R.id.irEduEndDate);
        }
    }
}
