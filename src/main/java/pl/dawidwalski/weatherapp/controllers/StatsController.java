package pl.dawidwalski.weatherapp.controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import pl.dawidwalski.weatherapp.model.Dao.Impl.WeatherStatDaoImpl;
import pl.dawidwalski.weatherapp.model.Dao.WeatherStatDao;
import pl.dawidwalski.weatherapp.model.WeatherStat;
import pl.dawidwalski.weatherapp.model.utils.Connector;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class StatsController implements Initializable{
    @FXML
    BarChart<String, Number> weatherChart;

    @FXML
    ListView listOfCities;

    @FXML
    Button backButton;

    @FXML
    TextField averageTemp;

    private Connector connector = Connector.getInstance();
    private WeatherStatDao weatherStatDao = new WeatherStatDaoImpl();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        registerBackButton();
        loadCityNames();
        registerClickItemOnList();
        showAverageTemp();
    }

    private void registerClickItemOnList(){
        listOfCities.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            loadChart((String) newValue);
        });
    }

    private void loadChart(String city) {
        XYChart.Series series = new XYChart.Series();
        series.setName(city);
        int counter = 1;
        for (WeatherStat weatherStat: weatherStatDao.getLastSixStats(city)) {
            series.getData().add(new XYChart.Data("" + counter, weatherStat.getTemp()));
            counter++;
        }
        weatherChart.getData().clear();
        weatherChart.getData().add(series);
    }

    private void loadCityNames() {
        listOfCities.setItems(FXCollections.observableList(weatherStatDao.getAllCities()));
    }

    private void registerBackButton() {
        backButton.setOnMouseClicked( e -> {
            Stage stage = (Stage) backButton.getScene().getWindow();

            try {
                Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("sample.fxml"));
                stage.setScene(new Scene(root,600,400));
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
    }

    private void showAverageTemp() {
        listOfCities.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            getCityAvgTemp((String) newValue);
        });
    }

    private void getCityAvgTemp(String newValue) {
        averageTemp.setText("Average temperature for all results: " + String.valueOf(weatherStatDao.countCityAvgTemp(newValue)) + " C");
    }
}