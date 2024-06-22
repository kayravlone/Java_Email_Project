//@Author Ahmet Emre Cakmak
package javaemailproject;

import java.io.IOException;
import java.net.URL;
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
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

public class NewHostController implements Initializable {

    @FXML
    private RadioButton imapRB;
    @FXML
    private RadioButton pop3RB;
    @FXML
    private TextField ReceivePortTF;
    @FXML
    private TextField SendPortTF;

    @FXML
    private TextField ToReceiveTF;
    @FXML
    private TextField ToSendTF;
    @FXML
    private TextField UsernameTF;
    @FXML
    private TextField PasswordTF;
    @FXML
    private Button SaveButton;

    private ToggleGroup toggleGroup;
    private EmailAccount emailaccount;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        toggleGroup = new ToggleGroup();

        imapRB.setToggleGroup(toggleGroup);
        pop3RB.setToggleGroup(toggleGroup);

        toggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == imapRB) {
                ReceivePortTF.setText("993");
                ToReceiveTF.setText("imap.gmail.com");
            } else if (newValue == pop3RB) {
                ReceivePortTF.setText("995");
                ToReceiveTF.setText("pop.gmail.com");
            }
            SendPortTF.setText("465");
            ToSendTF.setText("smtp.gmail.com");
        });

        SaveButton.setOnAction(event -> {

            RadioButton selectedRadioButton = (RadioButton) toggleGroup.getSelectedToggle();
            try {
                if (selectedRadioButton == null || UsernameTF.getText().isEmpty() || PasswordTF.getText().isEmpty()) {
                    // Kullanıcı adı, şifre veya protokol seçimi eksikse uyarı göster
                    showAlert("Uyarı", "Lütfen tüm alanları doldurun ve bir protokol seçin.");
                    return;
                }
                if (selectedRadioButton == imapRB) {
                    
                } else if (selectedRadioButton == pop3RB) {
                }

                if (selectedRadioButton == imapRB) {

                    emailaccount = new EmailAccount(ReceivePortTF.getText(), SendPortTF.getText(), ToReceiveTF.getText(), ToSendTF.getText(), UsernameTF.getText(), PasswordTF.getText(), "imaps");

                    ReceiveEmail.remailaccount = emailaccount;
                    ComposeNewController.cemailaccount = emailaccount;

                } else if (selectedRadioButton == pop3RB) {

                    emailaccount = new EmailAccount(ReceivePortTF.getText(), SendPortTF.getText(), ToReceiveTF.getText(), ToSendTF.getText(), UsernameTF.getText(), PasswordTF.getText(), "pop3");

                    ReceiveEmail.remailaccount = emailaccount;
                    ComposeNewController.cemailaccount = emailaccount;

                }
            } catch (Exception e) {
                showAlert("Uyarı", e.getMessage());
            }

            try {
                MainController.hostReceived = UsernameTF.getText();
                URL composeNewUrl = getClass().getResource("Main.fxml");
                FXMLLoader fxmlLoader = new FXMLLoader(composeNewUrl);
                Parent composeNewRoot = fxmlLoader.load();

                Stage composeNewStage = new Stage();
                composeNewStage.setMinWidth(600);
                composeNewStage.setMinHeight(450);
                composeNewStage.setTitle("Main");
                composeNewStage.setScene(new Scene(composeNewRoot));

                Stage currentStage = (Stage) SaveButton.getScene().getWindow();
                currentStage.close();

                composeNewStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void sendHostInfo(EmailAccount emailAccount) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("Main.fxml"));
        Parent root = loader.load();
        MainController mainc = loader.getController();
        mainc.setEmailAccount(emailAccount);
    }
}
