package edu.psu.mob_apps.lab5.persistence;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity {

	//*******************************************************
	//                  CONSTANTS
	//*******************************************************
	
	// key used to store, in the bundle, the file which is used by the camera intent 
    private static final String BUNDLE_TARGET_FILE = "TargetFile";

	// key used to store, in the bundle, the file which is displayed in the ImageView
    private static final String BUNDLE_IMAGE_FILE = "ImageFile";

    // Constants used to distinguish which intents are returning results
	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
	private static final int SETTINGS_ACTIVITY_REQUEST_CODE = 200;

	//*******************************************************
	//                  MEMBER VARIABLES
	//*******************************************************

    // An instance of the Joke database, which is initialized in this main activity, so that 
    // it will be ready when needed
    JokeDb theDb; 

    // The location of the file where the Camera intent will store the picture
    private String targetFile;

    // The location of the file where the picture for ImageView is stored
    private String theFile;    
    
    /** When Activity is created, set the View, restore state, and create
     *  an instance of the JokeDB
     */
    SharedPreferences ipref;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
        // Load targetFile from Bundle and display saved image in ImageView
		ipref=PreferenceManager.getDefaultSharedPreferences(this);
		
	    	targetFile = ipref.getString(BUNDLE_TARGET_FILE,targetFile);
	        theFile = ipref.getString(BUNDLE_IMAGE_FILE,theFile);
	    	if (theFile != null) {
	    		loadImage(theFile);
	    	
	    	
	    	
	    	
		}
    	loadQuoteText();
        		
    	// Get an instance of the Joke Database to initialize it, since this
    	// may take time.  Note that the Db is not used in this Activity.
    	theDb = JokeDb.getInstance(this);    	
	}
	
	/** Load the image stored in the file at location specified by theFile into
	 *  the ImageView in this Activity 
	 */
	private void loadImage(String theFile) {
		
		DisplayMetrics displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		int width = displaymetrics.widthPixels;
		int height = displaymetrics.heightPixels;
		
		BitmapWorkerTask bwt = new BitmapWorkerTask((ImageView) findViewById(R.id.imgPicture));
		bwt.execute(new BitmapWorkerTaskArgs(theFile, width, height));
	}
	
	
	/** Load the values for the quote author and saying... */
	private void loadQuoteText() {

    	SharedPreferences spref = PreferenceManager.getDefaultSharedPreferences(this);
      	TextView author = (TextView) findViewById(R.id.txtAuthor);
      	TextView text=(TextView) findViewById(R.id.txtQuote);
      	
      	int n = Integer.parseInt(spref.getString(getString(R.string.pref_key_quote_action), getString(R.string.pref_default_quote_action)));
      	String[] resources = getResources().getStringArray(R.array.prefQuoteActionEntries);
      	author.setText(spref.getString(getString(R.string.pref_key_quote_author), getString(R.string.pref_default_quote_author)) + " " + resources[n]);      	
	// for the shared preferences
      	text.setText(spref.getString(getString(R.string.pref_key_quote_message), getString(R.string.pref_default_quote_text)));
	}
	
	/** Persist state if Framework destroys this Activity due to low memory conditions */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);	
		
       
    }
	@Override protected void onPause()
	{
		super.onPause();
		if (theFile!=null)
		{
			SharedPreferences.Editor editor= ipref.edit();
        	editor.putString(BUNDLE_TARGET_FILE,targetFile);
        	// Save file location
    		theFile = targetFile;
        	editor.putString(BUNDLE_IMAGE_FILE,theFile);
        	editor.commit();
        	}
	}
	
	/** Inflate Action Bar */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
    
	/** Handle Action Item clicks */
	@Override 
	public boolean onOptionsItemSelected(MenuItem item) {
	     switch (item.getItemId()) {
	     case R.id.menu_main_camera: 
    	    // create a file to save the image
    	    Uri fileUri = getOutputMediaFileUri();  
     	    if (fileUri != null) {
     	   	    targetFile = fileUri.getPath();
     	   	    
     	   	    
        	    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
     	   	    intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);   
	    	    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
	    	    startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
    	    }
	        return true;

	     case R.id.menu_main_joke: 
	    	 // Before starting the next activity check to make sure that the jokeDb is ready
	    	 if (theDb.initialized()) {
		 		 Intent i = new Intent(this, JokeActivity.class);
		 		 startActivity(i);
	    	 }
	    	 else {
	    		 Toast.makeText(this, getString(R.string.error_db_still_initializing), Toast.LENGTH_LONG).show();
	    	 }
             
	         return true;
	         
	     case R.id.menu_main_list: 
	    	 // Before starting the next activity check to make sure that the jokeDb is ready
	    	 if (theDb.initialized()) {
		 		 Intent i = new Intent(this, JokeListActivity.class);
		 		 startActivity(i);
	    	 }
	    	 else {
	    		 Toast.makeText(this, getString(R.string.error_db_still_initializing), Toast.LENGTH_LONG).show(); 
	    	 }            
	         return true;
	         
	     case R.id.menu_main_settings: 
	    	 //create new Intent
	    	 Intent i = new Intent(this, SettingsActivity.class);
		     startActivityForResult(i, SETTINGS_ACTIVITY_REQUEST_CODE);
		     return true;

	     default:
	         return super.onOptionsItemSelected(item);
	     }
	}
	
	
	/** Handle results returned from the Settings AND Camera Activities */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if (requestCode == SETTINGS_ACTIVITY_REQUEST_CODE) {
	    	loadQuoteText();
	    }
	    else if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
	        if (resultCode == RESULT_OK) {	 
	        	

	        	if (targetFile != null) {
	        		// Load image into ImageView
	        		loadImage(targetFile);
	        		SharedPreferences.Editor editor= ipref.edit();
		        	editor.putString(BUNDLE_TARGET_FILE,targetFile);
		        	

	        		// Save file location
	        		theFile = targetFile;
		        	editor.putString(BUNDLE_IMAGE_FILE,theFile);
		        	editor.commit();

	        	}
	        	
	        } else if (resultCode == RESULT_CANCELED) {
	            // User cancelled the image capture
	        	Toast.makeText(this, getString(R.string.main_camera_cancelled), Toast.LENGTH_LONG).show();
	        } else {
	            // Image capture failed, advise user
	        	Toast.makeText(this, getString(R.string.error_saving_picture), Toast.LENGTH_LONG).show();
	        }
	    }
	}

	/** Create a file Uri for saving an image or video */
	private Uri getOutputMediaFileUri(){
		File f = getOutputMediaFile();
		if (f == null) {
			return null;
		} else {
	      return Uri.fromFile(f);
		}
	}

	/** Create a File for saving an image or video */
	private File getOutputMediaFile() {
	    // To be safe, you should check that the SDCard is mounted
	    // using Environment.getExternalStorageState() before doing this.
	    String state = Environment.getExternalStorageState();

	    if (Environment.MEDIA_MOUNTED.equals(state)) {
	        // We can read and write the media
	    } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
	        // We can only read the media
 		    Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.error_flash_ram_read_only), Toast.LENGTH_SHORT); 
	    	toast.show();
	        return null;
	    } else {
	        // Something else is wrong. It may be one of many other states, but all we need
	        //  to know is we can neither read nor write
 		    Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.error_flash_ram_not_there), Toast.LENGTH_SHORT); 
	    	toast.show();
	        return null;
	    }

	    // This location works best if you want the created images to be shared
	    // between applications and persist after your app has been uninstalled.
	    File mediaStorageDir = new File(
	              Environment.getExternalStoragePublicDirectory(
	                               Environment.DIRECTORY_PICTURES), getString(R.string.app_name));

	    // Create the storage directory if it does not exist
	    if (! mediaStorageDir.exists()){
	        if (! mediaStorageDir.mkdirs()){
	            Log.d(getString(R.string.app_name), "failed to create directory");
	            return null;
	        }
	    }

	    // Create a media file name
	    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
	    File mediaFile = new File(mediaStorageDir.getPath() + File.separator +
	        "IMG_"+ timeStamp + ".jpg");

	    return mediaFile;
	}

	 

}
