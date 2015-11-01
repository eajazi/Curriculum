package reachingimmortality.com.curriculum.adapters;

import android.app.Notification;
import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.afollestad.materialdialogs.Theme;

import reachingimmortality.com.curriculum.R;
import reachingimmortality.com.curriculum.database_library.MyContentProvider;


/**
 * Created by Reaching Immortality on 2.7.2015..
 */
public class CustomCursorAdapterEvaluation extends SimpleCursorAdapter implements SpinnerAdapter {

    private Context mContext;
    private int layout;
    private Cursor cr;
    private final LayoutInflater inflater;
    private TextView tvLevel,tvDescription;
    private boolean TEXT_IS_DARK;

    public CustomCursorAdapterEvaluation(Context context, int layout, Cursor c, String[] from, int[] to, boolean TEXT_IS_DARK) {
        super(context,layout,c,from,to,0);
        this.layout=layout;
        this.mContext = context;
        this.inflater=LayoutInflater.from(context);
        this.cr=c;
        this.TEXT_IS_DARK = TEXT_IS_DARK;
    }

    @Override
    public View newView (Context context, Cursor cursor, ViewGroup parent) {
        return inflater.inflate(layout, null);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        super.bindView(view, context, cursor);

        tvLevel=(TextView)view.findViewById(R.id.evalMainLevel);
        tvDescription=(TextView)view.findViewById(R.id.evalMainLevelDescription);

        tvLevel.setText(cursor.getString(cursor.getColumnIndexOrThrow(MyContentProvider.EVALUATION_VIEW_LEVEL)));
        tvDescription.setText(cursor.getString(cursor.getColumnIndexOrThrow(MyContentProvider.EVALUATION_VIEW_DESCRIPTION)));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = View.inflate(mContext, R.layout.spinner_evaluation_selected, null);
        tvLevel=(TextView)view.findViewById(R.id.evalMainLevel);
        if(TEXT_IS_DARK){
            tvLevel.setTextColor(mContext.getResources().getColor(R.color.text_primary_dark));
        }else {
            tvLevel.setTextColor(mContext.getResources().getColor(R.color.colorAccent));
        }
        tvLevel.setText(cr.getString(cr.getColumnIndexOrThrow(MyContentProvider.EVALUATION_VIEW_LEVEL)));
        return view;
    }
}