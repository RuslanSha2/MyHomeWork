package my_tests;

import my_common.MyCommonFunctions;
import my_model.GroupData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;

public class MyGroupModificationTests extends TestBase {

    @Test
    void canModifyMyGroup() {
        if (my_app.my_hbm().getMyGroupsCount() == 0) {
            my_app.my_hbm().createMyGroup(new GroupData().withRandomData(2));
        }
        var myOldGroups = my_app.my_hbm().getMyGroupList();
        var my_rnd = new Random();
        var my_index = my_rnd.nextInt(myOldGroups.size());
        var myTestData = new GroupData().withName(String.format("modified name %s+%s",
                MyCommonFunctions.randomString(3),
                MyCommonFunctions.randomNumber(2)));
        my_app.my_groups().modifyMyGroup(myOldGroups.get(my_index), myTestData);
        var myNewGroups = my_app.my_hbm().getMyGroupList();
        var myExpectedList = new ArrayList<>(myOldGroups);
        myExpectedList.set(my_index, myTestData
                .withId(myOldGroups.get(my_index).my_id())
                .withHeader(myOldGroups.get(my_index).my_header())
                .withFooter(myOldGroups.get(my_index).my_footer()));
        Comparator<GroupData> compareById = (o1, o2) -> {
            return Integer.compare(Integer.parseInt(o1.my_id()), Integer.parseInt(o2.my_id()));
        };
        myNewGroups.sort(compareById);
        myExpectedList.sort(compareById);
        Assertions.assertEquals(myNewGroups, myExpectedList);
    }
}
