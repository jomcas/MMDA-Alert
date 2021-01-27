/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mmdaalert;

import model.Vehicles;
import model.Tweets;
import model.Accidents;
import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import javax.swing.JOptionPane;
import twitter4j.FilterQuery;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;
import model.Constants;
import twitter4j.TwitterFactory;
import twitter4j.conf.Configuration;

/**
 * FXML Controller class
 *
 * @author jomar
 */
public class HomeViewController implements Initializable {

    /**
     * Initializes the controller class.
     */
    ArrayList<String> tweets = new ArrayList<>();
    ArrayList<String> accidents = new ArrayList<>();
    ArrayList<String> vehicles = new ArrayList<>();
    ArrayList<String> time = new ArrayList<>();
    ArrayList<String> bound = new ArrayList<>();
    ArrayList<String> places = new ArrayList<>();
    ArrayList<String> notes = new ArrayList<>();
    TwitterStream twitterStream;
    TwitterFactory tf;
    StatusListener listener;
    ConfigurationBuilder cb;

    @FXML
    private Tab overviewTab;
    @FXML
    private Label tweetsNumberLbl;
    @FXML
    private TableView<Accidents> accidentsCountTable;
    @FXML
    private TableColumn<Accidents, String> accidentsColumn;
    @FXML
    private TableColumn<Accidents, String> accidentsCountColumn;
    @FXML
    private TableView<Vehicles> vehicleCountTable;
    @FXML
    private TableColumn<Vehicles, String> vehiclesColumn;
    @FXML
    private TableColumn<Vehicles, String> vehiclesCountColumn;
    @FXML
    private Tab barGraphTab;
    @FXML
    private Tab lineGraphTab;
    @FXML
    private Tab pieChartTab;
    @FXML
    private Tab tweetsTab;
    @FXML
    private ListView tweetsList;
    @FXML
    private Button addFileBtn;
    @FXML
    private Button refreshBtn;
    @FXML
    private Button followBtn;
    @FXML
    private Button exitBtn;
    @FXML
    private Label timeLbl;
    @FXML
    private Label dateLbl;
    @FXML
    private Label latestTweetLbl;
    @FXML
    private CheckBox streamChkBox;
    @FXML
    private LineChart<String, Number> vehicleToAccidentsLine;
    @FXML
    private NumberAxis line1Y;
    @FXML
    private CategoryAxis line1X;
    @FXML
    private NumberAxis line1Y1;
    @FXML
    private CategoryAxis line1X1;
    @FXML
    private LineChart<String, Number> vehicleToAccidentsPercentLine;
    @FXML
    private PieChart accidentPie;
    @FXML
    private PieChart accidentAMPie;
    @FXML
    private PieChart accidentPMPie;
    @FXML
    private TableView<Tweets> tweetsCategoriesTbl;
    @FXML
    private TableColumn<Tweets, String> accidentTweetCol;
    @FXML
    private TableColumn<Tweets, String> placeTweetCol;
    @FXML
    private TableColumn<Tweets, String> boundTweetCol;
    @FXML
    private TableColumn<Tweets, String> vehicleTweetCol;
    @FXML
    private TableColumn<Tweets, String> timeTweetCol;
    @FXML
    private TableColumn<Tweets, String> noteTweetCol;
    @FXML
    private BarChart<String, Number> timeBar;
    @FXML
    private BarChart<String, Number> boundBar;
    @FXML
    private BarChart<String, Number> laneBar;
    @FXML
    private Label accidentPieLbl;
    @FXML
    private AnchorPane accidentPiePane;
    @FXML
    private Label accidentPieLAMbl;
    @FXML
    private Label accidentPiePMLbl;
    @FXML
    private Label streamLbl;
    @FXML
    private Button genTweetsBtn;
    @FXML
    private LineChart<String, Number> accidentTimeAMLine;
    @FXML
    private NumberAxis line1Y11;
    @FXML
    private CategoryAxis line1X11;
    @FXML
    private LineChart<String, Number> accidentTimePMLine;
    @FXML
    private NumberAxis line1Y111;
    @FXML
    private CategoryAxis line1X111;
    @FXML
    private Button clearBtn;
    @FXML
    private Button backupBtn;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.getTweets();
        if (!tweets.isEmpty()) {
            setup();
        }
    }

    public void setup() {
        //Resetting the arrays 
        tweets.clear();
        accidents.clear();
        vehicles.clear();
        time.clear();
        bound.clear();
        notes.clear();
        timeBar.getData().clear();
        boundBar.getData().clear();
        laneBar.getData().clear();
        vehicleToAccidentsLine.getData().clear();
        vehicleToAccidentsPercentLine.getData().clear();
        accidentTimeAMLine.getData().clear();
        accidentTimePMLine.getData().clear();

        //Storing the data in arrayList
        this.getTweets();
        this.getAccidents();
        this.getPlace();
        this.getBound();
        this.getVehicles();
        this.getTime();
        this.getNote();
        
        for(int i = 0; i < vehicles.size(); i++) {
            System.out.println(vehicles.get(i));
        }

        // Overview accident Table Setup
        accidentsColumn.setCellValueFactory(new PropertyValueFactory<Accidents, String>("category"));
        accidentsCountColumn.setCellValueFactory(new PropertyValueFactory<Accidents, String>("count"));
        accidentsCountColumn.setSortType(TableColumn.SortType.DESCENDING);
        accidentsCountTable.setItems(this.populateAccidentsTable());
        accidentsCountTable.getSortOrder().add(accidentsCountColumn);
        accidentsCountTable.setEditable(false);

        // Overview vehicle Table Setup
        vehiclesColumn.setCellValueFactory(new PropertyValueFactory<Vehicles, String>("category"));
        vehiclesCountColumn.setCellValueFactory(new PropertyValueFactory<Vehicles, String>("count"));
        vehiclesCountColumn.setSortType(TableColumn.SortType.DESCENDING);
        vehicleCountTable.setItems(this.populateVehiclesTable());
        vehicleCountTable.getSortOrder().add(vehiclesCountColumn);
        vehicleCountTable.setEditable(false);

        // Tweet Categories
        accidentTweetCol.setCellValueFactory(new PropertyValueFactory<Tweets, String>("accident"));
        placeTweetCol.setCellValueFactory(new PropertyValueFactory<Tweets, String>("place"));
        boundTweetCol.setCellValueFactory(new PropertyValueFactory<Tweets, String>("bound"));
        vehicleTweetCol.setCellValueFactory(new PropertyValueFactory<Tweets, String>("vehicle"));
        timeTweetCol.setCellValueFactory(new PropertyValueFactory<Tweets, String>("time"));
        noteTweetCol.setCellValueFactory(new PropertyValueFactory<Tweets, String>("note"));
        tweetsCategoriesTbl.setItems(this.populateTweetCategories());
        tweetsCategoriesTbl.setEditable(false);

        // Overview number of tweets label
        tweetsList.autosize();
        tweetsNumberLbl.setText(tweets.size() + "");
        latestTweetLbl.setText(tweets.get(tweets.size() - 1).substring(12));

        // Overview real time clock
        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            int second = LocalDateTime.now().getSecond();
            int minute = LocalDateTime.now().getMinute();
            int hour = LocalDateTime.now().getHour();
            String month = LocalDateTime.now().getMonth().toString();
            int day = LocalDateTime.now().getDayOfMonth();
            int year = LocalDateTime.now().getYear();
            timeLbl.setText(hour + ":" + (minute) + ":" + second);
            dateLbl.setText(month + " " + day + ", " + year);
        }),
                new KeyFrame(Duration.seconds(1))
        );
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();

        // Populate Chart
        this.populateTweetList();
        this.populateTimeBar();
        this.populateBoundBar();
        this.populateLaneBar();
        this.populateLine1();
        this.populateLine2();
        this.populateLine3();
        this.populateLine4();
        this.populateAccidentPie();
        this.populateAccidentAMPie();
        this.populateAccidentPMPie();

        cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey(Constants.consumerKey)
                .setOAuthConsumerSecret(Constants.consumerSecret)
                .setOAuthAccessToken(Constants.accessToken)
                .setOAuthAccessTokenSecret(Constants.accessTokenSecret);

        Configuration config = cb.build();

        listener = new StatusListener() {

            public void onStatus(Status status) {
                String text = status.getText();
                System.out.println(status.getUser().getName() + " : " + text);
                File file = new File(Constants.path);
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
                    if (text.contains("MMDA ALERT")) {
                        for (int i = 0; i < Constants.accidentCategories.length; i++) {
                            if (text.contains(Constants.accidentCategories[i])) {
                                writer.newLine();
                                writer.append(text);
                                System.out.println("nangyayari");
                            }
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Error");
                }
            }

            @Override
            public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
            }

            @Override
            public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
            }

            @Override
            public void onException(Exception ex) {
                ex.printStackTrace();
            }

            @Override
            public void onScrubGeo(long arg0, long arg1) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onStallWarning(StallWarning arg0) {
                // TODO Auto-generated method stub

            }
        };

        twitterStream = new TwitterStreamFactory(config).getInstance();
        tf = new TwitterFactory(config);

    }

    public void getTweets() {
        try (BufferedReader reader = new BufferedReader(new FileReader(Constants.path))) {
            String line = "";

            while ((line = reader.readLine()) != null) {
                tweets.add(line);
            }

        } catch (Exception e) {
            System.out.println("Error");
            System.out.println(e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error");
            alert.setContentText("Invalid File");

            alert.showAndWait();
        }
    }

    public void getAccidents() {
        // between ":" and "at"
        Pattern pattern = Pattern.compile("(?<=\\: )(.*?)(?=at)");

        for (int i = 0; i < tweets.size(); i++) {
            Matcher matcher = pattern.matcher(tweets.get(i));

            if (matcher.find()) {
                String[] matchArr = matcher.group().split(" ");
                if (matchArr[0].equals("Stalled")) {
                    accidents.add("Stall");
                } else {
                    accidents.add(matcher.group());
                }
            } else {
                accidents.add("Unknown");
            }
        }
    }

    public void getVehicles() {
        // between "involving" and "as of"
        Pattern pattern = Pattern.compile("(?<=involving )(.*?)(?=as of)");
        Pattern stallPattern = Pattern.compile("(?<=Stalled )(.*?)(?=due)");

        for (int i = 0; i < tweets.size(); i++) {
            Matcher matcher = pattern.matcher(tweets.get(i));
            Matcher Stallmatcher = stallPattern.matcher(tweets.get(i));
            if (accidents.get(i).equals("Stall")) {
                if (Stallmatcher.find()) {
                    vehicles.add(Stallmatcher.group());
                } else {
                    vehicles.add("Unknown");
                }
            } else if (matcher.find()) {
                vehicles.add(matcher.group());
            } else {
                vehicles.add("Unknown");
            }
        }

    }

    public void getPlace() {
        Pattern pattern = Pattern.compile("(?<=at )(.*?)(?=involving)");
        Pattern stallPattern = Pattern.compile("(?<= at )(.*?)(?=as of)");

        for (int i = 0; i < tweets.size(); i++) {
            Matcher matcher = pattern.matcher(tweets.get(i));
            Matcher Stallmatcher = stallPattern.matcher(tweets.get(i));
            if (accidents.get(i).equals("Stall")) {
                if (Stallmatcher.find()) {
                    places.add(Stallmatcher.group());
                } else {
                    places.add("Unknown");
                }
            } else if (matcher.find()) {
                places.add(matcher.group());
            } else {
                places.add("Unknown");
            }
        }
    }

    public void getBound() {
        for (int i = 0; i < places.size(); i++) {
            if (places.get(i).contains("NB")) {
                bound.add("North");
            } else if (places.get(i).contains("SB")) {
                bound.add("South");
            } else if (places.get(i).contains("WB")) {
                bound.add("West");
            } else if (places.get(i).contains("EB")) {
                bound.add("East");
            } else {
                bound.add("Unknown");
            }
        }
    }

    public void getTime() {

        // between "as of" and "."
        Pattern pattern = Pattern.compile("(?<=as of )(.*?)(?=\\.)");

        for (int i = 0; i < tweets.size(); i++) {
            Matcher matcher = pattern.matcher(tweets.get(i));
            if (matcher.find()) {
                time.add(matcher.group());
            } else {
                time.add("Unknown");
            }
        }

    }

    public void getNote() {
        Pattern pattern = Pattern.compile("(?<=M\\. )(.*)(?=\\.)");

        for (int i = 0; i < tweets.size(); i++) {
            Matcher matcher = pattern.matcher(tweets.get(i));
            if (matcher.find()) {
                notes.add(matcher.group());
            } else {
                notes.add("Unknown");
            }
        }
    }

    public void populateTweetList() {
        for (int i = 0; i < tweets.size(); i++) {
            tweetsList.getItems().add(tweets.get(i));
        }
    }

    public ObservableList<Accidents> populateAccidentsTable() {
        int[] accidentCount = new int[Constants.accidentCategories.length];

        for (int i = 0; i < accidentCount.length; i++) {
            accidentCount[i] = 0;
        }

        for (int i = 0; i < accidents.size(); i++) {
            for (int j = 0; j < Constants.accidentCategories.length; j++) {
                if (accidents.get(i).equalsIgnoreCase(Constants.accidentCategories[j])) {
                    accidentCount[j]++;
                }
            }
        }

        ObservableList<Accidents> accidentsList = FXCollections.observableArrayList();

        for (int i = 0; i < Constants.accidentCategories.length; i++) {
            accidentsList.add(new Accidents(Constants.accidentCategories[i], accidentCount[i]));
        }

        return accidentsList;

    }

    public ObservableList<Vehicles> populateVehiclesTable() {
        int[] vehicleCount = new int[Constants.vehicleCategories.length];

        for (int i = 0; i < vehicleCount.length; i++) {
            vehicleCount[i] = 0;
        }

        for (int i = 0; i < vehicles.size(); i++) {
            for (int j = 0; j < Constants.vehicleCategories.length; j++) {
                if (vehicles.get(i).toLowerCase().contains(Constants.vehicleCategories[j].toLowerCase())) {
                    vehicleCount[j]++;
                }
            }
        }

        ObservableList<Vehicles> vehiclesList = FXCollections.observableArrayList();

        for (int i = 0; i < Constants.accidentCategories.length; i++) {
            vehiclesList.add(new Vehicles(Constants.vehicleCategories[i], vehicleCount[i]));
        }

        return vehiclesList;

    }

    public void populateTimeBar() {
        XYChart.Series AM = new XYChart.Series<>();
        XYChart.Series PM = new XYChart.Series<>();
        AM.setName("Morning (AM)");
        PM.setName("Evening (PM)");

        for (int i = 0; i < Constants.accidentCategories.length; i++) {
            AM.getData().add(new XYChart.Data(Constants.accidentCategories[i], getTimeAMCount()[i] * 100));
        }

        for (int i = 0; i < Constants.accidentCategories.length; i++) {
            PM.getData().add(new XYChart.Data(Constants.accidentCategories[i], getTimePMCount()[i] * 100));
        }

        timeBar.getData().addAll(AM, PM);
    }

    public void populateBoundBar() {

        String[] boundCategories = {"NB", "SB", "EB", "WB"};
        int[][] boundCount = new int[4][Constants.accidentCategories.length];

        for (int i = 0; i < boundCount.length; i++) {
            for (int j = 0; j < boundCount[0].length; j++) {
                boundCount[i][j] = 0;
            }
        }

        for (int i = 0; i < accidents.size(); i++) {
            for (int j = 0; j < Constants.accidentCategories.length; j++) {
                if (accidents.get(i).equalsIgnoreCase(Constants.accidentCategories[j])) {
                    for (int k = 0; k < boundCategories.length; k++) {
                        if (places.get(i).contains(boundCategories[k])) {
                            boundCount[k][j]++;
                        }
                    }
                }
            }
        }

        for (int i = 0; i < boundCategories.length; i++) {
            XYChart.Series bound = new XYChart.Series<>();
            if (i == 0) {
                bound.setName("North");
            } else if (i == 1) {
                bound.setName("South");
            } else if (i == 2) {
                bound.setName("East");
            } else if (i == 3) {
                bound.setName("West");
            }
            for (int j = 0; j < Constants.accidentCategories.length; j++) {
                bound.getData().add(new XYChart.Data(Constants.accidentCategories[j], boundCount[i][j]));
            }
            boundBar.getData().add(bound);
        }

    }

    public void populateLaneBar() {
        int[][] laneCount = new int[2][Constants.accidentCategories.length];

        for (int i = 0; i < laneCount.length; i++) {
            for (int j = 0; j < laneCount[0].length; j++) {
                laneCount[i][j] = 0;
            }
        }

        for (int i = 0; i < accidents.size(); i++) {
            for (int j = 0; j < Constants.accidentCategories.length; j++) {
                if (accidents.get(i).equalsIgnoreCase(Constants.accidentCategories[j])) {
                    if (notes.get(i).contains("1")) {
                        laneCount[0][j]++;
                    } else if (notes.get(i).contains("2")) {
                        laneCount[1][j]++;
                    }
                }
            }
        }

        for (int i = 0; i < laneCount.length; i++) {
            XYChart.Series lane = new XYChart.Series<>();
            if (i == 0) {
                lane.setName("1 Lane Occupied");
            } else if (i == 1) {
                lane.setName("2 Lane Occupied");
            }
            for (int j = 0; j < Constants.accidentCategories.length; j++) {
                lane.getData().add(new XYChart.Data(Constants.accidentCategories[j], laneCount[i][j]));
            }
            laneBar.getData().add(lane);
        }

    }

    public ObservableList<Tweets> populateTweetCategories() {
        ObservableList<Tweets> tweetCategoriesList = FXCollections.observableArrayList();

        for (int i = 0; i < tweets.size(); i++) {
            tweetCategoriesList.add(new Tweets(accidents.get(i), places.get(i), bound.get(i), vehicles.get(i), time.get(i), notes.get(i)));
        }

        return tweetCategoriesList;
    }

    public double[][] getVehicleAccidentCorrelation() {
        // Correlation between the accidents and the type of vehicles involved

        double[][] vehicleAccidents = new double[Constants.vehicleCategories.length][Constants.accidentCategories.length];

        for (int i = 0; i < vehicleAccidents.length; i++) {
            for (int j = 0; j < vehicleAccidents[0].length; j++) {
                vehicleAccidents[i][j] = 0;
            }
        }

        for (int i = 0; i < accidents.size(); i++) {
            for (int j = 0; j < Constants.accidentCategories.length; j++) {
                if (accidents.get(i).equalsIgnoreCase(Constants.accidentCategories[j])) {
                    for (int k = 0; k < Constants.vehicleCategories.length; k++) {
                        if (vehicles.get(i).toLowerCase().contains(Constants.vehicleCategories[k].toLowerCase())) {
                            vehicleAccidents[k][j]++;
                        }
                    }
                }
            }
        }

        return vehicleAccidents;
    }

    public double[][] getVehicleAccidentCorrelationPercentage() {
        double[][] vehicleAccidentPercentage = new double[Constants.vehicleCategories.length][Constants.accidentCategories.length];
        double[] accidentTotal = new double[Constants.accidentCategories.length];

        for (int i = 0; i < accidentTotal.length; i++) {
            accidentTotal[i] = 0;
        }

        // Get the total no of accidents per all vehicles (get all i of j(0))
        for (int i = 0; i < vehicleAccidentPercentage[0].length; i++) {
            for (int j = 0; j < vehicleAccidentPercentage.length; j++) {
                accidentTotal[i] += getVehicleAccidentCorrelation()[j][i];
            }
        }

        for (int i = 0; i < vehicleAccidentPercentage.length; i++) {
            for (int j = 0; j < vehicleAccidentPercentage[0].length; j++) {
                vehicleAccidentPercentage[i][j] = getVehicleAccidentCorrelation()[i][j] / accidentTotal[j];
                
            }
        }

        return vehicleAccidentPercentage;
    }

    public double[][] getAccidentTimeAMLine() {
        double[][] accidentTime = new double[Constants.accidentCategories.length][12];

        for (int i = 0; i < accidentTime.length; i++) {
            for (int j = 0; j < accidentTime[0].length; j++) {
                accidentTime[i][j] = 0;
            }
        }
        for (int i = 0; i < tweets.size(); i++) {
            for (int j = 0; j < Constants.accidentCategories.length; j++) {
                for (int k = 0; k < 12; k++) {
                    if (time.get(i).charAt(1) == ':') {
                        if (accidents.get(i).equalsIgnoreCase(Constants.accidentCategories[j]) && time.get(i).contains("AM") && (time.get(i).charAt(0) + "").equals((k + 1) + "")) {
                            accidentTime[j][k]++;
                        }
                    } else if (accidents.get(i).equalsIgnoreCase(Constants.accidentCategories[j]) && time.get(i).contains("AM") && (time.get(i).substring(0, 2)).equals((k + 1) + "")) {
                        accidentTime[j][k]++;
                    }
                }
            }
        }
        return accidentTime;
    }

    public double[][] getAccidentTimePMLine() {
        double[][] accidentTime = new double[Constants.accidentCategories.length][12];

        for (int i = 0; i < accidentTime.length; i++) {
            for (int j = 0; j < accidentTime[0].length; j++) {
                accidentTime[i][j] = 0;
            }
        }
        for (int i = 0; i < tweets.size(); i++) {
            for (int j = 0; j < Constants.accidentCategories.length; j++) {
                for (int k = 0; k < 12; k++) {
                    if (time.get(i).charAt(1) == ':') {
                        if (accidents.get(i).equalsIgnoreCase(Constants.accidentCategories[j]) && time.get(i).contains("PM") && (time.get(i).charAt(0) + "").equals((k + 1) + "")) {
                            accidentTime[j][k]++;
                        }
                    } else if (accidents.get(i).equalsIgnoreCase(Constants.accidentCategories[j]) && time.get(i).contains("PM") && (time.get(i).substring(0, 2)).equals((k + 1) + "")) {
                        accidentTime[j][k]++;
                    }
                }
            }
        }
        return accidentTime;
    }

    public void populateLine1() {

        for (int i = 0; i < getVehicleAccidentCorrelation().length; i++) {
            XYChart.Series vehicleToAccidentData = new XYChart.Series<>();
            vehicleToAccidentData.setName(Constants.vehicleCategories[i]);

            for (int j = 0; j < getVehicleAccidentCorrelation()[i].length; j++) {
                vehicleToAccidentData.getData().add(new XYChart.Data(Constants.accidentCategories[j], getVehicleAccidentCorrelation()[i][j]));
            }
            vehicleToAccidentsLine.getData().add(vehicleToAccidentData);

        }

    }

    public void populateLine2() {
        for (int i = 0; i < getVehicleAccidentCorrelationPercentage().length; i++) {
            XYChart.Series vehicleToAccidentData = new XYChart.Series<>();
            vehicleToAccidentData.setName(Constants.vehicleCategories[i]);
            for (int j = 0; j < getVehicleAccidentCorrelationPercentage()[i].length; j++) {
                vehicleToAccidentData.getData().add(new XYChart.Data(Constants.accidentCategories[j], getVehicleAccidentCorrelationPercentage()[i][j]));
            }
            vehicleToAccidentsPercentLine.getData().add(vehicleToAccidentData);
        }
    }

    public void populateLine3() {
        System.out.println(Arrays.deepToString(getAccidentTimeAMLine()));
        for (int i = 0; i < getAccidentTimeAMLine().length; i++) {
            XYChart.Series vehicleToAccidentData = new XYChart.Series<>();
            vehicleToAccidentData.setName(Constants.accidentCategories[i]);
            
            vehicleToAccidentData.getData().add(new XYChart.Data("12 AM", getAccidentTimeAMLine()[i][11]));
            
            for (int j = 0; j < getAccidentTimeAMLine()[i].length - 1; j++) {
                vehicleToAccidentData.getData().add(new XYChart.Data((j + 1) + " AM", getAccidentTimeAMLine()[i][j]));
            }

            accidentTimeAMLine.getData().add(vehicleToAccidentData);

        }
    }

    public void populateLine4() {
        for (int i = 0; i < getAccidentTimePMLine().length; i++) {
            XYChart.Series vehicleToAccidentData = new XYChart.Series<>();
            vehicleToAccidentData.setName(Constants.accidentCategories[i]);

            vehicleToAccidentData.getData().add(new XYChart.Data("12 PM", getAccidentTimePMLine()[i][11]));
            System.out.println(Arrays.deepToString(getAccidentTimePMLine()));
            for (int j = 0; j < getAccidentTimePMLine()[i].length-1 ; j++) {
                vehicleToAccidentData.getData().add(new XYChart.Data((j + 1) + " PM", getAccidentTimePMLine()[i][j]));
            }
            accidentTimePMLine.getData().add(vehicleToAccidentData);

        }

    }

    public double[] getAccidentCount() {
        double[] accidentCount = new double[Constants.accidentCategories.length];
        double[] accidentCountPercentage = new double[Constants.accidentCategories.length];
        for (int i = 0; i < accidentCount.length; i++) {
            accidentCount[i] = 0;
        }

        for (int i = 0; i < accidents.size(); i++) {
            for (int j = 0; j < Constants.accidentCategories.length; j++) {
                if (accidents.get(i).equalsIgnoreCase(Constants.accidentCategories[j])) {
                    accidentCount[j]++;
                }
            }
        }

        for (int i = 0; i < accidentCountPercentage.length; i++) {
            accidentCountPercentage[i] = accidentCount[i] / accidents.size();
        }

        return accidentCountPercentage;
    }

    public void populateAccidentPie() {
        ObservableList<PieChart.Data> accidentPieDate = FXCollections.observableArrayList();

        for (int i = 0; i < getAccidentCount().length; i++) {
            accidentPieDate.add(new PieChart.Data(Constants.accidentCategories[i], getAccidentCount()[i]));
        }
        accidentPie.setData(accidentPieDate);
        accidentPie.setLabelsVisible(true);
        accidentPie.setLegendSide(Side.RIGHT);

        accidentPie.getData().stream().forEach((data) -> {
            data.getNode().addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {;
                accidentPieLbl.setText(String.valueOf(data.getName() + ": " + Math.round(data.getPieValue() * 100.0) + "%"));
            });

        });

    }

    public double[] getTimeAMCount() {
        double[] timeAMCount = new double[Constants.accidentCategories.length];
        double[] timeAMCountPercentage = new double[Constants.accidentCategories.length];

        for (int i = 0; i < timeAMCount.length; i++) {
            timeAMCount[i] = 0;
        }

        for (int i = 0; i < accidents.size(); i++) {
            String[] temp = time.get(i).split(" ");
            if (temp[temp.length - 1].equals("AM")) {
                for (int j = 0; j < Constants.accidentCategories.length; j++) {
                    if (accidents.get(i).equalsIgnoreCase(Constants.accidentCategories[j])) {
                        timeAMCount[j]++;
                    }
                }
            }
        }

        for (int i = 0; i < timeAMCountPercentage.length; i++) {
            timeAMCountPercentage[i] = timeAMCount[i] / accidents.size();
        }

        return timeAMCountPercentage;
    }

    public void populateAccidentAMPie() {
        ObservableList<PieChart.Data> accidentPieDate = FXCollections.observableArrayList();

        for (int i = 0; i < getTimeAMCount().length; i++) {
            accidentPieDate.add(new PieChart.Data(Constants.accidentCategories[i], getTimeAMCount()[i]));
        }
        accidentAMPie.setData(accidentPieDate);

        accidentAMPie.setLegendSide(Side.RIGHT);

        accidentAMPie.getData().stream().forEach((data) -> {
            data.getNode().addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {;
                accidentPieLAMbl.setText(String.valueOf(data.getName() + ": " + Math.round(data.getPieValue() * 100.0) + "%"));
            });

        });
    }

    public double[] getTimePMCount() {
        double[] timePMCount = new double[Constants.accidentCategories.length];
        double[] timePMCountPercentage = new double[Constants.accidentCategories.length];

        for (int i = 0; i < timePMCount.length; i++) {
            timePMCount[i] = 0;
        }

        for (int i = 0; i < accidents.size(); i++) {
            String[] temp = time.get(i).split(" ");
            if (temp[temp.length - 1].equals("PM")) {
                for (int j = 0; j < Constants.accidentCategories.length; j++) {
                    if (accidents.get(i).equalsIgnoreCase(Constants.accidentCategories[j])) {
                        timePMCount[j]++;
                    }
                }
            }
        }

        for (int i = 0; i < timePMCountPercentage.length; i++) {
            timePMCountPercentage[i] = timePMCount[i] / accidents.size();
        }

        return timePMCountPercentage;
    }

    public void populateAccidentPMPie() {
        ObservableList<PieChart.Data> accidentPieDate = FXCollections.observableArrayList();

        for (int i = 0; i < getAccidentCount().length; i++) {
            accidentPieDate.add(new PieChart.Data(Constants.accidentCategories[i], getTimePMCount()[i]));
        }
        accidentPMPie.setData(accidentPieDate);

        accidentPMPie.setLegendSide(Side.RIGHT);

        accidentPMPie.getData().stream().forEach((data) -> {
            data.getNode().addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {;
                accidentPiePMLbl.setText(String.valueOf(data.getName() + ": " + Math.round(data.getPieValue() * 100.0) + "%"));
            });

        });
    }

    @FXML
    private void handleAddFileBtn(ActionEvent event) {
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        File defaultDirectory = new File(Constants.genPath);
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(extFilter);
        fileChooser.setInitialDirectory(defaultDirectory);
        fileChooser.setTitle("Select Local File");
        String str = "";
        try {
            str = fileChooser.showOpenDialog(null).getAbsolutePath().replace("\\", "\\\\");
        } catch (NullPointerException ne) {

        }
        File file = new File(Constants.path);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(Constants.path, true))) {

            try (BufferedReader reader = new BufferedReader(new FileReader(str))) {
                String line = "";
                for (int i = 0; (line = reader.readLine()) != null; i++) {
                    if (i == 0) {
                        writer.append(line);
                    } else {
                        writer.newLine();
                        writer.append(line);
                    }
                }
                reader.close();
            } catch (Exception e) {
                System.out.println("Error 2");
            }
            writer.close();
            JOptionPane.showMessageDialog(null, "Text File Added!");

        } catch (Exception e) {
            System.out.println("Error");
        }

    }

    public void streamTweetsOn() throws InterruptedException, IOException {
        Process process = java.lang.Runtime.getRuntime().exec("ping www.geeksforgeeks.org");
        int x = process.waitFor();
        if (x == 0) {
            try {
                FilterQuery tweetFilterQuery = new FilterQuery();

                tweetFilterQuery.follow(new long[]{171574926, 442786483});
                twitterStream.addListener(listener);
                twitterStream.filter(tweetFilterQuery);

                streamLbl.setText("Streaming tweets...");
                JOptionPane.showMessageDialog(null, "Stream was turned on");
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error!");
            }
        } else {
            streamLbl.setText("No Internet Connection");
        }

    }

    // Not yet stable
    public void streamTweetsOff() {
        twitterStream.clearListeners();
        twitterStream.cleanUp();
        twitterStream.shutdown();
        System.out.println("nangyayari");
        JOptionPane.showMessageDialog(null, "Stream was turned off");
    }

    @FXML
    private void handleRefreshBtn(ActionEvent event) {
        setup();
        JOptionPane.showMessageDialog(null, "Data Updated!");
    }

    @FXML
    private void handleFollowBtn(ActionEvent event) {
        try {
            Desktop.getDesktop().browse(new URL("https://twitter.com/MMDA").toURI());
        } catch (Exception e) {
            System.out.println("error");
        }
    }

    @FXML
    private void handleExitBtn(ActionEvent event) {
        Stage mainStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        mainStage.close();
    }

    @FXML
    private void handleStreamChkBox(ActionEvent event) throws InterruptedException, IOException {
        if (streamChkBox.isSelected()) {
            this.streamTweetsOn();
        } else {
            this.streamTweetsOff();
        }
    }

    @FXML
    private void handleGenTweetsBtn(ActionEvent event) throws IOException {
        Parent changeToReg = FXMLLoader.load(getClass().getResource("/view/generateTweetsView.fxml"));
        Scene changeRegScene = new Scene(changeToReg);
        Stage mainStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        changeRegScene.getStylesheets().add(MmdaAlert.class.getResource("/style/bootstrap3.css").toExternalForm());
        mainStage.setScene(changeRegScene);
        mainStage.centerOnScreen();
        mainStage.sizeToScene();
        mainStage.setTitle("MMDA ALERT");
        mainStage.show();
    }

    @FXML
    private void handleClearBtn(ActionEvent event) {
        //JOptionPane.showConfirmDialog(null, "Do you really want to clear all tweets?");

        File file = new File(Constants.path);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.close();
        } catch (Exception e) {
            System.out.println("Error");
        }

        tweets.clear();
        accidents.clear();
        vehicles.clear();
        time.clear();
        bound.clear();
        notes.clear();
        timeBar.getData().clear();
        boundBar.getData().clear();
        vehicleToAccidentsLine.getData().clear();
        vehicleToAccidentsPercentLine.getData().clear();
        accidentTimeAMLine.getData().clear();
        accidentTimePMLine.getData().clear();
        accidentsCountTable.getItems().clear();
        vehicleCountTable.getItems().clear();
        tweetsCategoriesTbl.getItems().clear();
        tweetsList.getItems().clear();
        accidentAMPie.getData().clear();
        accidentPMPie.getData().clear();
        accidentPie.getData().clear();
        tweetsNumberLbl.setText("0");
        JOptionPane.showMessageDialog(null, "All tweets deleted!");
    }

    @FXML
    private void handleBackupBtn(ActionEvent event) throws IOException {
        if (tweets.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No Generated Tweets Yet!");
        } else {
            String pattern = "yyyy-MM-dd";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            String date = simpleDateFormat.format(new Date());
            File file = new File(Constants.backupPath + "backup_" + date + ".txt");
            file.createNewFile();
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
                for (int i = 0; i < tweets.size(); i++) {
                    if (i == (tweets.size() - 1)) {
                        writer.append(tweets.get(i));
                    } else {
                        writer.append(tweets.get(i));
                        writer.newLine();
                    }
                }
                JOptionPane.showMessageDialog(null, "Tweets Backup Created!");
                writer.close();
            } catch (Exception e) {
                System.out.println("Error");
            }
        }
    }

}
