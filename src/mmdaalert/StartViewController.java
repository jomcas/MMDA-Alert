/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mmdaalert;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author jomar
 */
public class StartViewController implements Initializable {

    @FXML
    private Button exitBtn;
    @FXML
    private Button startBtn;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }
    

    @FXML
    private void handleExBtn(ActionEvent event) throws IOException {
        Parent changeToReg = FXMLLoader.load(getClass().getResource("/view/MainForm.fxml"));
        Scene changeRegScene = new Scene(changeToReg);
        Stage mainStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        mainStage.setScene(changeRegScene);
        mainStage.show();
    }

    @FXML
    private void handleStartBtn(ActionEvent event) throws IOException {
        Parent changeToReg = FXMLLoader.load(getClass().getResource("/view/homeView.fxml"));
        Scene changeRegScene = new Scene(changeToReg);
        Stage mainStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        changeRegScene.getStylesheets().add(MmdaAlert.class.getResource("/style/bootstrap3.css").toExternalForm());
        mainStage.setScene(changeRegScene);
        mainStage.centerOnScreen();
        mainStage.sizeToScene();
        mainStage.setTitle("MMDA ALERT");
        mainStage.show();
    }

}
