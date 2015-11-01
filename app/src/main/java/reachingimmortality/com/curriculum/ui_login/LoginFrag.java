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
import reachingimmortality.com.curriculum.interfaces.LoginCallback;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFrag extends Fragment implements View.OnClickListener {

    //UI ELEMENTS
    private EditText etEmail,etPassword;

    //Interfaces
    private LoginCallback mCallback;

    public LoginFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try{
            mCallback = (LoginCallback) getActivity();
        }catch (Exception ex){ex.printStackTrace();}
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Initializing UI elements
        Button btLogin = (Button) view.findViewById(R.id.btLoginLogin);
        Button btGoToRegister = (Button) view.findViewById(R.id.btLoginGoToRegister);

        etEmail = (EditText) view.findViewById(R.id.etLogEmail);
        etPassword = (EditText) view.findViewById(R.id.etLogPass);

        btLogin.setOnClickListener(this);
        btGoToRegister.setOnClickListener(this);
        view.findViewById(R.id.sign_in_button).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btLoginLogin:
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();
                if (  ( !email.equals("")) && ( !password.equals("")) )
                {
                    mCallback.login(email,password);
                }
                else if ( ( !email.equals("")) )
                {
                    Toast.makeText(getActivity().getApplicationContext(),
                            getResources().getString(R.string.message_empty_password), Toast.LENGTH_SHORT).show();
                }
                else if ( ( !password.equals("")) )
                {
                    Toast.makeText(getActivity().getApplicationContext(),
                            getResources().getString(R.string.message_empty_email), Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(getActivity().getApplicationContext(),
                            getResources().getString(R.string.message_empty_email_and_password), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btLoginGoToRegister:
                mCallback.goToRegister();
                break;

            case R.id.sign_in_button:
                mCallback.onSignInClicked();
                break;
        }
    }
}
