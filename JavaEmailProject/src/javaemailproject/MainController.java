//@Author Kayra Cansin Gokmen
package javaemailproject;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class MainController implements Initializable {

    @FXML
    private Button refreshButton;
    @FXML
    private Button ComposeNewButton;
    @FXML
    private Button ReplyButton;
    @FXML
    private Button NewHost;

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
    private Label CurrentHostLabel;
    @FXML
    private TextArea TextArea;

    private EmailAccount selectedEmailAccount;

    private ReceiveEmail emailReceiver;

    static String hostReceived;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        CurrentHostLabel.setText("Current Host: " + hostReceived);
        emailReceiver = new ReceiveEmail();
        NewHost.setOnAction(setOnAction -> {
            try {
                URL newHost = getClass().getResource("NewHost.fxml");
                FXMLLoader fxmlLoader = new FXMLLoader(newHost);
                Parent composeNewRoot = fxmlLoader.load();

                Stage newHostStage = new Stage();
                newHostStage.setMinWidth(600);
                newHostStage.setMinHeight(450);
                newHostStage.setTitle("New Host");
                newHostStage.setScene(new Scene(composeNewRoot));

                Stage currentStage = (Stage) NewHost.getScene().getWindow();
                currentStage.close();

                newHostStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        refreshButton.setOnAction(action -> {
            TableView.getItems().clear();
            SenderNameColumn.setCellValueFactory(cellData -> cellData.getValue().senderProperty());
            SubjectColumn.setCellValueFactory(cellData -> cellData.getValue().subjectProperty());
            DateColumn.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
            AttachmentColumn.setCellValueFactory(cellData -> cellData.getValue().hasAttachmentProperty());

            List<Email> emails = emailReceiver.retrieveEmails();
            TableView.getItems().addAll(emails);

            TableView.getSelectionModel().selectedItemProperty().addListener(
                    (observable, oldValue, newValue) -> showEmailDetails(newValue));
        });
        ComposeNewButton.setOnAction(c -> {
            try {
                URL composeNewUrl = getClass().getResource("ComposeNew.fxml");
                FXMLLoader fxmlLoader = new FXMLLoader(composeNewUrl);
                Parent composeNewRoot = fxmlLoader.load();

                Stage composeNewStage = new Stage();
                composeNewStage.setMinWidth(600);
                composeNewStage.setMinHeight(450);
                composeNewStage.setTitle("Compose New Email");
                composeNewStage.setScene(new Scene(composeNewRoot));

                Stage currentStage = (Stage) ComposeNewButton.getScene().getWindow();
                currentStage.close();

                composeNewStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        TextArea.setEditable(false);
        emailReceiver = new ReceiveEmail();

        SenderNameColumn.setCellValueFactory(cellData -> cellData.getValue().senderProperty());
        SubjectColumn.setCellValueFactory(cellData -> cellData.getValue().subjectProperty());
        DateColumn.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
        AttachmentColumn.setCellValueFactory(cellData -> cellData.getValue().hasAttachmentProperty());

        List<Email> emails = emailReceiver.retrieveEmails();
        TableView.getItems().addAll(emails);

        TableView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showEmailDetails(newValue));
    }

    private void showEmailDetails(Email email) {
        if (email != null) {
            TextArea.setText(email.getContent());
        } else {
            TextArea.setText(""); // Varsayılan değeri belirleyebilirsiniz.
        }
    }

    public void setEmailAccount(EmailAccount emailAccount) {
        selectedEmailAccount = emailAccount;
        System.out.println(selectedEmailAccount.getUsername());

        System.out.println(CurrentHostLabel.getText());
    }

}
