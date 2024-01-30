package my_tests;

import my_model.ContactData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Random;

public class MyContactRemovalTests extends TestBase {

    @Test
    public void canRemoveMyContact() {
        if (my_app.my_hbm().getMyContactsCount() == 0) {
            my_app.my_hbm().createMyContact(new ContactData().withRandomData(2,
                    my_app.my_properties().getProperty("file.photoDir")));
        }
        var myOldContacts = my_app.my_hbm().getMyContactList();
        var my_rnd = new Random();
        var my_index = my_rnd.nextInt(myOldContacts.size());
        my_app.my_contacts().removeMyContact(myOldContacts.get(my_index));
        var myNewContacts = my_app.my_hbm().getMyContactList();
        var myExpectedList = new ArrayList<>(myOldContacts);
        myExpectedList.remove(my_index);
        Assertions.assertEquals(myNewContacts, myExpectedList);
    }
}
