package reachingimmortality.com.curriculum.interfaces;

/**
 * Created by Reaching Immortality on 14.7.2015..
 */
public interface LoginCallback {

    void login(String email, String password);
    void register(String email, String uname, String password);

    void goToRegister();
    void goToLogin();

    //Google sign-in
    void onSignInClicked();
}
