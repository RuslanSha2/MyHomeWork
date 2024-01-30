package my_tests;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import my_model.GroupData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MyGroupCreationTests extends TestBase {

    public static List<GroupData> myGroupProvider() throws IOException {
        var my_groups = new ArrayList<GroupData>();
        var myGroupJson = Files.readString(Paths.get("my_groups.json"));
        ObjectMapper myGroupMapper = new ObjectMapper();
        var myGroupValue = myGroupMapper.readValue(myGroupJson, new TypeReference<List<GroupData>>() {
        });
        my_groups.addAll(myGroupValue);
        return my_groups;
    }

    @ParameterizedTest
    @MethodSource("myGroupProvider")
    public void canCreateGroup(GroupData my_group) {
        var myOldGroups = my_app.my_jdbc().getMyGroupList();
        my_app.my_groups().createMyGroup(my_group);
        var myNewGroups = my_app.my_jdbc().getMyGroupList();
        Comparator<GroupData> compareById = (o1, o2) -> {
            return Integer.compare(Integer.parseInt(o1.my_id()), Integer.parseInt(o2.my_id()));
        };
        myNewGroups.sort(compareById);
        var myMaxId = myNewGroups.get(myNewGroups.size() - 1).my_id();

        var myExpectedList = new ArrayList<>(myOldGroups);
        myExpectedList.add(my_group.withId(myMaxId));
        myExpectedList.sort(compareById);
        Assertions.assertEquals(myNewGroups, myExpectedList);
    }
}
