package contactlist;

import java.util.List;

public interface IContactStore extends Iterable<Contact>
{

    public int getMaxId();
    public Contact add(Contact contact);
    public Contact remove(Contact contact);
    public List<Contact> find(Contact findContact);
    public String print();
}
