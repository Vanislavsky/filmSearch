package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.apache.hc.core5.net.URIBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

public class mainController {
    @FXML
    private TextField searchField;

    @FXML
    private Button searchButton;

    @FXML
    private ListView<String> filmList;

    @FXML
    void initialize() {
        searchField.setStyle("-fx-text-fill:  #fefefe#fefefe; -fx-background-color:  #4f4f4f#4f4f4f");

        searchButton.setOnAction(actionEvent ->  {
                JSONObject jo = null;
                try {
                    jo = Parser.ParseByKeyWord(searchField.getText());
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                for(Object film : (JSONArray)jo.get("films")) {
                    filmList.getItems().addAll((String)((JSONObject)film).get("nameRu"));
                }
            });

        MultipleSelectionModel<String> filmSelectionModel = filmList.getSelectionModel();
        // устанавливаем слушатель для отслеживания изменений
        filmSelectionModel.selectedItemProperty().addListener(new ChangeListener<String>(){

            public void changed(ObservableValue<? extends String> changed, String oldValue, String newValue){
                Parent root;
                try {
                    Controller.prevFilm = null;
                    Controller.keyWord = newValue;
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("sample.fxml"));
                    root = loader.load(getClass().getResource("filmCard.fxml"));
                    Stage stage = new Stage();
                    stage.setTitle("FilmSearch");
                    stage.setScene(new Scene(root, 1012, 612));
                    stage.setResizable(false);
                    stage.show();
                    searchButton.getScene().getWindow().hide();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}