/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mmdaalert;

import javafx.scene.image.Image;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author jomar
 */
public class MmdaAlert extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/view/startView.fxml"));
        //new JMetro(JMetro.Style.LIGHT).applyTheme(root);
        Scene scene = new Scene(root);
        Image icon = new Image("/images/Metro_Manila_Development_Authority_(MMDA).svg.png") {};
        scene.getStylesheets().add(MmdaAlert.class.getResource("/style/bootstrap3.css").toExternalForm());
        stage.setScene(scene);
        stage.getIcons().add(icon);
        stage.setTitle("MMDA ALERT");
        stage.show();   
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        java.net.URL url = ClassLoader.getSystemResource("/images/Metro_Manila_Development_Authority_(MMDA).svg.png");
        launch(args);
    }

}
