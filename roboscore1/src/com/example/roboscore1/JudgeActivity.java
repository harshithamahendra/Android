package com.example.roboscore1;

import android.os.Bundle;
import android.annotation.SuppressLint;
//import android.app.ActionBar.LayoutParams;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
//import android.widget.Toast;
//import android.widget.RelativeLayout;
import android.widget.Spinner;

public class JudgeActivity extends Activity implements View.OnClickListener {
	Spinner s;
	 SharedPreferences spref;
	 private Integer[] array_spinner;
	 int n;
	 EditText[] myTextView;
	// EditText e;
	 SQLiteDatabase theDb;
	 TaskDb taskDb;


	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_judge);
		LinearLayout l=(LinearLayout) findViewById(R.id.activity_judge);
		
       // l.setOrientation(LinearLayout.VERTICAL);
       LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		
		s=(Spinner) findViewById(R.id.spinner2);
		spref= this.getSharedPreferences("MaxValues",Context.MODE_PRIVATE);
		//pref=this.getSharedPreferences("MaxValues",Context.MODE_PRIVATE);
		int i;
		i=spref.getInt("no_of_teams", 0);
	 n=spref.getInt("no_of_subtasks", 0);
	 /*
		//create 'n' columns in the database.
		 taskDb = TaskDb.getInstance(this);
		 theDb = taskDb.getDB();
		
		 for(int k=1;k<=n;k++)
		{
			theDb.execSQL("ALTER TABLE "+TaskDbContract.TblTask.TABLE_NAME1+" ADD COLUMN "+"subtask"+k+" "+"INTEGER"+";");
			
		}
		theDb.execSQL("ALTER TABLE "+TaskDbContract.TblTask.TABLE_NAME1+" ADD COLUMN "+"total_score"+" "+"INTEGER"+";");*/
		//load the spinner with Max_Team value
		array_spinner=new Integer[i];
		for(int x=0;x<i;x++)
		{
			array_spinner[x]= x+1;
		}
		ArrayAdapter <Integer> adapter=new ArrayAdapter<Integer>(this,android.R.layout.simple_spinner_item,array_spinner);
		s.setAdapter(adapter);
		int y=1;
		//adding editTexts to the layout
		  myTextView = new EditText[n];
		for (int j = 0; j < n; j++) {
		    // create a new EditText
			myTextView[j]=new EditText(this);
		    // set some properties of rowTextView or something
		    myTextView[j].setHint("Subtask " +y);
		    myTextView[j].setLayoutParams(lp);
		    myTextView[j].setId(y);
		    //lp.addRule(RelativeLayout.BELOW, R.id.scoresView1);
		    
		    // add the textview to the linearlayout
		    l.addView(myTextView[j]);
		    // save a reference to the textview for later
		    y++;
		}
		/* e=new EditText(this);
		e.setHint("Total Scores");
		e.setLayoutParams(lp);
		e.setId(0);
		l.addView(e);*/
		Button send= new Button(this);
		send.setText("SEND");
		send.setId(222);
		send.setLayoutParams(lp);
		send.callOnClick();
		l.addView(send);
		// final int id_=send.getId();
		send.setOnClickListener(this);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_judge, menu);
		return true;
	}
	@SuppressLint("NewApi")
	public boolean submit(View view)
	{
		DialogFragment dialog=new Alertdialog();
		dialog.show(getFragmentManager(), "Alertdialog");
	return true;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		taskDb = TaskDb.getInstance(this);
		 theDb = taskDb.getDB();
		 int team= (Integer)s.getSelectedItem();
		 final String query="SELECT SUM(" +TaskDbContract.TblTask.COLUMN_NAME_SCORE + ") FROM " +TaskDbContract.TblTask.TABLE_NAME1+ " WHERE "+TaskDbContract.TblTask.COLUMN_NAME_TEAM_NO+ "="+team;
		ContentValues values= new ContentValues();
		
		for(int r=0;r<n;r++)
		{
			values.put(TaskDbContract.TblTask.COLUMN_NAME_TEAM_NO,team);
			values.put(TaskDbContract.TblTask.COLUMN_NAME_SUBTASK_NO,r+1);
			values.put(TaskDbContract.TblTask.COLUMN_NAME_SCORE, Integer.valueOf(myTextView[r].getText().toString()));
		}
		theDb.insert(TaskDbContract.TblTask.TABLE_NAME1, null, values);
		
		 int sum = 0;
		Cursor cursor=theDb.rawQuery(query, null);
		if(cursor.moveToFirst()) {
         sum = cursor.getInt(0);
               }
		Toast.makeText(getApplicationContext(), "success "+ sum, Toast.LENGTH_LONG).show();
		
		

		//int sum=cursor.getInt(arg0)
		//theDb.execSQL( , null);
		Intent email = new Intent(Intent.ACTION_SEND);
		String name=spref.getString("team_name","null");
		email.setType("message/rfc825");
		email.putExtra(android.content.Intent.EXTRA_SUBJECT, "scores");
		email.putExtra(Intent.EXTRA_TEXT   ,"Team name= "+name+ "total score="+ sum);
		

		
	}
		
		/*values.put(TaskDbContract.TblTask.COLUMN_NAME_TEAM_NO,team);
		int t=1;
		for(int p=0;p<n;p++)
		{
			values.put("subtask"+t,Integer.valueOf(myTextView[p].getText().toString()));
			t++;
		}
		values.put("total_score",Integer.valueOf(e.getText().toString()));
		
		theDb.insert(TaskDbContract.TblTask.TABLE_NAME, null, values);
		
		*/
}
	
	/*@SuppressLint("NewApi")

	Intent t = new Intent(Intent.ACTION_SEND);
	
	
	*/
	
	


