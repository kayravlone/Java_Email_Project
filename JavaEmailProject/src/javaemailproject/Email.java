//@Author Kayra Cansin Gokmen

package javaemailproject;
import javafx.beans.property.*;

public class Email {

    private final StringProperty sender;
    private final StringProperty subject;
    private final StringProperty date;
    private final BooleanProperty hasAttachment;
    private final StringProperty content;

    public Email(String sender, String subject, String date, boolean hasAttachment, String content) {
        this.sender = new SimpleStringProperty(sender);
        this.subject = new SimpleStringProperty(subject);
        this.date = new SimpleStringProperty(date);
        this.hasAttachment = new SimpleBooleanProperty(hasAttachment);
        this.content = new SimpleStringProperty(content);
    }

    public String getSender() {
        return sender.get();
    }

    public String getSubject() {
        return subject.get();
    }

    public String getDate() {
        return date.get();
    }

    public boolean hasAttachment() {
        return hasAttachment.get();
    }

    public String getContent() {
        return content.get();
    }

    public StringProperty senderProperty() {
        return sender;
    }

    public StringProperty subjectProperty() {
        return subject;
    }

    public StringProperty dateProperty() {
        return date;
    }

    public BooleanProperty hasAttachmentProperty() {
        return hasAttachment;
    }

    public StringProperty contentProperty() {
        return content;
    }
}
