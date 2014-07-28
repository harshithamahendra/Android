package com.robometrix;
import com.robometrix.Audio;
import com.robometrix.JoystickController;
import com.robometrix.R;

import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import android.media.ToneGenerator;
import android.media.AudioManager;

import static android.media.ToneGenerator.TONE_DTMF_0;
import static android.media.ToneGenerator.TONE_DTMF_1;
import static android.media.ToneGenerator.TONE_DTMF_2;
import static android.media.ToneGenerator.TONE_DTMF_3;
import static android.media.ToneGenerator.TONE_DTMF_4;
import static android.media.ToneGenerator.TONE_DTMF_5;
import static android.media.ToneGenerator.TONE_DTMF_6;
import static android.media.ToneGenerator.TONE_DTMF_8;
import static android.media.ToneGenerator.TONE_DTMF_9;
import static android.media.ToneGenerator.TONE_DTMF_S;
import static android.media.ToneGenerator.TONE_DTMF_P;

public class MainActivity extends Activity implements JoystickController.JoystickControllerListener {
	// Track the location and kinematics of the cursor in the Surface view
	private float xValue = 0;
	private float yValue = 0;
	private char direction = '0';
	private Audio soundThread;
	private long mLastToneTime;
	private Handler mToneHandler;
	private Button phoneMic;
	private int DTMF_tone_length;
	private int inter_tone_delay;
	private static final String DTMFToneLength_key = "DTMFToneLength";
	private static final String interToneDelay_key = "interToneDelay";
	private final double joystick_radius = 7.2;
	private final double joystick_center = 0.05;
	private Button turnSpeed;
	private Button calibrate;
	private Button eStop;
	private Button reverseCamera;
	private Button speaker;
	private ImageButton down;
	private ImageButton up;
	private SharedPreferences spref;
	private MusicIntentReceiver musicReceiver;

	private ToneGenerator _toneGenerator;
	Runnable mToneGenerator = new Runnable() {
		@Override 
		public void run() {
			respond(); 
		}
	};
	/*
	 * (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getPreferences();
		_toneGenerator = new ToneGenerator(AudioManager.STREAM_DTMF, DTMF_tone_length );
		setContentView(R.layout.activity_main);
		JoystickController jsc = (JoystickController) findViewById(R.id.joystick);
		jsc.setJoystickControllerListener(this);
		mToneHandler = new Handler();
		mToneGenerator.run();
		phoneMic = (Button) findViewById(R.id.phoneMic);
		phoneMic.setText("Microphone off");
		turnSpeed = (Button) findViewById(R.id.turnSpeed);
		calibrate  = (Button) findViewById(R.id.calibrate);
		speaker = (Button) findViewById(R.id.speaker);
		reverseCamera = (Button) findViewById(R.id.reverseCamera);
		eStop = (Button) findViewById(R.id.eStop);
		up = (ImageButton) findViewById(R.id.up);
		down = (ImageButton) findViewById(R.id.down);
		setButtonListeners();
	}
	
	/*
	 * Set the button listeners for all buttons
	 */
	public void setButtonListeners(){
		// Toggle Microphone on and off on button press
				phoneMic.setOnTouchListener(new View.OnTouchListener() {

					@SuppressWarnings("deprecation")
					@Override
					public boolean onTouch(View v, MotionEvent event) {
						if(event.getAction() == MotionEvent.ACTION_DOWN){
							if (soundThread == null){
								microphoneOn();
							}
							else
								microphoneOff();
							return true;
						}
						return false;
					}
				});

				turnSpeed.setOnTouchListener(new OnTouchListener() {
					@Override
					public boolean onTouch(View v, MotionEvent event) {
						if(event.getAction() == MotionEvent.ACTION_DOWN){
							_toneGenerator.startTone(TONE_DTMF_S,DTMF_tone_length);
							return true;
						}
						return false;
					}
				});

				calibrate.setOnTouchListener(new OnTouchListener() {
					@Override
					public boolean onTouch(View v, MotionEvent event) {
						if(event.getAction() == MotionEvent.ACTION_DOWN){
							_toneGenerator.startTone(TONE_DTMF_P,DTMF_tone_length);
							return true;
						}
						return false;
					}
				});

				speaker.setOnTouchListener(new OnTouchListener() {
					@Override
					public boolean onTouch(View v, MotionEvent event) {
						if(event.getAction() == MotionEvent.ACTION_DOWN){
							_toneGenerator.startTone(TONE_DTMF_5,DTMF_tone_length);
							return true;
						}
						return false;
					}
				});

				reverseCamera.setOnTouchListener(new OnTouchListener() {
					@Override
					public boolean onTouch(View v, MotionEvent event) {
						if(event.getAction() == MotionEvent.ACTION_DOWN){
							_toneGenerator.startTone(TONE_DTMF_1,DTMF_tone_length);
							return true;
						}
						return false;
					}
				});

				eStop.setOnTouchListener(new OnTouchListener() {
					@Override
					public boolean onTouch(View v, MotionEvent event) {
						if(event.getAction() == MotionEvent.ACTION_DOWN){
							_toneGenerator.startTone(TONE_DTMF_0,DTMF_tone_length);
							return true;
						}
						return false;
					}
				});

				up.setOnTouchListener(new OnTouchListener() {
					@Override
					public boolean onTouch(View v, MotionEvent event) {
						if(event.getAction() == MotionEvent.ACTION_DOWN){
							_toneGenerator.startTone(TONE_DTMF_3,DTMF_tone_length);
							return true;
						}
						return false;
					}
				});

				down.setOnTouchListener(new OnTouchListener() {
					@Override
					public boolean onTouch(View v, MotionEvent event) {
						if(event.getAction() == MotionEvent.ACTION_DOWN){
							_toneGenerator.startTone(TONE_DTMF_9,DTMF_tone_length);
							return true;
						}
						return false;
					}
				});
	}
	/*
	 * Get the variables persisted in the preference file
	 */
	public void getPreferences(){
		spref = PreferenceManager.getDefaultSharedPreferences(this);
		DTMF_tone_length = Integer.parseInt(spref.getString(DTMFToneLength_key, "100"));
		inter_tone_delay = Integer.parseInt(spref.getString(interToneDelay_key, "3000"));
	}
	/*
	 * (non-Javadoc)
	 * @see android.app.Activity#onPause()
	 */
	@Override 
	public void onPause(){
		// was initially playing 
		microphoneOff();
		super.onPause();
	}
	/*
	 * (non-Javadoc)
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	/*
	 * (non-Javadoc)
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	// On selecting Help option, naviagte to the help activity
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch(item.getItemId())
		{
		case R.id.help_option:
			Intent intent = new Intent(this,Help.class);
			startActivity(intent);
			return true;
		case R.id.menu_settings:
			Intent intent1 = new Intent(this,SettingsActivity.class);
			startActivityForResult(intent1,0);
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}
	/*
	 * (non-Javadoc)
	 * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		super.onActivityResult(requestCode, resultCode, data);
		getPreferences();
	}
	/*
	 * (non-Javadoc)
	 * @see com.robometrix.JoystickController.JoystickControllerListener#onMoved(float, float)
	 */
	@Override
	public void onMoved(float x, float y) {
		xValue = x * 10;
		yValue = y * 10;
		respond();
	}
	/*
	 * Turn microphone on
	 */
	@SuppressWarnings("deprecation")
	public void microphoneOn(){
		AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		//
		if(am.isWiredHeadsetOn() == false){
			Toast toast = Toast.makeText(MainActivity.this, "In order to send your voice to the robot please connect the speaker wire to the headphone jack on the android device.", Toast.LENGTH_LONG);
			toast.show();
		}
		else {
			musicReceiver = new MusicIntentReceiver();
			IntentFilter filter = new IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY);
			musicReceiver.setActivity(this);
			this.registerReceiver(musicReceiver, filter);
			soundThread = new Audio(); 
			soundThread.start();
			phoneMic.setText("Microphone on");
		}
		//audio manager
	}
	/*
	 * Turn microphone off
	 */
	public void microphoneOff(){
		if(soundThread != null){
			this.unregisterReceiver(musicReceiver);
			soundThread.close();
			soundThread = null;
			phoneMic.setText("Microphone off");
		}
	}

	/*
	 * Respond to joystick operations
	 *
	 */
	public void respond(){
		
		if(xValue < -joystick_radius){
			if (System.currentTimeMillis() - mLastToneTime >= inter_tone_delay || direction != 'l')
			{
				direction  = 'l';
				_toneGenerator.startTone(TONE_DTMF_4,DTMF_tone_length);
				mLastToneTime = System.currentTimeMillis();
				mToneHandler.postDelayed(mToneGenerator, inter_tone_delay + 10);
			} 
		}
		else if(xValue > joystick_radius){
			if (System.currentTimeMillis() - mLastToneTime >= inter_tone_delay || direction != 'r')
			{
				direction = 'r';
				_toneGenerator.startTone(TONE_DTMF_6,DTMF_tone_length);
				mLastToneTime = System.currentTimeMillis();
				mToneHandler.postDelayed(mToneGenerator, inter_tone_delay + 10);
			}
		}
		else if(yValue > joystick_radius){
			if (System.currentTimeMillis() - mLastToneTime >= inter_tone_delay || direction != 'b')
			{
				direction = 'b';
				_toneGenerator.startTone(TONE_DTMF_8,DTMF_tone_length);
				mLastToneTime = System.currentTimeMillis();
				mToneHandler.postDelayed(mToneGenerator, inter_tone_delay + 10);
			}
		}
		else if(yValue < -joystick_radius){
			if (System.currentTimeMillis() - mLastToneTime >= inter_tone_delay || direction != 'f')
			{
				direction = 'f';
				_toneGenerator.startTone(TONE_DTMF_2,DTMF_tone_length);
				mLastToneTime = System.currentTimeMillis();
				mToneHandler.postDelayed(mToneGenerator, inter_tone_delay + 10);
			}
		}
		else if((direction != '0' && (Math.abs(xValue) <= joystick_center && Math.abs(yValue) <= joystick_center))){
			direction = '0';
			_toneGenerator.startTone(TONE_DTMF_0,DTMF_tone_length);
			//mLastToneTime = System.currentTimeMillis();
			//mToneHandler.postDelayed(mToneGenerator, DTMF_tone_length + 10);
		}
		else{
			_toneGenerator.stopTone();
			
		}
	}
	;
	/*
	 * Turns off the microphone if the audio becomes very noisy 
	 */
	public static class MusicIntentReceiver extends android.content.BroadcastReceiver{
		MainActivity main;
		@Override
		public void onReceive(Context ctx, Intent intent) {
			if (intent.getAction().equals(
					android.media.AudioManager.ACTION_AUDIO_BECOMING_NOISY)) {
				main.microphoneOff();
			}
		}
		public void setActivity(MainActivity main){
			this.main = main;
		}
	}
}
