package my_manager;

import io.qameta.allure.Step;
import my_manager.hbm.ContactRecord;
import my_manager.hbm.GroupInContact;
import my_manager.hbm.GroupRecord;
import my_model.ContactData;
import my_model.GroupData;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.cfg.Configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

public class HibernateHelper extends HelperBase {
    private final SessionFactory sessionFactory;
    private static String jdbcMysqlUrl;
    private static String jdbcMysqlUsername;
    private static String jdbcMysqlPassword;

    public HibernateHelper(ApplicationManager my_manager) {
        super(my_manager);
        jdbcMysqlUrl = my_manager.my_properties().getProperty("db.baseUrl");
        jdbcMysqlUsername = my_manager.my_properties().getProperty("db.username");
        jdbcMysqlPassword = my_manager.my_properties().getProperty("db.password");

        sessionFactory = new Configuration()
                .addAnnotatedClass(ContactRecord.class)
                .addAnnotatedClass(GroupRecord.class)
                .addAnnotatedClass(GroupInContact.class)
                .setProperty(AvailableSettings.URL, jdbcMysqlUrl)
                .setProperty(AvailableSettings.USER, jdbcMysqlUsername)
                .setProperty(AvailableSettings.JAKARTA_JDBC_PASSWORD, jdbcMysqlPassword)
                .buildSessionFactory();
    }

    @Step
    public void createMyContact(ContactData my_contactData) {
        sessionFactory.inSession(session -> {
            session.getTransaction().begin();
            try {
                session.persist(convert(my_contactData));
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                session.getTransaction().commit();
            } catch (UnsupportedOperationException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Step
    public void createMyGroup(GroupData my_groupData) {
        sessionFactory.inSession(session -> {
            session.getTransaction().begin();
            session.persist(convert(my_groupData));
            session.getTransaction().commit();
        });
    }

    @Step
    public void addGroupToMyContact(ContactData my_contactdata, GroupData my_groupdata) {
        sessionFactory.inSession(session -> {
            var newGroupInContact = new GroupInContact(Integer.parseInt(my_contactdata.my_id()),
                    Integer.parseInt(my_groupdata.my_id()));
            session.getTransaction().begin();
            session.persist(newGroupInContact);
            session.getTransaction().commit();
        });
    }

    @Step
    public void removeGroupFromMyContact(ContactData my_contactdata, GroupData my_groupdata) {
        sessionFactory.inSession(session -> {
            var oldGroupInContact = new GroupInContact(Integer.parseInt(my_contactdata.my_id()),
                    Integer.parseInt(my_groupdata.my_id()));
            session.getTransaction().begin();
            session.remove(oldGroupInContact);
            session.getTransaction().commit();
        });
    }

    @Step
    public long getMyContactsCount() {
        return sessionFactory.fromSession(session -> {
            return session.createQuery("select count (*) from ContactRecord", Long.class).getSingleResult();
        });
    }

    @Step
    public long getMyGroupsCount() {
        return sessionFactory.fromSession(session -> {
            return session.createQuery("select count (*) from GroupRecord", Long.class).getSingleResult();
        });
    }

    @Step
    public List<ContactData> getMyContactList() {
        return sessionFactory.fromSession(session -> {
            return convertContactList(session.createQuery("from ContactRecord", ContactRecord.class).list());
        });
    }

    @Step
    public List<GroupData> getMyGroupList() {
        return sessionFactory.fromSession(session -> {
            return convertGroupList(session.createQuery("from GroupRecord", GroupRecord.class).list());
        });
    }

    @Step
    public List<ContactData> getMyContactsInGroup(GroupData my_group) {
        return sessionFactory.fromSession(session -> {
            var result = session.get(GroupRecord.class, my_group.my_id()).my_contacts;
            var uniq_result = result.stream().distinct();
            return convertContactList(uniq_result.collect(Collectors.toList()));
        });
    }

    @Step
    public long getMyGroupsInContactCount() {
        return sessionFactory.fromSession(session -> {
            return session.createQuery("select count(distinct(group_id)) from GroupInContact",
                    Long.class).getSingleResult();
        });
    }

    @Step
    public List<GroupData> getMyGroupsInContact() {
        return sessionFactory.fromSession(session -> {
            var my_contacts = getMyContactList();
            List<GroupRecord> result = new ArrayList<>();
            for (var my_contact : my_contacts) {
                result.addAll(session.get(ContactRecord.class, my_contact.my_id()).my_groups);
            }
            var uniq_result = result.stream().distinct();
            return convertGroupList(uniq_result.collect(Collectors.toList()));
        });
    }

    static List<ContactData> convertContactList(List<ContactRecord> my_records) {
        return my_records.stream().map(HibernateHelper::convert).collect(Collectors.toList());
    }

    static List<GroupData> convertGroupList(List<GroupRecord> my_records) {
        return my_records.stream().map(HibernateHelper::convert).collect(Collectors.toList());
    }

    private static ContactData convert(ContactRecord my_record) {
        return new ContactData().withId("" + my_record.my_id)
                .withFirstname(my_record.my_firstname)
                .withMiddlename(my_record.my_middlename)
                .withLastname(my_record.my_lastname)
                .withNickname(my_record.my_nickname)
                .withTitle(my_record.my_title)
                .withCompany(my_record.my_company)
                .withAddress(my_record.my_address)
                .withMobile(my_record.my_mobile)
                .withEmail(my_record.my_email)
                .withPhoto(my_record.my_photo)
                .withWorkphone(my_record.work)
                .withHomephone(my_record.home)
                .withEmail2(my_record.email2)
                .withEmail3(my_record.email3)
                .withPhone2(my_record.phone2);
    }

    private static ContactRecord convert(ContactData my_data) throws IOException, SQLException {
        var my_id = my_data.my_id();
        if ("".equals(my_id)) {
            my_id = "0";
        }

        var my_photo = "";
        if (my_data.my_photo().startsWith("PHOTO;ENCODING=BASE64;TYPE=")) {
            my_photo = my_data.my_photo();
        } else {
            File photoFile = new File(my_data.my_photo());
            FileInputStream photoStream = new FileInputStream(photoFile);
            byte[] photoBytes = new byte[(int) photoFile.length()];
            photoStream.read(photoBytes);
            photoStream.close();

            var myPhotoType = "";
            var photoFileType = Files.probeContentType(photoFile.toPath());
            if (photoFileType.equals("image/apng")) {
                myPhotoType = "APNG";
            } else if (photoFileType.equals("image/avif")) {
                myPhotoType = "AVIF";
            } else if (photoFileType.equals("image/png")) {
                myPhotoType = "PNG";
            } else if (photoFileType.equals("image/gif")) {
                myPhotoType = "GIF";
            } else if (photoFileType.equals("image/jpeg")) {
                myPhotoType = "JPEG";
            } else if (photoFileType.equals("image/svg+xml")) {
                myPhotoType = "SVG";
            } else if (photoFileType.equals("image/webp")) {
                myPhotoType = "WebP";
            } else {
                myPhotoType = "UNKNOWN";
            }
            byte[] base64photoBytes = Base64.getEncoder().encode(photoBytes);
            my_photo = "PHOTO;ENCODING=BASE64;TYPE=" + myPhotoType + ":" + new String(base64photoBytes);
        }

        var my_result = new ContactRecord(Integer.parseInt(my_id),
                my_data.my_firstname(),
                my_data.my_middlename(),
                my_data.my_lastname(),
                my_data.my_nickname(),
                my_data.my_title(),
                my_data.my_company(),
                my_data.my_address(),
                my_data.my_mobilephone(),
                my_data.my_email(),
                my_photo);
        return my_result;
    }

    private static GroupData convert(GroupRecord my_record) {
        return new GroupData("" + my_record.my_id,
                my_record.my_name,
                my_record.my_header,
                my_record.my_footer);
    }

    private static GroupRecord convert(GroupData my_data) {
        var my_id = my_data.my_id();
        if ("".equals(my_id)) {
            my_id = "0";
        }
        return new GroupRecord(Integer.parseInt(my_id),
                my_data.my_name(),
                my_data.my_header(),
                my_data.my_footer());
    }
}
