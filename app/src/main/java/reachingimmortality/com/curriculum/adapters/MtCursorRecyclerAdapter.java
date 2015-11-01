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
 * Created by Reaching Infinity on 26.6.2015..
 */
public class MtCursorRecyclerAdapter extends CursorRecyclerAdapter<MtCursorRecyclerAdapter.ViewHolder>  {

    public MtCursorRecyclerAdapter(Cursor c) {
        super(c);
    }

    @Override
    public void onBindViewHolder(MtCursorRecyclerAdapter.ViewHolder holder, Cursor cursor) {
        holder.tvMotherTongue.setText(cursor.getString(cursor.getColumnIndex(MyContentProvider.USER_VIEW_LANGUAGE_NAME)));
    }

    @Override
    public MtCursorRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_item_section_language, parent, false);
        ViewHolder vh = new ViewHolder(itemView);
        return vh;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvMotherTongue;

        public ViewHolder(View view) {
            super(view);
            tvMotherTongue = (TextView) view.findViewById(R.id.skillMainRowLang);
        }
    }
}
