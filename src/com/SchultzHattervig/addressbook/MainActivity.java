package com.SchultzHattervig.addressbook;

import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.widget.ArrayAdapter;
/**
 * 
 * 
 * @author Josh Schultz
 * @author Erik Hattervig
 *
 */
public class MainActivity extends Activity implements IContactControlListener
{
	private Model _model;
	private FragmentManager _fragmentManager;
	private ListFragmentView _listFragmentView;
	private DetailFragmentView _detailFragment;
	private ArrayAdapter<Contact> _adapter;
	private Contact _contact;
	private final String TAG = "MainActivity";
	private final String FRAGMENT_LIST_TAG = "ListTag";
	private final String FRAGMENT_DETAIL_TAG = "DetailTag";
	private final String KEY_NAME = "Name";
	private final String KEY_PHONE = "Phone";
	private final String KEY_EMAIL = "Email";
	private final String KEY_STREET = "Street";
	private final String KEY_CITY = "City";
	private final String KEY_ID = "id";
	
	/**
	 * The onCreate override for MainActivity. Creates the fragmentManager,
	 * and loads in the fragmentMaager's instances if they exist.
	 * 
	 * @author Josh Schultz
	 * @author Erik Hattervig
	 * 
	 * @param savedInstanceState The saved information from the activity life cycle changing 
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		_fragmentManager = getFragmentManager();

		        // If the fragment is not found, create it.

		//_listFragmentView = (ListFragmentView) _fragmentManager.findFragmentByTag(FRAGMENT_LIST_TAG);
		if(savedInstanceState != null)
		{
		Log.d(TAG, "Instance isn't NULL");
		String name = savedInstanceState.getString(KEY_NAME);
		String phone = savedInstanceState.getString(KEY_PHONE);
		String email = savedInstanceState.getString(KEY_EMAIL);
		String street = savedInstanceState.getString(KEY_STREET);
		String city = savedInstanceState.getString(KEY_CITY);
		Long id = savedInstanceState.getLong(KEY_ID);
		_contact = new Contact(name, phone, email, street, city, id);
		}

		_listFragmentView = (ListFragmentView) _fragmentManager.findFragmentByTag(FRAGMENT_LIST_TAG);
		        if (_listFragmentView == null)
		        {
		                _listFragmentView = new ListFragmentView();
		        }
		        
		        _detailFragment = (DetailFragmentView) _fragmentManager.findFragmentByTag(FRAGMENT_DETAIL_TAG);
		        if (_detailFragment == null)
		        {
		                _detailFragment = new DetailFragmentView();
		                _fragmentManager.beginTransaction().replace(R.id.fragmentContainerFrame, _listFragmentView, FRAGMENT_LIST_TAG).commit();
		        }
		        _model = Model.getInstance(this);
		        
		        if(savedInstanceState == null)
		        _fragmentManager.beginTransaction().replace(R.id.fragmentContainerFrame, _listFragmentView, FRAGMENT_LIST_TAG).commit();


		        refreshArrayAdapter();
	}
	
	/**
	 * 
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) 
	{
		super.onSaveInstanceState(outState);
		outState.putString(KEY_NAME, _contact.getName());
		outState.putString(KEY_PHONE, _contact.getPhone());
		outState.putString( KEY_EMAIL, _contact.getEmail());
		outState.putString(KEY_STREET, _contact.getStreet());
		outState.putString(KEY_CITY, _contact.getCity());
		outState.putLong(KEY_ID, _contact.getID());
		if(_detailFragment.isVisible())
		_fragmentManager.putFragment(outState, FRAGMENT_DETAIL_TAG, _detailFragment);
		_fragmentManager.putFragment(outState, FRAGMENT_LIST_TAG, _listFragmentView);
	}
	
	/**
	 * The onCreateOptionsMenu override; prevents the the MainActivity
	 * from create its own options menu.
	 * 
	 * @author Josh Schultz
	 * @author Erik Hattervig
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		return false;
	}

	/**
	 * Brings up the DetailFragment of the contact that is passed to it.
	 * 
	 * @author Josh Schultz
	 * @author Erik Hattervig
	 */
	@Override
	public void selectContact(Contact contact) {
		// TODO Auto-generated method stub
		_contact = contact;
		showDetailFragment();
		Log.d(TAG, contact.getName() + " selected");
	}

	/**
	 * Inserts the hints for each of the add contacts fields into a contact.
	 * and then creates the fragment for inserting a new contact.
	 * 
	 * @author Josh Schultz
	 * @author Erik Hattervig
	 */
	@Override
	public void insertContact() 
	{
		// TODO Auto-generated method stub
		String name = getResources().getString(R.string.default_name);
		String phone = getResources().getString(R.string.default_phone);
		String email = getResources().getString(R.string.default_email);
		String street = getResources().getString(R.string.default_street);
		String city = getResources().getString(R.string.default_city);
		
		_contact = new Contact(name,
							   phone,
							   email,
							   street,
							   city);
		//showDetailFragment();
		selectContact(_contact);
		Log.d(TAG, "insertContact()");
	}

	/**
	 * Inserts the contact passed to it into the database
	 * 
	 * @author Josh Schultz
	 * @author Erik Hattervig
	 */
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

	/**
	 * Deletes the contact passed to it from the database.
	 * 
	 * @author Josh Schultz
	 * @author Erik Hattervig
	 */
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

	/**
	 * Updates the contact passed to it in the database by removing
	 * the old information and adding the new information in its
	 * place.
	 * 
	 * @author Josh Schultz
	 * @author Erik Hattervig
	 */
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

	/**
	 * Returns the contact that is currently in focus by the MainActivity.
	 * 
	 * @author Josh Schultz
	 * @author Erik Hattervig
	 */
	@Override
	public Contact getContact() 
	{
		return _contact;
	}

	/**
	 * Returns the current ArrayAdapter.
	 * 
	 * @author Josh Schultz
	 * @author Erik Hattervig
	 */
	@Override
	public ArrayAdapter<Contact> getContactArrayAdapter() {
		// TODO Auto-generated method stub
		refreshArrayAdapter();
		return _adapter;
	}
	
	/**
	 * Refreshes the ArrayAdapter so that the list of contacts is up to date.
	 * 
	 * @author Josh Schultz
	 * @author Erik Hattervig
	 */
    private void refreshArrayAdapter()
    {
        // Get an Array List of Contact objects.
        List<Contact> _contacts = Model.getInstance(this).getContacts();
        
        // Assign list to ArrayAdapter to be used with assigning
        // to the ListFragment list adapter.
        _adapter = new ArrayAdapter<Contact>(this, android.R.layout.simple_list_item_1, _contacts);
    }

    /**
     * Creates a DetailFragment and adds it to the back stack.
     * 
     * @author Josh Schultz
	 * @author Erik Hattervig
     */
    private void showDetailFragment()
    {
            
            // Perform the fragment transaction to display the details fragment.
            _fragmentManager.beginTransaction()
                                    .replace(R.id.fragmentContainerFrame, _detailFragment, FRAGMENT_DETAIL_TAG)
                                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                                    .addToBackStack(null)
                                    .commit();
    }
    
}
