package edu.psu.mob_apps.lab5.persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class JokeDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Joke.db";
	
    private static final String SQL_CREATE_ENTRIES =
    	    "CREATE TABLE " + JokeDbContract.TblJoke.TABLE_NAME + " (" +
    	    		JokeDbContract.TblJoke._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
    	    		JokeDbContract.TblJoke.COLUMN_NAME_SETUP + " " +
    	    		JokeDbContract.TblJoke.COLUMN_TYPE_SETUP + "," +
    	    		JokeDbContract.TblJoke.COLUMN_NAME_PUNCHLINE + " " +
    	    		JokeDbContract.TblJoke.COLUMN_TYPE_PUNCHLINE + ","+
    	    		JokeDbContract.TblJoke.COLUMN_NAME_USED+ " "+
    	    		JokeDbContract.TblJoke.COLUMN_TYPE_USED+ ")";
    
    private static final String SQL_DELETE_ENTRIES =
    	    "DROP TABLE IF EXISTS " +  JokeDbContract.TblJoke.TABLE_NAME;

    private Context context;
	
    public JokeDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        
        this.context = context;
    }

	@Override
	public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);	
        
        String[] setups = context.getResources().getStringArray(R.array.JokeSetup);
        String[] punchlines = context.getResources().getStringArray(R.array.JokePunchline);
        

        for (int i = 0; i < setups.length; i++) {
        	ContentValues values = new ContentValues();
        	values.put(JokeDbContract.TblJoke.COLUMN_NAME_SETUP, setups[i]);
        	values.put(JokeDbContract.TblJoke.COLUMN_NAME_PUNCHLINE, punchlines[i]);
        	values.put(JokeDbContract.TblJoke.COLUMN_NAME_USED, 0);
        	db.insert(JokeDbContract.TblJoke.TABLE_NAME, null, values);
        }
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
	}
	
    public void onDowngrade(SQLiteDatabase db, int oldVersion, 
            int newVersion) {
    	onUpgrade(db, oldVersion, newVersion);
    }
}
	

