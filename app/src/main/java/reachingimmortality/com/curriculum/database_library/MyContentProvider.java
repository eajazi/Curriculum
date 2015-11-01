package reachingimmortality.com.curriculum.database_library;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import java.util.HashMap;

/**
 * Created by Ajki on 15.5.2015..
 */
public class MyContentProvider extends ContentProvider {

    //name of provider
    public static final String PROVIDER_NAME = "beastmode.curriculumvitae.database_library.MyContentProvider";
    //list of URI-es
    public static final Uri CONTENT_URI_COUNTRIES_LIST = Uri.parse("content://" + PROVIDER_NAME + "/list_countries" );
    public static final Uri CONTENT_URI_LANGUAGES_LIST = Uri.parse("content://" + PROVIDER_NAME + "/list_languages" );
    public static final Uri CONTENT_URI_USER = Uri.parse("content://" + PROVIDER_NAME + "/users");
    public static final Uri CONTENT_URI_PROFILE = Uri.parse("content://" + PROVIDER_NAME + "/profile");
    public static final Uri CONTENT_URI_APPLICATION_TYPE = Uri.parse("content://" + PROVIDER_NAME + "/application_type");
    public static final Uri CONTENT_URI_APPLICATION = Uri.parse("content://" + PROVIDER_NAME + "/application");
    public static final Uri CONTENT_URI_USER_APPLICATION = Uri.parse("content://" + PROVIDER_NAME + "/user_application");
    public static final Uri CONTENT_URI_WORK_EXPERIENCE = Uri.parse("content://" + PROVIDER_NAME + "/work_experience");
    public static final Uri CONTENT_URI_USER_WORK_EXPERIENCE = Uri.parse("content://" + PROVIDER_NAME + "/user_work_experience");
    public static final Uri CONTENT_URI_EDUCATION = Uri.parse("content://" + PROVIDER_NAME + "/education");
    public static final Uri CONTENT_URI_USER_EDUCATION = Uri.parse("content://" + PROVIDER_NAME + "/user_education");
    public static final Uri CONTENT_URI_SKILLS = Uri.parse("content://" + PROVIDER_NAME + "/skills");
    public static final Uri CONTENT_URI_LANG_TYPE = Uri.parse("content://" + PROVIDER_NAME + "/language_type");
    public static final Uri CONTENT_URI_LANGUAGE = Uri.parse("content://" + PROVIDER_NAME + "/language");
    public static final Uri CONTENT_URI_USER_LANGUAGE = Uri.parse("content://" + PROVIDER_NAME + "/user_language");
    public static final Uri CONTENT_URI_ASSESSMENT_HYPER_TYPE = Uri.parse("content://" + PROVIDER_NAME + "/assessment_hyper_type");
    public static final Uri CONTENT_URI_ASSESSMENT_SUB_TYPE = Uri.parse("content://" + PROVIDER_NAME + "/assessment_sub_type");
    public static final Uri CONTENT_URI_ASSESSMENT_USER_TYPE = Uri.parse("content://" + PROVIDER_NAME + "/assessment_user_type");
    public static final Uri CONTENT_URI_ASSESSMENT_LEVEL = Uri.parse("content://" + PROVIDER_NAME + "/assessment_level");
    public static final Uri CONTENT_URI_EVALUATION = Uri.parse("content://" + PROVIDER_NAME + "/evaluation");
    public static final Uri CONTENT_URI_VIEW_USER_APPLICATION = Uri.parse("content://" + PROVIDER_NAME + "/user_application_view");
    public static final Uri CONTENT_URI_VIEW_USER_MOTHER_LANGUAGE = Uri.parse("content://" + PROVIDER_NAME + "/user_mother_language_view");
    public static final Uri CONTENT_URI_VIEW_USER_OTHER_LANGUAGE = Uri.parse("content://" + PROVIDER_NAME + "/user_other_language_view");
    public static final Uri CONTENT_URI_VIEW_EVALUATION = Uri.parse("content://" + PROVIDER_NAME + "/evaluation_view");
    public static final Uri CONTENT_URI_VIEW_DATE_EXPERIENCE = Uri.parse("content://" + PROVIDER_NAME + "/date_experience_view");
    public static final Uri CONTENT_URI_VIEW_DATE_EDUCATION = Uri.parse("content://" + PROVIDER_NAME + "/date_education_view");
    public static final Uri CONTENT_URI_VIEW_EVALUATED_LANGUAGE = Uri.parse("content://" + PROVIDER_NAME + "/evaluated_language_view");


    //List of columns and tables
    public static final String SQL_INSERT_OR_REPLACE = "__sql_insert_or_replace__";

    //CONSTANT VARIABLES SUB TYPE ID
    public static final String LISTENING_SUB_TYPE_ID = "1";
    public static final String READING_SUB_TYPE_ID = "2";
    public static final String SPOKEN_INTERACTION_SUB_TYPE_ID = "3";
    public static final String SPOKEN_PRODUCTION_SUB_TYPE_ID = "4";
    public static final String WRITING_SUB_TYPE_ID = "5";

    //TABLE COUNTRY
    public static final String COUNTRY_LIST_TABLE = "list_countries";
    public static final String COUNTRY_ID = "_id";
    public static final String COUNTRY_NAME = "country_name";

    //TABLE LANGUAGE
    public static final String LANGUAGES_LIST_TABLE = "list_languages";
    public static final String LANGUAGES_LIST_ID = "_id";
    public static final String LANGUAGES_LIST_NAME = "language_name";

    // TABLE PERSONAL
    public static final String PROFILE_TABLE = "profile";
    public static final String PROFILE_NAME = "name";
    public static final String PROFILE_SURNAME = "surname";
    public static final String PROFILE_EMAIL = "email";
    public static final String PROFILE_CITY = "city";
    public static final String PROFILE_POSTAL_CODE = "postal_code";
    public static final String PROFILE_STREET_ADRESS = "street_adress";
    public static final String PROFILE_COUNTRY = "country";
    public static final String PROFILE_TEL_NUMBER = "tel_number";
    public static final String PROFILE_WEB_BLOG = "website_blog";
    public static final String PROFILE_USERS_PK_FK = "users__id";

    //TABLE USER
    public static final String USER_TABLE = "users";
    public static final String USER_ID = "_id";
    public static final String USER_UNAME = "username";
    public static final String USER_EMAIL = "email";
    public static final String USER_UID = "unique_id";
    public static final String USER_CREATED_AT = "created_at";

    //TABLE APPLICATION
    public static final String APPLICATION_TABLE = "application";
    public static final String APPLICATION_ID = "_id";
    public static final String APPLICATION_DESCRIPTION = "description";
    public static final String APPLICATION_TYPE_ID_FK = "application_type__id";

    //TABLE APPLICATION_TYPE
    public static final String APPLICATION_TYPE_TABLE = "application_type";
    public static final String APPLICATION_TYPE_ID = "_id";
    public static final String APPLICATION_TYPE_NAME = "type_name";

    //TABLE USER APPLICATION
    public static final String USER_APPLICATION_TABLE = "user_application";
    public static final String USER_APPLICATION_ID = "_id";
    public static final String USER_APPLICATION_USER_FK = "users__id";
    public static final String USER_APPLICATION_APPLICATION_FK = "application__id";

    //TABLE WORK EXPERIENCE
    public static final String WORK_EXPERIENCE_TABLE = "work_experience";
    public static final String WORK_EXPERIENCE_ID = "_id";
    public static final String WORK_EXPERIENCE_START_DATE= "start_date";
    public static final String WORK_EXPERIENCE_END_DATE = "end_date";
    public static final String WORK_EXPERIENCE_POSITION = "position";
    public static final String WORK_EXPERIENCE_EMPLOYER = "employer";
    public static final String WORK_EXPERIENCE_CITY= "city";
    public static final String WORK_EXPERIENCE_COUNTRY = "country";
    public static final String WORK_EXPERIENCE_MAIN_ACTIVITIES= "main_activities";

    //TABLE USER WORK EXPERIENCE
    public static final String USER_WORK_EXPERIENCE_TABLE = "user_work_experience";
    public static final String USER_WORK_EXPERIENCE_ID = "_id";
    public static final String USER_WORK_EXPERIENCE_USER_FK = "users__id";
    public static final String USER_WORK_EXPERIENCE_WORK_EXPERIENCE_FK = "work_experience__id";

    //TABLE EDUCATION
    public static final String EDUCATION_TABLE = "education";
    public static final String EDUCATION_ID = "_id";
    public static final String EDUCATION_START_DATE= "start_date";
    public static final String EDUCATION_END_DATE = "end_date";
    public static final String EDUCATION_QUALIFICATION_NAME = "qualification";
    public static final String EDUCATION_PROVIDER = "provider";
    public static final String EDUCATION_CITY = "city";
    public static final String EDUCATION_COUNTRY = "country";
    public static final String EDUCATION_MAIN_SUBJECTS = "main_subjects";


    //TABLE USER EDUCATION
    public static final String USER_EDUCATION_TABLE = "user_education";
    public static final String USER_EDUCATION_ID = "_id";
    public static final String USER_EDUCATION_USERS_FK = "users__id";
    public static final String USER_EDUCATION_EDUCATION_FK= "education__id";

    //TABLE SKILLS
    public static final String SKILL_TABLE = "skills";
    public static final String SKILL_ID = "_id";
    public static final String SKILL_COMMUNICATION = "communication";
    public static final String SKILL_MANAGMENT = "managament";
    public static final String SKILL_JOB_RELATED = "job_related";
    public static final String SKILL_IT = "digital_competence";
    public static final String SKILL_USERS_FK = "users__id";

    //TABLE TYPE LANGUAGE
    public static final String TYPE_LANGUAGE_TABLE = "type_language";
    public static final String TYPE_LANGUAGE_ID = "_id";
    public static final String TYPE_LANGUAGE_NAME = "type_name";

    //TABLE LANGUAGE
    public static final String LANGUAGE_TABLE = "language";
    public static final String LANGUAGE_ID = "_id";
    public static final String LANGUAGE_NAME = "language_name";
    public static final String LANGUAGE_TYPE_FK = "type_language__id";

    //TABLE USER LANGUAGE
    public static final String USER_LANGUAGE_TABLE = "user_language";
    public static final String USER_LANG_ID = "_id";
    public static final String USER_LANG_USERS_ID_FK = "users__id";
    public static final String USER_LANG_LANG_FK = "language__id";

    //TABLE PERSONAL SKILLS OTHER LANGUAGE EVALUATED
    public static final String LANGUAGE_EVALUATION_TABLE = "language_evaluation";
    public static final String LANGUAGE_EVALUATION_ID = "_id";
    public static final String LANGUAGE_EVALUATION_ASSESSMENT_FK = "assessment_evaluation__id";
    public static final String LANGUAGE_EVALUATION_LANG_FK = "languages__id";

    //TABLE ASSESSMENT USER TYPE
    public static final String ASSESSMENT_USER_TABLE = "assessment_user_type";
    public static final String ASSESSMENT_USER_ID = "_id";
    public static final String ASSESSMENT_USER_NAME = "user_type";

    //TABLE ASSESSMENT HYPER TYPE
    public static final String ASSESSMENT_HYPER_TYPE_TABLE = "assessment_hyper_type";
    public static final String ASSESSMENT_HYPER_TYPE_ID = "_id";
    public static final String ASSESSMENT_HYPER_TYPE_NAME = "hyper_type_name";

    //TABLE ASSESSMENT SUB TYPE
    public static final String ASSESSMENT_SUB_TYPE_TABLE = "assessment_sub_type";
    public static final String ASSESSMENT_SUB_TYPE_ID = "_id";
    public static final String ASSESSMENT_SUB_TYPE_NAME = "sub_type_name";
    public static final String ASSESSMENT_SUB_HYP_TYPE_FK = "assessmet_hyper_type__id";

    //TABLE ASSESSMENT EVALUATION LEVEL
    public static final String ASSESSMENT_LEVEL_TABLE = "assessment_sub_type";
    public static final String ASSESSMENT_LEVEL_ID = "_id";
    public static final String ASSESSMENT_LEVEL_NAME = "level";
    public static final String ASSESSMENT_LEVEL_ASS_USER_TYPE_FK = "assessment_user_type__id";

    //TABLE ASSESSMENT EVALUATION
    public static final String ASSESSMENT_EVALUATION_TABLE = "assessment_evaluation";
    public static final String ASSESSMENT_EVALUATION_ID = "_id";
    public static final String ASSESSMENT_EVALUATION_DESCRIPTION = "description";
    public static final String ASSESSMENT_EVALUATION_SUB_TYPE_FK = "assessment_sub_type__id";
    public static final String ASSESSMENT_EVALUATION_LEVEL_FK = "assessment_evaluation_levels__id";

    //VIEW ASSESSMENT EVALUATION
    public static final String USER_APPLICATION_VIEW = "view_user_application";

    //VIEW ASSESSMENT EVALUATION
    public static final String EVALUATION_VIEW = "assessment_evaluation_view";
    public static final String EVALUATION_VIEW_ID = "_id";
    public static final String EVALUATION_VIEW_DESCRIPTION = "level_description";
    public static final String EVALUATION_VIEW_LEVEL = "level";
    public static final String EVALUATION_VIEW_SUB_ID= "sub_id";

    //VIEW USER LANGUAGE
    public static final String USER_MOTHER_LANGUAGE_VIEW = "view_user_mother_language";
    public static final String USER_OTHER_LANGUAGE_VIEW = "view_user_other_language";
    public static final String USER_VIEW_LANGUAGE_ID = "_id";
    public static final String USER_VIEW_LANGUAGE_NAME = "language_name";
    public static final String USER_VIEW_LANGUAGE_TYPE = "type_language__id";

    //VIEW SECTIONS DATE WORK EXPERIENCE
    public static final String DATE_EXPERIENCE_VIEW = "view_dates_section_experience";
    public static final String DATE_EXPERIENCE_ID= "_id";
    public static final String DATE_EXPERIENCE_START_DAY= "start_day";
    public static final String DATE_EXPERIENCE_START_MONTH = "start_month";
    public static final String DATE_EXPERIENCE_START_YEAR = "start_year";
    public static final String DATE_EXPERIENCE_END_DAY = "end_day";
    public static final String DATE_EXPERIENCE_END_MONTH = "end_month";
    public static final String DATE_EXPERIENCE_END_YEAR = "end_year";

    //VIEW SECTIONS DATE EDUCATION
    public static final String DATE_EDUCATION_VIEW = "view_dates_section_education";
    public static final String DATE_EDUCATION_ID= "_id";
    public static final String DATE_EDUCATION_START_DAY= "start_day";
    public static final String DATE_EDUCATION_START_MONTH = "start_month";
    public static final String DATE_EDUCATION_START_YEAR = "start_year";
    public static final String DATE_EDUCATION_END_DAY = "end_day";
    public static final String DATE_EDUCATION_END_MONTH = "end_month";
    public static final String DATE_EDUCATION_END_YEAR = "end_year";

    //EVALUATION VIEW
    public static final String LANG_EVALUATION_VIEW = "view_lang_evaluation";
    public static final String LANG_EVALUATION_ID = "_id";
    public static final String LANG_EVALUATION_ASSESSMENT_ID = "assessment_evaluation__id";
    public static final String LANG_EVALUATION_SUB_TYPE = "assessment_sub_type__id";

    //Constants to identify the requested operation
    //country
    private static final int COUNTRIES_LIST = 1;
    private static final int COUNTRIES_LIST_ID = 2;
    //languages
    private static final int LANGUAGES_LISTS = 3;
    private static final int LANGUAGES_LISTS_ID = 4;
    //users
    private static final int USERS = 5;
    private static final int USERS_ID = 6;

    //personal
    private static final int PROFILES = 7;
    private static final int PROFILES_ID = 8;

    //APPLICATION
    private static final int APPLICATIONS=53;
    private static final int APPLICATIONS_ID=54;

    //Application type
    private static final int APPLICATION_TYPES=9;
    private static final int APPLICATION_TYPES_ID=10;

    //USER APPLICATION
    private static final int USER_APPLICATIONS=11;
    private static final int USER_APPLICATIONS_ID=12;

    //WORK EXPERIENCE
    private static final int WORK_EXPERIENCES=13;
    private static final int WORK_EXPERIENCES_ID=14;

    //USER WORK EXPERIENCE
    private static final int USER_WORK_EXPERIENCES=15;
    private static final int USER_WORK_EXPERIENCES_ID=16;

    //EDUCATION
    private static final int EDUCATIONS=17;
    private static final int EDUCATIONS_ID=18;

    //USER EDUCATION
    private static final int USER_EDUCATIONS=19;
    private static final int USER_EDUCATIONS_ID=20;

    //PERSONAL SKILLS
    private static final int SKILLS =21;
    private static final int SKILLS_ID =22;

    //LANGUAGE TYPES
    private static final int LANGUAGE_TYPES = 23;
    private static final int LANGUAGE_TYPES_ID = 24;

    //LANGUAGES
    private static final int LANGUAGES = 25;
    private static final int LANGUAGES_ID = 26;

    //USER/SKILLS LANGUAGES
    private static final int USER_LANGUAGES = 47;
    private static final int USER_LANGUAGES_ID = 48;

    //ASSESSMENT USER TYPE
    private static final int ASSESSMENT_USERS = 27;
    private static final int ASSESSMENT_USERS_ID = 28;

    //ASSESSMENT HYPER TYPE
    private static final int ASSESSMENT_HYPERS=29;
    private static final int ASSESSMENT_HYPERS_ID=30;

    //ASSESSMENT SUB TYPE
    private static final int ASSESSMENT_SUBS=31;
    private static final int ASSESSMENT_SUBS_ID=32;

    //ASSESSMENT LEVEL
    private static final int ASSESSMENT_LEVELS=33;
    private static final int ASSESSMENT_LEVELS_ID=34;

    //ASSESSMENT EVALUATION
    private static final int EVALUATIONS =35;
    private static final int EVALUATIONS_ID =36;

    //PERSONAL OTHER LANGUAGE
    private static final int LANGUAGE_OTHERS_EV=37;
    private static final int LANGUAGE_OTHERS_EV_ID=38;

    //USER MOTHER LANGUAGE VIEW
    private static final int USER_APPLICATIONS_VIEW =55;
    private static final int USER_APPLICATIONS_VIEW_ID =56;
    //USER MOTHER LANGUAGE VIEW
    private static final int USER_MOTHER_LANGUAGES_VIEW =49;
    private static final int USER_MOTHER_LANGUAGES_VIEW_ID =50;

    //USER OTHER LANGUAGE VIEW
    private static final int USER_OTHER_LANGUAGES_VIEW =51;
    private static final int USER_OTHER_LANGUAGES_VIEW_ID =52;

    //EVALUTION VIEW
    private static final int EVALUATIONS_VIEW =39;
    private static final int EVALUATIONS_VIEW_ID =40;

    //DATE EXPERIENCE VIEW
    private static final int DATE_EXPERIENCES_VIEW=41;
    private static final int DATE_EXPERIENCES_VIEW_ID=42;

    //DATE EDUCATION VIEW
    private static final int DATE_EDUCATIONS_VIEW =43;
    private static final int DATE_EDUCATIONS_VIEW_ID =44;

    //DATE EDUCATION VIEW
    private static final int EVALUATED_LANGUAGE_VIEWS =45;
    private static final int EVALUATED_LANGUAGE_VIEWS_ID =46;

    //TYPE OF URI-ES
    //PROFILE
    public static final String CONTENT_ITEM_TYPE_PROFILE= ContentResolver.CURSOR_ITEM_BASE_TYPE
            + "/profile";

    //APPLICATION
    public static final String CONTENT_ITEM_TYPE_APPLICATION = ContentResolver.CURSOR_ITEM_BASE_TYPE
            + "/application";

    //EXPERIENCE
    public static final String CONTENT_TYPE_WORK_EXPERIENCE = ContentResolver.CURSOR_DIR_BASE_TYPE
            + "/work_experiences";
    public static final String CONTENT_ITEM_TYPE_WORK_EXPERIENCE = ContentResolver.CURSOR_ITEM_BASE_TYPE
            + "/work_experience";

    //EDUCATION
    public static final String CONTENT_TYPE_EDUCATION = ContentResolver.CURSOR_DIR_BASE_TYPE
            + "/educations";
    public static final String CONTENT_ITEM_TYPE_EDUCATION = ContentResolver.CURSOR_ITEM_BASE_TYPE
            + "/education";

    public static final String CONTENT_TYPE_MOTHER_TONGUE = ContentResolver.CURSOR_DIR_BASE_TYPE
            + "/skills_native_languages";
    public static final String CONTENT_ITEM_TYPE_MOTHER_TONGUE = ContentResolver.CURSOR_ITEM_BASE_TYPE
            + "/skills_native_language";

    public static final String CONTENT_TYPE_OTHER_LANGUAGE = ContentResolver.CURSOR_DIR_BASE_TYPE
            + "/skills_other_languages";
    public static final String CONTENT_ITEM_TYPE_OTHER_LANGUAGE = ContentResolver.CURSOR_ITEM_BASE_TYPE
            + "/skills_other_language";

    public static final String CONTENT_TYPE_COMMUNICATION = ContentResolver.CURSOR_DIR_BASE_TYPE
            + "/skills";
    public static final String CONTENT_ITEM_TYPE_SKILL = ContentResolver.CURSOR_ITEM_BASE_TYPE
            + "/skills";

    private SQLiteDatabase db;
    private ExternalDbOpenHelper dbHelper;

    //HASHMAPS
    private static HashMap<String, String> COUNTRIES_LIST_PROJECTION_MAP;
    private static HashMap<String, String> LANGUAGES__LIST_PROJECTION_MAP;
    private static HashMap<String, String> USERS_PROJECTION_MAP;
    private static HashMap<String, String> PROFILE_PROJECTION_MAP;
    private static HashMap<String, String> APPLICATION_TYPE_PROJECTION_MAP;
    private static HashMap<String, String> APPLICATION_PROJECTION_MAP;
    private static HashMap<String, String> USER_APPLICATION_PROJECTION_MAP;
    private static HashMap<String, String> WORK_EXPERIENCE_PROJECTION_MAP;
    private static HashMap<String, String> USER_WORK_EXPERIENCE_PROJECTION_MAP;
    private static HashMap<String, String> EDUCATION_PROJECTION_MAP;
    private static HashMap<String, String> USER_EDUCATION_PROJECTION_MAP;
    private static HashMap<String, String> SKILLS_PROJECTION_MAP;
    private static HashMap<String, String> LANGUAGE_TYPE_PROJECTION_MAP;
    private static HashMap<String, String> LANGUAGE_PROJECTION_MAP;
    private static HashMap<String, String> USER_LANGUAGE_PROJECTION_MAP;
    private static HashMap<String, String> ASSESSMENT_HYPER_PROJECTION_MAP;
    private static HashMap<String, String> ASSESSMENT_SUB_PROJECTION_MAP;
    private static HashMap<String, String> ASSESSMENT_EVALUATION_PROJECTION_MAP;
    private static HashMap<String, String> ASSESSMENT_LEVEL_PROJECTION_MAP;
    private static HashMap<String, String> USER_APPLICATION_VIEW_PROJECTION_MAP;
    private static HashMap<String, String> USER_MOTHER_LANGUAGE_VIEW_PROJECTION_MAP;
    private static HashMap<String, String> USER_OTHER_LANGUAGE_VIEW_PROJECTION_MAP;
    private static HashMap<String, String> EVALUATION_VIEW_PROJECTION_MAP;
    private static HashMap<String, String> DATE_EXPERIENCE_VIEW_PROJECTION_MAP;
    private static HashMap<String, String> DATE_EDUCATION_VIEW_PROJECTION_MAP;
    private static HashMap<String, String> EVALUATED_LANGUAGE_VIEW_PROJECTION_MAP;

    private static final UriMatcher uriMatcher ;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "list_countries", COUNTRIES_LIST);
        uriMatcher.addURI(PROVIDER_NAME, "list_countries/#", COUNTRIES_LIST_ID);

        uriMatcher.addURI(PROVIDER_NAME, "list_languages", LANGUAGES_LISTS);
        uriMatcher.addURI(PROVIDER_NAME, "list_languages/#",LANGUAGES_ID);

        uriMatcher.addURI(PROVIDER_NAME, "users", USERS);
        uriMatcher.addURI(PROVIDER_NAME, "users/#", USERS_ID);

        uriMatcher.addURI(PROVIDER_NAME, "profile", PROFILES);
        uriMatcher.addURI(PROVIDER_NAME, "profile/#", PROFILES_ID);

        uriMatcher.addURI(PROVIDER_NAME, "application_type",APPLICATION_TYPES);
        uriMatcher.addURI(PROVIDER_NAME, "application_type/#",APPLICATION_TYPES_ID);

        uriMatcher.addURI(PROVIDER_NAME, "application",APPLICATIONS);
        uriMatcher.addURI(PROVIDER_NAME, "application/#",APPLICATIONS_ID);

        uriMatcher.addURI(PROVIDER_NAME, "user_application",USER_APPLICATIONS);
        uriMatcher.addURI(PROVIDER_NAME, "user_application/#",USER_APPLICATIONS_ID);

        uriMatcher.addURI(PROVIDER_NAME, "work_experience",WORK_EXPERIENCES);
        uriMatcher.addURI(PROVIDER_NAME, "work_experience/#",WORK_EXPERIENCES_ID);

        uriMatcher.addURI(PROVIDER_NAME, "user_work_experience",USER_WORK_EXPERIENCES);
        uriMatcher.addURI(PROVIDER_NAME, "user_work_experience/#",USER_WORK_EXPERIENCES_ID);

        uriMatcher.addURI(PROVIDER_NAME, "education",EDUCATIONS);
        uriMatcher.addURI(PROVIDER_NAME, "education/#",EDUCATIONS_ID);

        uriMatcher.addURI(PROVIDER_NAME, "user_education",USER_EDUCATIONS);
        uriMatcher.addURI(PROVIDER_NAME, "user_education/#",USER_EDUCATIONS_ID);

        uriMatcher.addURI(PROVIDER_NAME, "skills", SKILLS);
        uriMatcher.addURI(PROVIDER_NAME, "skills/#", SKILLS_ID);

        uriMatcher.addURI(PROVIDER_NAME, "language_type",LANGUAGE_TYPES);
        uriMatcher.addURI(PROVIDER_NAME, "language_type/#",LANGUAGE_TYPES_ID);

        uriMatcher.addURI(PROVIDER_NAME, "language",LANGUAGES);
        uriMatcher.addURI(PROVIDER_NAME, "language/#",LANGUAGES_ID);

        uriMatcher.addURI(PROVIDER_NAME, "user_language", USER_LANGUAGES);
        uriMatcher.addURI(PROVIDER_NAME, "user_language/#", USER_LANGUAGES_ID);

        uriMatcher.addURI(PROVIDER_NAME, "assessment_hyper_type",ASSESSMENT_HYPERS);
        uriMatcher.addURI(PROVIDER_NAME, "assessment_hyper_type/#",ASSESSMENT_HYPERS_ID);

        uriMatcher.addURI(PROVIDER_NAME, "assessment_sub_type",ASSESSMENT_SUBS);
        uriMatcher.addURI(PROVIDER_NAME, "assessment_sub_type/#",ASSESSMENT_SUBS_ID);

        uriMatcher.addURI(PROVIDER_NAME, "assessment_user_type",ASSESSMENT_USERS);
        uriMatcher.addURI(PROVIDER_NAME, "assessment_user_type/#",ASSESSMENT_USERS_ID);

        uriMatcher.addURI(PROVIDER_NAME, "assessment_level",ASSESSMENT_LEVELS);
        uriMatcher.addURI(PROVIDER_NAME, "assessment_level/#",ASSESSMENT_LEVELS_ID);

        uriMatcher.addURI(PROVIDER_NAME, "evaluation", EVALUATIONS);
        uriMatcher.addURI(PROVIDER_NAME, "evaluation/#", EVALUATIONS_ID);

        uriMatcher.addURI(PROVIDER_NAME, "user_application_view", USER_APPLICATIONS_VIEW);
        uriMatcher.addURI(PROVIDER_NAME, "user_application_view/#", USER_APPLICATIONS_VIEW_ID);

        uriMatcher.addURI(PROVIDER_NAME, "user_mother_language_view", USER_MOTHER_LANGUAGES_VIEW);
        uriMatcher.addURI(PROVIDER_NAME, "user_mother_language_view/#", USER_MOTHER_LANGUAGES_VIEW_ID);

        uriMatcher.addURI(PROVIDER_NAME, "user_other_language_view", USER_OTHER_LANGUAGES_VIEW);
        uriMatcher.addURI(PROVIDER_NAME, "user_other_language_view/#", USER_OTHER_LANGUAGES_VIEW_ID);

        uriMatcher.addURI(PROVIDER_NAME, "evaluation_view", EVALUATIONS_VIEW);
        uriMatcher.addURI(PROVIDER_NAME, "evaluation_view/#", EVALUATIONS_VIEW_ID);

        uriMatcher.addURI(PROVIDER_NAME, "date_experience_view", DATE_EXPERIENCES_VIEW);
        uriMatcher.addURI(PROVIDER_NAME, "date_experience_view/#", DATE_EXPERIENCES_VIEW_ID);

        uriMatcher.addURI(PROVIDER_NAME, "date_education_view", DATE_EDUCATIONS_VIEW);
        uriMatcher.addURI(PROVIDER_NAME, "date_education_view/#", DATE_EDUCATIONS_VIEW_ID);

        uriMatcher.addURI(PROVIDER_NAME, "evaluated_language_view", EVALUATED_LANGUAGE_VIEWS);
        uriMatcher.addURI(PROVIDER_NAME, "evaluated_language_view/#", EVALUATED_LANGUAGE_VIEWS_ID);
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        dbHelper = new ExternalDbOpenHelper(context);
        dbHelper.getWritableDatabase();
        return true;
            }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        db = dbHelper.getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();


        switch (uriMatcher.match(uri)){
            //COUNTRIES LIST
            //********************************************************************************************************
            case COUNTRIES_LIST:
                qb.setTables(COUNTRY_LIST_TABLE);
                qb.setProjectionMap(COUNTRIES_LIST_PROJECTION_MAP);
                break;
            case COUNTRIES_LIST_ID:
                qb.setTables(COUNTRY_LIST_TABLE);
                qb.appendWhere(COUNTRY_ID +"=" +uri.getPathSegments().get(1));
                break;

            //LANGUAGE LIST
            //********************************************************************************************************
            case LANGUAGES_LISTS:
                qb.setTables(LANGUAGES_LIST_TABLE);
                qb.setProjectionMap(LANGUAGES__LIST_PROJECTION_MAP);
                break;
            case LANGUAGES_LISTS_ID:
                qb.setTables(LANGUAGES_LIST_TABLE);
                qb.appendWhere(LANGUAGES_LIST_ID+"="+uri.getPathSegments().get(1));
                break;

            //USERS
            //********************************************************************************************************
            case USERS:
                qb.setTables(USER_TABLE);
                qb.setProjectionMap(USERS_PROJECTION_MAP);
                break;
            case USERS_ID:
                qb.setTables(USER_TABLE);
                qb.appendWhere(USER_ID +"=" +uri.getPathSegments().get(1));
                break;

            //PROFILE
            //********************************************************************************************************
            case PROFILES:
                qb.setTables(PROFILE_TABLE);
                qb.setProjectionMap(PROFILE_PROJECTION_MAP);
                break;
            case PROFILES_ID:
                qb.setTables(PROFILE_TABLE);
                qb.appendWhere(PROFILE_USERS_PK_FK +"=" +uri.getPathSegments().get(1));
                break;

            //APPLICATION TYPES
            //********************************************************************************************************
            case APPLICATION_TYPES:
                qb.setTables(APPLICATION_TYPE_TABLE);
                qb.setProjectionMap(APPLICATION_TYPE_PROJECTION_MAP);
                break;
            case APPLICATION_TYPES_ID:
                qb.setTables(APPLICATION_TYPE_TABLE);
                qb.appendWhere(APPLICATION_TYPE_ID +"=" +uri.getPathSegments().get(1));

            //APPLICATION
            //********************************************************************************************************
            case APPLICATIONS:
                qb.setTables(APPLICATION_TABLE);
                qb.setProjectionMap(APPLICATION_PROJECTION_MAP);
                break;
            case APPLICATIONS_ID:
                qb.setTables(APPLICATION_TABLE);
                qb.appendWhere(APPLICATION_ID +"=" +uri.getPathSegments().get(1));

            //USER APPLICATION
            //********************************************************************************************************
            case USER_APPLICATIONS:
                qb.setTables(USER_APPLICATION_TABLE);
                qb.setProjectionMap(USER_APPLICATION_PROJECTION_MAP);
                break;
            case USER_APPLICATIONS_ID:
                qb.setTables(USER_APPLICATION_TABLE);
                qb.appendWhere(USER_APPLICATION_USER_FK +"=" +uri.getPathSegments().get(1));
                break;

            //WORK EXPERIENCE
            //********************************************************************************************************
            case WORK_EXPERIENCES:
                qb.setTables(WORK_EXPERIENCE_TABLE);
                qb.setProjectionMap(WORK_EXPERIENCE_PROJECTION_MAP);
                break;
            case WORK_EXPERIENCES_ID:
                qb.setTables(WORK_EXPERIENCE_TABLE);
                qb.appendWhere(WORK_EXPERIENCE_ID +"=" +uri.getPathSegments().get(1));
                break;

            //USER WORK EXPERIENCE
            //********************************************************************************************************
            case USER_WORK_EXPERIENCES:
                qb.setTables(USER_WORK_EXPERIENCE_TABLE);
                qb.setProjectionMap(USER_WORK_EXPERIENCE_PROJECTION_MAP);
                break;
            case USER_WORK_EXPERIENCES_ID:
                qb.setTables(USER_WORK_EXPERIENCE_TABLE);
                qb.appendWhere(USER_WORK_EXPERIENCE_ID +"=" +uri.getPathSegments().get(1));
                break;

            //EDUCATION
            //********************************************************************************************************
            case EDUCATIONS:
                qb.setTables(EDUCATION_TABLE);
                qb.setProjectionMap(EDUCATION_PROJECTION_MAP);
                break;
            case EDUCATIONS_ID:
                qb.setTables(EDUCATION_TABLE);
                qb.appendWhere(EDUCATION_ID +"=" +uri.getPathSegments().get(1));
                break;

            //USER EDUCATION
            //********************************************************************************************************
            case USER_EDUCATIONS:
                qb.setTables(USER_EDUCATION_TABLE);
                qb.setProjectionMap(USER_EDUCATION_PROJECTION_MAP);
                break;
            case USER_EDUCATIONS_ID:
                qb.setTables(USER_EDUCATION_TABLE);
                qb.appendWhere(USER_EDUCATION_ID +"=" +uri.getPathSegments().get(1));
                break;

            //SKILLS
            //**********************************************************************************************
            case SKILLS:
                qb.setTables(SKILL_TABLE);
                qb.setProjectionMap(SKILLS_PROJECTION_MAP);
                break;
            case SKILLS_ID:
                qb.setTables(SKILL_TABLE);
                qb.appendWhere(SKILL_ID +"=" +uri.getPathSegments().get(1));
                break;

            //LANGUAGE TYPE
            //**********************************************************************************************
            case LANGUAGE_TYPES:
                qb.setTables(TYPE_LANGUAGE_TABLE);
                qb.setProjectionMap(LANGUAGE_TYPE_PROJECTION_MAP);
                break;
            case LANGUAGE_TYPES_ID:
                qb.setTables(TYPE_LANGUAGE_TABLE);
                qb.appendWhere(TYPE_LANGUAGE_ID + "=" + uri.getPathSegments().get(1));
                break;

            //LANGUAGES
            //***********************************************************************************************
            case LANGUAGES:
                qb.setTables(LANGUAGE_TABLE);
                qb.setProjectionMap(LANGUAGE_PROJECTION_MAP);
                break;
            case LANGUAGES_ID:
                qb.setTables(LANGUAGE_TABLE);
                qb.appendWhere(LANGUAGE_ID + "=" + uri.getPathSegments().get(1));
                break;

            //USER LANGUAGE
            //***********************************************************************************************
            case USER_LANGUAGES:
                qb.setTables(USER_LANGUAGE_TABLE);
                qb.setProjectionMap(USER_LANGUAGE_PROJECTION_MAP);
                break;
            case USER_LANGUAGES_ID:
                qb.setTables(USER_LANGUAGE_TABLE);
                qb.appendWhere(USER_LANG_ID + "=" + uri.getPathSegments().get(1));
                break;

            //ASSESSMENT
            //***********************************************************************************************
            case ASSESSMENT_HYPERS:
                qb.setTables(ASSESSMENT_HYPER_TYPE_TABLE);
                qb.setProjectionMap(ASSESSMENT_HYPER_PROJECTION_MAP);
                break;
            case ASSESSMENT_HYPERS_ID:
                qb.setTables(ASSESSMENT_HYPER_TYPE_TABLE);
                qb.appendWhere(ASSESSMENT_HYPER_TYPE_ID + "=" + uri.getPathSegments().get(1));
                break;
            case ASSESSMENT_SUBS:
                qb.setTables(ASSESSMENT_SUB_TYPE_TABLE);
                qb.setProjectionMap(ASSESSMENT_SUB_PROJECTION_MAP);
                break;
            case ASSESSMENT_SUBS_ID:
                qb.setTables(ASSESSMENT_SUB_TYPE_TABLE);
                qb.appendWhere(ASSESSMENT_SUB_TYPE_ID + "=" + uri.getPathSegments().get(1));
                break;

            //EVALUATION
            //***********************************************************************************************
            case EVALUATIONS:
                qb.setTables(LANGUAGE_EVALUATION_TABLE);
                qb.setProjectionMap(ASSESSMENT_EVALUATION_PROJECTION_MAP);
                break;
            case EVALUATIONS_ID:
                qb.setTables(LANGUAGE_EVALUATION_TABLE);
                qb.appendWhere(LANGUAGE_EVALUATION_LANG_FK + "=" + uri.getPathSegments().get(1));
                break;

            //ASSESSMENT LEVELS
            //********************************************************************************************************
            case ASSESSMENT_LEVELS:
                qb.setTables(ASSESSMENT_LEVEL_TABLE);
                qb.setProjectionMap(ASSESSMENT_LEVEL_PROJECTION_MAP);
                break;
            case ASSESSMENT_LEVELS_ID:
                qb.setTables(ASSESSMENT_LEVEL_TABLE);
                qb.appendWhere(ASSESSMENT_LEVEL_ID + "=" + uri.getPathSegments().get(1));
                break;

            //USER APPLICATION VIEW
            //***********************************************************************************************
            case USER_APPLICATIONS_VIEW:
                qb.setTables(USER_APPLICATION_VIEW);
                qb.setProjectionMap(USER_APPLICATION_VIEW_PROJECTION_MAP);
                break;
            case USER_APPLICATIONS_VIEW_ID:
                qb.setTables(USER_APPLICATION_VIEW);
                qb.appendWhere(USER_APPLICATION_APPLICATION_FK + "=" + uri.getPathSegments().get(1));
                break;

            //USER MOTHER LANGUAGE VIEW
            //***********************************************************************************************
            case USER_MOTHER_LANGUAGES_VIEW:
                qb.setTables(USER_MOTHER_LANGUAGE_VIEW);
                qb.setProjectionMap(USER_MOTHER_LANGUAGE_VIEW_PROJECTION_MAP);
                break;
            case USER_MOTHER_LANGUAGES_VIEW_ID:
                qb.setTables(USER_MOTHER_LANGUAGE_VIEW);
                qb.appendWhere(USER_VIEW_LANGUAGE_ID + "=" + uri.getPathSegments().get(1));
                break;

            //USER OTHER LANGUAGE VIEW
            //***********************************************************************************************
            case USER_OTHER_LANGUAGES_VIEW:
                qb.setTables(USER_OTHER_LANGUAGE_VIEW);
                qb.setProjectionMap(USER_OTHER_LANGUAGE_VIEW_PROJECTION_MAP);
                break;
            case USER_OTHER_LANGUAGES_VIEW_ID:
                qb.setTables(USER_OTHER_LANGUAGE_VIEW);
                qb.appendWhere(USER_VIEW_LANGUAGE_ID + "=" + uri.getPathSegments().get(1));
                break;

            //EVALUATION VIEW
            //***********************************************************************************************
            case EVALUATIONS_VIEW:
                qb.setTables(EVALUATION_VIEW);
                qb.setProjectionMap(EVALUATION_VIEW_PROJECTION_MAP);
                break;
            case EVALUATIONS_VIEW_ID:
                qb.setTables(EVALUATION_VIEW);
                qb.appendWhere(EVALUATION_VIEW_ID + "=" + uri.getPathSegments().get(1));
                break;

            //EXPERIENCE DATE SECTION
            //***********************************************************************************************
            case DATE_EXPERIENCES_VIEW:
                qb.setTables(DATE_EXPERIENCE_VIEW);
                qb.setProjectionMap(DATE_EXPERIENCE_VIEW_PROJECTION_MAP);
                break;
            case DATE_EXPERIENCES_VIEW_ID:
                qb.setTables(DATE_EXPERIENCE_VIEW);
                qb.appendWhere(DATE_EXPERIENCE_ID + "=" + uri.getPathSegments().get(1));
                break;

            //EDUCATION DATE SECTION
            //***********************************************************************************************
            case DATE_EDUCATIONS_VIEW:
                qb.setTables(DATE_EDUCATION_VIEW);
                qb.setProjectionMap(DATE_EDUCATION_VIEW_PROJECTION_MAP);
                break;
            case DATE_EDUCATIONS_VIEW_ID:
                qb.setTables(DATE_EDUCATION_VIEW);
                qb.appendWhere(DATE_EDUCATION_ID + "=" + uri.getPathSegments().get(1));
                break;

            //EVALUATED LANGUAGE VIEW
            //***********************************************************************************************
            case EVALUATED_LANGUAGE_VIEWS:
                qb.setTables(LANG_EVALUATION_VIEW);
                qb.setProjectionMap(EVALUATED_LANGUAGE_VIEW_PROJECTION_MAP);
                break;
            case EVALUATED_LANGUAGE_VIEWS_ID:
                qb.setTables(LANG_EVALUATION_VIEW);
                qb.appendWhere(LANG_EVALUATION_ID + "=" + uri.getPathSegments().get(1));
                break;

            default:
                throw new IllegalArgumentException("Unknown Uri " + uri);

        }


        Cursor c =qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        //register to watch a content URI for changes
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return  c;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        boolean replace = false;

        Uri _uri = null;
        int uriType = uriMatcher.match(uri);
        SQLiteDatabase sqlDB = dbHelper.getWritableDatabase();
        if ( values.containsKey( SQL_INSERT_OR_REPLACE )) {
            replace = values.getAsBoolean( SQL_INSERT_OR_REPLACE );

            // Clone the values object, so we don't modify the original.
            // This is not strictly necessary, but depends on your needs
            values = new ContentValues( values );

            // Remove the key, so we don't pass that on to db.insert() or db.replace()
            values.remove( SQL_INSERT_OR_REPLACE );
        }
        switch (uriType) {

            //USERS
            case USERS:
                long idUser;
                if ( replace ) {
                    idUser = sqlDB.replace(USER_TABLE, null, values);
                    _uri = ContentUris.withAppendedId(CONTENT_URI_USER,idUser);
                    getContext().getContentResolver().notifyChange(_uri,null);
                } else {
                    idUser = sqlDB.insert(USER_TABLE, null, values);
                    _uri = ContentUris.withAppendedId(CONTENT_URI_USER,idUser);
                    getContext().getContentResolver().notifyChange(_uri,null);
                }
                break;

            //PROFILE
            case PROFILES:
                long idProfile;
                if ( replace ) {
                    idProfile = sqlDB.replace(PROFILE_TABLE, null, values);
                    _uri = ContentUris.withAppendedId(CONTENT_URI_PROFILE,idProfile);
                    getContext().getContentResolver().notifyChange(_uri,null);
                } else {
                    idProfile = sqlDB.insert(PROFILE_TABLE, null, values);
                    _uri = ContentUris.withAppendedId(CONTENT_URI_PROFILE,idProfile);
                    getContext().getContentResolver().notifyChange(_uri,null);
                }
                break;

            //USER APPLICATION
            case APPLICATIONS:
                long idApplication;
                if ( replace ) {
                    idApplication = sqlDB.replace(APPLICATION_TABLE, null, values);
                    _uri = ContentUris.withAppendedId(CONTENT_URI_APPLICATION,idApplication);
                    getContext().getContentResolver().notifyChange(_uri,null);
                } else {
                    idApplication = sqlDB.insert(APPLICATION_TABLE, null, values);
                    _uri = ContentUris.withAppendedId(CONTENT_URI_APPLICATION,idApplication);
                    getContext().getContentResolver().notifyChange(_uri,null);
                }
                break;
            //USER APPLICATION
            case USER_APPLICATIONS:
                long idUserApplication;
                if ( replace ) {
                    idUserApplication = sqlDB.replace(USER_APPLICATION_TABLE, null, values);
                    _uri = ContentUris.withAppendedId(CONTENT_URI_USER_APPLICATION,idUserApplication);
                    getContext().getContentResolver().notifyChange(_uri,null);
                } else {
                    idUserApplication = sqlDB.insert(USER_APPLICATION_TABLE, null, values);
                    _uri = ContentUris.withAppendedId(CONTENT_URI_USER_APPLICATION,idUserApplication);
                    getContext().getContentResolver().notifyChange(_uri,null);
                }
                break;

            //WORK EXPERIENCE
            case WORK_EXPERIENCES:
                long idExperience;
                if ( replace ) {
                    idExperience = sqlDB.replace(WORK_EXPERIENCE_TABLE, null, values);
                    _uri = ContentUris.withAppendedId(CONTENT_URI_WORK_EXPERIENCE,idExperience);
                    getContext().getContentResolver().notifyChange(_uri,null);
                } else {
                    idExperience = sqlDB.insert(WORK_EXPERIENCE_TABLE, null, values);
                    _uri = ContentUris.withAppendedId(CONTENT_URI_WORK_EXPERIENCE,idExperience);
                    getContext().getContentResolver().notifyChange(_uri,null);
                }
                break;

            //USER WORK EXPERIENCE
            case USER_WORK_EXPERIENCES:
                long idUserExperience;
                if ( replace ) {
                    idUserExperience = sqlDB.replace(USER_WORK_EXPERIENCE_TABLE, null, values);
                    _uri = ContentUris.withAppendedId(CONTENT_URI_USER_WORK_EXPERIENCE,idUserExperience);
                    getContext().getContentResolver().notifyChange(_uri,null);
                } else {
                    idUserExperience = sqlDB.insert(USER_WORK_EXPERIENCE_TABLE, null, values);
                    _uri = ContentUris.withAppendedId(CONTENT_URI_USER_WORK_EXPERIENCE,idUserExperience);
                    getContext().getContentResolver().notifyChange(_uri,null);
                }
                break;

            //EDUCATION
            case EDUCATIONS:
                long idEducation;
                if ( replace ) {
                    idEducation = sqlDB.replace(EDUCATION_TABLE, null, values);
                    _uri = ContentUris.withAppendedId(CONTENT_URI_EDUCATION,idEducation);
                    getContext().getContentResolver().notifyChange(_uri,null);
                } else {
                    idEducation = sqlDB.insert(EDUCATION_TABLE, null, values);
                    _uri = ContentUris.withAppendedId(CONTENT_URI_EDUCATION,idEducation);
                    getContext().getContentResolver().notifyChange(_uri,null);
                }
                break;

            //USER EDUCATION
            case USER_EDUCATIONS:
                long idUserEducation;
                if ( replace ) {
                    idUserEducation = sqlDB.replace(USER_EDUCATION_TABLE, null, values);
                    _uri = ContentUris.withAppendedId(CONTENT_URI_USER_EDUCATION,idUserEducation);
                    getContext().getContentResolver().notifyChange(_uri,null);
                } else {
                    idUserEducation = sqlDB.insert(USER_EDUCATION_TABLE, null, values);
                    _uri = ContentUris.withAppendedId(CONTENT_URI_USER_EDUCATION,idUserEducation);
                    getContext().getContentResolver().notifyChange(_uri,null);
                }
                break;

            //SKILLS
            case SKILLS:
                long idSkills;
                if ( replace ) {
                    idSkills = sqlDB.replace(SKILL_TABLE, null, values);
                    _uri = ContentUris.withAppendedId(CONTENT_URI_SKILLS,idSkills);
                    getContext().getContentResolver().notifyChange(_uri,null);
                } else {
                    idSkills = sqlDB.insert(SKILL_TABLE, null, values);
                    _uri = ContentUris.withAppendedId(CONTENT_URI_SKILLS,idSkills);
                    getContext().getContentResolver().notifyChange(_uri,null);
                }
                break;


            //LANGUAGE
            case LANGUAGES:
                long idLanguage;
                if ( replace ) {
                    idLanguage = sqlDB.replace(LANGUAGE_TABLE, null, values);
                    _uri = ContentUris.withAppendedId(CONTENT_URI_LANGUAGE,idLanguage);
                    Uri otherLanguageURI = ContentUris.withAppendedId(CONTENT_URI_VIEW_USER_OTHER_LANGUAGE, idLanguage);
                    Uri motherLanguageURI = ContentUris.withAppendedId(CONTENT_URI_VIEW_USER_MOTHER_LANGUAGE, idLanguage);
                    getContext().getContentResolver().notifyChange(_uri,null);
                    getContext().getContentResolver().notifyChange(motherLanguageURI,null);
                    getContext().getContentResolver().notifyChange(otherLanguageURI,null);
                } else {
                    idLanguage = sqlDB.insert(LANGUAGE_TABLE, null, values);
                    _uri = ContentUris.withAppendedId(CONTENT_URI_LANGUAGE,idLanguage);
                    Uri otherLanguageURI = ContentUris.withAppendedId(CONTENT_URI_VIEW_USER_OTHER_LANGUAGE, idLanguage);
                    Uri motherLanguageURI = ContentUris.withAppendedId(CONTENT_URI_VIEW_USER_MOTHER_LANGUAGE, idLanguage);
                    getContext().getContentResolver().notifyChange(_uri,null);
                    getContext().getContentResolver().notifyChange(motherLanguageURI,null);
                    getContext().getContentResolver().notifyChange(otherLanguageURI,null);
                }
                break;

            //USER LANGUAGE
            case USER_LANGUAGES:
                long idUserLang;
                if ( replace ) {
                    idUserLang = sqlDB.replace(USER_LANGUAGE_TABLE, null, values);
                    _uri = ContentUris.withAppendedId(CONTENT_URI_USER_LANGUAGE,idUserLang);
                    getContext().getContentResolver().notifyChange(_uri,null);
                } else {
                    idUserLang = sqlDB.insert(USER_LANGUAGE_TABLE, null, values);
                    _uri = ContentUris.withAppendedId(CONTENT_URI_USER_LANGUAGE,idUserLang);
                    getContext().getContentResolver().notifyChange(_uri,null);
                }
                break;

            //EVALUATION
            case EVALUATIONS:
                long idEvaluation;
                if ( replace ) {
                    idEvaluation = sqlDB.replace(LANGUAGE_EVALUATION_TABLE, null, values);
                    _uri = ContentUris.withAppendedId(CONTENT_URI_EVALUATION,idEvaluation);
                    getContext().getContentResolver().notifyChange(_uri,null);
                } else {
                    idEvaluation = sqlDB.insert(LANGUAGE_EVALUATION_TABLE, null, values);
                    _uri = ContentUris.withAppendedId(CONTENT_URI_EVALUATION,idEvaluation);
                    getContext().getContentResolver().notifyChange(_uri,null);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);


        }

        return _uri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        int uriType = uriMatcher.match(uri);
        String id = uri.getLastPathSegment();
        SQLiteDatabase sqlDB = dbHelper.getWritableDatabase();
        int rowsDeleted = 0;
        switch (uriType) {
            //PROFILE
            //**************************************************************************************************************
            case APPLICATIONS:
                rowsDeleted = sqlDB.delete(APPLICATION_TABLE, selection,
                        selectionArgs);
                break;
            case APPLICATIONS_ID:
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = sqlDB.delete(APPLICATION_TABLE,
                            APPLICATION_ID + "=" + id,
                            null);
                } else {
                    rowsDeleted = sqlDB.delete(PROFILE_TABLE,
                            USER_ID + "=" + id
                                    + " and " + selection,
                            selectionArgs);
                }
                break;

            //PROFILE
            //**************************************************************************************************************
            case PROFILES:
                rowsDeleted = sqlDB.delete(PROFILE_TABLE, selection,
                        selectionArgs);
                break;
            case PROFILES_ID:
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = sqlDB.delete(PROFILE_TABLE,
                            PROFILE_USERS_PK_FK + "=" + id,
                            null);
                } else {
                    rowsDeleted = sqlDB.delete(PROFILE_TABLE,
                            USER_ID + "=" + id
                                    + " and " + selection,
                            selectionArgs);
                }
                break;

            //EXPERIENCE
            //**************************************************************************************************************
            case WORK_EXPERIENCES:
                rowsDeleted = sqlDB.delete(WORK_EXPERIENCE_TABLE, selection,
                        selectionArgs);
                break;
            case WORK_EXPERIENCES_ID:
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = sqlDB.delete(WORK_EXPERIENCE_TABLE,
                            WORK_EXPERIENCE_ID + "=" + id,
                            null);
                } else {
                    rowsDeleted = sqlDB.delete(WORK_EXPERIENCE_TABLE,
                            WORK_EXPERIENCE_ID + "=" + id
                                    + " and " + selection,
                            selectionArgs);
                }
                break;

            //EDUCATION
            //**************************************************************************************************************
            case EDUCATIONS:
                rowsDeleted = sqlDB.delete(EDUCATION_TABLE, selection,
                        selectionArgs);
                break;
            case EDUCATIONS_ID:
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = sqlDB.delete(EDUCATION_TABLE,
                            EDUCATION_ID + "=" + id,
                            null);
                } else {
                    rowsDeleted = sqlDB.delete(EDUCATION_TABLE,
                            EDUCATION_ID + "=" + id
                                    + " and " + selection,
                            selectionArgs);
                }
                break;

            //LANGUAGE
            //**************************************************************************************************************
            case LANGUAGES:
                rowsDeleted = sqlDB.delete(LANGUAGE_TABLE, selection,
                        selectionArgs);
                break;
            case LANGUAGES_ID:
                Uri otherLanguageURI = ContentUris.withAppendedId(CONTENT_URI_VIEW_USER_OTHER_LANGUAGE, Long.parseLong(id));
                Uri motherLanguageURI = ContentUris.withAppendedId(CONTENT_URI_VIEW_USER_MOTHER_LANGUAGE, Long.parseLong(id));
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = sqlDB.delete(LANGUAGE_TABLE,
                            LANGUAGE_ID + "=" + id,
                            null);
                    getContext().getContentResolver().notifyChange(otherLanguageURI, null);
                    getContext().getContentResolver().notifyChange(motherLanguageURI, null);
                } else {
                    rowsDeleted = sqlDB.delete(LANGUAGE_TABLE,
                           LANGUAGE_ID + "=" + id
                                    + " and " + selection,
                            selectionArgs);
                    getContext().getContentResolver().notifyChange(otherLanguageURI, null);
                    getContext().getContentResolver().notifyChange(motherLanguageURI, null);
                }
                break;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        db.close();
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int uriType = uriMatcher.match(uri);
        SQLiteDatabase sqlDB = dbHelper.getWritableDatabase();
        int rowsUpdated = 0;
        String id = uri.getLastPathSegment();

        Uri motherLanguageURI,otherLanguageURI;
        switch (uriType) {
            //PROFILE

            //EXPERIENCE
            //**************************************************************************************************************
            case WORK_EXPERIENCES:
                rowsUpdated = sqlDB.update(WORK_EXPERIENCE_TABLE,
                        values,
                        selection,
                        selectionArgs);
                break;
            case WORK_EXPERIENCES_ID:
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = sqlDB.update(WORK_EXPERIENCE_TABLE,
                            values,
                            WORK_EXPERIENCE_ID + "=" + id,
                            null);
                } else {
                    rowsUpdated = sqlDB.update(WORK_EXPERIENCE_TABLE,
                            values,
                            WORK_EXPERIENCE_ID + "=" + id
                                    + " and "
                                    + selection,
                            selectionArgs);
                }
                break;

            //EDUCATION
            //**************************************************************************************************************
            case EDUCATIONS:
                rowsUpdated = sqlDB.update(EDUCATION_TABLE,
                        values,
                        selection,
                        selectionArgs);
                break;
            case EDUCATIONS_ID:
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = sqlDB.update(EDUCATION_TABLE,
                            values,
                            EDUCATION_ID + "=" + id,
                            null);
                } else {
                    rowsUpdated = sqlDB.update(EDUCATION_TABLE,
                            values,
                            EDUCATION_ID + "=" + id
                                    + " and "
                                    + selection,
                            selectionArgs);
                }
                break;

            //LANGUAGE
            //**************************************************************************************************************
            case LANGUAGES:
                //Initialize languages URI-s
                otherLanguageURI = ContentUris.withAppendedId(CONTENT_URI_VIEW_USER_OTHER_LANGUAGE, Long.parseLong(id));
                motherLanguageURI = ContentUris.withAppendedId(CONTENT_URI_VIEW_USER_MOTHER_LANGUAGE, Long.parseLong(id));

                rowsUpdated = sqlDB.update(LANGUAGE_TABLE,
                        values,
                        selection,
                        selectionArgs);
                //Notify lnguage views
                getContext().getContentResolver().notifyChange(otherLanguageURI, null);
                getContext().getContentResolver().notifyChange(motherLanguageURI, null);
                break;
            case LANGUAGES_ID:
                //Initialize languages URI-s
                otherLanguageURI = ContentUris.withAppendedId(CONTENT_URI_VIEW_USER_OTHER_LANGUAGE, Long.parseLong(id));
                motherLanguageURI = ContentUris.withAppendedId(CONTENT_URI_VIEW_USER_MOTHER_LANGUAGE, Long.parseLong(id));

                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = sqlDB.update(LANGUAGE_TABLE,
                            values,
                            LANGUAGE_ID + "=" + id,
                            null);
                    //Notify lnguage views
                    getContext().getContentResolver().notifyChange(otherLanguageURI, null);
                    getContext().getContentResolver().notifyChange(motherLanguageURI, null);

                } else {
                    rowsUpdated = sqlDB.update(LANGUAGE_TABLE,
                            values,
                            LANGUAGE_ID + "=" + id
                                    + " and "
                                    + selection,
                            selectionArgs);

                    //Notify lnguage views
                    getContext().getContentResolver().notifyChange(otherLanguageURI, null);
                    getContext().getContentResolver().notifyChange(motherLanguageURI, null);
                }
                break;

            //USER LANGUAGE
            //**************************************************************************************************************
            case USER_LANGUAGES:
                rowsUpdated = sqlDB.update(USER_LANGUAGE_TABLE,
                        values,
                        selection,
                        selectionArgs);
                break;
            case USER_LANGUAGES_ID:
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = sqlDB.update(USER_LANGUAGE_TABLE,
                            values,
                            USER_LANG_ID + "=" + id,
                            null);
                } else {
                    rowsUpdated = sqlDB.update(USER_LANGUAGE_TABLE,
                            values,
                            USER_LANG_ID + "=" + id
                                    + " and "
                                    + selection,
                            selectionArgs);
                }
                break;
            //EVALUATION
            case EVALUATIONS:
                rowsUpdated = sqlDB.update(LANGUAGE_EVALUATION_TABLE,
                        values,
                        selection,
                        selectionArgs);
                break;
            case EVALUATIONS_ID:

                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = sqlDB.update(LANGUAGE_EVALUATION_TABLE,
                            values,
                            "EXISTS(SELECT 1 FROM " + ASSESSMENT_EVALUATION_TABLE + " WHERE "+ ASSESSMENT_EVALUATION_ID
                            + " = " + LANGUAGE_EVALUATION_ASSESSMENT_FK + " and " + ASSESSMENT_EVALUATION_SUB_TYPE_FK + " =  ? " + " and "+
                                    LANGUAGE_EVALUATION_LANG_FK + " = ? )",
                            new String[]{selection,id});

                }else{
                    rowsUpdated = sqlDB.update(LANGUAGE_EVALUATION_TABLE,
                            values,
                            "EXISTS(SELECT 1 FROM " + ASSESSMENT_EVALUATION_TABLE + " WHERE "+ ASSESSMENT_EVALUATION_ID
                                    + " = " + LANGUAGE_EVALUATION_ASSESSMENT_FK + " and " + ASSESSMENT_EVALUATION_SUB_TYPE_FK + " =  ? " + " and "+
                                    LANGUAGE_EVALUATION_LANG_FK + " = ? )",
                            new String[]{selection,id});
                }
                break;

            //SKILLS
            //**************************************************************************************************************
            case SKILLS:
                rowsUpdated = sqlDB.update(SKILL_TABLE,
                        values,
                        selection,
                        selectionArgs);
                break;
            case SKILLS_ID:
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = sqlDB.update(SKILL_TABLE,
                            values,
                            SKILL_ID + "=" + id,
                            null);
                } else {
                    rowsUpdated = sqlDB.update(SKILL_TABLE,
                            values,
                            SKILL_ID + "=" + id
                                    + " and "
                                    + selection,
                            selectionArgs);
                }
                break;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }

}
