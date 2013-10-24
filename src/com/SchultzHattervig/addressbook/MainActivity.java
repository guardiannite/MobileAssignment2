package com.SchultzHattervig.addressbook;

import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.widget.ArrayAdapter;

public class MainActivity extends Activity implements IContactControlListener
{
	private Model _model;
	private FragmentManager _fragmentManager;
	private ListFragmentView _listFragmentView;
	private DetailFragmentView _detailFragment;
	private ArrayAdapter<Contact> _adapter;
	private Contact _contact;
	private final String TAG = "MainActivity";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		_fragmentManager = getFragmentManager();
        
        // If the fragment is not found, create it.
        //_listFragmentView = (ListFragmentView) _fragmentManager.findFragmentByTag(FRAGMENT_LIST_TAG);
        if (_listFragmentView == null)
        {
                _listFragmentView = new ListFragmentView();
        }
        
        //_detailFragmentView = (DetailFragmentView) _fragmentManager.findFragmentByTag(FRAGMENT_DETAIL_TAG);
        if (_detailFragment == null)
        {
                _detailFragment = new DetailFragmentView();
        }
        _model = Model.getInstance(this);
		//_model.insertSampleContacts();  //creates 4 contacts
        refreshArrayAdapter();
        _fragmentManager.beginTransaction().add(R.id.fragmentContainerFrame,  _listFragmentView).commit();
        
		
		//Contact contact = new Contact("Josh", "490-4124", "joshua.schultz@mines.sdsmt.edu", "805 Blaine Ave", "Rapid City");
		//_model.insertContact(contact);
		//_model.deleteContact(contact);
		Log.d(TAG, "Just starting onCreate()");
		List<Contact> contacts = _model.getContacts();
		for(int i = 0; i < contacts.size(); i++)
		{
			Log.d(TAG, contacts.toArray()[i].toString());
		}		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void selectContact(Contact contact) {
		// TODO Auto-generated method stub
		_contact = contact;
		showDetailFragment();
		Log.d(TAG, contact.getName() + " selected");
	}

	@Override
	public void insertContact() {
		// TODO Auto-generated method stub
		_contact = new Contact("", "", "", "", "");
		showDetailFragment();
		Log.d(TAG, "insertContact()");
	}

	@Override
	public void insertContact(Contact contact) {
		// TODO Auto-generated method stub
		_adapter.add(contact);
		_adapter.sort(contact);
		_adapter.notifyDataSetChanged();
		
		_model.insertContact(contact);
		_fragmentManager.popBackStackImmediate();
		Log.d(TAG, contact.getName() + " inserted");
	}

	@Override
	public void deleteContact(Contact contact) 
	{
		_adapter.remove(contact);
		_adapter.sort(contact);
		_adapter.notifyDataSetChanged();
		
		_model.deleteContact(contact);
		_fragmentManager.popBackStackImmediate();
		Log.d(TAG, contact.getName() + " deleted");
	}

	@Override
	public void updateContact(Contact contact) {
		_adapter.remove(contact);
		_adapter.add(contact);
		_adapter.sort(contact);
		_adapter.notifyDataSetChanged();
		
		_model.updateContact(contact);
		_fragmentManager.popBackStackImmediate();
		Log.d(TAG, contact.getName() + " updated");
	}

	@Override
	public Contact getContact() 
	{
		return _contact;
	}

	@Override
	public ArrayAdapter<Contact> getContactArrayAdapter() {
		// TODO Auto-generated method stub
		return _adapter;
	}
	
    private void refreshArrayAdapter()
    {
        // Get an Array List of Contact objects.
        List<Contact> _contacts = Model.getInstance(this).getContacts();
        
        // Assign list to ArrayAdapter to be used with assigning
        // to the ListFragment list adapter.
        _adapter = new ArrayAdapter<Contact>(this, android.R.layout.simple_list_item_1, _contacts);
    }

    private void showDetailFragment()
    {
            
            // Perform the fragment transaction to display the details fragment.
            _fragmentManager.beginTransaction()
                                    .replace(R.id.fragmentContainerFrame, _detailFragment)
                                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                                    .addToBackStack(null)
                                    .commit();
    }
}
