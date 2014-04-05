package com.example.roboscore1;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class ResetPasswordActivity extends Activity {
SharedPreferences pref;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reset_password);
		pref=this.getSharedPreferences("Password",Context.MODE_PRIVATE);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_reset_password, menu);
		return true;
	}
	public void save(View view)
	{
		EditText e1=(EditText) findViewById(R.id.changepassword);
		String new_pass=e1.getText().toString();
		EditText e2=(EditText) findViewById(R.id.confirmpassword);
		String confirm_pass=e2.getText().toString();
		if(new_pass.equals(confirm_pass))
		{
		SharedPreferences.Editor editor=pref.edit(); 
		editor.putString("admin_password", new_pass);
		editor.commit();
		}
		else
		{
			Toast.makeText(getApplicationContext(), "Passwords dont match ", Toast.LENGTH_LONG).show();

		}
	}

}
