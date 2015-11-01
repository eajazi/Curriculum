package reachingimmortality.com.curriculum.controllers;

import android.content.Context;
import android.graphics.Matrix;
import android.widget.ImageView;
import android.widget.Spinner;

import reachingimmortality.com.curriculum.ui_tour.TourActivity;

/**
 * Created by Reaching Immortality on 25.7.2015..
 */
public class LayoutController {

    //GET ITEM ID OF SPINNER DATA WHERE STRING = ?
    public int getIndex(Spinner spinner, String string) {
        int index = 0;

        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).equals(string)){
                index = i;
            }
        }
        return index;
    }

    //FIND HEIGHT OF STATUS BAR
    public int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    //FIND HEIGHT OF NAVIGATION BAR
    public int getNavigationBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    //SET PICTURES WITH MATRIX
    //***************************************************************************************************************
    //***************************************************************************************************************
    public void setPicWithMatrix(final ImageView image, int imgRes) {
        image.setImageResource(imgRes);
        image.post(new Runnable() {
            @Override
            public void run() {
                Matrix matrix = new Matrix();
                matrix.reset();

                float wv = image.getWidth();
                float hv = image.getHeight();

                float wi = image.getDrawable().getIntrinsicWidth();
                float hi = image.getDrawable().getIntrinsicHeight();

                float width = wv;
                float height = hv;

                if (wi / wv > hi / hv) {
                    matrix.setScale(hv / hi, hv / hi);
                    width = wi * hv / hi;
                } else {
                    matrix.setScale(wv / wi, wv / wi);
                    height = hi * wv / wi;
                }

                matrix.preTranslate((wv - width) / 2, (hv - height) / 2);
                image.setScaleType(ImageView.ScaleType.MATRIX);
                image.setImageMatrix(matrix);
            }
        });
    }
}
