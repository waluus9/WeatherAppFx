package pl.dawidwalski.weatherapp.model.Dao;

import pl.dawidwalski.weatherapp.model.WeatherStat;

import java.util.List;

public interface WeatherStatDao {
    void saveStat(WeatherStat weatherStat);
    List<WeatherStat> getLastSixStats(String cityname);
    List<String> getAllCities();
    double countCityAvgTemp(String cityname);
}
