package com.example.roboscore1;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
//import android.app.DialogFragment;
//import android.content.Context;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
//import android.text.Editable;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
//import android.widget.EditText;
//import android.widget.Toast;

public class ConfigureActivity extends Activity  {
public int i;
public EditText task_name;
public EditText teams;
public EditText password;
public Button b;
public EditText subtasks;
SharedPreferences spref,pref;
SQLiteDatabase theDb;
TaskDb taskDb;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_configure);
		/*EditText et=(EditText) findViewById(R.id.Maxteam);
		i=Integer.parseInt(et.getText().toString());*/
		 spref= this.getSharedPreferences("MaxValues", Context.MODE_PRIVATE);
		 pref= this.getSharedPreferences("Password", Context.MODE_PRIVATE);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_configure, menu);
		return true;
	}

	/*@SuppressLint("NewApi")
	@Override
	public void onDialogPositiveClick(DialogFragment dialog, String password,
			String email) {
		// TODO Auto-generated method stub
		*/
		
		
		//}
	
	
	
	@SuppressLint("CommitPrefEdits")
	public void save(View view)
	{
		// store the max team value in shared preferences
		
		teams=(EditText) findViewById(R.id.Maxteam);
		String in= teams.getText().toString();
		int i=Integer.parseInt(in);
		
		SharedPreferences.Editor editor=spref.edit(); 
		editor.putInt("no_of_teams", i);
		EditText name=(EditText) findViewById(R.id.editText1);
		String team_name=name.getText().toString();
		editor.putString("team_name", team_name);
		subtasks=(EditText) findViewById(R.id.Maxsubtask);
		String in1=subtasks.getText().toString();
		int y= Integer.parseInt(in1);
		editor.putInt("no_of_subtasks", y);
		editor.commit();
		// editor for pref
		EditText pass=(EditText)findViewById(R.id.editText3);
		String password=pass.getText().toString();
		SharedPreferences.Editor editor1=pref.edit(); 
		editor1.putString("judge_password", password);
		editor1.commit();
		
/*
		task_name=(EditText) findViewById(R.id.editText1);
		//create 'n' columns in the database.
		 taskDb = TaskDb.getInstance(this);
		 theDb = taskDb.getDB();
		
		 for(int k=1;k<=y;k++)
		{
			theDb.execSQL("ALTER TABLE "+TaskDbContract.TblTask.TABLE_NAME1+" ADD COLUMN "+"subtask"+y+" "+"INTEGER"+";");
			
		}
		theDb.execSQL("ALTER TABLE "+TaskDbContract.TblTask.TABLE_NAME1+" ADD COLUMN "+"total_score"+" "+"INTEGER"+";");
		*/
	//String name=task_name.getText().toString();
		
		
	}
	@SuppressLint("NewApi")
	public void emailPassword(View view)
	{
		String pas=pref.getString("judge_password", "judge");
		Intent e=new Intent(Intent.ACTION_SEND);
		e.setType("message/rfc822");
		e.putExtra(Intent.EXTRA_EMAIL  , new String[]{"recipient@example.com"});
		e.putExtra(Intent.EXTRA_SUBJECT, "password for the app");
		e.putExtra(Intent.EXTRA_TEXT   , pas);	
		startActivity(e);
		//Context context = getApplicationContext();
		
	}

}
