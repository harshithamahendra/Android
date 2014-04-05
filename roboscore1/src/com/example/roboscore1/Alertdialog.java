package com.example.roboscore1;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DialogFragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

@SuppressLint("NewApi")
public class Alertdialog extends DialogFragment {
	 @SuppressLint("NewApi")
	    @Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
	        // Build the dialog and set up the button click handlers
	    	
	        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	        builder.setTitle(R.string.dialog_title)
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
			Context context = getActivity().getApplicationContext();
			int duration = Toast.LENGTH_SHORT;
			Toast toast = Toast.makeText(context, R.string.toast1, duration);
			toast.show();
			Toast.makeText(context, R.string.toast2, duration);
			toast.show();
			

	 }

}

