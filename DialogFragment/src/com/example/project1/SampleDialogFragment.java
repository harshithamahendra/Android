package com.example.project1;



import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;


public class SampleDialogFragment extends DialogFragment {
    View theView = null;

    @Override 
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Build the dialog and set up the button click handlers
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Save view for later use
        theView = inflater.inflate(R.layout.dialog_fragment, null);

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

         public interface SampleDialogListener {
             public void onDialogPositiveClick(DialogFragment dialog, 
                                               String result);
         }
     
         // Use this instance of the interface to deliver action events
         SampleDialogListener mListener;

         // Override the Fragment.onAttach() method to instantiate the
         // NoticeDialogListener
         @Override
         public void onAttach(Activity activity) {
             super.onAttach(activity);
             // Verify that the host activity implements the callback interface
             try {
                 // Instantiate the NoticeDialogListener so we can send events to
                 // the host
                 mListener = (SampleDialogListener) activity;
             } catch (ClassCastException e) {
                 // The activity doesn't implement the interface, throw exception
                 throw new ClassCastException(activity.toString()
                         + " must implement NoticeDialogListener");
             }
         }

       public void handleOK() {
         // Note that with DialogFragment, we need to call findViewById
            // differently    
            EditText et = (EditText) theView.findViewById(R.id.editText2);
            mListener.onDialogPositiveClick(this, et.getText().toString());
        }
}

