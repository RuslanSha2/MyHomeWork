package my_tests;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import my_model.ContactData;
import my_model.GroupData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MyContactCreationTests extends TestBase {
    public static List<ContactData> myContactProvider() throws IOException {
        var my_contacts = new ArrayList<ContactData>();
        var myContactJson = Files.readString(Paths.get("my_contacts.json"));
        ObjectMapper myContactMapper = new ObjectMapper();
        var myContactValue = myContactMapper.readValue(myContactJson, new TypeReference<List<ContactData>>() {
        });
        my_contacts.addAll(myContactValue);
        return my_contacts;
    }

    @ParameterizedTest
    @MethodSource("myContactProvider")
    public void canCreateMyMultipleContact(ContactData my_contact) {
        var myOldContacts = my_app.my_jdbc().getMyContactList();
        my_app.my_contacts().createMyContact(my_contact);
        var myNewContacts = my_app.my_jdbc().getMyContactList();
        Comparator<ContactData> compareById = (o1, o2) -> {
            return Integer.compare(Integer.parseInt(o1.my_id()), Integer.parseInt(o2.my_id()));
        };
        myNewContacts.sort(compareById);

        int myLastNdx = myNewContacts.size() - 1;
        var myLastId = myNewContacts.get(myLastNdx).my_id();
        var myLastPhoto = myNewContacts.get(myLastNdx).my_photo();
        var myExpectedList = new ArrayList<>(myOldContacts);
        myExpectedList.add(my_contact.withId(myLastId).withPhoto(myLastPhoto));

        myExpectedList.sort(compareById);
        Assertions.assertEquals(myExpectedList, myNewContacts);
    }

    @Test
    void canCreateMyContactInMyGroup() {
        var my_contact = new ContactData().withRandomData(2,
                my_app.my_properties().getProperty("file.photoDir"));
        if (my_app.my_hbm().getMyGroupsCount() == 0) {
            my_app.my_hbm().createMyGroup(new GroupData().withRandomData(2));
        }
        var my_group = my_app.my_hbm().getMyGroupList().get(0);
        var oldRelated = my_app.my_hbm().getMyContactsInGroup(my_group);
        my_app.my_contacts().createMyContact(my_contact, my_group);
        var newRelated = my_app.my_hbm().getMyContactsInGroup(my_group);
        Assertions.assertEquals(oldRelated.size() + 1, newRelated.size());
    }
}
