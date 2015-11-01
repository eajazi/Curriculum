package reachingimmortality.com.curriculum;


import android.app.Activity;
import android.app.Fragment;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.software.shell.fab.ActionButton;
import com.software.shell.fab.ActionButton;

import reachingimmortality.com.curriculum.database_library.MyContentProvider;
import reachingimmortality.com.curriculum.interfaces.MainCallback;
import reachingimmortality.com.curriculum.material_navigation_drawer.MaterialNavigationDrawer;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileMainFragment extends Fragment implements View.OnClickListener {

    //UI ELEMENTS
    private TextView tvName,tvSurname,tvEmail,tvTelNumber, tvStreetAdress, tvPostal, tvCity, tvCountry, tvWebBlog;
    private ActionButton fabProMain;
    //INTERFACES
    private MainCallback mainCallback;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile_main, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //UI ELEMENTS
        //FAB ANIMATIONS
        fabProMain = (ActionButton) getActivity().findViewById(R.id.fabMain);
        fabProMain.setImageResource(R.mipmap.ic_edit_white);
        fabProMain.setButtonColor(getResources().getColor(R.color.colorAccent));
        fabProMain.setButtonColorRipple(android.R.color.white);

        fabProMain.setShowAnimation(ActionButton.Animations.ROLL_FROM_RIGHT);
        fabProMain.setHideAnimation(ActionButton.Animations.ROLL_TO_RIGHT);


        fabProMain.playShowAnimation();
        fabProMain.show();



        tvName = (TextView) view.findViewById(R.id.tvProMainName);
        tvSurname = (TextView) view.findViewById(R.id.tvProMainSurname);
        tvEmail = (TextView) view.findViewById(R.id.tvProMainEmail);
        tvTelNumber = (TextView) view.findViewById(R.id.tvProMainTelNumber);
        tvStreetAdress = (TextView) view.findViewById(R.id.tvProMainStrAdress);
        tvPostal = (TextView) view.findViewById(R.id.tvProMainPostalCode);
        tvCity = (TextView) view.findViewById(R.id.tvProMainCity);
        tvCountry = (TextView) view.findViewById(R.id.tvProMainCountry);
        tvWebBlog = (TextView) view.findViewById(R.id.tvProMainWebBlog);

        //SET FAB CLICK LISTENERS
        fabProMain.setOnClickListener(this);

        //Check for profile data
        Cursor cursor = getActivity().getContentResolver().query(MyContentProvider.CONTENT_URI_PROFILE, null, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                String name = cursor.getString(cursor.getColumnIndex(MyContentProvider.PROFILE_NAME));
                String surname = cursor.getString(cursor.getColumnIndex(MyContentProvider.PROFILE_SURNAME));
                String email = cursor.getString(cursor.getColumnIndex(MyContentProvider.PROFILE_EMAIL));
                String country = cursor.getString(cursor.getColumnIndex(MyContentProvider.PROFILE_COUNTRY));
                String city = cursor.getString(cursor.getColumnIndex(MyContentProvider.PROFILE_CITY));
                String postal = cursor.getString(cursor.getColumnIndex(MyContentProvider.PROFILE_POSTAL_CODE));
                String streetAdr = cursor.getString(cursor.getColumnIndex(MyContentProvider.PROFILE_STREET_ADRESS));
                String telNumber = cursor.getString(cursor.getColumnIndex(MyContentProvider.PROFILE_TEL_NUMBER));
                String webBlog = cursor.getString(cursor.getColumnIndex(MyContentProvider.PROFILE_WEB_BLOG));

                //NAME
                if(name.equals("")){
                    tvName.setText(getActivity().getResources().getString(R.string.empty_content));
                }else{
                    tvName.setText(name);
                }
                //SURNAME
                if(surname.equals("")){
                    tvSurname.setText(getActivity().getResources().getString(R.string.empty_content));
                }else{
                    tvSurname.setText(surname);
                }
                //EMAIL
                if(email.equals("")){
                    tvEmail.setText(getActivity().getResources().getString(R.string.empty_content));
                }else{
                    tvEmail.setText(email);
                }
                //TEL NUMBER
                if(telNumber.equals("")){
                    tvTelNumber.setText(getActivity().getResources().getString(R.string.empty_content));
                }else{
                    tvTelNumber.setText(telNumber);
                }
                //STREET ADRESS
                if(streetAdr.equals("")){
                    tvStreetAdress.setText(getActivity().getResources().getString(R.string.empty_content));
                }else{
                    tvStreetAdress.setText(streetAdr);
                }
                //POSTAL CODE
                if(postal.equals("")){
                    tvPostal.setText(getActivity().getResources().getString(R.string.empty_content));
                }else{
                    tvPostal.setText(postal);
                }
                //CITY
                if(city.equals("")){
                    tvCity.setText(getActivity().getResources().getString(R.string.empty_content));
                }else{
                    tvCity.setText(city);
                }
                //COUNTRY
                if(country.equals("")){
                    tvCountry.setText(getActivity().getResources().getString(R.string.empty_content));
                }else{
                    tvCountry.setText(country);
                }
                //WEB BLOG
                if(webBlog.equals("")){
                    tvWebBlog.setText(getActivity().getResources().getString(R.string.empty_content));
                }else{
                    tvWebBlog.setText(webBlog);
                }
            }else{
                tvName.setText(getActivity().getResources().getString(R.string.empty_content));
                tvSurname.setText(getActivity().getResources().getString(R.string.empty_content));
                tvEmail.setText(getActivity().getResources().getString(R.string.empty_content));
                tvTelNumber.setText(getActivity().getResources().getString(R.string.empty_content));
                tvStreetAdress.setText(getActivity().getResources().getString(R.string.empty_content));
                tvPostal.setText(getActivity().getResources().getString(R.string.empty_content));
                tvCity.setText(getActivity().getResources().getString(R.string.empty_content));
                tvCountry.setText(getActivity().getResources().getString(R.string.empty_content));
                tvWebBlog.setText(getActivity().getResources().getString(R.string.empty_content));

            }

        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try{
            mainCallback = (MainCallback) activity;
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fabMain:
                fabProMain.hide();
                fabProMain.playHideAnimation();
                new Handler().postDelayed(getShowRunnable(), MainActivity.ACTION_BUTTON_POST_DELAY_MS);
                break;
        }
    }

    private Runnable getShowRunnable() {
        return new Runnable() {
            @Override
            public void run() {
                goToProfileEdit();
            }
        };

    }

    private void goToProfileEdit() {
        ((MaterialNavigationDrawer)this.getActivity()).setFragmentChild(new ProfileMainEditFragment(),
                getResources().getString(R.string.title_profile_edit));
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("Profile Main","onPause");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d("Profile Main", "onDetach");
        fabProMain.setOnClickListener(null);
    }
}
