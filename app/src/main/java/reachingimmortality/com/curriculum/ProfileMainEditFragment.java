package reachingimmortality.com.curriculum;

import android.app.Activity;
import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.software.shell.fab.ActionButton;

import reachingimmortality.com.curriculum.database_library.MyContentProvider;
import reachingimmortality.com.curriculum.http_calls.JSONFunctions;
import reachingimmortality.com.curriculum.interfaces.MainCallback;


public class ProfileMainEditFragment extends Fragment implements View.OnClickListener, View.OnFocusChangeListener {
    //UI elements
    private TextView tvTitleAbout,tvTitleMail, tvTitleLoc, tvTitleTel, tvTitleWeb;
    private EditText etName,etSurname,etMail,etStreetAdress,etCity,etPostalCode, etCountry, etWebBlog,etTelNumber;
    private ImageView icColorAbout,icAbout,icColorMail, icMail,icColorLocation, icLocation,icColorTel, icTel,icColorWeb, icWeb;
    private ActionButton fabProMainEdit;

    //Controllers
    private MainCallback mainCallback;
    private JSONFunctions jsonFunctions;

    public ProfileMainEditFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile_main_edit, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //INIT UI ELEMENTS
        etName = (EditText) view.findViewById(R.id.proMainEditName);
        etSurname = (EditText) view.findViewById(R.id.proMainEditSurname);
        etMail = (EditText) view.findViewById(R.id.proMainEditMail);
        etStreetAdress = (EditText) view.findViewById(R.id.proMainEditStreetAdr);
        etCity = (EditText) view.findViewById(R.id.proMainEditCity);
        etPostalCode = (EditText) view.findViewById(R.id.proMainEditPostal);
        etCountry = (EditText) view.findViewById(R.id.proMainEditCountry);
        etTelNumber = (EditText) view.findViewById(R.id.proMainEditTel);
        etWebBlog = (EditText) view.findViewById(R.id.proMainEditWeb);

        tvTitleAbout = (TextView) view.findViewById(R.id.proMainTitlAbout);
        tvTitleMail = (TextView) view.findViewById(R.id.proMainTitlMail);
        tvTitleLoc = (TextView) view.findViewById(R.id.proMainTitlLoc);
        tvTitleTel = (TextView) view.findViewById(R.id.proMainTitlTel);
        tvTitleWeb = (TextView) view.findViewById(R.id.proMainTitlWeb);

        //IMAGE VIEWS
        icColorAbout = (ImageView) view.findViewById(R.id.circleColorAbout);
        icAbout = (ImageView) view.findViewById(R.id.circleIconAbout);

        icColorMail = (ImageView) view.findViewById(R.id.circleColorMail);
        icMail = (ImageView) view.findViewById(R.id.circleIconMail);

        icColorLocation = (ImageView) view.findViewById(R.id.circleColorLocation);
        icLocation = (ImageView) view.findViewById(R.id.expMainCircleIcon);

        icColorTel = (ImageView) view.findViewById(R.id.circleColorTel);
        icTel = (ImageView) view.findViewById(R.id.circleIconTelephone);

        icColorWeb = (ImageView) view.findViewById(R.id.circleColorWeb);
        icWeb = (ImageView) view.findViewById(R.id.circleIconWeb);

        //FAB ANIMATIONS
        fabProMainEdit = (ActionButton) getActivity().findViewById(R.id.fabMain);

        fabProMainEdit.setImageResource(R.mipmap.ic_checked_white);
        fabProMainEdit.setButtonColor(getResources().getColor(R.color.colorAccent));
        fabProMainEdit.setButtonColorRipple(android.R.color.white);

        fabProMainEdit.setShowAnimation(ActionButton.Animations.ROLL_FROM_RIGHT);
        fabProMainEdit.setHideAnimation(ActionButton.Animations.ROLL_TO_RIGHT);

        fabProMainEdit.hide();
        fabProMainEdit.playShowAnimation();
        fabProMainEdit.show();

        fabProMainEdit.setOnClickListener(this);

        //CHECK IF THERE'S DATA FOR PROFILE
        Cursor cursor = getActivity().getContentResolver().query(MyContentProvider.CONTENT_URI_PROFILE,null,null,null,null);
        if(cursor != null) {
            if (cursor.moveToFirst()) {
                etName.setText(cursor.getString(cursor.getColumnIndex(MyContentProvider.PROFILE_NAME)));
                etSurname.setText(cursor.getString(cursor.getColumnIndex(MyContentProvider.PROFILE_SURNAME)));
                etMail.setText(cursor.getString(cursor.getColumnIndex(MyContentProvider.PROFILE_EMAIL)));
                etStreetAdress.setText(cursor.getString(cursor.getColumnIndex(MyContentProvider.PROFILE_STREET_ADRESS)));
                etCity.setText(cursor.getString(cursor.getColumnIndex(MyContentProvider.PROFILE_CITY)));
                etCountry.setText(cursor.getString(cursor.getColumnIndex(MyContentProvider.PROFILE_COUNTRY)));
                etPostalCode.setText(cursor.getString(cursor.getColumnIndex(MyContentProvider.PROFILE_POSTAL_CODE)));
                etTelNumber.setText(cursor.getString(cursor.getColumnIndex(MyContentProvider.PROFILE_TEL_NUMBER)));
                etWebBlog.setText(cursor.getString(cursor.getColumnIndex(MyContentProvider.PROFILE_WEB_BLOG)));
            }
            cursor.close();
        }

        etName.setOnFocusChangeListener(this);
        etSurname.setOnFocusChangeListener(this);
        etMail.setOnFocusChangeListener(this);
        etStreetAdress.setOnFocusChangeListener(this);
        etPostalCode.setOnFocusChangeListener(this);
        etCity.setOnFocusChangeListener(this);
        etCountry.setOnFocusChangeListener(this);
        etTelNumber.setOnFocusChangeListener(this);
        etWebBlog.setOnFocusChangeListener(this);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try{
            mainCallback = (MainCallback) activity;
        }catch (Exception ex){ex.printStackTrace();}
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        //ABOUT SECTION

            if (etName.hasFocus() || etSurname.hasFocus()){
                tvTitleAbout.setTextColor(getResources().getColor(R.color.colorAccent));
                icColorAbout.setImageResource(R.drawable.icon_circle_red);
                icAbout.bringToFront();
            }else{
                tvTitleAbout.setTextColor(getResources().getColor(R.color.text_primary_light));
                icColorAbout.setImageResource(R.drawable.icon_circle);
                icAbout.bringToFront();
            }

        //TEL SECTION

            if (etMail.hasFocus()){
                tvTitleMail.setTextColor(getResources().getColor(R.color.colorAccent));
                icColorMail.setImageResource(R.drawable.icon_circle_red);
                icMail.bringToFront();
            }else{
                tvTitleMail.setTextColor(getResources().getColor(R.color.text_primary_light));
                icColorMail.setImageResource(R.drawable.icon_circle);
                icMail.bringToFront();
            }

        //LOCATION SECTION


            if (etStreetAdress.hasFocus() || etPostalCode.hasFocus() || etCity.hasFocus() || etCountry.hasFocus()){
                tvTitleLoc.setTextColor(getResources().getColor(R.color.colorAccent));
                icColorLocation.setImageResource(R.drawable.icon_circle_red);
                icLocation.bringToFront();
            }else{
                tvTitleLoc.setTextColor(getResources().getColor(R.color.text_primary_light));
                icColorLocation.setImageResource(R.drawable.icon_circle);
                icLocation.bringToFront();
            }


        //TEL SECTION

            if (etTelNumber.hasFocus()){
                tvTitleTel.setTextColor(getResources().getColor(R.color.colorAccent));
                icColorTel.setImageResource(R.drawable.icon_circle_red);
                icTel.bringToFront();
            }else{
                tvTitleTel.setTextColor(getResources().getColor(R.color.text_primary_light));
                icColorTel.setImageResource(R.drawable.icon_circle);
                icTel.bringToFront();
            }


        //WEBSITE PROFILE

            if (etWebBlog.hasFocus()){
                tvTitleWeb.setTextColor(getResources().getColor(R.color.colorAccent));
                icColorWeb.setImageResource(R.drawable.icon_circle_red);
                icWeb.bringToFront();
            }else{
                tvTitleWeb.setTextColor(getResources().getColor(R.color.text_primary_light));
                icColorWeb.setImageResource(R.drawable.icon_circle);
                icWeb.bringToFront();
            }
    }


    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.fabMain:

                fabProMainEdit.hide();
                fabProMainEdit.playHideAnimation();
                new Handler().postDelayed(getShowRunnable(), MainActivity.ACTION_BUTTON_POST_DELAY_MS);

                break;
        }
    }

    private Runnable getShowRunnable() {
        return new Runnable() {
            @Override
            public void run() {
                jsonFunctions = new JSONFunctions();

                String profile = jsonFunctions.composeProfileList(
                        etName.getText().toString(),
                        etSurname.getText().toString(),
                        etMail.getText().toString(),
                        etStreetAdress.getText().toString(),
                        etPostalCode.getText().toString(),
                        etCity.getText().toString(),
                        etCountry.getText().toString(),
                        etTelNumber.getText().toString(),
                        etWebBlog.getText().toString());

                mainCallback.saveProfileData(profile);
            }
        };
    }
}

