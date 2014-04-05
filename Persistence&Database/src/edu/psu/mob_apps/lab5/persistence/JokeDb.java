package edu.psu.mob_apps.lab5.persistence;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

// Singleton class
public class JokeDb {

	private static JokeDb theJokeDb;
	private SQLiteDatabase theDb;
	private boolean isReady;
	
	private JokeDb() { }
	
	public static JokeDb getInstance(Context context) {
		
		if (theJokeDb == null) {		
		    theJokeDb = new JokeDb();
		    theJokeDb.isReady = false;
			
			// Open DB in an AsyncTask, since it may take a while
		    theJokeDb.new OpenDbAsyncTask().execute(context);
		}
		return theJokeDb;
	}
	
	public boolean initialized() { return isReady; }
	
    private class OpenDbAsyncTask extends AsyncTask<Context, Void, Void> {
  
		@Override
		protected Void doInBackground(Context... params) {
			JokeDbHelper theDbHelper = new JokeDbHelper(params[0]);
			theDb = theDbHelper.getWritableDatabase();
			isReady = true;
			return null;
		}
  
    }
    
    public SQLiteDatabase getDB() {
    	if (isReady == false) return null;
    	return theDb;
    }
}
