package pl.dawidwalski.weatherapp.model.service;

import pl.dawidwalski.weatherapp.model.WeatherData;

public interface IWeatherObserver {
    void onWeatherUpdate (WeatherData data);
}
