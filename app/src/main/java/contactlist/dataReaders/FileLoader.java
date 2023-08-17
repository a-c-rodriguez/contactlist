package contactlist.dataReaders;

import java.io.BufferedReader;
import java.io.FileReader;
import java.text.MessageFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;

import com.google.common.base.Strings;

import contactlist.App;
import contactlist.Contact;
import contactlist.IContactStore;

public class FileLoader {

    private String fileName;
    private IContactStore contactStore;
    private int loadCount=0;
    
    public FileLoader(String file, IContactStore contactStore) {
        this.fileName = file;
        this.contactStore = contactStore;
    }

    public void processFile() throws Exception {
        Instant starts = Instant.now();
        FileReader fileReader = null;
        BufferedReader bufferedReader = null;
        try{
            fileReader = new FileReader(this.fileName);
            bufferedReader = new BufferedReader(fileReader);
            bufferedReader.lines().filter(line -> !Strings.isNullOrEmpty(line) &&
                    !line.equalsIgnoreCase("ID,FirstName,LastName,StreetAddress,City,State,PostalCode"))
                .iterator().forEachRemaining(line -> {
                    HashMap args = getArgs(line);
                    int id = 0;
                    if(args.containsKey("id")){
                        id = Integer.parseInt(args.get("id").toString());
                    }
                    Contact loadContact = Contact.makeContact(id, args);
                    Contact c = contactStore.add(loadContact);
                    App.print("loaded " + c.toStringWithId());
                    this.loadCount++;
                });
        } catch (Exception e) {
            App.print(e.getMessage());
            StackTraceElement[] stack = e.getStackTrace();
            for(int i =0; i < stack.length; i++)
                App.print("\t" + stack[i]);
        } finally {
            if(null != bufferedReader) bufferedReader.close();
            if(null != fileReader) fileReader.close();
        }
        Instant stops = Instant.now();
        Duration d =  Duration.between(starts,stops);
        App.print(MessageFormat.format("Loaded {0} contacts in {1} s", this.loadCount, d.toSeconds()));
        App.print("Max Contact ID is " + contactStore.getMaxId());
    }
    

    public HashMap getArgs(String line) {
        HashMap h = new HashMap<>();
        String[] terms = line.split(",");
        h.put("id", terms[0]);
        h.put("fn", terms[1]);
        h.put("ln", terms[2]);
        h.put("addr", terms[3]);
        h.put("city", terms[4]);
        h.put("state", terms[5]);
        h.put("code", terms[6]);
        return h;
    }
}
