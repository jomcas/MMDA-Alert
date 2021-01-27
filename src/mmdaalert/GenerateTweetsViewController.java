package mmdaalert;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javax.swing.JOptionPane;
import model.Constants;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

/**
 * FXML Controller class
 *
 * @author jomar
 */
public class GenerateTweetsViewController implements Initializable {

    List<String> tweetArray = new ArrayList<>();
    TwitterFactory tf;
    ConfigurationBuilder cb;
    @FXML
    private Button genBtn;
    @FXML
    private Button exportBtn;
    @FXML
    private Button backBtn;
    @FXML
    private TextArea genTweetsTxtArea;
    @FXML
    private TextField tweetsNumberTf;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void handleGenBtn(ActionEvent event) throws TwitterException {
        cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey(Constants.consumerKey)
                .setOAuthConsumerSecret(Constants.consumerSecret)
                .setOAuthAccessToken(Constants.accessToken)
                .setOAuthAccessTokenSecret(Constants.accessTokenSecret);
        tf = new TwitterFactory(cb.build());
        Twitter twitter = tf.getInstance();
        List<Status> status = twitter.getUserTimeline();
        int tweetCount = Integer.parseInt(tweetsNumberTf.getText());
        Query query = new Query("MMDA ALERT");
        Date date = new Date();

        String modifiedDate = new SimpleDateFormat("yyyymmdd").format(date);

        query.setSince(modifiedDate);
        query.count(10);

        QueryResult result;

        for (int i = 0; tweetArray.size() < tweetCount;) {
            result = twitter.search(query);
            List<Status> tweets = result.getTweets();
            for (Status tweet : tweets) {
                if (tweet.getText().contains("MMDA ALERT:") && tweet.getUser().getScreenName().equals("MMDA")) {
                    System.out.println("@" + tweet.getUser().getScreenName() + "|" + tweet.getText() + "|" + tweet.isRetweeted());
                    tweetArray.add(tweet.getText());
                }
            }
            if (tweetArray.size() >= tweetCount) {
                for (int j = 0; j < tweetArray.size() - 1; j++) {
                    genTweetsTxtArea.appendText(tweetArray.get(j) + "\n" + "\n");
                }
            }
        }

    }

    @FXML
    private void handleExportBtn(ActionEvent event) throws IOException {
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String date = simpleDateFormat.format(new Date());
        if (tweetArray.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No Generated Tweets Yet!");
        } else {
            File file = new File(Constants.genPath + date + ".txt");
            file.createNewFile();
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
                for (int i = 0; i < tweetArray.size(); i++) {
                    writer.append(tweetArray.get(i));
                    writer.newLine();
                }
                JOptionPane.showMessageDialog(null, "Text File Created!");
                writer.close();
            } catch (Exception e) {
                System.out.println("Error");
            }
        }

    }

    @FXML
    private void handleBackBtn(ActionEvent event) throws IOException {
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
