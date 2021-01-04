package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;


public class Controller {
    public static String keyWord;
    public static String prevFilm = null;

    @FXML
    private ImageView filmView;

    @FXML
    private WebView trailer;

    @FXML
    private Label filmName;

    @FXML
    private Label enName;

    @FXML
    private Label rating;

    @FXML
    private Label ageRating;

    @FXML
    private Label countryLabel;

    @FXML
    private Label createAgeLabel;

    @FXML
    private Label taglinetaglineLabel;

    @FXML
    private Label creatorLabel;

    @FXML
    private Label genresLabel;

    @FXML
    private Label durationLabel;

    @FXML
    private Label countVoteLabel;

    @FXML
    private TextArea description;

    @FXML
    private Button prevButton;

    @FXML
    private TextField searchField;

    @FXML
    private Button searchButton;

    @FXML
    private ListView<String> factsList;

    @FXML
    void initialize() throws IOException, URISyntaxException, org.json.simple.parser.ParseException {
        JSONObject Data = (JSONObject)((JSONArray) Parser.ParseByKeyWord(keyWord).get("films")).get(0);
        JSONObject film = Parser.ParseById(((Long)Data.get("filmId")).toString());
        String trailerUrl = Parser.ParseTrailer(((Long)Data.get("filmId")).toString());


        filmName.setText((String) film.get("nameRu"));
        enName.setText((String) film.get("nameEn"));
        createAgeLabel.setText((String) film.get("year"));
        taglinetaglineLabel.setText((String) film.get("slogan"));

        String countries = "";
        for(Object country : (JSONArray) film.get("countries")) {
            countries += (String)((JSONObject)country).get("country");
            countries += ", ";
        }
        countryLabel.setText(countries);

        String genres = "";
        for(Object genre : (JSONArray) film.get("genres")) {
            genres += (String)((JSONObject)genre).get("genre");
            genres += ", ";

        }
        genresLabel.setText(genres);
        genresLabel.setWrapText(true);

        durationLabel.setText((String) film.get("filmLength"));
        rating.setText((String)Data.get("rating"));
        Long countRate = (Long)Data.get("ratingVoteCount");
        countVoteLabel.setText(countRate.toString());

        URLConnection connection = new URL((String)Data.get("posterUrl")).openConnection();
        connection.connect();
        filmView.setImage(new Image(connection.getInputStream()));

        if(trailerUrl != null) {
            String changedTrailerUri = trailerUrl.substring(32, trailerUrl.length());
            String embancedUri = "https://www.youtube.com/embed/" + changedTrailerUri;
            trailer.getEngine().load(embancedUri);
        }

        description.setEditable(false);
        description.setWrapText(true);
        description.setStyle("-fx-text-fill: #fefefe#fefefe; -fx-font-size: 16px; -fx-control-inner-background: #181818#181818 ");
        description.setText((String)film.get("description"));

        factsList.setStyle("-fx-text-fill: #fefefe#fefefe; -fx-font-size: 16px; -fx-control-inner-background: #181818#181818 ");
        for(Object fact : (JSONArray)film.get("facts")) {
            factsList.getItems().addAll((String) fact);
        }


        prevButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                Parent root = null;
                if (prevFilm == null) {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("main.fxml"));
                        root = loader.load(getClass().getResource("main.fxml"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        var temp = keyWord;
                        Controller.keyWord = prevFilm;
                        Controller.prevFilm = temp;
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("filmCard.fxml"));
                        root = loader.load(getClass().getResource("filmCard.fxml"));

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                Stage stage = new Stage();
                stage.setTitle("FilmSearch");
                stage.setScene(new Scene(root, 1012, 612));
                stage.setResizable(false);
                stage.show();
                ((Node) (event.getSource())).getScene().getWindow().hide();
            }
        });

        searchField.setStyle("-fx-text-fill:  #fefefe#fefefe; -fx-background-color:   #181818#181818");
        searchButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                Parent root;
                try {
                    Controller.prevFilm = keyWord;
                    Controller.keyWord = searchField.getText();
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("filmCard.fxml"));
                    root = loader.load(getClass().getResource("filmCard.fxml"));
                    Stage stage = new Stage();
                    stage.setTitle("FilmSearch");
                    stage.setScene(new Scene(root, 1012, 612));
                    stage.show();
                    ((Node)(event.getSource())).getScene().getWindow().hide();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });



    }


}
