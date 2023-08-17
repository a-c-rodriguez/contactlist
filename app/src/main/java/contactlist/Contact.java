package contactlist;

import java.text.MessageFormat;
import java.util.HashMap;

public class Contact implements Comparable<Contact> {
    public int id=0;
    public String firstName;
    public String lastName;
    public String streetAddress;
    public String city;
    public String state;
    public String postalCode;

    public static Contact makeContact(int uid, HashMap args) {
        Contact contact = new Contact();
        contact.id = uid;
        for(Object key : args.keySet()) {
            switch(key.toString()){
                case "fn":
                case "first":
                case "firstname":
                    contact.firstName = args.get(key).toString();
                    break;
                case "ln":
                case "last":
                case "lastname": 
                    contact.lastName =  args.get(key).toString();
                    break;
                case "addr":
                case "address":
                    contact.streetAddress = args.get(key).toString();
                    break;
                case "ci":
                case "city":
                    contact.city = args.get(key).toString();
                    break;
                case "st":
                case "state":
                    contact.state = args.get(key).toString();
                    break;
                case "po":
                case "postal":
                case "code":
                    contact.postalCode = args.get(key).toString();
                    break;
                default:
                    break;
            }
        }
        
        return contact;
    }

    public String toStringWithId() {
        return MessageFormat.format("{0}|{1}|{2}|{3}|{4}|{5}|{6}", id, firstName, lastName, streetAddress, city, state, postalCode);
    }

    @Override
    public String toString() {
        return MessageFormat.format("{0}|{1}|{2}|{3}|{4}|{5}", firstName, lastName, streetAddress, city, state, postalCode);
    }

    @Override
    public boolean equals(Object o) {
        return this.toString().equalsIgnoreCase(o.toString());
    }

    @Override
    public int hashCode() {
        return this.toStringWithId().hashCode();
    }

    @Override
    public int compareTo(Contact contact) {
        return this.toString().compareTo(contact.toString()); 
    }
}
