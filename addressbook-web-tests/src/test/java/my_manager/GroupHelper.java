package my_manager;

import my_model.GroupData;
import org.openqa.selenium.By;

import java.util.ArrayList;
import java.util.List;

public class GroupHelper extends HelperBase {
    public GroupHelper(ApplicationManager my_manager) {
        super(my_manager);
    }

    public void createMyGroup(GroupData my_group) {
        openMyGroupsPage();
        initMyGroupCreation();
        fillMyGroupForm(my_group);
        submitMyGroupCreation();
        returnToMyGroupsPage();
    }

    public void removeMyGroup(GroupData my_group) {
        openMyGroupsPage();
        selectMyGroup(my_group);
        removeMySelectedGroup();
        returnToMyGroupsPage();
    }

    public void modifyMyGroup(GroupData my_group, GroupData my_modifiedGroup) {
        openMyGroupsPage();
        selectMyGroup(my_group);
        initMyGroupModification();
        fillMyGroupName(my_modifiedGroup);
        submitMyGroupModification();
        returnToMyGroupsPage();
    }

    public void openMyGroupsPage() {
        if (!my_manager.isElementPresent(By.name("new"))) {
            click(By.linkText("groups"));
        }
    }

    public int getMyGroupsCount() {
        openMyGroupsPage();
        return my_manager.my_driver.findElements(By.name("selected[]")).size();
    }

    public List<GroupData> getMyGroupList() {
        openMyGroupsPage();
        var my_groups = new ArrayList<GroupData>();
        var my_spans = my_manager.my_driver.findElements(By.cssSelector("span.group"));
        for (var my_span : my_spans) {
            var my_name = my_span.getText();
            var my_checkbox = my_span.findElement(By.name("selected[]"));
            var my_id = my_checkbox.getAttribute("value");
            my_groups.add(new GroupData().withId(my_id).withName(my_name));
        }
        return my_groups;
    }

    private void initMyGroupCreation() {
        click(By.name("new"));
    }

    private void initMyGroupModification() {
        click(By.name("edit"));
    }

    private void fillMyGroupForm(GroupData my_group) {
        type(By.name("group_name"), my_group.my_name());
        type(By.name("group_header"), my_group.my_header());
        type(By.name("group_footer"), my_group.my_footer());
    }

    private void fillMyGroupName(GroupData my_group) {
        type(By.name("group_name"), my_group.my_name());
    }

    private void submitMyGroupCreation() {
        click(By.name("submit"));
    }

    private void submitMyGroupModification() {
        click(By.name("update"));
    }

    private void returnToMyGroupsPage() {
        click(By.linkText("group page"));
    }

    private void selectMyGroup(GroupData my_group) {
        click(By.cssSelector(String.format("input[value='%s']", my_group.my_id())));
    }

    private void removeMySelectedGroup() {
        click(By.name("delete"));
    }
}
