package com.SchultzHattervig.addressbook;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
/**
 * Handles insertion, deletion, and modifying of contacts in the
 * database.
 * 
 * @author Josh Schultz
 * @author Erik Hattervig
 */
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

    /**
     * The onCreate override. Opens up the SQLite database passed to it.
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        // Execute the CREATE TABLE statement defined as a const.
    	//If the call is an invalid SQL statement, SQLException is thrown
        db.execSQL(TABLE_CREATE_MYcontactS);
    }
    
    /**
     * Forces there to be only one database in existence for this app.
     * @param context
     * @return
     */
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

    /**
     * Inserts a contact into the database.
     * 
     * @author Josh Schultz
	 * @author Erik Hattervig
     * @param contact
     */
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
    
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    	// TODO Auto-generated method stub
    	
    }

    /**
     * Updates a contact in the database that is passed to it.
     * 
     * @author Josh Schultz
	 * @author Erik Hattervig
     * @param contact
     * @return
     */
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

    /**
     * Deletes the contact that is passed to it from the database.
     * 
     * @author Josh Schultz
	 * @author Erik Hattervig
     * @param contact
     * @return
     */
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

    
    /**
     * Gets the Contact's information based on the id that is passed in
     * 
     * @author Josh Schultz
	 * @author Erik Hattervig
     * @param id
     */
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
    
    /**
     * Gets and returns a list of all of the contacts that are currently
     * in the database
     * 
     * @author Josh Schultz
	 * @author Erik Hattervig
     * @return
     */
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

    /**
     * Opens a connection to the database for reading and writing to it.
     * 
     * @author Josh Schultz
	 * @author Erik Hattervig
     */
    private void openDBConnection()
    {
        // Opens connection to the database for writing specifically.
        _db = getWritableDatabase();
    }

    /**
     * Closes the database connection
     * 
     * @author Josh Schultz
	 * @author Erik Hattervig
     */
    private void closeDBConnection()
    {
        if (_db != null && _db.isOpen() == true)
        {
                // Close connection to database if open.
                _db.close();
        }
    }

    /**
     * Gets the information from the database from a cursor.
     * 
     * @author Josh Schultz
	 * @author Erik Hattervig
     * 
     * @param cursor
     * @return 
     */
    private Contact cursorToContact(Cursor cursor)
    {
        long id = cursor.getLong(cursor.getColumnIndex(KEY_ID));
        String name = cursor.getString(cursor.getColumnIndex(KEY_NAME)); 
        String phone = cursor.getString(cursor.getColumnIndex(KEY_PHONE)); 
        String email = cursor.getString(cursor.getColumnIndex(KEY_EMAIL)); 
        String street = cursor.getString(cursor.getColumnIndex(KEY_STREET)); 
        String city = cursor.getString(cursor.getColumnIndex(KEY_CITY)); 
        
        Contact contact = new Contact(name, phone, email, street, city, id);
        return contact;
    }

    /**
     * Commonly used to make calls to the database.  This creates (more/less)
     * a string that contains all of the contact's information.
     * 
     * @param contact The contact that will be parsed into contentValues
     * 
     * @return The contentValues of the contact.  This contains the name,
     * phone, email, and address.
     */
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
