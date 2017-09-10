package pl.dawidwalski.weatherapp.model.service;

import org.json.JSONObject;
import pl.dawidwalski.weatherapp.Config;
import pl.dawidwalski.weatherapp.Utils;
import pl.dawidwalski.weatherapp.model.WeatherData;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WeatherService {

    private static WeatherService INSTANCE = new WeatherService();

    public static WeatherService getService() {
        return INSTANCE;
    }

    private List<IWeatherObserver> observerList;

    private ExecutorService executorService;

    private WeatherService() {
        executorService = Executors.newFixedThreadPool(2);
        observerList = new ArrayList<>();
    }

    public void registerObserver(IWeatherObserver observer) {
        observerList.add(observer);
    }

    private void notifyObservers(WeatherData data) {
        for (IWeatherObserver iWeatherObserver : observerList) {
            iWeatherObserver.onWeatherUpdate(data);
        }
    }

    public void init(final String city) {
        Runnable taskInIt = new Runnable() {
            @Override
            public void run() {
                String text = Utils.readWebsiteContext(Config.API_URL + city + "&appid=" + Config.APP_KEY);
                parseJsonFromString(text);
            }
        };
        executorService.execute(taskInIt);
    }

    private void parseJsonFromString(String text) {

        WeatherData data = new WeatherData();

        JSONObject root = new JSONObject(text);
        JSONObject main = root.getJSONObject("main");

        data.setTemp(main.getDouble("temp"));
        data.setHumidity(main.getInt("humidity"));
        data.setPressure(main.getInt("pressure"));

        JSONObject cloudsObject = root.getJSONObject("clouds");
        data.setClouds(cloudsObject.getInt("all"));

        notifyObservers(data);
    }
}
