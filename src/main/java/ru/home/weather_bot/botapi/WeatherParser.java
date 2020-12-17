package ru.home.weather_bot.botapi;

import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherParser {

    public String parse(float lat, float lon){
        HttpURLConnection connection = null;
        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL("http://api.weather.yandex.ru/v2/forecast?lat=" + lat + "&lon=" + lon + "&hours=true&lang=ru_RU");
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setUseCaches(false);
            connection.setDoOutput(true);
            connection.setRequestProperty("X-Yandex-API-Key", "1affd626-dcdd-49ea-b281-9e816b4112de");
            connection.connect();

            if(HttpURLConnection.HTTP_OK == connection.getResponseCode()) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;

                while ((line = in.readLine()) != null) {
                    sb.append(line);
                }

                JSONObject json = new JSONObject(sb.toString());
                return sendWeather(json);
            }else {
                return "))))))))";
            }
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return "Не удалось найти погоду";
    }

    public String sendWeather(JSONObject json){
        String weatherMsg;
        weatherMsg = "Текущая температура: " + json.getJSONObject("fact").getInt("temp") + "\n";

        weatherMsg += ("Средняя температура утром: " + json.getJSONArray("forecasts").getJSONObject(0).getJSONObject("parts").getJSONObject("morning").getInt("temp_avg") + "\n");
        weatherMsg += ("Средняя температура днем: " + json.getJSONArray("forecasts").getJSONObject(0).getJSONObject("parts").getJSONObject("day").getInt("temp_avg") + "\n");
        weatherMsg += ("Средняя температура вечером: " + json.getJSONArray("forecasts").getJSONObject(0).getJSONObject("parts").getJSONObject("evening").getInt("temp_avg") + "\n");
        weatherMsg += ("Средняя температура ночью: " + json.getJSONArray("forecasts").getJSONObject(0).getJSONObject("parts").getJSONObject("night").getInt("temp_avg") + "\n");

        weatherMsg += "Если вы хотите получать погоду по данному месту каджый день напишите /subscribe";
        return weatherMsg;
    }
}
