package pl.dawidwalski.weatherapp.model.Dao.Impl;

import pl.dawidwalski.weatherapp.model.Dao.WeatherStatDao;
import pl.dawidwalski.weatherapp.model.WeatherStat;
import pl.dawidwalski.weatherapp.model.utils.Connector;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class WeatherStatDaoImpl implements WeatherStatDao {

    private Connector connector = Connector.getInstance();

    @Override
    public void saveStat(WeatherStat weatherStat) {
        PreparedStatement preparedStatement = connector.newPreparedStatement(
                "INSERT INTO weather VALUES(?, ?, ?)"
        );
        try {
            preparedStatement.setInt(1,0);
            preparedStatement.setString(2,weatherStat.getCityname());
            preparedStatement.setFloat(3, (float) (weatherStat.getTemp()-273.15));
            preparedStatement.execute();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<WeatherStat> getLastSixStats(String cityname) {
        List<WeatherStat> weatherStatList = new ArrayList<>();
        PreparedStatement preparedStatement = connector.newPreparedStatement(
                "SELECT * FROM weather WHERE cityname = ? ORDER BY id DESC LIMIT 6"
        );
        try {
            WeatherStat weatherStat;
            preparedStatement.setString(1,cityname);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                weatherStat = new WeatherStat(resultSet.getString("cityname"), resultSet.getFloat("temp"));
                weatherStatList.add(weatherStat);
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return weatherStatList;
    }

    @Override
    public List<String> getAllCities() {
        List<String> cities = new ArrayList<>();
        PreparedStatement preparedStatement = connector.newPreparedStatement(
                "SELECT DISTINCT cityname FROM weather"
        );
        try {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                cities.add(resultSet.getString("cityname"));
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cities;
    }

    @Override
    public double countCityAvgTemp(String cityname) {
        List<Double> allTemps = new ArrayList<>();
        double average;
        PreparedStatement preparedStatement = connector.newPreparedStatement(
                "SELECT temp FROM weather WHERE cityname = ?"
        );
        try {
            preparedStatement.setString(1, cityname);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                allTemps.add(resultSet.getDouble("temp"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        double sum = 0;
        for (Double temp : allTemps) {
            sum += temp;
        }
        average = sum/allTemps.size();
        return average;
    }
}
