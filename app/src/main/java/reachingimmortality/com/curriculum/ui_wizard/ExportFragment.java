package reachingimmortality.com.curriculum.ui_wizard;


import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import reachingimmortality.com.curriculum.R;
import reachingimmortality.com.curriculum.controllers.LayoutController;
import reachingimmortality.com.curriculum.controllers.SessionManager;
import reachingimmortality.com.curriculum.database_library.MyContentProvider;
import reachingimmortality.com.curriculum.interfaces.WizardCallback;

/**
 * A simple {@link Fragment} subclass.
 */
public class ExportFragment extends Fragment implements View.OnClickListener {

    //UI ELEMENTS
    ImageView imgFirst,imgSecond, imgText, imgThird, imgFour, imgFive, imgSix, imgSeven, imgEight, imgNine, imgTen;
    //INTERFACES
    private WizardCallback wizardCallback;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.wizard_fragment_export, container, false);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try{
            wizardCallback = (WizardCallback) activity;
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //SET SHARED PREFS
        SessionManager sessionManager = new SessionManager(getActivity());
        sessionManager.setProfile(false);
        sessionManager.setApplication(false);
        sessionManager.setExperience(false);
        sessionManager.setEducation(false);
        sessionManager.setMotherLanguage(false);
        sessionManager.setOtherLanguage(false);
        sessionManager.setSkills(true);

        //UI ELEMENTS
        RelativeLayout layoutMain = (RelativeLayout) view.findViewById(R.id.expMainLayout);

        imgText = (ImageView) view.findViewById(R.id.expImgTextAbove);
        imgFirst = (ImageView) view.findViewById(R.id.imgPrint1);
        imgSecond = (ImageView) view.findViewById(R.id.imgPrint2);
        imgThird = (ImageView) view.findViewById(R.id.imgPrint3);
        imgFour = (ImageView) view.findViewById(R.id.imgPrint4);
        imgFive = (ImageView) view.findViewById(R.id.imgPrint5);
        imgSix = (ImageView) view.findViewById(R.id.imgPrint6);
        imgSeven = (ImageView) view.findViewById(R.id.imgPrint7);
        imgEight = (ImageView) view.findViewById(R.id.imgPrint8);
        imgNine = (ImageView) view.findViewById(R.id.imgPrint9);
        imgTen = (ImageView) view.findViewById(R.id.imgPrint10);

        Button btNext = (Button) view.findViewById(R.id.btMainPositiveButton);
        Button btBack = (Button) view.findViewById(R.id.btMainNegativeButton);

        //UI ELEMENTS INIT
        layoutMain.setBackgroundResource(R.color.export_frag_bground);
        LayoutController layoutController = new LayoutController();
        layoutMain.setPadding(0, layoutController.getStatusBarHeight(getActivity()), 0, 0);

        imgText.setImageResource(R.mipmap.text_img_one_step);

        //ANIMATION INIT
        setAnimations();

        //SET BUTTON TITLE
        btNext.setText(R.string.button_finish);
        btBack.setText(R.string.button_back);

        //Init listener on button
        btNext.setOnClickListener(this);
        btBack.setOnClickListener(this);
    }

    //ANIMATION
    //****************************************************************************************************************
    private void setAnimations() {

        imgFirst.setVisibility(View.VISIBLE);
        imgFirst.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.anim_print_1));

        imgSecond.setVisibility(View.VISIBLE);
        imgSecond.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.anim_print_2));

        imgThird.setVisibility(View.VISIBLE);
        imgThird.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.anim_print_3));

        imgFour.setVisibility(View.VISIBLE);
        imgFour.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.anim_print_4));

        imgFive.setVisibility(View.VISIBLE);
        imgFive.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.anim_print_5));

        imgSix.setVisibility(View.VISIBLE);
        imgSix.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.anim_print_6));

        imgSeven.setVisibility(View.VISIBLE);
        imgSeven.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.anim_print_7));

        imgEight.setVisibility(View.VISIBLE);
        imgEight.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.anim_print_8));

        imgNine.setVisibility(View.VISIBLE);
        imgNine.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.anim_print_9));

        imgTen.setVisibility(View.VISIBLE);
        imgTen.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.anim_print_10));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btMainPositiveButton:
                String mail = null;
                Cursor cursor = getActivity().getContentResolver().query(MyContentProvider.CONTENT_URI_USER,
                        new String[]{MyContentProvider.USER_EMAIL},null,null,null);
                if(cursor.moveToFirst()){
                    mail = cursor.getString(cursor.getColumnIndex(MyContentProvider.USER_EMAIL));
                }
                new MaterialDialog.Builder(getActivity())
                        .title(R.string.title_export)
                        .content(getResources().getString(R.string.dialog_message_mail_to) +" "+ mail)
                        .widgetColorRes(R.color.material_dialog_buttons)
                        .positiveText(R.string.button_agree)
                        .negativeText(R.string.button_cancel)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                wizardCallback.exportCV();
                                dialog.dismiss();
                            }
                        })
                        .show();
                break;

            case R.id.btMainNegativeButton:
                wizardCallback.goToSkills();
                break;
        }
    }


}
