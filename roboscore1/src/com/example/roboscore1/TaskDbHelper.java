package com.example.roboscore1;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TaskDbHelper extends SQLiteOpenHelper {
	public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Task.db";
    //private Context context;
	//INITIALISE TASK TABLE
    private static final String SQL_CREATE_TABLE1 =
    	    "CREATE TABLE " + TaskDbContract.TblTask.TABLE_NAME + " (" +
    	    		TaskDbContract.TblTask._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
    	    		TaskDbContract.TblTask.COLUMN_NAME_SUBTASK + " " +
    	    		TaskDbContract.TblTask.COLUMN_TYPE_SUBTASK + "," +
    	    		TaskDbContract.TblTask.COLUMN_NAME_SCORE_LIMIT + " " +
    	    		TaskDbContract.TblTask.COLUMN_TYPE_SCORE_LIMIT + ","+
    	    		TaskDbContract.TblTask.COLUMN_NAME_DESCRIPTION+ " "+
    	    		TaskDbContract.TblTask.COLUMN_TYPE_DESCRIPTION+ ")";
    //INITIALISE SCORE TABLE
    private static final String SQL_CREATE_TABLE2=
    		"CREATE TABLE " + TaskDbContract.TblTask.TABLE_NAME1 + " (" +
    	    		TaskDbContract.TblTask._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
    	    		TaskDbContract.TblTask.COLUMN_NAME_TEAM_NO + " " +
    	    		TaskDbContract.TblTask.COLUMN_TYPE_TEAM_NO+ ","+
    	    		TaskDbContract.TblTask.COLUMN_NAME_SUBTASK_NO+ " "+
    	    		TaskDbContract.TblTask.COLUMN_TYPE_SUBTASK_NO+ ","+
    	    		TaskDbContract.TblTask.COLUMN_NAME_SCORE+" "+
    	    		TaskDbContract.TblTask.COLUMN_TYPE_SCORE+ ")";
    		
    
    private static final String SQL_DELETE_TABLE1 =
    	    "DROP TABLE IF EXISTS " +  TaskDbContract.TblTask.TABLE_NAME;
    
    private static final String SQL_DELETE_TABLE2 =
    	    "DROP TABLE IF EXISTS " +  TaskDbContract.TblTask.TABLE_NAME1;
    
    
    
   
    
    public TaskDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //this.context=context;   
    }
    @Override
	public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE1);	
        db.execSQL(SQL_CREATE_TABLE2);
    }
    @Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_TABLE1);
        db.execSQL(SQL_DELETE_TABLE2);

        onCreate(db);
	}
	
    public void onDowngrade(SQLiteDatabase db, int oldVersion, 
            int newVersion) {
    	onUpgrade(db, oldVersion, newVersion);
    }
    

}
