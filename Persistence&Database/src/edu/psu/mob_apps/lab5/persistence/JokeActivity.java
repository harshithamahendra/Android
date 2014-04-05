package edu.psu.mob_apps.lab5.persistence;

import android.app.Activity;
import android.app.Fragment;
import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class JokeActivity extends Activity {
	public static final String BUNDLE_JOKE_SETUP = "JokeSetup";
	public static final String BUNDLE_JOKE_PUNCHLINE = "JokePunchline";
	
	String theSetup;
	String thePunchline;
	int used;
	
	
	/** Set the view for the activity, and find the joke to be displayed */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_joke);

        // Get the currently displayed joke from the savedInstanceState, if available, 
		//    or the extra info passed with the Intent (from the JokeListActivity), or
		//    a completely new joke
 		Bundle b = getIntent().getExtras();
 		if (savedInstanceState != null) {
     		theSetup = savedInstanceState.getString(BUNDLE_JOKE_SETUP);
     		thePunchline = savedInstanceState.getString(BUNDLE_JOKE_PUNCHLINE);
     	} 
     	else if (b != null && b.containsKey(BUNDLE_JOKE_SETUP) != false) {
    		theSetup = b.getString(BUNDLE_JOKE_SETUP);
     		thePunchline = b.getString(BUNDLE_JOKE_PUNCHLINE);     			
     	}
     	else {
   			findNewJoke();
     	}

 		// Initialize the setup and punchline fragments
     	SetupFragment sf = (SetupFragment) getFragmentManager().findFragmentById(R.id.frameJokes);
     	sf.showSetup(theSetup);

     	PunchlineFragment pf = (PunchlineFragment) getFragmentManager().findFragmentById(R.id.landFramePunchline);
     	if (pf != null && pf.isVisible())
       	    pf.showPunchline("");
	}

	/** Save the currently displayed joke and punchline if Framework needs to unload this activity */
	@Override
	public void onSaveInstanceState(Bundle state) {
 		state.putString(BUNDLE_JOKE_SETUP, theSetup);
 		state.putString(BUNDLE_JOKE_PUNCHLINE, thePunchline);
	}
	
	/** Inflate the Action Bar */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_joke, menu);
		return true;
	}

	/** Respond to an Action Item click event */
	@Override 
	public boolean onOptionsItemSelected(MenuItem item) {
     	SetupFragment sf = (SetupFragment) getFragmentManager().findFragmentById(R.id.frameJokes);
     	PunchlineFragment pf = (PunchlineFragment) getFragmentManager().findFragmentById(R.id.landFramePunchline);
     	
     	switch (item.getItemId()) {
		case R.id.menu_joke_new:
			findNewJoke();
	    	sf.showSetup(theSetup);
	     	if (pf != null && pf.isVisible())
	       	    pf.showPunchline("");
            return true;
	     case R.id.menu_joke_punchline:
		     if (pf != null && pf.isVisible()) {
   	    	     pf.showPunchline(thePunchline);
		     }else {
	             Intent intent = new Intent();
	             intent.setClass(this, PunchlineActivity.class);
	             intent.putExtra("JokePunchline", thePunchline);
	             startActivity(intent);		    	 
		     }
             return true;
	     }
	     return super.onOptionsItemSelected(item);
	}
	
	/** Query the database for a new joke */
	private void findNewJoke() {
		JokeDb jokeDb = JokeDb.getInstance(this);
		SQLiteDatabase theDb = jokeDb.getDB();
		
		// Query database for a joke that has not been used, update the fields
		// theJoke and thePunchline appropriately
		String[] columns = {JokeDbContract.TblJoke._ID,
				            JokeDbContract.TblJoke.COLUMN_NAME_PUNCHLINE,
				            JokeDbContract.TblJoke.COLUMN_NAME_SETUP,
				            JokeDbContract.TblJoke.COLUMN_NAME_USED
				            };
		
		Cursor c = theDb.query(JokeDbContract.TblJoke.TABLE_NAME, columns, null,
				            null, null, null, null);
		
		if (c.moveToFirst() == false) {
			Toast.makeText(this, R.string.error_retrieving_joke, Toast.LENGTH_LONG).show();
			Log.e(getString(R.string.app_name),"No jokes retreived from DB in JokeActivity.findNewJoke()!");
		}
		else { 
			int id=0;
			do 
			{
			used=c.getInt(c.getColumnIndexOrThrow(JokeDbContract.TblJoke.COLUMN_NAME_USED));
			if(used==0)
			{
			theSetup = c.getString(c.getColumnIndexOrThrow(JokeDbContract.TblJoke.COLUMN_NAME_SETUP));
			thePunchline = c.getString(c.getColumnIndexOrThrow(JokeDbContract.TblJoke.COLUMN_NAME_PUNCHLINE));
			id=c.getInt(c.getColumnIndexOrThrow(JokeDbContract.TblJoke._ID));
			ContentValues values= new ContentValues();
			values.put(JokeDbContract.TblJoke.COLUMN_NAME_USED, 1);
			theDb.update(JokeDbContract.TblJoke.TABLE_NAME, values, JokeDbContract.TblJoke._ID+"="+id,null);
			break;
			}
			}while(c.moveToNext());
			if (id==0)
			{
				ContentValues v=new ContentValues();
				v.put(JokeDbContract.TblJoke.COLUMN_NAME_USED, 0);
				theDb.update(JokeDbContract.TblJoke.TABLE_NAME,v,null,null);
				c.moveToFirst();
				theSetup = c.getString(c.getColumnIndexOrThrow(JokeDbContract.TblJoke.COLUMN_NAME_SETUP));
				thePunchline = c.getString(c.getColumnIndexOrThrow(JokeDbContract.TblJoke.COLUMN_NAME_PUNCHLINE));
				
			}
		}
	}
	
	/** The Joke Setup Fragment */
	public static class SetupFragment extends Fragment {
        private View theView;

		@Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                 Bundle savedInstanceState) {
	        theView = inflater.inflate(R.layout.fragment_joke, container, false);       	         
	        return theView;
	    }

	    public void showSetup(String theJoke) {	         
	         TextView txtJoke = (TextView) theView.findViewById(R.id.txtSetup);
	         txtJoke.setText(theJoke);	    	
	    }
	}

	/** The Joke Punchline Fragment */
	public static class PunchlineFragment extends Fragment {

	     View theView;
	     
	     @Override
	     public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                              Bundle savedInstanceState) {
	         theView = inflater.inflate(R.layout.fragment_punchline, container, false);
	         return theView;
	     } 
	     
	     public void showPunchline(String thePunchline) {
	         TextView txtPunchline = (TextView) theView.findViewById(R.id.txtPunchline);
	         
 	         txtPunchline.setText(thePunchline);
	     }
	}

	/** The Punchline Activity, which displays the PunchlineFragment when in Portrait
	 *  orientation.
	 */
	public static class PunchlineActivity extends Activity {
	 
		private String thePunchline;
		
	    @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	         
	        if (getResources().getConfiguration().orientation
	                 == Configuration.ORIENTATION_LANDSCAPE) {
	             // If the screen is now in landscape mode, we can show the
	             // dialog in-line with the list so we don't need this activity.
	             finish();
	             return;
	        }
	 	 
	        setContentView(R.layout.activity_punchline);

	        PunchlineFragment pf = (PunchlineFragment) getFragmentManager().findFragmentById(R.id.framePunchline);
	        
	        if (savedInstanceState != null) {
	        	thePunchline = savedInstanceState.getString("thePunchline");
	        }
	        else {
	        	thePunchline = getIntent().getExtras().getString("JokePunchline");
		    }
	        pf.showPunchline(thePunchline);
	    } 
	     
	 	@Override
	 	public void onSaveInstanceState(Bundle state) {
	 		state.putString("thePunchline", thePunchline);
	 	}
	}

}