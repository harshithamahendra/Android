package com.example.roboscore1;


import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;

public class MainActivity extends Activity {
	//public String password="admin";
	TaskDb theDb;
	
//SharedPreferences spref;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//Log.d("a", "***************db**********");
		theDb=TaskDb.getInstance(this);
		
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	@SuppressLint("NewApi")
	public boolean openAdmin(View view){
		DialogFragment newFragment = new LoginFragment();
		newFragment.show(getFragmentManager(), "LoginFragment");
		return true;
		
	}
	@SuppressLint("NewApi")
	public boolean openJudge(View view){
		JudgeLoginFragment newFragment = new JudgeLoginFragment();
		newFragment.show(getFragmentManager(), "JudgeLoginFragment");
		return true;
}
}