package com.SchultzHattervig.addressbook;

import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
/**
 * This fragment displays a list of the current contacts being
 * stored in the database by name. The fragment brings up a detail
 * fragment of a contact when the name is pressed.
 * 
 * @author Josh Schultz
 * @author Erik Hattervig
 */
public class ListFragmentView extends ListFragment 
{
	//IMPORTANT NOTE: There are no public members.
    
    private IContactControlListener _listener;

    /**
     * onCreate override for this class. Makes a menu option
     * on the screen.
     * 
     * @author Josh Schultz
     * @author Erik Hattervig
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        
        // Tell the host activity that an options menu is
        // associated with this fragment.
        setHasOptionsMenu(true);
        
        //setRetainInstance(true);
            
    }
    
    /**
     * Creates a menu on the screen with the option Create Contact
     * 
     * @author Josh Schultz
     * @author Erik Hattervig
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflator)
    {
        // Inflate the Menu resource to be associated with
        // this activity.
        getActivity().getMenuInflater().inflate(R.menu.add_contact_menu, menu);

        // Call super to give the inflated menu back to the host activity.
        super.onCreateOptionsMenu(menu, menuInflator);
    }

    /**
     * Attaches this fragment to an activity. In the case of this app
     * it is attached to the main activity.
     * 
     * @author Josh Schultz
     * @author Erik Hattervig
     */
    @Override
    public void onAttach(Activity activity)
    {
        try
        {
            // Assign listener reference from hosting activity.
            _listener = (IContactControlListener) activity;
            Log.d("ListFrag", "List fragment was attached");
        }
        catch (ClassCastException e)
        {
            throw new ClassCastException(activity.toString());
        }
        
        super.onAttach(activity);
    }


    /**
     * The onResume override for this fragment. Refreshes the contact
     * list so that it is up to date.
     * 
     * @author Josh Schultz
     * @author Erik Hattervig
     */
    @Override
    public void onResume()
    {
            super.onResume();

            // Just get the list of courses from the database again.
            refreshContactList();
    }

    /**
     * Handles the clicking of the menu button add contact. When clicked
     * calls the insertContact method of our listener.
     * 
     * @author Josh Schultz
     * @author Erik Hattervig
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.action_add_contact:
            {
                _listener.insertContact();
            }
            default:
            {
                return super.onOptionsItemSelected(item);
            }
        }
    }

    /**
     * Handles the clicking of an item in the contacts list. When clicked
     * calls the seletContact method of our listener with the position of
     * the selected contact.
     * 
     * @author Josh Schultz
     * @author Erik Hattervig
     */
    @Override
    public void onListItemClick(ListView l, View v, int position, long id)
    {
        Contact contact = null;
        
        contact = (Contact) getListAdapter().getItem(position);
        if (contact != null)
        {
            _listener.selectContact(contact);
        }
    }

    /**
     * Refreshes the contact list so that it is up date.
     * 
     * @author Josh Schultz
     * @author Erik Hattervig
     */
    private void refreshContactList()
    {
        // Assign the adapter.
        setListAdapter(_listener.getContactArrayAdapter());        
    }
}
