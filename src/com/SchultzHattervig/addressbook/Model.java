package com.SchultzHattervig.addressbook;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class Model extends SQLiteOpenHelper 
{



    public static final String KEY_ID = "ContactID";
    public static final String KEY_NAME = "Name";
    public static final String KEY_PHONE = "PhoneNumber";
    public static final String KEY_EMAIL = "Email";
    public static final String KEY_STREET = "StreetAddress";
    public static final String KEY_CITY = "City";
    
    private static final String TAG = "SchultzHattervig.AddressBook";

    private static final String DATABASE_NAME = "Schultz_Hattervig.AddressBook.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_MYCONTACTS = "Contacts";

    private static final String TABLE_CREATE_MYcontactS =
                            "CREATE TABLE " +
                            TABLE_MYCONTACTS +
                                    "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                    KEY_NAME + " TEXT, " +
                                    KEY_PHONE + " TEXT, " +
                                    KEY_EMAIL + " TEXT, " +
                                    KEY_STREET + " TEXT, " +
                                    KEY_CITY + " TEXT);";

    private SQLiteDatabase _db;
    private static Model _instance;
    
    /**
     * Class defined to allow for the passing of class data between
     * the model, activity, and fragments.  Also, used to define the 
     * contents of an ArrayAdapter.
     * @author brianb
     *
     */

    public Model(Context context)
    {
        // Call the parent class and pass the actual name and version of the
        // database to be created. The version will be used in the future for
        // determine whether onUpgrade() is called from the SQLiteOpenHelper
        // extension.
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        // Execute the CREATE TABLE statement defined as a const.
    	//If the call is an invalid SQL statement, SQLException is thrown
        db.execSQL(TABLE_CREATE_MYcontactS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        // If there is ever a need to upgrade the database and/or table.
        // Compare old and new versions to determine if modifications
        // to the database are necessary. Typically, this will be done with
        // ALTER TABLE or CREATE TABLE SQL statements depending on the
        // change required.

        if (newVersion == 2)
        {
        	// No version 2 upgrade process yet.
        }
    }
    
    public static synchronized Model getInstance(Context context)
    {
        // Used to synchronize access and force singleton on the 
        // database helper object.
        if (_instance == null)
        {
        	_instance = new Model(context);
        }
        
        return _instance;
    }

    public void insertContact(Contact contact)
    {
        // Take parameters and pass to method to populate the
        // ContentValues data structure.
        ContentValues values = populateContentValues(contact);

        // Open the database connect, keep it close to the actual operation.
        openDBConnection();

        // Execute query to update the specified contact.
        long id = _db.insert(TABLE_MYCONTACTS, null, values);

        if(id != -1)
        {
        	Log.d(TAG, "ContactID inserted = " + String.valueOf(id));
        }
        else
        {
        	Log.d(TAG, "Contact not inserted");
        }

        // Close the database connection as soon as possible.
        closeDBConnection();

    }

    public void insertSampleContacts()
    {
        Contact contact;
        
        contact = new Contact("Josh", "460-4124", "josh.schultz@mines.sdsmt.edu", "923 Blaine Ave", "Rapid City");
        insertContact(contact);
        
        contact = new Contact("Dave", "279-4129", "David.Jarsen@yahoo.com", "9011 Jackson Blvd", "Chicago");
        insertContact(contact);
        
        contact = new Contact("George", "272-2482", "gman1955@yahoo.com", "8th SW Ave", "Denver");
        insertContact(contact);
        
        contact = new Contact("Zehn", "994-2985", "zehn23456@gmail.com", "262 Park St", "Atlanta");
        insertContact(contact);
    }

    public boolean updateContact(Contact contact)
    {
    	//Before opening the database, verify the contact contains a valid ID
    	if(contact.getID() == Contact.INVALID_ID)
    	{
    		return false;
    	}
    	
        // Take parameters and pass to method to populate the
        // ContentValues data structure.
        ContentValues values = populateContentValues(contact);

        // Open the database connect, keep it close to the actual operation.
        openDBConnection();

        // Execute query to update the specified contact.
        int rowsAffected = _db.update(TABLE_MYCONTACTS,
                                                                        values,
                                                                        KEY_ID + " = ?",
                                                                        new String[] { String.valueOf(contact.getID()) });

        // Close the database connection as soon as possible.
        closeDBConnection();

        if (rowsAffected == 0)
        {
                // The contact row was not updated, what should be done?
                Log.d(TAG, "Contact not updated!");
                return false;
        }
        return true;
    }

    public boolean deleteContact(Contact contact)
    {
    	//Before opening the database, verify the contact contains a valid ID
    	if(contact.getID() == Contact.INVALID_ID)
    	{
    		Log.d(TAG, "Couldn't delete contact; the contact ID is invalid");
    		return false;
    	}
    	
        // Open the database connect, keep it close to the actual operation.
        openDBConnection();

        // Execute query to delete the specified contact.
        int rowsAffected = _db.delete(TABLE_MYCONTACTS,
                                                                        KEY_ID + " = ?",
                                                                        new String[] { String.valueOf(contact.getID()) });
        
        // Close the database connection as soon as possible.
        closeDBConnection();

        if (rowsAffected == 0)
        {
                // The contact row was not deleted, what should be done?
                Log.d(TAG, "Contact not deleted!");
                return false;
        }
        return true;
    }

    public void clearAllContacts()
    {
    	List<Contact> contacts = getContacts();
    	
    	openDBConnection();
    	
    	for(int i = 0; i < contacts.size(); i++)
    		if(!deleteContact((Contact)contacts.toArray()[i]))
    		{
    			Log.d(TAG, "Error in clearing all contacts");
    		}
    	
    	closeDBConnection();
    }
    
    public Contact getContact(long id)
    {
        Contact contact = null;
        
        openDBConnection();
        
        // Return the specific contact row based on ID passed.
        // _id is required by SimpleCursorAdaptor.
        Cursor cursor = _db.query(TABLE_MYCONTACTS,
                                          new String[] { KEY_ID, KEY_NAME, KEY_PHONE, KEY_EMAIL, KEY_STREET, KEY_CITY},
                                          KEY_ID + " = ?",
                                          new String[] {String.valueOf(id)},
                                          null,
                                          null,
                                          KEY_NAME);
        
        if (cursor.moveToFirst())
        {
                contact = cursorToContact(cursor);
        }
        
        cursor.close();
        closeDBConnection();

        return contact;
    }
    
    public List<Contact> getContacts()
    {
        List<Contact> contacts = new ArrayList<Contact>();
        
        openDBConnection();
        
        // Query for a list of contacts.
        Cursor cursor = _db.query(TABLE_MYCONTACTS,
                                  new String[] { KEY_ID, KEY_NAME, KEY_PHONE, KEY_EMAIL, KEY_STREET, KEY_CITY},
                                  null,
                                  null,
                                  null,
                                  null,
                                  KEY_NAME);
        
        // Populate the contact List by iterating through Cursor.
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) 
        {
            Contact contact = cursorToContact(cursor);
                contacts.add(contact);
            cursor.moveToNext();
        }
        
        cursor.close();
        closeDBConnection();
        
        return contacts;
    }

    private void openDBConnection()
    {
        // Opens connection to the database for writing specifically.
        _db = getWritableDatabase();
    }

    private void closeDBConnection()
    {
        if (_db != null && _db.isOpen() == true)
        {
                // Close connection to database if open.
                _db.close();
        }
    }

    private Contact cursorToContact(Cursor cursor)
    {
        Contact contact = new Contact(cursor.getLong(cursor.getColumnIndex(KEY_ID))); 
        contact.setName(cursor.getString(cursor.getColumnIndex(KEY_NAME))); 
        contact.setPhone(cursor.getString(cursor.getColumnIndex(KEY_PHONE))); 
        contact.setEmail(cursor.getString(cursor.getColumnIndex(KEY_EMAIL))); 
        contact.setStreet(cursor.getString(cursor.getColumnIndex(KEY_STREET))); 
        contact.setCity(cursor.getString(cursor.getColumnIndex(KEY_CITY))); 
        
        return contact;
    }

    //Whatever values are returned will be populated in the database
    private ContentValues populateContentValues(Contact contact)
    {
        // Common function used to populate the ContentValues to be used in SQL
        // insert or update methods.
        
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, contact.getName());
        values.put(KEY_PHONE, contact.getPhone());
        values.put(KEY_EMAIL, contact.getEmail());
        values.put(KEY_STREET, contact.getStreet());
        values.put(KEY_CITY, contact.getCity());

        return values;
    }

}
