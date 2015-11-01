package reachingimmortality.com.curriculum.interfaces;

import android.net.Uri;

/**
 * Created by Reaching Immortality on 20.8.2015..
 */
public interface WizardCallback {
    //INSERTING INTO MYSQL
    //****************************************************************************************************************************
    //****************************************************************************************************************************
    //PROFILE
    void insertProfileIntoMySQL(String jsonProfileList);

    //APPLICATION
    void insertApplicationIntoMySQL(String applicationCase, String jsonApplicationList);

    //EXPERIENCE
    void insertExperienceIntoMySQL(String experienceCase, String experienceJSONList);

    //EDUCATION
    void insertEducationIntoMySQL(String educationCase, String jsonEducationList);

    //LANGUAGE
    void insertLanguageIntoMYSQL(String languageCase, String jsonLanguageList, String jsonEvaluationList);

    //SKILLS
    void insertSkillsIntoMySQL(String skillsCase, String skillType, String jsonSkillsList);

    //GO TO METHODS
    //****************************************************************************************************************************
    //****************************************************************************************************************************
    //PROFILE
    void goToProfile();

    //APPLICATION
    void goToApplication();

    //EDUCATION
    void goToEducation();

    //EDUCATION
    void goToExperience();

    //LANGUAGE
    void goToMotherLanguage();
    void goToOtherLanguage();

    //SKILLS
    void goToSkills();

    //EXPORT
    void goToExport();
    //SHOW EDIT FRAGMENTS
    //****************************************************************************************************************************
    //****************************************************************************************************************************
    //EXPERIENCE
    void showExperienceAddEditFragment(String type, Uri uri);

    //EDUCATION
    void showEducationAddEditFragment(String type, Uri uri);

    //MOTHER TONGUE DIALOG
    void callMotherTongueAddDialog();
    void callMotherTongueEditDialog(long itemId);

    //EVALUATION OF OTHER LANGUAGE(S)
    void showEvaluationAddEditFragment(String evaluationUpdate, Uri uri);

    //SKILLS
    void showSkillsAddEditFragment(String skillsType, Uri uri);

    //DELETE DIALOG
    void showDeleteDialog(long itemId, String type, String skillsType);

    //EXPORT CV TO MAIL
    void exportCV();

}
