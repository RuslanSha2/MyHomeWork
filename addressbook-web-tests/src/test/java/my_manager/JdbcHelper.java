package my_manager;

import my_model.ContactData;
import my_model.GroupData;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcHelper extends HelperBase {
    private static String jdbcMysqlUrl;
    private static String jdbcMysqlUsername;
    private static String jdbcMysqlPassword;

    public JdbcHelper(ApplicationManager my_manager) {
        super(my_manager);
        jdbcMysqlUrl = my_manager.my_properties().getProperty("db.baseUrl");
        jdbcMysqlUsername = my_manager.my_properties().getProperty("db.username");
        jdbcMysqlPassword = my_manager.my_properties().getProperty("db.password");
    }

    public List<ContactData> getMyContactList() {
        var my_contacts = new ArrayList<ContactData>();
        try (var my_conn = DriverManager.getConnection(jdbcMysqlUrl, jdbcMysqlUsername, jdbcMysqlPassword);
             var my_statement = my_conn.createStatement();
             var my_result = my_statement.executeQuery(
            "SELECT id, firstname, middlename, lastname, nickname, title, company, address, mobile, email, photo FROM addressbook;"))
        {
            while (my_result.next()) {
                my_contacts.add(new ContactData()
                        .withId(my_result.getString("id"))
                        .withFirstname(my_result.getString("firstname"))
                        .withMiddlename(my_result.getString("middlename"))
                        .withLastname(my_result.getString("lastname"))
                        .withNickname(my_result.getString("nickname"))
                        .withTitle(my_result.getString("title"))
                        .withCompany(my_result.getString("company"))
                        .withAddress(my_result.getString("address"))
                        .withMobile(my_result.getString("mobile"))
                        .withEmail(my_result.getString("email"))
                        .withPhoto(my_result.getString("photo")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return my_contacts;
    }

    public List<GroupData> getMyGroupList() {
        var my_groups = new ArrayList<GroupData>();
        try (var my_conn = DriverManager.getConnection(jdbcMysqlUrl, jdbcMysqlUsername, jdbcMysqlPassword);
             var my_statement = my_conn.createStatement();
             var my_result = my_statement.executeQuery(
            "SELECT group_id, group_name, group_header, group_footer FROM group_list;"))
        {
            while (my_result.next()) {
                my_groups.add(new GroupData()
                        .withId(my_result.getString("group_id"))
                        .withName(my_result.getString("group_name"))
                        .withHeader(my_result.getString("group_header"))
                        .withFooter(my_result.getString("group_footer")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return my_groups;
    }
}
