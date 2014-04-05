package edu.psu.jjb24.navigationexample;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;


@SuppressLint("SimpleDateFormat")
public class MainActivity extends Activity {

    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
	}
	//Altered code
	@Override public void onSaveInstanceState(Bundle savedInstanceState )
	{
		savedInstanceState.putString("targetFile1", targetFile);
	    super.onSaveInstanceState(savedInstanceState);

	}
	
	@Override 
	public void onRestoreInstanceState(Bundle savedInstanceState) {
	    // Always call the superclass so it can restore the view hierarchy
	    super.onRestoreInstanceState(savedInstanceState);
	    targetFile=savedInstanceState.getString("targetFile1");
	    theFile=savedInstanceState.getString("targetFile1");
	    
    	ImageView imgPicture = (ImageView) findViewById(R.id.imgPicture);
	    BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 4;
		imgPicture.setImageBitmap(BitmapFactory.decodeFile(theFile, options));
	}
	//end of the altered code
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;

	private String theFile;
    private String targetFile;
   // private String targetFile1;
    
	@Override 
	public boolean onOptionsItemSelected(MenuItem item) {
	     switch (item.getItemId()) {
	     case R.id.menu_camera: 
    	    //create new Intent
    	    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

    	    // create a file to save the video
    	    Uri fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);  
     	    if (fileUri != null) {
     	   	    targetFile = fileUri.getPath();
     	   	    
	    	    // set the image file name
	    	    intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);   
	
	    	    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
	    	    
	    	    // start the image capture Intent
	    	    startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
    	    }
	         
	        return true;

	     case R.id.menu_joke: 
	 		 Intent i = new Intent(this, JokeActivity.class);
	 		 startActivity(i);
             
	         return true;


	     default:
	         return super.onOptionsItemSelected(item);
	     }
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
	        if (resultCode == RESULT_OK) {
	            // Image captured and saved to fileUri specified in the Intent
	        	ImageView imgPicture = (ImageView) findViewById(R.id.imgPicture);
	        	if (targetFile != null) {
	        		theFile = targetFile;
	        		BitmapFactory.Options options = new BitmapFactory.Options();
	        		options.inSampleSize = 4;
	        		imgPicture.setImageBitmap(BitmapFactory.decodeFile(theFile, options));
	        		}
	        	
	        } else if (resultCode == RESULT_CANCELED) {
	            // User cancelled the image capture
	        } else {
	            // Image capture failed, advise user
	        	Toast.makeText(this, "Your picture could not be saved.", Toast.LENGTH_LONG).show();
	        }
	    }
	}

	
	public static final int MEDIA_TYPE_IMAGE = 1;

	/** Create a file Uri for saving an image or video */
	private Uri getOutputMediaFileUri(int type){
		File f = getOutputMediaFile(type);
		if (f == null) {
			return null;
		} else {
	      return Uri.fromFile(f);
		}
	}

	/** Create a File for saving an image or video */
	private File getOutputMediaFile(int type) {
	    // To be safe, you should check that the SDCard is mounted
	    // using Environment.getExternalStorageState() before doing this.
	    String state = Environment.getExternalStorageState();

	    if (Environment.MEDIA_MOUNTED.equals(state)) {
	        // We can read and write the media
	    } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
	        // We can only read the media
 		    Toast toast = Toast.makeText(getApplicationContext(), "You picture would be saved because you are not able to write to the external flash memory.", Toast.LENGTH_SHORT); 
	    	toast.show();
	        return null;
	    } else {
	        // Something else is wrong. It may be one of many other states, but all we need
	        //  to know is we can neither read nor write
 		    Toast toast = Toast.makeText(getApplicationContext(), "You picture would be saved because you are not able to read or write to the external flash memory.", Toast.LENGTH_SHORT); 
	    	toast.show();
	        return null;
	    }



	    File mediaStorageDir = new File(
	              Environment.getExternalStoragePublicDirectory(
	                               Environment.DIRECTORY_PICTURES), "CSJokeApp");
	    // This location works best if you want the created images to be shared
	    // between applications and persist after your app has been uninstalled.

	    // Create the storage directory if it does not exist
	    if (! mediaStorageDir.exists()){
	        if (! mediaStorageDir.mkdirs()){
	            Log.d("CSJokeApp", "failed to create directory");
	            return null;
	        }
	    }

	    // Create a media file name
	    String timeStamp = new 
	                        SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
	    File mediaFile;
	    if (type == MEDIA_TYPE_IMAGE){
	        mediaFile = new File(mediaStorageDir.getPath() + File.separator +
	        "IMG_"+ timeStamp + ".jpg");
	    } else {
	        return null;
	    }

	    return mediaFile;
	}

	 

}
