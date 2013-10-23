package com.SchultzHattervig.addressbook;

import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.app.FragmentManager;
import android.util.Log;
import android.view.Menu;
import android.widget.ArrayAdapter;

public class MainActivity extends Activity implements IContactControlListener
{
	private Model _model;
	private FragmentManager _fragmentManager;
	private ListFragmentView _listFragmentView;
	private ArrayAdapter<Contact> _adapter;
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
        _model = Model.getInstance(this);
		//_model.insertSampleContacts();  //creates 4 contacts
        refreshArrayAdapter();
        _fragmentManager.beginTransaction().add(R.id.fragmentContainerFrame,  _listFragmentView).commit();
        
		
		//Contact contact = new Contact("Josh", "490-4124", "joshua.schultz@mines.sdsmt.edu", "805 Blaine Ave", "Rapid City");
		//_model.insertContact(contact);
		//_model.deleteContact(contact);
		
		Log.d("MainActivity", "Just starting onCreate()");
		List<Contact> contacts = _model.getContacts();
		for(int i = 0; i < contacts.size(); i++)
		{
			Log.d("MainActivity", contacts.toArray()[i].toString());
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
		
	}

	@Override
	public void insertContact() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void insertContact(Contact contact) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteContact(Contact contact) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateContact(Contact contact) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Contact getContact() {
		// TODO Auto-generated method stub
		return null;
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

}
