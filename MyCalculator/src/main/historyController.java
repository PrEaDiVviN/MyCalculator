package main;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;


public class historyController {
    @FXML
    private ListView listView;

    public void initialize (){
        BufferedReader reader;
        List<String> list = new ArrayList<>();
        try {

            reader = new BufferedReader(new FileReader("C:\\Users\\PrEaD\\Desktop\\Small Projects\\MyCalculator\\src\\logs\\history.txt"));
            String line = reader.readLine();
            while (line != null) {
                if(line.charAt(0) != '\n')
                    list.add(line);
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ObservableList<String> items = FXCollections.observableArrayList (
                list);
        listView.setItems(items);
        listView.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
    }

    @FXML
    public void goBackToCalculator(ActionEvent event) throws IOException {
        FXMLLoader loader = null;
        try {
            loader = new FXMLLoader(new File("src\\main\\computerStructure.fxml").toURI().toURL());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Parent home_page_parent =loader.load();
        Scene home_page_scene = new Scene(home_page_parent);
        Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        app_stage.hide();
        app_stage.setScene(home_page_scene);
        app_stage.show();
    }
}
