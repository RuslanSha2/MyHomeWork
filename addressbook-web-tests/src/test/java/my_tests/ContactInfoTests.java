package my_tests;

import my_model.ContactData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ContactInfoTests  extends TestBase {

    @Test
    void testPhones() {
        var my_contacts = my_app.my_hbm().getMyContactList();
        var my_expected = my_contacts.stream().collect(Collectors.toMap(ContactData::my_id, contact ->
                Stream.of(contact.my_homephone(), contact.my_mobilephone(), contact.my_workphone(), contact.my_phone2())
                        .filter(s -> s != null && !s.isEmpty())
                        .collect(Collectors.joining("\n"))
        ));
        var my_phones = my_app.my_contacts().getMyPhones();
        Assertions.assertEquals(my_expected, my_phones);
    }

    @Test
    void testEmails() {
        var my_contacts = my_app.my_hbm().getMyContactList();
        var my_expected = my_contacts.stream().collect(Collectors.toMap(ContactData::my_id, contact ->
                Stream.of(contact.my_email(), contact.my_email2(), contact.my_email3())
                        .filter(s -> s != null && !s.isEmpty())
                        .collect(Collectors.joining("\n"))
        ));
        var my_emailes = my_app.my_contacts().getMyEmailes();
        Assertions.assertEquals(my_expected, my_emailes);
    }

    @Test
    void testAddress() {
        var my_contacts = my_app.my_hbm().getMyContactList();
        var my_expected = my_contacts.stream().collect(Collectors.toMap(ContactData::my_id, contact ->
                Stream.of(contact.my_address().replace("\r",""))
                        .filter(s -> s != null && !s.isEmpty())
                        .collect(Collectors.joining("\n"))
        ));
        var my_address = my_app.my_contacts().getMyAddress();
        Assertions.assertEquals(my_expected, my_address);
    }
}
