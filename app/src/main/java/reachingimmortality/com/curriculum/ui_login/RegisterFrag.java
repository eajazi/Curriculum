package reachingimmortality.com.curriculum.ui_login;


import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import reachingimmortality.com.curriculum.R;
import reachingimmortality.com.curriculum.http_calls.JSONFunctions;
import reachingimmortality.com.curriculum.interfaces.LoginCallback;


/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFrag extends Fragment implements View.OnClickListener {
    //UI elements
    private EditText etMail,etUname,etPassword;
    private Button btRegister,btGoToLogin;
    //Controllers
    private JSONFunctions jsonFunctions;
    private LoginCallback mCallback;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Initializing UI elements
        etMail = (EditText) view.findViewById(R.id.etRegisterEmail);
        etUname = (EditText) view.findViewById(R.id.etRegisterUname);
        etPassword = (EditText) view.findViewById(R.id.etRegisterPass);

        btRegister = (Button) view.findViewById(R.id.btRegisterReg);
        btGoToLogin = (Button) view.findViewById(R.id.btRegisterGoToLogin);

        btRegister.setOnClickListener(this);
        btGoToLogin.setOnClickListener(this);


    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try{
            mCallback = (LoginCallback) getActivity();
        }catch (Exception ex){ex.printStackTrace();}
        jsonFunctions = new JSONFunctions();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btRegisterReg:
                String uname = etUname.getText().toString();
                String email = etMail.getText().toString();
                String password = etPassword.getText().toString();
                if (  ( !uname.equals("")) && ( !password.equals(""))  && ( !email.equals("")) )
                {
                    if ( uname.length() > 4 ){
                        mCallback.register(email,uname,password);
                    }
                    else
                    {
                        Toast.makeText(getActivity().getApplicationContext(),
                                getResources().getString(R.string.message_short_uname), Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(getActivity().getApplicationContext(),
                            getResources().getString(R.string.message_empty_fields), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btRegisterGoToLogin:
                mCallback.goToLogin();
                break;
        }
    }
}
