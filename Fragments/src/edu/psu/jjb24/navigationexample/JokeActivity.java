package edu.psu.jjb24.navigationexample;

import java.util.Random;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class JokeActivity extends Activity {
	//public int mCurJoke = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_joke);
	}
		
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_joke, menu);
		return true;
	}

	@Override 
	public boolean onOptionsItemSelected(MenuItem item) {
     	SetupFragment sf = (SetupFragment) getFragmentManager().findFragmentById(R.id.frameJokes);
     	PunchlineFragment pf = (PunchlineFragment) getFragmentManager().findFragmentById(R.id.landFramePunchline);
     	
     	switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		case R.id.menu_new_joke: 
	    	sf.showSetup(-1);
	     	if (pf != null && pf.isVisible())
	       	    pf.showPunchline(-1);
            return true;
	     case R.id.menu_punchline:
		     if (pf != null && pf.isVisible()) {
   	    	     pf.showPunchline(sf.getJokeId());
		     }else {
	             Intent intent = new Intent();
	             intent.setClass(this, PunchlineActivity.class);
	             intent.putExtra("index", sf.getJokeId());
	             startActivity(intent);		    	 
		     }
             return true;
	     }
	     return super.onOptionsItemSelected(item);
	}
	
	public static class SetupFragment extends Fragment {

	    
	    private int mCurJoke;
        private View theView;

		@Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                 Bundle savedInstanceState) {
	        theView = inflater.inflate(R.layout.fragment_joke, container, false);
	        
	         	         
	        return theView;
	    }
		
		@Override public void onSaveInstanceState(Bundle savedInstanceState )
		{
			savedInstanceState.putInt("newCurJoke",mCurJoke);
			super.onSaveInstanceState(savedInstanceState);
		}
		@Override public void onActivityCreated(Bundle savedInstanceState){
			SetupFragment sf = (SetupFragment) getFragmentManager().findFragmentById(R.id.frameJokes);
			super.onActivityCreated(savedInstanceState);
			if(savedInstanceState!=null)
	        {
	        	mCurJoke=savedInstanceState.getInt("newCurJoke");
	        	sf.showSetup(mCurJoke);
	        }
			else
			{
				sf.showSetup(-1);
			}
			
		}
		
		

        public int getJokeId() {
        	return mCurJoke;
        }

	    public void showSetup(int jokeId) {
	         // Display a random joke
	         String[] jokes = getResources().getStringArray(R.array.JokeSetup);
	         if (jokeId == -1) {
		         Random prng = new Random();
		         mCurJoke = prng.nextInt(jokes.length);
	         } else {
	        	 mCurJoke = jokeId;
	         }
	         
	         TextView txtJoke = (TextView) theView.findViewById(R.id.txtSetup);
	         txtJoke.setText(jokes[mCurJoke]);	    	
	    }
	}

	public static class PunchlineFragment extends Fragment {

	     View theView;
	     
	     @Override
	     public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                              Bundle savedInstanceState) {

	         theView = inflater.inflate(R.layout.fragment_punchline, container, false);
	         return theView;
	     } 
	     
	     public void showPunchline(int ndx) {
	         TextView txtPunchline = (TextView) theView.findViewById(R.id.txtPunchline);
	         
	         if (ndx == -1) {
 	             txtPunchline.setText("");
	         } else {
		         String[] punchlines = getResources().getStringArray(R.array.JokePunchline);
		         txtPunchline.setText(punchlines[ndx]);
	         }
	     }
	}

	public static class PunchlineActivity extends Activity {
	 
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
	     	 if (pf != null)
	       	     pf.showPunchline(getIntent().getExtras().getInt("index"));
	    } 
	     
	 	@Override 
		public boolean onOptionsItemSelected(MenuItem item) {
	     	
	     	switch (item.getItemId()) {
			case android.R.id.home:
				NavUtils.navigateUpFromSameTask(this);
				return true;
	     	}
		    return super.onOptionsItemSelected(item);
	 	}
	}

}