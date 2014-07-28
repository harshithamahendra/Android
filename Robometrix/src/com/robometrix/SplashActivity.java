package com.robometrix;

import com.robometrix.R;

import android.os.Bundle;
import android.os.Handler;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;

public class SplashActivity extends Activity {

	protected int SPLASHTIME = 3000;
	@SuppressLint("NewApi")
	/*
	 * (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				startActivity(new Intent(SplashActivity.this, MainActivity.class));
				SplashActivity.this.finish();
			}
			
			
		}, SPLASHTIME);
		
	}


}
