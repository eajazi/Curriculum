package reachingimmortality.com.curriculum.dialogs;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;

import java.util.Calendar;

import reachingimmortality.com.curriculum.MainActivity;

/**
 * Created by ReachingIm on 12.10.2015..
 */
public class MyDatePickerDialog extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private OnDateSelectedCallback onDateSelectedCallback;

    public static MyDatePickerDialog newInstance(String dialogCase, String day, String month, String year){

        MyDatePickerDialog myDatePickerDialog = new MyDatePickerDialog();

        Bundle args = new Bundle();

        args.putString(MainActivity.DIALOG_CASE, dialogCase);
        args.putString(MainActivity.DATE_DAY, day);
        args.putString(MainActivity.DATE_MONTH, month);
        args.putString(MainActivity.DATE_YEAR, year);

        myDatePickerDialog.setArguments(args);

        return myDatePickerDialog;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            onDateSelectedCallback = (OnDateSelectedCallback) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException("Calling fragment must implement DialogClickListener interface");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        int year, month, day;
        Bundle args = getArguments();
        if(args.getString(MainActivity.DATE_YEAR) != null){
            year = Integer.parseInt(args.getString(MainActivity.DATE_YEAR));
            month = Integer.parseInt(args.getString(MainActivity.DATE_MONTH));
            day = Integer.parseInt(args.getString(MainActivity.DATE_DAY));
        }else {
            final Calendar c = Calendar.getInstance();
            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH);
            day = c.get(Calendar.DAY_OF_MONTH);
        }
        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        String dateCase = getArguments().getString(MainActivity.DIALOG_CASE);
        onDateSelectedCallback.onPositiveClick(dateCase, year, monthOfYear, dayOfMonth);
    }

    public interface OnDateSelectedCallback{
        void onPositiveClick(String dateCase, int year, int monthOfYear, int dayOfMonth);
        void onNegativeClick();
    }
}