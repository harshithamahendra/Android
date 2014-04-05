package com.example.roboscore1;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DialogFragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.content.SharedPreferences;



@SuppressLint("NewApi")
public class JudgeLoginFragment extends DialogFragment  {
	View theView = null;
	public String password;
	String pass;
    @SuppressLint("NewApi")
    @Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
    	
    	//get password from shared pref
        Context context=getActivity();
        SharedPreferences pref=context.getSharedPreferences("Password", Context.MODE_PRIVATE);
        password=pref.getString("judge_password", "judge");
        // Build the dialog and set up the button click handlers
    	
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Save view for later use
        theView = inflater.inflate(R.layout.login_fragment, null);
        
     // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(theView)
              .setPositiveButton("OK", new
                 DialogInterface.OnClickListener() {
                      public void onClick(DialogInterface dialog, int id) {
                          // Send the positive button event back to the host
                          // activity
                          handleOK();
                      }             
                  })
              .setNegativeButton("Cancel", new
                 DialogInterface.OnClickListener() {
                     public void onClick(DialogInterface dialog, int id) {
                       // Do nothing, and dialog framework will cancel the dialog
                    	 
                    	 
                     }
                  });
           return builder.create();
        

        
    }
    @SuppressLint("NewApi")
	public void handleOK()
    {
    	EditText et= (EditText) theView.findViewById(R.id.password);
    	//pass= et.getText().toString();
    	
    	if(et.getText().toString().equals(password))
    	{  
    	Intent i=new Intent();
    	i.setClass(getActivity(), JudgeActivity.class);
    	startActivity(i);
    	}
    	else
    	{
    		Toast.makeText(getActivity().getApplicationContext(), "Incorrect Password! ", Toast.LENGTH_LONG).show();
    	}
    	
    }
    
}
