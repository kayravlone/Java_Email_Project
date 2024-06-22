//@Author Ahmet Emre Cakmak
package javaemailproject;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.activation.DataSource;
import javax.mail.*;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class ComposeNewController implements Initializable {

    @FXML
    private Button SendButton;
    @FXML
    private SplitMenuButton SelectYourHostButton;
    @FXML
    private TextField toTF;
    @FXML
    private TextField SubjectTF;
    @FXML
    private Button AttachButton;
    @FXML
    private Button InboxButton;
    @FXML
    private Button SentItemsButton;
    @FXML
    private TableView<Email> TableView;
    @FXML
    private TableColumn<Email, String> SenderNameColumn;
    @FXML
    private TableColumn<Email, String> SubjectColumn;
    @FXML
    private TableColumn<Email, String> DateColumn;
    @FXML
    private TableColumn<Email, Boolean> AttachmentColumn;
    @FXML
    private ScrollBar ScrollBar;

    @FXML
    private Label MyEmailLabel;
    @FXML
    private TextArea TextArea;
    String filePath;
    private ReceiveEmail emailReceiver;

    static EmailAccount cemailaccount;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        AttachButton.setOnAction(c -> {
            FileChooser fileChooser = new FileChooser();
            File selectedFile = fileChooser.showOpenDialog(((Button) c.getSource()).getScene().getWindow());

            if (selectedFile != null) {

                System.out.println("Selected File: " + selectedFile.getAbsolutePath());
                filePath = selectedFile.getAbsolutePath();
                AttachButton.setText(selectedFile.getName().substring(0, Math.min(selectedFile.getName().length(), 7)));

            } else {
                System.out.println("File selection cancelled.");
            }
        });
        MyEmailLabel.setOnMouseClicked(click -> {
            try {
                URL composeNewUrl = getClass().getResource("Main.fxml");
                FXMLLoader fxmlLoader = new FXMLLoader(composeNewUrl);
                Parent composeNewRoot = fxmlLoader.load();

                Stage composeNewStage = new Stage();
                composeNewStage.setMinWidth(600);
                composeNewStage.setMinHeight(450);
                composeNewStage.setTitle("Email");
                composeNewStage.setScene(new Scene(composeNewRoot));
                composeNewStage.show();
                Stage currentStage = (Stage) MyEmailLabel.getScene().getWindow();
                currentStage.close();
            } catch (IOException ex) {
                Logger.getLogger(ComposeNewController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        SendButton.setOnAction(b -> {
            if (TextArea.getText().isEmpty() || toTF.getText().isEmpty()) {
                showErrorMessage("Error", "Lütfen alıcı ve mesaj girin.");
                return; // Gönderme işlemine devam etme
            }
            Properties props = new Properties();
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "465");
            props.put("mail.smtp.socketFactory.port", "465");
            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            props.put("mail.smtp.auth", "true");
            props.put("mail.from", cemailaccount.getUsername());

            Authenticator a = new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(cemailaccount.getUsername(), cemailaccount.getPassword());
                }

            };

            try {

                URL composeNewUrl = getClass().getResource("Main.fxml");
                FXMLLoader fxmlLoader = new FXMLLoader(composeNewUrl);
                Parent composeNewRoot = fxmlLoader.load();

                Stage composeNewStage = new Stage();
                composeNewStage.setMinWidth(600);
                composeNewStage.setMinHeight(450);
                composeNewStage.setTitle("Email");
                composeNewStage.setScene(new Scene(composeNewRoot));

                Stage currentStage = (Stage) SendButton.getScene().getWindow();
                currentStage.close();

                composeNewStage.show();
                showSuccessMessage("Success", "Mesaj başarıyla gönderildi!");
            } catch (IOException e) {
                e.printStackTrace();
            }
            Session session = Session.getDefaultInstance(props, a);

            try {
                MimeMessage msg = new MimeMessage(session);
                msg.setFrom();
                msg.setRecipients(Message.RecipientType.TO, toTF.getText());
                msg.setSubject(SubjectTF.getText());
                msg.setSentDate(new Date());
                Multipart mp = new MimeMultipart();
                BodyPart bp = new MimeBodyPart();
                bp.setText(TextArea.getText());
                mp.addBodyPart(bp);

                if (filePath != null && !filePath.isEmpty()) {
                    BodyPart partForAtt = new MimeBodyPart();
                    File file = new File(filePath);
                    DataSource source = new FileDataSource(file);
                    partForAtt.setDataHandler(new DataHandler(source));
                    partForAtt.setFileName(file.getName());
                    mp.addBodyPart(partForAtt);
                }

                msg.setContent(mp);

                Transport.send(msg);
                System.out.println("E-mail sent!");

            } catch (MessagingException mex) {
                System.out.println("Send failed, exception: " + mex);
            }
        });
        emailReceiver = new ReceiveEmail();
        TableView.getItems().clear();
        SenderNameColumn.setCellValueFactory(cellData -> cellData.getValue().senderProperty());
        SubjectColumn.setCellValueFactory(cellData -> cellData.getValue().subjectProperty());
        DateColumn.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
        AttachmentColumn.setCellValueFactory(cellData -> cellData.getValue().hasAttachmentProperty());

        List<Email> emails = emailReceiver.retrieveEmails();
        TableView.getItems().addAll(emails);

        TableView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    showSenderMail(newValue);
                    if (newValue != null) {

                        toTF.setText(newValue.getSender());
                    }
                });
    }

    private void showSenderMail(Email email) {
        toTF.setText(email.getSender());
        SubjectTF.setText(email.getSubject());

    }

    private void showEmailDetails(Email email) {
        TextArea.setText(email.getContent());
    }

    private void showErrorMessage(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void showSuccessMessage(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
