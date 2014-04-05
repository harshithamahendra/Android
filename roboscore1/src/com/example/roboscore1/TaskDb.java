package com.example.roboscore1;



import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;


public class TaskDb {
	
	private static TaskDb theJokeDb;
	private SQLiteDatabase theDb;
	private boolean isReady;

	private TaskDb(){}
public static TaskDb getInstance(Context context) {
		
		if (theJokeDb == null) {		
		    theJokeDb = new TaskDb();
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
			TaskDbHelper theDbHelper = new TaskDbHelper(params[0]);
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
