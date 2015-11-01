package reachingimmortality.com.curriculum;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.util.Timer;
import java.util.TimerTask;

import reachingimmortality.com.curriculum.controllers.SessionManager;
import reachingimmortality.com.curriculum.ui_login.LoginActivity;
import reachingimmortality.com.curriculum.ui_tour.TourActivity;
import reachingimmortality.com.curriculum.ui_wizard.WizardActivity;

public class SplashScreenActivity extends AppCompatActivity {
    // Set Duration of the Splash Screen
    long Delay = 1500;

    //Controllers
    private SessionManager sharedPref;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPref = new SessionManager(this);

        // create our manager instance after the content view is set
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        // enable status bar tint
        tintManager.setStatusBarTintEnabled(true);
        // Remove the Title Bar
        //requestWindowFeature(Window.FEATURE_NO_TITLE);

        // Get the view from splash_screen.xml
        setContentView(R.layout.activity_splash_screen);
        //Start animation
        startAnimation();
        // Create a Timer
        Timer RunSplash = new Timer();

        // Task to do when the timer ends
        TimerTask ShowSplash = new TimerTask() {
            @SuppressLint("PrivateResource")
            @Override
            public void run() {
        // Close SplashScreenActivity.class
                finish();
        // Start Activity
               if(sharedPref.isTourSeen() && sharedPref.isLoggedIn() && sharedPref.isWizardDone()){
                   goToMainActivity();
               }else if(sharedPref.isTourSeen() && sharedPref.isLoggedIn()){
                   goToWizardActivity();
               }else if(sharedPref.isTourSeen()){
                   goToLoginActivity();
               }else{
                   goToTourActivity();
               }
            }
        };

        // Start the timer
        RunSplash.schedule(ShowSplash, Delay);
    }

    private void startAnimation(){
        //INIT UI ELEMENTS
        ImageView imgThird = (ImageView) findViewById(R.id.imgPrint3);
        ImageView imgFour = (ImageView) findViewById(R.id.imgPrint4);
        ImageView imgFive = (ImageView) findViewById(R.id.imgPrint5);
        ImageView imgSix = (ImageView) findViewById(R.id.imgPrint6);
        ImageView imgSeven = (ImageView) findViewById(R.id.imgPrint7);
        ImageView imgEight = (ImageView) findViewById(R.id.imgPrint8);

        //SET IMAGE RESOURCES
        imgThird.setImageResource(R.mipmap.cat_print_white);
        imgFour.setImageResource(R.mipmap.cat_print_white);
        imgFive.setImageResource(R.mipmap.cat_print_white);
        imgSix.setImageResource(R.mipmap.cat_print_white);
        imgSeven.setImageResource(R.mipmap.cat_print_white);
        imgEight.setImageResource(R.mipmap.cat_print_white);

        //SET ANIMATION
        imgThird.setVisibility(View.VISIBLE);
        imgThird.startAnimation(AnimationUtils.loadAnimation(this, R.anim.anim_print_1));

        imgFour.setVisibility(View.VISIBLE);
        imgFour.startAnimation(AnimationUtils.loadAnimation(this, R.anim.anim_print_2));

        imgFive.setVisibility(View.VISIBLE);
        imgFive.startAnimation(AnimationUtils.loadAnimation(this, R.anim.anim_print_3));

        imgSix.setVisibility(View.VISIBLE);
        imgSix.startAnimation(AnimationUtils.loadAnimation(this, R.anim.anim_print_4));

        imgSeven.setVisibility(View.VISIBLE);
        imgSeven.startAnimation(AnimationUtils.loadAnimation(this, R.anim.anim_print_5));

        imgEight.setVisibility(View.VISIBLE);
        imgEight.startAnimation(AnimationUtils.loadAnimation(this, R.anim.anim_print_6));
    }


    private void goToTourActivity(){
        Intent myIntent = new Intent(SplashScreenActivity.this,
                TourActivity.class);
        startActivity(myIntent);
        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
    }

    private void goToWizardActivity(){
        Intent myIntent = new Intent(SplashScreenActivity.this,
                WizardActivity.class);
        startActivity(myIntent);
        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
    }

    private void goToLoginActivity(){
        Intent myIntent = new Intent(SplashScreenActivity.this,
                LoginActivity.class);
        startActivity(myIntent);
        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
    }

    private void goToMainActivity(){
        Intent myIntent = new Intent(SplashScreenActivity.this,
                MainActivity.class);
        startActivity(myIntent);
        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
    }

}

