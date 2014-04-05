package edu.psu.mob_apps.lab5.persistence;

import android.app.Activity;
import android.app.ListFragment;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class JokeListActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_joke_list);
	}
	
	public static class JokeListFragment extends ListFragment {
		
	     @Override
	     public void onActivityCreated(Bundle savedInstanceState) {
	         super.onActivityCreated(savedInstanceState);
             
	         SQLiteDatabase theDb = JokeDb.getInstance(getActivity()).getDB();
	         Cursor c = theDb.rawQuery("Select * from " + JokeDbContract.TblJoke.TABLE_NAME + 
	        		                   " order by " + JokeDbContract.TblJoke._ID, null);
	         
	         // Populate list 
	         ListAdapter a = new SimpleCursorAdapter(getActivity(),
	                 android.R.layout.simple_list_item_1,
	                 c, 
	                 new String[] {JokeDbContract.TblJoke.COLUMN_NAME_SETUP}, 
	                 new int[] {android.R.id.text1},
	                 0);
	         
	         setListAdapter(a);
	         
	     }
	 	 
	     @Override
	     public void onListItemClick(ListView l, View v, int position, long id) {
	         SQLiteDatabase theDb = JokeDb.getInstance(getActivity()).getDB();
	         Cursor c = theDb.rawQuery("Select * from " + JokeDbContract.TblJoke.TABLE_NAME + 
	        		                   " where " + JokeDbContract.TblJoke._ID + " = " + id, null);

	         if (c.moveToFirst() == false) {
	        	 Toast.makeText(getActivity(), getString(R.string.error_retrieving_joke), Toast.LENGTH_SHORT).show();
	         }
	         else {
	 			String theSetup = c.getString(c.getColumnIndexOrThrow(JokeDbContract.TblJoke.COLUMN_NAME_SETUP));
				String thePunchline = c.getString(c.getColumnIndexOrThrow(JokeDbContract.TblJoke.COLUMN_NAME_PUNCHLINE));
				Intent i = new Intent(getActivity(), JokeActivity.class);
				i.putExtra(JokeActivity.BUNDLE_JOKE_SETUP, theSetup);
				i.putExtra(JokeActivity.BUNDLE_JOKE_PUNCHLINE, thePunchline);
				startActivity(i);
	         }
	     }
	 
	}
}
