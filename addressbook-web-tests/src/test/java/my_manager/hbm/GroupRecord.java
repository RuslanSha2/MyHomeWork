package my_manager.hbm;

import jakarta.persistence.*;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "group_list")
public class GroupRecord {
    @Id
    @Column(name = "group_id")
    public int my_id;
    @Column(name = "group_name")
    public String my_name;
    @Column(name = "group_header")
    public String my_header;
    @Column(name = "group_footer")
    public String my_footer;

    public Date deprecated = new Date();

    @ManyToMany
    @JoinTable(name = "address_in_groups",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "id"))
    public List<ContactRecord> my_contacts;

    public GroupRecord() {
    }

    public GroupRecord(int my_id,
                       String my_name,
                       String my_header,
                       String my_footer) {
        this.my_id = my_id;
        this.my_name = my_name;
        this.my_header = my_header;
        this.my_footer = my_footer;
    }
}
