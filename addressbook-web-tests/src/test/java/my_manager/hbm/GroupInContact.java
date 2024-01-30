package my_manager.hbm;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.Date;

@Entity
@Table(name = "address_in_groups")
public class GroupInContact {
    @Id
    int id;
    int group_id;

    public int domain_id = 0;
    public Date deprecated = new Date();

    public GroupInContact() {
    }

    public GroupInContact(int my_id,
                          int my_group_id) {
        this.id = my_id;
        this.group_id = my_group_id;
    }
}
