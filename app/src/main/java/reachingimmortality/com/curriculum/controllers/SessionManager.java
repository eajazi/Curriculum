package reachingimmortality.com.curriculum.controllers;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

public class SessionManager {


    // LogCat tag
    private static String TAG = SessionManager.class.getSimpleName();

    // Shared Preferences
    SharedPreferences pref;

    Editor editor;
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "CurriculumLogin";

    // Shared preferences keys
    private static final String KEY_IS_FIRST_TIME_HERE = "isFirstTimeHere";
    private static final String KEY_IS_LOGGEDIN = "isLoggedIn";
    private static final String KEY_IS_TOUR_SEEN = "isTourSeen";
    private static final String KEY_IS_WIZARD_DONE = "isWizardDone";
    private static final String KEY_IS_PROFILE_EDITED = "isProfileEdited";
    private static final String KEY_IS_APPLICATION_EDITED = "isApplicationEdited";
    private static final String KEY_IS_EXPERIENCE_EDITED = "isExperienceEdited";
    private static final String KEY_IS_EDUCATION_EDITED = "isEducationEdited";
    private static final String KEY_IS_MOTHER_LANGUAGE_EDITED = "isMotherLanguageEdited";
    private static final String KEY_IS_OTHER_LANGUAGE_EDITED = "isOtherLanguageEdited";
    private static final String KEY_IS_SKILLS_EDITED = "isSkillsEdited";

    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setIsNoMoreFirstTime(boolean isFirstTime){
        editor.putBoolean(KEY_IS_FIRST_TIME_HERE, isFirstTime);
        editor.commit();
        Log.d(TAG, "User is no more first time here.");
    }


    public void setTour(boolean isTourSeen){
        editor.putBoolean(KEY_IS_TOUR_SEEN,isTourSeen);
        editor.commit();
        Log.d(TAG, "User have seen tour.");
    }

    public void setWizard(boolean isWizardDone){
        editor.putBoolean(KEY_IS_WIZARD_DONE, isWizardDone);
        editor.commit();
        Log.d(TAG, "User filled wizard.");
    }

    public void setLogin(boolean isLoggedIn) {

        editor.putBoolean(KEY_IS_LOGGEDIN, isLoggedIn);

        // commit changes
        editor.commit();

        Log.d(TAG, "User login session modified!");
    }

    public void setProfile(boolean isProfileEdited){
        editor.putBoolean(KEY_IS_PROFILE_EDITED,isProfileEdited);
        editor.commit();
        Log.d(TAG, "User have edited profile.");
    }

    public void setApplication(boolean isApplicationEdited){
        editor.putBoolean(KEY_IS_APPLICATION_EDITED,isApplicationEdited);
        editor.commit();
        Log.d(TAG, "User have edited application.");
    }

    public void setExperience(boolean isExperienceEdited){
        editor.putBoolean(KEY_IS_EXPERIENCE_EDITED,isExperienceEdited);
        editor.commit();
        Log.d(TAG, "User have edited experience.");
    }

    public void setEducation(boolean isEducationEdited){
        editor.putBoolean(KEY_IS_EDUCATION_EDITED,isEducationEdited);
        editor.commit();
        Log.d(TAG, "User have edited education.");
    }

    public void setMotherLanguage(boolean isMotherLanguageEdited){
        editor.putBoolean(KEY_IS_MOTHER_LANGUAGE_EDITED, isMotherLanguageEdited);
        editor.commit();
        Log.d(TAG, "User have edited mother language.");
    }

    public void setOtherLanguage(boolean isOtherLanguageEdited){
        editor.putBoolean(KEY_IS_OTHER_LANGUAGE_EDITED, isOtherLanguageEdited);
        editor.commit();
        Log.d(TAG, "User have edited other language.");
    }

    public void setSkills(boolean isSkillsEdited){
        editor.putBoolean(KEY_IS_SKILLS_EDITED, isSkillsEdited);
        editor.commit();
        Log.d(TAG, "User have edited skills.");
    }

    public boolean isNoMoreFirstTimeHere(){
        return pref.getBoolean(KEY_IS_FIRST_TIME_HERE, false);
    }

    public boolean isWizardDone() {return pref.getBoolean(KEY_IS_WIZARD_DONE, false);}
    public boolean isTourSeen() {return pref.getBoolean(KEY_IS_TOUR_SEEN, false);}
    public boolean isLoggedIn(){
        return pref.getBoolean(KEY_IS_LOGGEDIN, false);
    }
    public boolean isProfileEdited() {return pref.getBoolean(KEY_IS_PROFILE_EDITED, false);}
    public boolean isApplicationEdited() {return pref.getBoolean(KEY_IS_APPLICATION_EDITED, false);}
    public boolean isExperienceEdited() {return pref.getBoolean(KEY_IS_EXPERIENCE_EDITED, false);}
    public boolean isEducationEdited() {return pref.getBoolean(KEY_IS_EDUCATION_EDITED, false);}
    public boolean isMotherLanguageEdited() {return pref.getBoolean(KEY_IS_MOTHER_LANGUAGE_EDITED, false);}
    public boolean isOtherLanguageEdited() {return pref.getBoolean(KEY_IS_OTHER_LANGUAGE_EDITED, false);}
    public boolean isSkillsEdited() {return pref.getBoolean(KEY_IS_SKILLS_EDITED, false);}
}