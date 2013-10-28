package com.SchultzHattervig.addressbook;

import android.widget.ArrayAdapter;
/** 
 * 
 * @author Josh Schultz & Erik Hattervig
 *
 */
public interface IContactControlListener 
{
    public void selectContact(Contact contact);
    public void insertContact();
    public void insertContact(Contact contact);
    public void deleteContact(Contact contact);
    public void updateContact(Contact contact);
    public Contact getContact();
    public ArrayAdapter<Contact> getContactArrayAdapter();
}
