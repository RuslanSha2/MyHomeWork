package my_manager.hbm;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "addressbook")
public class ContactRecord {
    @Id
    @Column(name = "id")
    public int my_id;
    @Column(name = "firstname")
    public String my_firstname;
    @Column(name = "middlename")
    public String my_middlename;
    @Column(name = "lastname")
    public String my_lastname;
    @Column(name = "nickname")
    public String my_nickname;
    @Column(name = "title")
    public String my_title;
    @Column(name = "company")
    public String my_company;
    @Column(name = "address")
    public String my_address;
    @Column(name = "mobile")
    public String my_mobile;
    @Column(name = "email")
    public String my_email;
    @Column(name = "photo")
    public String my_photo;
    public String home = "";
    public String work = "";
    public String phone2;
    public String fax = "";
    public String email2 = "";
    public String email3 = "";
    public String homepage = "";

    @ManyToMany
    @JoinTable(name = "address_in_groups",
            joinColumns = @JoinColumn(name = "id"),
            inverseJoinColumns = @JoinColumn(name = "group_id"))
    public List<GroupRecord> my_groups;

    public ContactRecord() {
    }

    public ContactRecord(int my_id,
                         String my_firstname,
                         String my_middlename,
                         String my_lastname,
                         String my_nickname,
                         String my_title,
                         String my_company,
                         String my_address,
                         String my_mobile,
                         String my_email,
                         String my_photo) {
        this.my_id = my_id;
        this.my_firstname = my_firstname;
        this.my_middlename = my_middlename;
        this.my_lastname = my_lastname;
        this.my_nickname = my_nickname;
        this.my_title = my_title;
        this.my_company = my_company;
        this.my_address = my_address;
        this.my_mobile = my_mobile;
        this.my_email = my_email;
        this.my_photo = my_photo;
    }
}
