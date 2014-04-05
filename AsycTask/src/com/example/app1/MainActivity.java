/*
*Find prime of a number using Async Task to calculate the prime
*/
package com.example.AsyncTask;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

public class MainActivity extends Activity {
	ProgressBar progressBar;
	
	public final static String EXTRA_MESSAGE="com.example.app1.MESSAGE";
	

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = (ProgressBar)findViewById(R.id.progressbar_Horizontal);
        progressBar.setProgress(0);
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
   
    	    
    	BackgroundAsyncTask batFactor = null;
    //called when button is clicked
    public void checkForPrime(View view)
    {
        	EditText editText = (EditText) findViewById(R.id.edit_message);
    	
    	String input = editText.getText().toString();
    	String message1;
    	
    	
    	try
    	{
    		long number= Integer.parseInt(input);
    		if(batFactor!=null)
    			batFactor.cancel(false);
    		batFactor=(BackgroundAsyncTask) new BackgroundAsyncTask().execute(number);
    	}
    	   	   	
    	//if the entered value is not a number
    		catch(NumberFormatException e)
    	{
    
    		Intent intent = new Intent(this, DisplayMessageActivity.class);
    		message1="Invalid input";
    		intent.putExtra(EXTRA_MESSAGE, message1);// send the variable through intent
        	startActivity(intent);
    	} 
    
    }
    
    public class BackgroundAsyncTask extends AsyncTask<Long, Integer, Void> {
  
@Override protected void onPreExecute() {}
  @Override protected void onPostExecute(Void result) {}
  @Override protected Void doInBackground(Long... number) {
	  String message;
	  int x=0;
	  int myProgress=0;
	  if (number[0]<=1)
	  {
		  message ="Enter integer greater than 1";
	  }
	  else
	  {
	  
		  for(int i=2;i<=Math.sqrt(number[0]);i++)
		  {
		
			  if(number[0]%2==0)
				  x=1;
			  myProgress = (int)((i/Math.sqrt(number[0]))*100);
		  }//end for
		  publishProgress(myProgress);
		  
		   if (isCancelled())
		        {
		            return null;
		        }
	  
	  if(x==0)
		 message=number[0] +" "+ "is prime";
	  else
		  message=number[0]+ " "+ "is not prime";
	  }//end else
	  
  //Create intent for the display activity
    Intent intent = new Intent(MainActivity.this,
                                 DisplayMessageActivity.class);
    intent.putExtra(EXTRA_MESSAGE, message);
    startActivity(intent);
      return null;
  }
@Override protected void onProgressUpdate(Integer... values) {
      progressBar.setProgress(values[0]);
  }

    }// end BackgroundAsyncTask class
 }


    
    
    

