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

public class ListFragmentView extends ListFragment 
{
	//IMPORTANT NOTE: There are no public members.
    
    private IContactControlListener _listener;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        
        // Tell the host activity that an options menu is
        // associated with this fragment.
        setHasOptionsMenu(true);
        
        //setRetainInstance(true);
            
    }
    
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflator)
    {
        // Inflate the Menu resource to be associated with
        // this activity.
        getActivity().getMenuInflater().inflate(R.menu.add_contact_menu, menu);

        // Call super to give the inflated menu back to the host activity.
        super.onCreateOptionsMenu(menu, menuInflator);
    }

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


    @Override
    public void onResume()
    {
            super.onResume();

            // Just get the list of courses from the database again.
            refreshContactList();
    }

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

    private void refreshContactList()
    {
        // Assign the adapter.
        setListAdapter(_listener.getContactArrayAdapter());        
    }
}
