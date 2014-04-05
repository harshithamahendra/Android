package com.example.roboscore1;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class AdminTaskActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_admin_task);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_admin_task, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch(item.getItemId())
		{
		case R.id.change_password: 
									Intent i=new Intent(this,ResetPasswordActivity.class);
									startActivity(i);
									return true;
		case R.id.logout:		Intent e=new Intent(this,MainActivity.class);
									startActivity(e);
									return true;
			
		default: return super.onOptionsItemSelected(item);
		}
	}
	public void DefineTask(View view)
	{
		Intent i=new Intent(this,TaskActivity.class);
		startActivity(i);
	}
	public void Configure(View view)
	{
		Intent i=new Intent(this,ConfigureActivity.class);
		startActivity(i);
	}
	public void Scores(View view)
	{
		Intent i=new Intent(this,ScoresActivity.class);
		startActivity(i);
	}

}
