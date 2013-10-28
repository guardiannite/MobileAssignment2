package com.SchultzHattervig.addressbook;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
/**
 * The DetailFragmentView fragment handles the input dialog from the 
 * user when creating a new contact and editing a contact, as well as 
 * displaying a detailed view of the contact's information.
 * @author Josh Schultz
 * @author Erik Hattervig
 */
public class DetailFragmentView extends Fragment implements OnClickListener
{
    //IMPORTANT NOTE: There are no public members.
    
    private IContactControlListener _listener;
    private Contact _contact = null;
    private boolean _isEditing;
    private boolean _isOrientationChanging = false;
    
    private EditText _editTextName;
    private EditText _editTextPhone;
    private EditText _editTextEmail;
    private EditText _editTextAddress;
    private EditText _editTextCity;
    private Button _saveButton;

    /**
     * The onCreate for this fragment. Handles which option menu to
     * create when the fragment is created
     * 
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
            super.onCreate(savedInstanceState);

            // Keep member variables and state, not the best approach, 
            // but the Course class would need to implement Parceable
            // in order to be passed in Bundle (from both outside the
            // fragment and inside the fragment on rotation).
            setRetainInstance(true);
            
            // Tells the host Activity to display the appropriate
            // option menu.
            setHasOptionsMenu(true);
            
            //State the view isn't being edited
            _isEditing = false;
    }

    /**
     * 
     * @param inflater
     * @param container
     * @param saveInstanceState
     * 
     * @return
     * 
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
            // Inflate the UI.
            View rootView = inflater.inflate(R.layout.detail_fragment, container, false);

            // Assign instances of Views from the Layout Resource.
            _editTextName = (EditText) rootView.findViewById(R.id.editTextName);
            _editTextPhone = (EditText) rootView.findViewById(R.id.editTextPhone);
            _editTextEmail = (EditText) rootView.findViewById(R.id.editTextEmail);
            _editTextAddress = (EditText) rootView.findViewById(R.id.editTextAddress);
            _editTextCity = (EditText) rootView.findViewById(R.id.editTextCity);

            _saveButton = (Button) rootView.findViewById(R.id.saveButton);
            _saveButton.setOnClickListener(this);
            
            _editTextName.setHint(R.string.default_name);
            _editTextPhone.setHint(R.string.default_phone);
            _editTextEmail.setHint(R.string.default_email);
            _editTextAddress.setHint(R.string.default_street);
            _editTextCity.setHint(R.string.default_city);
            
            _editTextName.addTextChangedListener(new TwitterEditTextHandler());
            return rootView;
    }


    /**
     * @param activity
     */
    @Override
    public void onAttach(Activity activity)
    {
            try
            {
                    // Assign listener reference from host activity.
                    _listener = (IContactControlListener) activity;
            }
            catch (ClassCastException e)
            {
                    throw new ClassCastException(activity.toString());
            }
            
            super.onAttach(activity);
    }
    
    
    /**
     * 
     */
    @Override
    public void onResume()
    {
            super.onResume();

            // If we are changing orientation, use the existing _course
            // member to populate the view.
            if (_isOrientationChanging == false)
            {
                    // Get a reference to the course that was selected from 
                    // the list through the listener interface.
                    _contact = _listener.getContact();
                    
            }
            
            displayContact();
    }
    
    /**
     * 
     */
    @Override
    public void onPause()
    {
            // Provides a mechanism by which the Fragment knows if the
            // host Activity is being re-created.  If so, we will want
            // to just use the currently selected _course object to 
            // populate the view which is possible because we are using
            // setRetainInstance(true).
            _isOrientationChanging = getActivity().isChangingConfigurations();
            
            super.onPause();
    }

    /**
     * Creates the menu for editing view if editing view is present.
     * @param menu
     * @param menuInflator
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflator)
    {
            // Only provide the host activity with a menu if there is an 
            // actual course that is being edited.  Otherwise, it is a new
            // course and neither of the Update or Delete menu items should
            // be available.
            if (_contact.getID() > 0)
            {
                    // Inflate the menu; this adds items to the action bar if it is present.
                    getActivity().getMenuInflater().inflate(R.menu.detail_menu, menu);
            }
    }

    /**
     * Handles the clicking of the menu. The menu has two options, Update Contact
     * and Delete Contact.
     * @param item
     * 
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
            switch (item.getItemId())
            {
                    case R.id.action_update_contact:
                    {
                        enableEditing(true);
                            //_listener.updateContact(_contact);
                            return true;
                    }
                    case R.id.action_delete_contact:
                    {
                    	onDeleteClicked();
                            //_listener.deleteContact(_contact);
                            return true;
                    }
                    default:
                    {
                            return super.onOptionsItemSelected(item);
                    }
            }
    }

    /**
     * Enters the contact information into the text fields when a
     * contact is selected. If creating a new contact, sets fields
     * to text's appropriate hint.
     * 
     * @author Josh Schultz
     * @author Erik Hattervig
     */
    private void displayContact()
    {
            if (_contact.getID() > 0)
            {
                    _editTextName.setText(_contact.getName());
                    _editTextPhone.setText(_contact.getPhone());
                    _editTextEmail.setText(_contact.getEmail());
                    _editTextAddress.setText(_contact.getStreet());
                    _editTextCity.setText(_contact.getCity());
                    
                    enableEditing(false);
            }
            else
            {
                
                //If the text isn't cleared, the hints won't be displayed
            	if(!_isEditing)
            	{
	                _editTextName.setText("");
	                _editTextPhone.setText("");
	                _editTextEmail.setText("");
	                _editTextAddress.setText("");
	                _editTextCity.setText("");
            	}
                
                enableEditing(true);
            }
            
    }

    /**
     * Enables or disables the editing view by setting the fragments
     * text boxes to enabled or disabled and shows or hides the save button.
     * 
     * @author Josh Schultz
     * @author Erik Hattervig
     * 
     * @param value
     */
    private void enableEditing(boolean value)
    {
        _editTextName.setEnabled(value);
        _editTextPhone.setEnabled(value);
        _editTextEmail.setEnabled(value);
        _editTextAddress.setEnabled(value);
        _editTextCity.setEnabled(value);
        
        if(value)
        {
        	_saveButton.setVisibility(View.VISIBLE);
        }
        else
        {
        	_saveButton.setVisibility(View.INVISIBLE);
        }
        _isEditing = value;
    }
    
    
    /**
     * Handles the clicking of the save button. This function converts
     * the contents of the text fields into strings and then turns them
     * into a contact object and then stores the contact into the database.
     * 
     * @author Josh Schultz
     * @author Erik Hattervig
     * 
     * @param v
     */
	@Override
	public void onClick(View v) 
	{	
		String name = _editTextName.getText().toString();
		String phone = _editTextPhone.getText().toString();
		String email = _editTextEmail.getText().toString();
		String street = _editTextAddress.getText().toString();
		String city = _editTextCity.getText().toString();
		_contact = new Contact(name, phone, email, street, city, _contact.getID());
    	
		if(_contact.getID() == Contact.INVALID_ID)
		{
			//New Entry
			_listener.insertContact(_contact);
		}
		else
		{
			//Already exists in the database (since ID is valid)
			_listener.updateContact(_contact);
		}
		
	}
	
    /**
     * Handles the clicking of the delete contact button in the menu.
     * This function calls a alert message prompt for the user asking
     * them to confirm the deletion. If the user presses yes then the
     * function deletes the current contact from the database and pops
     * this fragment of of the back stack.
     * 
     * @author Josh Schultz
     * @author Erik Hattervig
     */
    public void onDeleteClicked ()
    {
    	//Create the alertDialog on the current activity
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
        
        //Set the title and message based on the R file
        alertBuilder.setTitle(R.string.alert_title);
        alertBuilder.setMessage(R.string.alert_message);
        
        //Allow the user to cancel the dialog
        alertBuilder.setCancelable(true);
        
        //If the user presses 'yes' call the clearTags onClick
        alertBuilder.setPositiveButton(R.string.alert_yes, deleteContact);
        
        //If the user cancels, don't do anything
        alertBuilder.setNegativeButton(R.string.alert_cancel, null);
        
        //Create and show the dialog
        AlertDialog alert = alertBuilder.create();
        alert.show();
    }
    
	public DialogInterface.OnClickListener deleteContact = new DialogInterface.OnClickListener() 
	{
		
		@Override
		public void onClick(DialogInterface dialog, int which) 
		{
			_listener.deleteContact(_contact);
			
		}
	};
	
	/**
	 * The class is used only to handle changes in text for
	 * the name field.  When it changes, the afterTextChanged()
	 * checks to see if there is no text contained in both
	 * the search and tag editTexts.  If no text is contained in
	 * the name field, the save button from the parent class is
	 * disabled.  Otherwise, the save button is enabled.
	 *  
	 * @author Josh Schultz
	 * @author Erik Hattervig
	 *
	 */
	private class TwitterEditTextHandler implements TextWatcher
	{
        
			/**
			 * Called after s changes, if the TextName field is not an empty string,
			 * enables the Save Button, otherwise disable the Save Button.
			 * 
			 * @author Josh Schultz
			 * @author Erik Hattervig
			 */
			@Override
			public void afterTextChanged(Editable s) 
			{
				if(_editTextName.getText().toString().matches(""))
				{
					_saveButton.setEnabled(false);
				}else
				{
					_saveButton.setEnabled(true);
				}
			}

			/**
			 * Called before s is about to change; this method does nothing. It is
			 * included because the interface requires it.
			 * 
			 * @author Josh Schultz
			 * @author Erik Hattervig
			 */
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}

		     /**
			 * Called before s is about to change; this method does nothing. It is
			 * included because the interface requires it.
			 * 
			 * @author Josh Schultz
			 * @author Erik Hattervig
			 */
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}
		
	}
}
