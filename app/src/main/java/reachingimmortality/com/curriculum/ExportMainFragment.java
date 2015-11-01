package reachingimmortality.com.curriculum;

import android.app.Activity;
import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.software.shell.fab.ActionButton;

import reachingimmortality.com.curriculum.database_library.MyContentProvider;
import reachingimmortality.com.curriculum.interfaces.MainCallback;

/**
 * Created by ReachingIm on 21.10.2015..
 */
public class ExportMainFragment extends Fragment implements View.OnClickListener {

    //UI ELEMENTS
    private ActionButton fabExpMain;
    //INTERFACES
    private MainCallback mainCallback;

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
            mainCallback = (MainCallback) activity;
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //UI ELEMENTS
        ImageView imgFirst = (ImageView) view.findViewById(R.id.imgPrint1);
        ImageView imgSecond = (ImageView) view.findViewById(R.id.imgPrint2);

        //UI ELEMENTS INIT
        RelativeLayout layoutButtons = (RelativeLayout) view.findViewById(R.id.layDownButtons);
        layoutButtons.setVisibility(View.GONE);

        imgFirst.setImageResource(R.mipmap.cat_print_blue);
        imgSecond.setImageResource(R.mipmap.cat_print_blue);

        //FAB ANIMATIONS
        fabExpMain = (ActionButton) getActivity().findViewById(R.id.fabMain);
        fabExpMain.setImageResource(R.mipmap.ic_checked_white);
        fabExpMain.setButtonColor(getResources().getColor(R.color.colorAccent));
        fabExpMain.setButtonColorRipple(android.R.color.white);

        fabExpMain.setShowAnimation(ActionButton.Animations.ROLL_FROM_RIGHT);
        fabExpMain.setHideAnimation(ActionButton.Animations.ROLL_TO_RIGHT);


        fabExpMain.playShowAnimation();
        fabExpMain.show();

        //CLICK LISTENERS
        fabExpMain.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fabMain:
                String mail=null;
                fabExpMain.hide();
                fabExpMain.playHideAnimation();
                Cursor cursor = getActivity().getContentResolver().query(MyContentProvider.CONTENT_URI_USER,
                        new String[]{MyContentProvider.USER_EMAIL},null,null,null);
                if(cursor.moveToFirst()){
                    mail = cursor.getString(cursor.getColumnIndex(MyContentProvider.USER_EMAIL));
                }
                new MaterialDialog.Builder(getActivity())
                        .title("Export")
                        .content("Your cv will be sent to " + mail)
                        .widgetColorRes(R.color.material_dialog_buttons)
                        .positiveText(R.string.button_agree)
                        .negativeText(R.string.button_cancel)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                fabExpMain.playShowAnimation();
                                fabExpMain.show();
                                mainCallback.exportCV();
                            }
                        })
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                fabExpMain.show();
                                fabExpMain.playShowAnimation();
                            }
                        })
                        .show();
                break;
        }
    }
}
