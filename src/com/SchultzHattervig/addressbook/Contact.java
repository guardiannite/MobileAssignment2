package com.SchultzHattervig.addressbook;

import java.util.Comparator;
/**
 * Stores a specific contact's information.  This includes a name,
 * phone number, email address, and address.  The class also has an
 * id associated with it.  The id represents the id autogenerated from
 * the database.  The default is set to INVALID_ID.
 * @author Josh Schultz
 * @author Erik Hattervig
 */
public class Contact implements Comparator<Contact>
{
	//Used for other classes to compare if the contact has a valid database lookup ID
	public static final long INVALID_ID = -1;
	
	private long _id;
	private String _name;
	private String _phone;
	private String _email;
	private String _street;
	private String _city;
	
	/**
	 * Constructor for Contact.  The contact's field are populated and the
	 * private _id field is set to {@link INVALID_ID}.
	 * @param name The name of the contact
	 * @param phone The phone number of the contact
	 * @param email The email of the contact
	 * @param street The street address of the contact
	 * @param city The city, state, and zip code of the contact
	 */
	public Contact(String name, String phone, String email, String street, String city)
	{
		_id = INVALID_ID;
		_name = name;
		_phone = phone;
		_email = email;
		_street = street;
		_city = city;
	}
	
	/**
	 * 
	 * @param name The name of the contact
	 * @param phone The phone number of the contact
	 * @param email The email of the contact
	 * @param street The street address of the contact
	 * @param city The city, state and zip code of the contact
	 * @param id The id used in the database
	 */
	public Contact(String name, String phone, String email, String street, String city, long id)
	{
		this(name, phone, email, street, city);
		_id = id;
	}
	
	/**
	 * 
	 * @return The database's id number for the contact, otherwise INVALID_ID
	 */
	public long getID()
	{
		return _id;
	}
	
	/**
	 * 
	 * @return The name of the contact
	 */
	public String getName()
	{
		return _name;
	}
	
	/**
	 * 
	 * @return The phone number of the contact
	 */
	public String getPhone()
	{
		return _phone;
	}
	
	/**
	 * 
	 * @return The email of the contact
	 */
	public String getEmail()
	{
		return _email;
	}
	
	/**
	 * 
	 * @return The street address of the contact
	 */
	public String getStreet()
	{
		return _street;
	}
	
	/**
	 * 
	 * @return The city, state, and zip code of the contact
	 */
	public String getCity()
	{
		return _city;
	}
	
	/**
	 * @return The name of the contact
	 */
    @Override
    public String toString() 
    {
        return String.valueOf(_name);
    }

    /**
     * @return (Negative) If the lhs name precedes the rhs.
     * (Positive) If the rhs name precedes the lhs.
     * 0 If the two contacts are the same name.
     * 
     */
	@Override
	public int compare(Contact lhs, Contact rhs) 
	{
		//Case sensitive sort
		return lhs.getName().compareTo(rhs.getName());
	}
}
