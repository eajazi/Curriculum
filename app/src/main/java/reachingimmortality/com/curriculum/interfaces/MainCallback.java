package reachingimmortality.com.curriculum.interfaces;

import android.net.Uri;

import com.software.shell.fab.ActionButton;


/**
 * Created by ReachingIm on 7.10.2015..
 */
public interface MainCallback {
    //SAVE METHODS
    //*********************************************************************************************************************

    //PROFILE
    void saveProfileData(String jsonProfileList);
    //APPLICATION
    void saveApplicationData(String typeCase, String applicationJSONListt);
    //EXPERIENCE
    void saveExperience(String experienceCase, String experienceJSONList);
    //EDUCATION
    void saveEducation(String educationCase, String educationJSONList);
    //LANGUAGE
    void insertLanguage(String languageCase, String languageJSONList, String evaluationJSONList, ActionButton fabSkillMain);
    //SKILLS
    void saveSkills(String skillsCase, String skillsType, String skillJSONList);

    //DELETE
    //*********************************************************************************************************************
    void showDeleteDialog(long itemId, String type, String skillsType);
    //EXPORT CV
    void exportCV();
}
