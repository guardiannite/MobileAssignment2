package com.SchultzHattervig.addressbook;

import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;

public class MainActivity extends Activity 
{
	private Model _model;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		_model = Model.getInstance(this);
		Contact contact = new Contact("Josh", "490-4124", "joshua.schultz@mines.sdsmt.edu", "805 Blaine Ave", "Rapid City");
		_model.insertContact(contact);
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

}
