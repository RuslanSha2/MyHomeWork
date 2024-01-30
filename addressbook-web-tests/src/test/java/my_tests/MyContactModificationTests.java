package my_tests;

import my_common.MyCommonFunctions;
import my_model.ContactData;
import my_model.GroupData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Random;
import java.util.Comparator;

public class MyContactModificationTests extends TestBase {

    @Test
    void canModifyMyContact() {
        if (my_app.my_hbm().getMyContactsCount() == 0) {
            my_app.my_hbm().createMyContact(new ContactData().withRandomData(2,
                    my_app.my_properties().getProperty("file.photoDir")));
        }
        var myOldContacts = my_app.my_hbm().getMyContactList();
        var my_rnd = new Random();
        var my_index = my_rnd.nextInt(myOldContacts.size());
        var myTestData = new ContactData()
                .withFirstname(String.format("mod_fn_%s+%s",
                        MyCommonFunctions.randomString(3),
                        MyCommonFunctions.randomNumber(2)))
                .withLastname(String.format("mod_ln_%s+%s",
                        MyCommonFunctions.randomString(3),
                        MyCommonFunctions.randomNumber(2)));
        my_app.my_contacts().modifyMyContact(myOldContacts.get(my_index), myTestData);
        var myNewContacts = my_app.my_hbm().getMyContactList();
        var myExpectedList = new ArrayList<>(myOldContacts);
        myExpectedList.set(my_index, myTestData
                .withId(myOldContacts.get(my_index).my_id())
                .withMiddlename(myOldContacts.get(my_index).my_middlename())
                .withNickname(myOldContacts.get(my_index).my_nickname())
                .withTitle(myOldContacts.get(my_index).my_title())
                .withCompany(myOldContacts.get(my_index).my_company())
                .withAddress(myOldContacts.get(my_index).my_address())
                .withMobile(myOldContacts.get(my_index).my_mobilephone())
                .withEmail(myOldContacts.get(my_index).my_email())
                .withPhoto(myOldContacts.get(my_index).my_photo())
                .withWorkphone(myOldContacts.get(my_index).my_workphone())
                .withHomephone(myOldContacts.get(my_index).my_homephone())
                .withEmail2(myOldContacts.get(my_index).my_email2())
                .withEmail3(myOldContacts.get(my_index).my_email3())
                .withPhone2(myOldContacts.get(my_index).my_phone2())
        );
        Comparator<ContactData> compareById = (o1, o2) -> {
            return Integer.compare(Integer.parseInt(o1.my_id()), Integer.parseInt(o2.my_id()));
        };
        myNewContacts.sort(compareById);
        myExpectedList.sort(compareById);
        Assertions.assertEquals(myNewContacts, myExpectedList);
    }

    @Test
    void canModifyMyContactInMyGroup() {
        if (my_app.my_hbm().getMyContactsCount() == 0) {
            my_app.my_hbm().createMyContact(new ContactData().withRandomData(2,
                    my_app.my_properties().getProperty("file.photoDir")));
        }
        if (my_app.my_hbm().getMyGroupsCount() == 0) {
            my_app.my_hbm().createMyGroup(new GroupData().withRandomData(2));
        }
        var my_contact = my_app.my_hbm().getMyContactList().get(0);
        var my_group = my_app.my_hbm().getMyGroupList().get(0);
        if (my_app.my_hbm().getMyContactsInGroup(my_group).contains(my_contact)) {
            my_app.my_hbm().removeGroupFromMyContact(my_contact, my_group);
        }
        var oldRelated = my_app.my_hbm().getMyContactsInGroup(my_group);
        my_app.my_contacts().modifyMyContact(my_contact, my_group);
        var newRelated = my_app.my_hbm().getMyContactsInGroup(my_group);
        Assertions.assertEquals(oldRelated.size() + 1, newRelated.size());
    }

    @Test
    void canModifyMyContactOutMyGroup() {
        if (my_app.my_hbm().getMyContactsCount() == 0) {
            my_app.my_hbm().createMyContact(new ContactData().withRandomData(2,
                    my_app.my_properties().getProperty("file.photoDir")));
        }
        if (my_app.my_hbm().getMyGroupsCount() == 0) {
            my_app.my_hbm().createMyGroup(new GroupData().withRandomData(2));
        }
        if (my_app.my_hbm().getMyGroupsInContactCount() == 0) {
            my_app.my_hbm().addGroupToMyContact(my_app.my_hbm().getMyContactList().get(0),
                    my_app.my_hbm().getMyGroupList().get(0));
        }
        var my_group = my_app.my_hbm().getMyGroupsInContact().get(0);
        var oldRelated = my_app.my_hbm().getMyContactsInGroup(my_group);
        var my_contact = oldRelated.get(0);
        my_app.my_contacts().removeGroupFromMyContact(my_contact, my_group);
        var newRelated = my_app.my_hbm().getMyContactsInGroup(my_group);
        Assertions.assertEquals(oldRelated.size() - 1, newRelated.size());
    }
}
