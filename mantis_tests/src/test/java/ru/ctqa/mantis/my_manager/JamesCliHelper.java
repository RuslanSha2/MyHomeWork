package ru.ctqa.mantis.my_manager;

import org.openqa.selenium.io.CircularOutputStream;
import org.openqa.selenium.os.CommandLine;

public class JamesCliHelper extends HelperBase {
    public JamesCliHelper(ApplicationManager my_manager) {
        super(my_manager);
    }

    public void addUser(String email, String password) {
        CommandLine my_command = new CommandLine(
                "h:/Java/zulu17.46.19-ca-jdk17.0.9-win_x64/bin/java",
                "-cp", "\"james-server-jpa-app.lib/*\"",
                "org.apache.james.cli.ServerCmd",
                "AddUser", email, password);
        my_command.setWorkingDirectory(manager.property("james.workingDir"));
        CircularOutputStream out = new CircularOutputStream();
        my_command.copyOutputTo(out);
        my_command.execute();
        my_command.waitFor();
        System.out.println(out);
    }
}
