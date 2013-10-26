package com.SchultzHattervig.addressbook;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class DetailFragmentView extends Fragment implements OnClickListener
{
    //IMPORTANT NOTE: There are no public members.
    
    private IContactControlListener _listener;
    private Contact _contact = null;
    private boolean _isOrientationChanging = false;
    
    private EditText _editTextName;
    private EditText _editTextPhone;
    private EditText _editTextEmail;
    private EditText _editTextAddress;
    private EditText _editTextCity;
    private Button _saveButton;

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
    }

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
            return rootView;
    }


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
                            _listener.deleteContact(_contact);
                            return true;
                    }
                    default:
                    {
                            return super.onOptionsItemSelected(item);
                    }
            }
    }

    
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
                    // Not fully implemented.
                _editTextName.setHint(_contact.getName());
                _editTextPhone.setHint(_contact.getPhone());
                _editTextEmail.setHint(_contact.getEmail());
                _editTextAddress.setHint(_contact.getStreet());
                _editTextCity.setHint(_contact.getCity());
                
                //If the text isn't cleared, the hints won't be displayed
                _editTextName.setText("");
                _editTextPhone.setText("");
                _editTextEmail.setText("");
                _editTextAddress.setText("");
                _editTextCity.setText("");
                
                enableEditing(true);
            }
            
    }

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
    }
    
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
}
