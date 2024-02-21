package my_tests;

import io.qameta.allure.Allure;
import my_model.GroupData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Random;

public class MyGroupRemovalTests extends TestBase {
    @Test
    public void canRemoveMyGroup() {
        Allure.step("Checking precondition", step -> {
            if (my_app.my_hbm().getMyGroupsCount() == 0) {
                my_app.my_hbm().createMyGroup(new GroupData().withRandomData(2));
            }
        });

        var myOldGroups = my_app.my_hbm().getMyGroupList();
        var my_rnd = new Random();
        var my_index = my_rnd.nextInt(myOldGroups.size());
        my_app.my_groups().removeMyGroup(myOldGroups.get(my_index));
        var myNewGroups = my_app.my_hbm().getMyGroupList();
        var myExpectedList = new ArrayList<>(myOldGroups);
        myExpectedList.remove(my_index);

        Allure.step("Validating results", step -> {
            Assertions.assertEquals(myNewGroups, myExpectedList);
        });
    }
}
