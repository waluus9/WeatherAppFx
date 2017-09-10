package pl.dawidwalski.weatherapp.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import pl.dawidwalski.weatherapp.model.Dao.Impl.WeatherStatDaoImpl;
import pl.dawidwalski.weatherapp.model.Dao.WeatherStatDao;
import pl.dawidwalski.weatherapp.model.WeatherData;
import pl.dawidwalski.weatherapp.model.WeatherStat;
import pl.dawidwalski.weatherapp.model.service.IWeatherObserver;
import pl.dawidwalski.weatherapp.model.service.WeatherService;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements IWeatherObserver, Initializable {

    @FXML
    Button buttonShow;

    @FXML
    TextField textCity;

    @FXML
    TextArea showInfo;

    @FXML
    ProgressIndicator progressLoad;

    @FXML
    Button statisticButton;

    private String lastCityName;

    private WeatherService weatherService = WeatherService.getService();
    private WeatherStatDao weatherStatDao = new WeatherStatDaoImpl();

    @Override
    public void onWeatherUpdate(WeatherData data) {
        weatherStatDao.saveStat(new WeatherStat(lastCityName, (float) data.getTemp()));
        showInfo.setText("Temperature: " + (data.getTemp() - 273.15) + " C\nHumidity: " + data.getHumidity() + " %\nPressure: " + data.getPressure() + " hP\nClouds: " + data.getClouds() + " %");
        progressLoad.setVisible(false);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        weatherService.registerObserver(this);
        registerShowButtonAction();
        registerEnterListener();
        registerStatisticButton();
    }

    private void registerShowButtonAction() {
        buttonShow.setOnMouseClicked(e -> prepareRequestAndClear());
        textCity.clear();
    }

    private void registerEnterListener() {
        textCity.setOnKeyPressed( e -> {
            if (e.getCode() == KeyCode.ENTER) {
                prepareRequestAndClear();
            }
        });
    }

    private void prepareRequestAndClear() {
        lastCityName = textCity.getText();
        progressLoad.setVisible(true);
        weatherService.init(textCity.getText());
        textCity.clear();
    }

    private void registerStatisticButton() {
        statisticButton.setOnMouseClicked( e -> {
            Stage stage = (Stage) statisticButton.getScene().getWindow();
            try {
                Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("stats.fxml"));
                stage.setScene(new Scene(root,700,500));
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
    }
}
