package contactlist;

import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

import contactlist.dataStructures.ASTBinarySearchTree;
import contactlist.dataStructures.BinarySearchTree;
import contactlist.dataStructures.LinkedListImpl;
import contactlist.dataStructures.ArrayASTBinarySearchTree;

public class ContactStoreImpl implements IContactStore {

    //private LinkedListImpl<Contact> contacts = new LinkedListImpl<>();
    //private BinarySearchTree<Contact> contacts = new BinarySearchTree<>();
    private ASTBinarySearchTree<Contact> contacts = new ASTBinarySearchTree<>();
    //private ArrayASTBinarySearchTree<Contact> contacts = new ArrayASTBinarySearchTree<>();

    public int getMaxId(){
        return contacts.size();
    }

    public Contact add(Contact contact) {
        if(contact.id == 0){
            int maxId = getMaxId();
            if(contact.id <= maxId) {
                App.print(MessageFormat.format("Changes contact id from {0} to {1}", contact.id, maxId+1));
                contact.id = maxId+1;
            }
        }

        if(!contacts.contains(contact)) {
            boolean added = contacts.add(contact);
            return (added) ? contact : null;
        }

        return null;
    }

    public Contact remove(Contact contact) {
        List<Contact> matches = find(contact);
        App.print(MessageFormat.format("Found {0} matches", matches.size()));
        if(matches.isEmpty()) {
            return null;
        } else {
            Contact match = matches.get(0);
            App.print(MessageFormat.format("Match is {0}", match));
            boolean removed = contacts.remove(match);
            App.print("Was contact removed? "+removed);
            return (removed) ? match : null;
        }
    }

    public Iterator<Contact> iterator(){
        return contacts.iterator();
    }

    public List<Contact> find(Contact findContact){
        return contacts.stream()
            .filter(contact -> {
                if(null == contact) return false;
                if(findContact.id > 0) {
                    //System.out.println(MessageFormat.format("contact.id {0} and findContact.id {1}", contact.id, findContact.id));
                    return contact.id == findContact.id;
                } else {
                    boolean hasFirstName = null != findContact.firstName;
                    boolean hasLastName = null != findContact.lastName;
                    boolean hasStreetAddress = null != findContact.streetAddress;
                    boolean hasCity = null != findContact.city;
                    boolean hasState = null != findContact.state;
                    boolean hasPostalCode = null != findContact.postalCode;

                    boolean matchFirstName = (!hasFirstName) ? true : Objects.equals(findContact.firstName, contact.firstName);
                    boolean matchLastName = (!hasLastName) ? true : Objects.equals(findContact.lastName, contact.lastName);
                    boolean matchStreetAddress = (!hasStreetAddress) ? true : Objects.equals(findContact.streetAddress, contact.streetAddress);
                    boolean matchCity = (!hasCity) ? true : Objects.equals(findContact.city, contact.city);
                    boolean matchState = (!hasState) ? true : Objects.equals(findContact.state, contact.state);
                    boolean matchPostalCode = (!hasPostalCode) ? true : Objects.equals(findContact.postalCode, contact.postalCode);

                    return matchFirstName && matchLastName && matchStreetAddress && matchCity && matchState && matchPostalCode;
                }
            })
            .collect(Collectors.toList());
    }

    public String print() {
        return contacts.print();
    }
}
