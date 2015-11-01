package reachingimmortality.com.curriculum.ui_tour;


import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.squareup.picasso.Picasso;

import reachingimmortality.com.curriculum.interfaces.TourCallback;
import reachingimmortality.com.curriculum.R;
import reachingimmortality.com.curriculum.controllers.LayoutController;

/**
 * A simple {@link Fragment} subclass.
 */
public class ParallaxFragment extends Fragment {
    //TOUR CALLBACK / INTERFACE
    private TourCallback mCallback;

    //UI ELEMENTS
    private ImageView imgBgroung, imgTxtAbove, imgTxtBelow, imgLogo;
    private Button btNext;

    //LAYOUT MANAGER
    private LayoutController layoutController;

    //Variables
    private Boolean isLast;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);

        return inflater.inflate(R.layout.fragment_parallax, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //INIT OF UI ELEMENTS
        imgBgroung = (ImageView) view.findViewById(R.id.img_background);
        imgTxtAbove = (ImageView) view.findViewById(R.id.imgTxtParallaxAbove);
        imgTxtBelow = (ImageView) view.findViewById(R.id.imgTxtParallaxBelow);
        imgLogo = (ImageView) view.findViewById(R.id.imgParaLogo);

        btNext = (Button) view.findViewById(R.id.btTourNext);

        //SET LAYOUT BELOW STATUS BAR
        layoutController = new LayoutController();
        RelativeLayout layParallaxStart = (RelativeLayout) view.findViewById(R.id.layParralax);

        layParallaxStart.setPadding(0, layoutController.getStatusBarHeight(getActivity().getApplicationContext()),
                0,layoutController.getNavigationBarHeight(getActivity().getApplicationContext()));

        Bundle args = getArguments();

        isLast = args.getBoolean(TourActivity.TAG_IS_LAST);

        btNext.setVisibility(View.GONE);
        if(isLast){
            btNext.setVisibility(View.VISIBLE);
            btNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCallback.goToLogin();
                }
            });
        }
        //setPicWithMatrix(image);
        //SET PICS WITH PICASSO
        //*********************************************************************************************************+

        //BACKGROUND
        if(args.getInt(TourActivity.IMG_BGROUND_TAG) != 0)
            //Picasso.with(getActivity()).load(args.getInt(TourActivity.IMG_BGROUND_TAG)).
            //        centerCrop().fit().into(imgBgroung);
                setPicWithMatrix(imgBgroung);

        //LOGO
        if(args.getInt(TourActivity.IMG_LOGO_TAG) != 0)
            Picasso.with(getActivity()).load(args.getInt(TourActivity.IMG_LOGO_TAG)).
                    centerCrop().fit().into(imgLogo);

        //ABOVE TEXT
        if(args.getInt(TourActivity.IMG_TXT_ABOVE_TAG) != 0){
            Picasso.with(getActivity()).load(args.getInt(TourActivity.IMG_TXT_ABOVE_TAG)).
                    centerCrop().fit().into(imgTxtAbove);
        }

        //BELOW TEXT
        if(args.getInt(TourActivity.IMG_TXT_BELOW_TAG) != 0){
            Picasso.with(getActivity()).load(args.getInt(TourActivity.IMG_TXT_BELOW_TAG)).
                    centerCrop().fit().into(imgTxtBelow);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCallback = (TourCallback) activity;
    }

    private void setPicWithMatrix(final ImageView image) {
        image.setImageResource(getArguments().getInt(TourActivity.IMG_BGROUND_TAG));
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
