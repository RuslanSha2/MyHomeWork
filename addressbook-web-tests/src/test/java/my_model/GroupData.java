package my_model;

import my_common.MyCommonFunctions;

public record GroupData(String my_id, String my_name, String my_header, String my_footer) {
    public GroupData() {
        this("", "", "", "");
    }

    public GroupData withId(String my_id) {
        return new GroupData(my_id,
                this.my_name,
                this.my_header,
                this.my_footer);
    }

    public GroupData withName(String my_name) {
        return new GroupData(this.my_id, my_name, this.my_header, this.my_footer);
    }

    public GroupData withHeader(String my_header) {
        return new GroupData(this.my_id, this.my_name, my_header, this.my_footer);
    }

    public GroupData withFooter(String my_footer) {
        return new GroupData(this.my_id, this.my_name, this.my_header, my_footer);
    }

    public GroupData withRandomData(int my_salt) {
        return new GroupData(this.my_id,
                MyCommonFunctions.randomString(my_salt * 3),
                MyCommonFunctions.randomString(my_salt * 3),
                MyCommonFunctions.randomString(my_salt * 3));
    }
}
