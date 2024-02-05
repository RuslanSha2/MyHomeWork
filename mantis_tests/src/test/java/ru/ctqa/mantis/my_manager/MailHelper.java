package ru.ctqa.mantis.my_manager;

import jakarta.mail.Folder;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Store;
import ru.ctqa.mantis.my_model.MailMessage;

import java.io.IOException;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class MailHelper extends HelperBase {
    public MailHelper(ApplicationManager my_manager) {
        super(my_manager);
    }

    public List<MailMessage> receive(String username, String password, Duration duration) {
        var my_start = System.currentTimeMillis();
        while (System.currentTimeMillis() < my_start + duration.toMillis()) {
            try {
                var my_inbox = getInbox(username, password);
                my_inbox.open(Folder.READ_ONLY);
                var my_messages = my_inbox.getMessages();
                var my_result = Arrays.stream(my_messages)
                        .map(m -> {
                            try {
                                return new MailMessage()
                                        .withFrom(m.getFrom()[0].toString())
                                        .withContent((String) m.getContent());
                            } catch (MessagingException | IOException e) {
                                throw new RuntimeException(e);
                            }
                        })
                        .toList();
                my_inbox.close();
                my_inbox.getStore().close();
                if (my_result.size() > 0) {
                    return my_result;
                }
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        throw new RuntimeException("No mail");
    }

    private static Folder getInbox(String username, String password) {
        try {
            var my_session = Session.getInstance(new Properties());
            Store my_store = my_session.getStore("pop3");
            my_store.connect("localhost", username, password);
            var my_inbox = my_store.getFolder("INBOX");
            return my_inbox;
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
