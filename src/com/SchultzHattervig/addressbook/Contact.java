package com.SchultzHattervig.addressbook;

import java.util.Comparator;

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
	
	public Contact(String name, String phone, String email, String street, String city)
	{
		_id = INVALID_ID;
		_name = name;
		_phone = phone;
		_email = email;
		_street = street;
		_city = city;
	}
	
	public Contact(String name, String phone, String email, String street, String city, long id)
	{
		this(name, phone, email, street, city);
		_id = id;
	}
	
	public long getID()
	{
		return _id;
	}
	
	public String getName()
	{
		return _name;
	}
	
	public String getPhone()
	{
		return _phone;
	}
	
	public String getEmail()
	{
		return _email;
	}
	
	public String getStreet()
	{
		return _street;
	}
	
	public String getCity()
	{
		return _city;
	}
	
    @Override
    public String toString() 
    {
        return String.valueOf(_name);//String.valueOf(_id) + ": " + _name + " from " + _street + " " + _city + " with email " + _email + " and phone number " + _phone;
    }

	@Override
	public int compare(Contact lhs, Contact rhs) 
	{
		//Case sensitve sort
		return lhs.getName().compareTo(rhs.getName());
	}
}
