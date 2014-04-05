package com.example.roboscore1;

import android.os.Bundle;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

public class TaskActivity extends Activity {
 Spinner s;
 EditText score, description;
 private Integer[] array_spinner;
 SharedPreferences spref;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_task);
		s=(Spinner) findViewById(R.id.spinner1);
		score=(EditText) findViewById(R.id.editText4);
		description=(EditText) findViewById(R.id.editText6);
		
		// Get the no of subtasks enntered and populate the spinner with the value
		spref= this.getSharedPreferences("MaxValues", Context.MODE_PRIVATE);
		int i;
		i=spref.getInt("no_of_subtasks", 0);
		
		array_spinner=new Integer[i];
		for(int x=0;x<i;x++)
		{
			array_spinner[x]= x+1;
		}
		
		ArrayAdapter <Integer> adapter=new ArrayAdapter<Integer>(this,android.R.layout.simple_spinner_item,array_spinner);
		s.setAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_task, menu);
		return true;
	}
	@Override public boolean onOptionsItemSelected(MenuItem item)
	{
		switch(item.getItemId())
		{
		case R.id.menu_home:
			Intent i=new Intent(this,AdminTaskActivity.class);
			startActivity(i);
			return true;
		default: return super.onOptionsItemSelected(item);
			
		}
	}
	public void save(View v){
		TaskDb taskDb = TaskDb.getInstance(this);
		SQLiteDatabase theDb = taskDb.getDB();
		int a=Integer.valueOf(score.getText().toString());
		String des=description.getText().toString();
		int val=(Integer) s.getSelectedItem();
		
		ContentValues values= new ContentValues();
		values.put(TaskDbContract.TblTask.COLUMN_NAME_SUBTASK, val);
		values.put(TaskDbContract.TblTask.COLUMN_NAME_SCORE_LIMIT, a);
		values.put(TaskDbContract.TblTask.COLUMN_NAME_DESCRIPTION, des);
		theDb.insert(TaskDbContract.TblTask.TABLE_NAME, null, values);


		
		
	}

}
