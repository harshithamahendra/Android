package com.example.project1;



import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
	

public class MainActivity extends Activity implements TextWatcher, SampleDialogFragment.SampleDialogListener {
    public TextView tv;

	public String before;//string before change
	public String after;//string after the change
	public String output;//output that should appear in the textview
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ActionBar a=getActionBar();
		a.setIcon(R.drawable.ic_action_appicon);
		a.show();
		
		tv= (TextView) findViewById(R.id.TextView1);
		
		EditText tstEdit = (EditText) findViewById(R.id.editText1);
			
			//Add textwatcher to EditText
		   tstEdit.addTextChangedListener(this);

	}//end of onCreate
			 
	  @Override  public void beforeTextChanged (CharSequence s,int start , int count, int after) {
	        CharSequence s1=s.subSequence(start, start+count);
	        before = s1.toString();
	     }

	    @Override public void onTextChanged (CharSequence s,int start , int before, int count) {
	    	 CharSequence s1=s.subSequence(start, start+count);
		        after = s1.toString();
		    
	     }

	    @Override  public void afterTextChanged (Editable s) {
	        StringBuffer strbuf=new StringBuffer();
	        strbuf.append("'"+ before+"' => '"+after+"'\n");
	      String s1= (String) tv.getText();
	      
	        //TextView tv= (TextView) findViewById(R.layout.TextView1);
	        output= strbuf.toString()+s1;
	        tv.setText(output);
	     }
	

		
	     
	
	public void cleartext(View view) //function of the clear button 
	{
		//EditText editText = (EditText) findViewById(R.id.editText1);
		tv.setText("");//clear contents of the textview
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
			case R.id.editTextView:
				
				SampleDialogFragment newFragment = new SampleDialogFragment();
				newFragment.show(getFragmentManager(), "Change");
				return true;
			
			case R.id.Dispaly:
				
				Context context = getApplicationContext();
				CharSequence text = tv.getText();
				int duration = Toast.LENGTH_SHORT;
				Toast toast = Toast.makeText(context, text, duration);
				toast.show();
				return true;
				
			default:
				break;
		}
		return false;
	}


	
	
	

	@Override
	public void onDialogPositiveClick(DialogFragment dialog, String result) {
		tv.setText(result);
		
	}

}

	