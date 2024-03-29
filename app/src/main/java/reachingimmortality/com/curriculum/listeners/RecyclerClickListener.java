package reachingimmortality.com.curriculum.listeners;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class RecyclerClickListener implements RecyclerView.OnItemTouchListener {
    private OnItemClickListener mListener;
    @Nullable
    private View childView;

    private int childViewPosition;

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);

        public void onItemLongPress(View childView, int position);

    }

    GestureDetector mGestureDetector;

    public RecyclerClickListener(Context context, OnItemClickListener listener) {
        mListener = listener;
        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }

            @Override
            public void onLongPress(MotionEvent event) {
                if (childView != null) {
                    mListener.onItemLongPress(childView, childViewPosition);
                }
            }
        });

    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e) {
        childView = view.findChildViewUnder(e.getX(), e.getY());
        childViewPosition = view.getChildPosition(childView);
        if (childView != null && mListener != null && mGestureDetector.onTouchEvent(e)) {
            mListener.onItemClick(childView, childViewPosition);


        }
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView view, MotionEvent motionEvent) {
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }
}
