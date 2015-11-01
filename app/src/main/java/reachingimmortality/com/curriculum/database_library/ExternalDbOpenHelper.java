package reachingimmortality.com.curriculum.database_library;



import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ExternalDbOpenHelper extends SQLiteOpenHelper {

    //The Android's default system path of your application database.
    public static String DB_PATH;
    // name of existing database
    public static String DB_NAME = "virido_curriculum";
    public SQLiteDatabase database;
    public final Context context;


    public SQLiteDatabase getDb() {
        return database;
    }

    public ExternalDbOpenHelper(Context context) {
        super(context,DB_NAME, null, 1);
        this.context = context;
		 /*Constructor
	      Takes and keeps a reference of the passed context in order to access
	      to the application assets and resources.
	      @param context
	     */
        String packageName = context.getPackageName();
        DB_PATH = String.format("//data//data//%s//databases//", packageName);
        openDataBase();
        Log.d("ExternalDB","constructor");
    }

    //Creates a empty database on the system and rewrites it with your own database
    public void createDataBase() {
        boolean dbExist = checkDataBase();
        if (!dbExist) {
            this.getReadableDatabase();
            try {
                copyDataBase();
            } catch (IOException e) {
                Log.e(this.getClass().toString(), "Copying error");
                throw new Error("Error copying database!");
            }
        } else {
            Log.i(this.getClass().toString(), "Database already exists");
        }
    }

    //Check if the database already exist to avoid re-copying the file each time you open the application.
    //return true if it exists, false if it doesn't

    private boolean checkDataBase() {
        SQLiteDatabase checkDb = null;
        try {
            String path = DB_PATH + DB_NAME;
            checkDb = SQLiteDatabase.openDatabase(path, null,
                    SQLiteDatabase.OPEN_READONLY);
        } catch (SQLException e) {
            Log.e(this.getClass().toString(), "Error while checking db");
        }

        if (checkDb != null) {
            checkDb.close();
        }
        return checkDb != null;
    }

    //Copies your database from your local assets-folder to the just created empty database in the
    //system folder, from where it can be accessed and handled.
    //This is done by transfering bytestream.

    private void copyDataBase() throws IOException {
        // Open your local db as the input stream
        InputStream externalDbStream = context.getAssets().open(DB_NAME);

        // Path to the just created empty db
        String outFileName = DB_PATH + DB_NAME;

        //Open the empty db as the output stream
        OutputStream localDbStream = new FileOutputStream(outFileName);

        //Close the streams
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = externalDbStream.read(buffer)) > 0) {
            localDbStream.write(buffer, 0, bytesRead);
        }

        localDbStream.close();
        externalDbStream.close();

    }
    //Open the database
    public SQLiteDatabase openDataBase() throws SQLException {
        String path = DB_PATH + DB_NAME;
        if (database == null) {
            createDataBase();
            database = SQLiteDatabase.openDatabase(path, null,
                    SQLiteDatabase.OPEN_READWRITE);
        }
        return database;
    }

    @Override
    public synchronized void close() {
        if (database != null) {
            database.close();
        }
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }



}

