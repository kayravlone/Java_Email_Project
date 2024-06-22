//@Author Kayra Cansin Gokmen
package javaemailproject;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import static javaemailproject.ComposeNewController.cemailaccount;
import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeBodyPart;

public class ReceiveEmail {

    static EmailAccount remailaccount;

    public List<Email> retrieveEmails() {
        List<Email> emails = new ArrayList<>();

        Properties props = new Properties();

        props.put("mail.store.protocol", remailaccount.getProtocol());
        props.put("mail.pop3.port", remailaccount.getReceivePort());
        Session session = Session.getInstance(props, null);
        Authenticator a = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(cemailaccount.getUsername(), cemailaccount.getPassword());
            }

        };
        try {
            Store store = session.getStore(remailaccount.getProtocol());
            store.connect(remailaccount.getToReceive(), remailaccount.getUsername(), remailaccount.getPassword());

            Folder inbox = store.getFolder("Inbox");
            inbox.open(Folder.READ_ONLY);

            for (int i = 1; i <= inbox.getMessageCount(); i++) {
                Message message = inbox.getMessage(i);
                Address[] senders = message.getFrom();
                String sender = senders[0].toString();

                Object content = message.getContent();

                String subject = message.getSubject();
                String date = message.getReceivedDate().toString();
                boolean hasAttachment = false;
                String textContent = "";

                if (content instanceof Multipart) {
                    Multipart multipart = (Multipart) content;
                    int partCount = multipart.getCount();

                    for (int j = 0; j < partCount; j++) {
                        BodyPart bodyPart = multipart.getBodyPart(j);

                        if (bodyPart.isMimeType("text/plain")) {
                            textContent = (String) bodyPart.getContent();
                        } else if (Part.ATTACHMENT.equalsIgnoreCase(bodyPart.getDisposition())) {

                            String fileName = bodyPart.getFileName();
                            saveAttachment((MimeBodyPart) bodyPart, fileName);
                            hasAttachment = true;
                        }
                    }
                }

                if (subject != null && !subject.isEmpty()) {
                    emails.add(new Email(sender, subject, date, hasAttachment, textContent));
                } else {
                    emails.add(new Email(sender, "No Subject", date, hasAttachment, textContent));
                }
            }

            inbox.close(false);
            store.close();
        } catch (Exception mex) {
            mex.printStackTrace();
        }
        return emails;
    }

    private void saveAttachment(MimeBodyPart bodyPart, String fileName) throws Exception {
        String saveDirectory = "";

        try {
            String filePath = saveDirectory + fileName;

            bodyPart.saveFile(filePath);

            System.out.println("Ek dosyası kaydedildi: " + filePath);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

}
